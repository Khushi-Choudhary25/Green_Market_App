package khushi.example.greenmarket.ui.weather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import khushi.example.greenmarket.R;

public class WeatherForecastFragment extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    EditText cityInput;
    TextView tempText, humidityText, conditionText;
    Button fetchWeatherBtn;
    ImageView weatherIcon, tempIcon, humidityIcon, conditionIcon;

    String API_KEY = "35dc24b844de78f0ea80f40baef51702";

    FusedLocationProviderClient fusedLocationClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull android.view.LayoutInflater inflater,
                             @Nullable android.view.ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_forecast, container, false);

        cityInput = view.findViewById(R.id.cityInput);
        tempText = view.findViewById(R.id.tempText);
        humidityText = view.findViewById(R.id.humidityText);
        conditionText = view.findViewById(R.id.conditionText);
        fetchWeatherBtn = view.findViewById(R.id.fetchWeatherBtn);
        weatherIcon = view.findViewById(R.id.weatherIcon);
        tempIcon = view.findViewById(R.id.tempIcon);
        humidityIcon = view.findViewById(R.id.humidityIcon);
        conditionIcon = view.findViewById(R.id.conditionIcon);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        fetchWeatherBtn.setOnClickListener(v -> {
            String city = cityInput.getText().toString().trim();
            if (!city.isEmpty()) {
                String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city +
                        "&appid=" + API_KEY + "&units=metric";
                makeWeatherRequest(url);
            } else {
                fetchWeatherByGPS();
            }
        });

        return view;
    }

    private void fetchWeatherByGPS() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
            if (location != null) {
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon +
                        "&appid=" + API_KEY + "&units=metric";
                makeWeatherRequest(url);
            } else {
                Toast.makeText(getContext(), "Unable to get location", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void makeWeatherRequest(String url) {
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject main = response.getJSONObject("main");
                        String temp = main.getString("temp");
                        String humidity = main.getString("humidity");

                        JSONArray weatherArr = response.getJSONArray("weather");
                        String description = weatherArr.getJSONObject(0).getString("description");

                        String cityName = response.getString("name");


                        tempText.setText(temp + "°C");
                        tempText.setVisibility(View.VISIBLE);
                        tempIcon.setVisibility(View.VISIBLE);

                        humidityText.setText(humidity + "%");
                        humidityText.setVisibility(View.VISIBLE);
                        humidityIcon.setVisibility(View.VISIBLE);

                        conditionText.setText(description);
                        conditionText.setVisibility(View.VISIBLE);
                        conditionIcon.setVisibility(View.VISIBLE);

                        if (cityInput.getText().toString().trim().isEmpty()) {
                            cityInput.setText(cityName);
                        }


                        String lowerDesc = description.toLowerCase();
                        if (lowerDesc.contains("clear")) {
                            weatherIcon.setImageResource(R.drawable.ic_clear_sky);
                            weatherIcon.setVisibility(View.VISIBLE);
                        } else if (lowerDesc.contains("cloud")) {
                            weatherIcon.setImageResource(R.drawable.ic_cloudy);
                            weatherIcon.setVisibility(View.VISIBLE);
                        } else if (lowerDesc.contains("rain") || lowerDesc.contains("drizzle")) {
                            weatherIcon.setImageResource(R.drawable.ic_rainy);
                            weatherIcon.setVisibility(View.VISIBLE);
                        } else if (lowerDesc.contains("sun")) {
                            weatherIcon.setImageResource(R.drawable.ic_sunny);
                            weatherIcon.setVisibility(View.VISIBLE);
                        } else {
                            weatherIcon.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error parsing weather data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getContext(), "Failed to fetch weather", Toast.LENGTH_SHORT).show());

        queue.add(request);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchWeatherByGPS();
            } else {
                Toast.makeText(getContext(), "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
