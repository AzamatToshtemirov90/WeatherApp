import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.azamat.weatherapp.base.BaseApiResult
import com.azamat.weatherapp.base.Status
import com.azamat.weatherapp.model.remote.response.*
import com.azamat.weatherapp.model.repository.RemoteRepository
import com.azamat.weatherapp.ui.home.HomeViewModel
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: HomeViewModel
    private val remoteRepository = mockk<RemoteRepository>()
    private val ioDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        viewModel = HomeViewModel(ioDispatcher, remoteRepository)
    }

    @Test
    fun `getWeather should update weatherResponse on successful API call`() = runTest {
        // Arrange
        val lat = 29.4241
        val lon = -98.4936
        val weatherEntity = WeatherEntity(
            "stations",
            CloudsEntity(100),
            200,
            CoordEntity(lat, lon),
            1685402683,
            4726206,
            MainEntity(294.59, 294, 85, 1012, 291, 294.81, 291.85, 291.81),
            "San Antonio",
            SysEntity("US", 1685360140, 1685410044),
            -18000,
            10000,
            emptyList(),
            WindEntity(310, 13.89, 9.26)
        )
        val baseApiResult = BaseApiResult(Status.SUCCESS, weatherEntity, "SUCCESS")
        coEvery { remoteRepository.getWeatherByLatLong(lat, lon) } returns baseApiResult

        // Create an observer for weatherResponse
        val observer = spyk<Observer<WeatherEntity>>()
        viewModel.weatherResponse.observeForever(observer)

        // Act
        viewModel.getWeather(lat, lon)

        // Assert
        coVerify { remoteRepository.getWeatherByLatLong(lat, lon) }
        verify { observer.onChanged(weatherEntity) }
        assertEquals(weatherEntity, viewModel.weatherResponse.value)

        // Cleanup
        viewModel.weatherResponse.removeObserver(observer)
    }

    @Test
    fun `getWeather should post error message when API call fails`() = runTest {
        // Arrange
        val lat = 37.7749
        val lon = -122.4194
        val errorMessage = "Failed to fetch weather"
        val baseApiResult = BaseApiResult(Status.ERROR, null, errorMessage)
        coEvery { remoteRepository.getWeatherByLatLong(lat, lon) } returns baseApiResult

        // Create an observer for error
        val observer = spyk<Observer<String?>>()
        viewModel.error.observeForever(observer)

        // Act
        viewModel.getWeather(lat, lon)

        // Assert
        coVerify { remoteRepository.getWeatherByLatLong(lat, lon) }
        verify { observer.onChanged(errorMessage) }
        assertEquals(errorMessage, viewModel.error.value)

        // Cleanup
        viewModel.error.removeObserver(observer)
    }

    @Test
    fun `searchCity should update cityResponse on successful API call`() = runTest {
        // Arrange
        val query = "London"
        val cityList = listOf(CityNameEntity("London", 5.50, 0.12, "England"))
        val baseApiResult = BaseApiResult(Status.SUCCESS, cityList, query)
        coEvery { remoteRepository.searchCityByName(query) } returns baseApiResult

        // Create an observer for cityResponse
        val observer = spyk<Observer<List<CityNameEntity>>>()
        viewModel.cityResponse.observeForever(observer)

        // Act
        viewModel.searchCity(query)

        // Assert
        coVerify { remoteRepository.searchCityByName(query) }
        verify { observer.onChanged(cityList) }
        assertEquals(cityList, viewModel.cityResponse.value)

        // Cleanup
        viewModel.cityResponse.removeObserver(observer)
    }

    @Test
    fun `searchCity should post error message when API call fails`() = runTest {
        // Arrange
        val query = "London"
        val errorMessage = "Failed to search city"
        val baseApiResult = BaseApiResult(Status.ERROR, null, errorMessage)
        coEvery { remoteRepository.searchCityByName(query) } returns baseApiResult

        // Create an observer for error
        val observer = spyk<Observer<String?>>()
        viewModel.error.observeForever(observer)

        // Act
        viewModel.searchCity(query)

        // Assert
        coVerify { remoteRepository.searchCityByName(query) }
        verify { observer.onChanged(errorMessage) }
        assertEquals(errorMessage, viewModel.error.value)

        // Cleanup
        viewModel.error.removeObserver(observer)
    }
}
