package khushi.example.greenmarket.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import khushi.example.greenmarket.R;

public class AddNewAddressActivity extends AppCompatActivity {

    EditText etFullName, etPhone, etPincode, etState, etCity, etHouseNo, etRoadArea, etLandmark;
    Button btnSaveAddress, btnUseLocation;
    ImageView ivBack;
    DatabaseReference dbRef;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_address);

        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        etPincode = findViewById(R.id.etPincode);
        etState = findViewById(R.id.etState);
        etCity = findViewById(R.id.etCity);
        etHouseNo = findViewById(R.id.etHouseNo);
        etRoadArea = findViewById(R.id.etRoadArea);
        etLandmark = findViewById(R.id.etLandmark);
        btnSaveAddress = findViewById(R.id.btnSaveAddress);
        btnUseLocation = findViewById(R.id.btnUseLocation);
        ivBack = findViewById(R.id.ivBack);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        dbRef = FirebaseDatabase.getInstance().getReference("Addresses")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        btnSaveAddress.setOnClickListener(v -> {
            String name = etFullName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String pincode = etPincode.getText().toString().trim();
            String state = etState.getText().toString().trim();
            String city = etCity.getText().toString().trim();
            String house = etHouseNo.getText().toString().trim();
            String road = etRoadArea.getText().toString().trim();
            String landmark = etLandmark.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty() || pincode.isEmpty() || state.isEmpty() ||
                    city.isEmpty() || house.isEmpty() || road.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields!", Toast.LENGTH_SHORT).show();
            } else {
                String fullAddress = house + ", " + road + ", " + city + ", " + state + " - " + pincode;

                HashMap<String, String> map = new HashMap<>();
                map.put("name", name);
                map.put("phone", phone);
                map.put("fullAddress", fullAddress);
                map.put("landmark", landmark);

                String id = UUID.randomUUID().toString();
                dbRef.child(id).setValue(map).addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Address saved successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, SelectAddressActivity.class));
                    finish();
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save address!", Toast.LENGTH_SHORT).show();
                });
            }
        });

        ivBack.setOnClickListener(v -> onBackPressed());

        btnUseLocation.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return;
            }

            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (addresses != null && !addresses.isEmpty()) {
                            Address address = addresses.get(0);
                            etPincode.setText(address.getPostalCode());
                            etState.setText(address.getAdminArea());
                            etCity.setText(address.getLocality());
                            etRoadArea.setText(address.getSubLocality());
                            Toast.makeText(this, "Location fetched successfully!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to get address!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Location not available!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
