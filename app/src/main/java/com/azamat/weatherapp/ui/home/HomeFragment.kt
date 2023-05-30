package com.azamat.weatherapp.ui.home

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.azamat.weatherapp.base.LoggingFragment
import com.azamat.weatherapp.databinding.FragmentHomeBinding
import com.azamat.weatherapp.databinding.LayoutSunriseSunsetBinding
import com.azamat.weatherapp.databinding.LayoutWeatherAdditionalInfoBinding
import com.azamat.weatherapp.databinding.LayoutWeatherBasicInfoBinding
import com.azamat.weatherapp.utils.getIconUrl
import com.azamat.weatherapp.utils.kelvinToCelsius
import com.azamat.weatherapp.utils.loadImage
import com.azamat.weatherapp.utils.unixTimestampToTimeString
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : LoggingFragment() {
    private val PERMISSION_REQUEST_ACCESS_LOCATION = 1211

    private lateinit var binding: FragmentHomeBinding
    private lateinit var bindingBasicInfoBinding: LayoutWeatherBasicInfoBinding
    private lateinit var bindingSunriseSunsetBinding: LayoutSunriseSunsetBinding
    private lateinit var bindingAdditionalInfoBinding: LayoutWeatherAdditionalInfoBinding
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val viewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        bindingBasicInfoBinding = LayoutWeatherBasicInfoBinding.inflate(inflater, container, false)
        bindingAdditionalInfoBinding =
            LayoutWeatherAdditionalInfoBinding.inflate(inflater, container, false)
        bindingSunriseSunsetBinding = LayoutSunriseSunsetBinding.inflate(inflater, container, false)

        binding.searchViewCitySearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //TODO("Not yet implemented")
            }

            override fun afterTextChanged(string: Editable?) {
                viewModel.searchCity(SpannableStringBuilder(string).toString())
            }
        })
        binding.searchViewCitySearch.setOnItemClickListener { _, _, position, _ ->
            viewModel.onListItemClickListener(position)
        }
        viewModel.cityResponse.observe(viewLifecycleOwner) {
            adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                it.map { (it?.name + ", " + (it?.country ?: "")) }
            )
            binding.searchViewCitySearch.setAdapter(adapter)
            adapter.notifyDataSetChanged()
        }

        observeData()
        populateUI()

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        getCurrentLocation()
        return binding.root
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_ACCESS_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            }
        }
    }

    private fun checkPermission(): Boolean {
        return (ActivityCompat.checkSelfPermission(
            requireActivity(),
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
            requireActivity(),
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun isLocationEnable(): Boolean {
        val locationManager: LocationManager =
            context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun getCurrentLocation() {
        if (checkPermission()) {
            if (isLocationEnable()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                    val location: Location? = task.result
                    if (location == null) {

                        Log.d("AZAMAT", "${location?.latitude}")
                    } else {
                        Log.d("Location", "${location.latitude}")
                        viewModel.getWeather(location.latitude, location.longitude)
                    }
                }
            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermission()
        }
    }

    private fun populateUI() {
        binding.progressBar.visibility = View.GONE
        binding.textViewError.visibility = View.GONE

        viewModel.weatherResponse.observe(viewLifecycleOwner) {
            binding.layoutWeatherBasic.tvCityCountry.text = it?.name.toString()
            binding.layoutWeatherBasic.tvDateTime.text = it?.dt?.unixTimestampToTimeString()
            binding.layoutWeatherBasic.tvTemperature.text =
                it?.main?.temp?.kelvinToCelsius().toString()
            binding.layoutWeatherBasic.ivWeatherCondition.loadImage(
                it.weather[0].icon?.getIconUrl() ?: ""
            )
            binding.layoutWeatherBasic.tvWeatherCondition.text =
                it?.weather?.get(0)?.description.toString()

            binding.layoutSunsetSunrise.tvSunriseTime.text =
                it?.sys?.sunrise?.unixTimestampToTimeString()
            binding.layoutSunsetSunrise.tvSunsetTime.text =
                it?.sys?.sunset?.unixTimestampToTimeString()

            binding.layoutWeatherAdditional.tvHumidityValue.text = it?.main?.humidity.toString()
            binding.layoutWeatherAdditional.tvPressureValue.text = it?.main?.pressure.toString()
            binding.layoutWeatherAdditional.tvVisibilityValue.text = it?.visibility.toString()
        }


    }

    private fun observeData() {
        viewModel.error.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = View.GONE
            binding.textViewError.visibility = View.VISIBLE

            it?.let {
                if (it.contains("2147483647")) {
                    Toast.makeText(
                        requireContext(),
                        "NO INTERNET CONNECTION",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    binding.textViewError.text = it

                }
            }
        }
    }
}