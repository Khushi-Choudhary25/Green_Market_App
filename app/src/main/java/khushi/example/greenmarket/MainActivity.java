package khushi.example.greenmarket;

import android.content.Intent;
import android.net.Uri;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import khushi.example.greenmarket.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;
    private StorageReference storageReference;
    private AppCompatImageView profileImageView;

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private BillingClient billingClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        findViewById(R.id.fab).setOnClickListener(view -> {
            Toast.makeText(MainActivity.this, "Opening Add Product", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, khushi.example.greenmarket.ui.home.AddProductActivity.class);
            startActivity(intent);
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_crop_advisory, R.id.nav_weather_forecast, R.id.nav_market_trends)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                navController.navigate(R.id.nav_home);
                return true;
            } else if (id == R.id.nav_crop_advisory) {
                navController.navigate(R.id.nav_crop_advisory);
                return true;
            } else if (id == R.id.nav_weather_forecast) {
                navController.navigate(R.id.nav_weather_forecast);
                return true;
            } else if (id == R.id.nav_market_trends) {
                navController.navigate(R.id.nav_market_trends);
                return true;
            }

            return false;
        });


        View headerView = navigationView.getHeaderView(0);
        TextView userNameText = headerView.findViewById(R.id.nav_user_name);
        TextView userTypeText = headerView.findViewById(R.id.nav_user_type);
        profileImageView = headerView.findViewById(R.id.imageView);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("profile_images");

        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
            userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String fullName = snapshot.child("fullName").getValue(String.class);
                        String userType = snapshot.child("userType").getValue(String.class);
                        String profileImageUrl = snapshot.child("profileImageUrl").getValue(String.class);

                        Log.d("FIREBASE_DATA", "Name: " + fullName + ", Type: " + userType);

                        if (fullName != null) userNameText.setText(fullName);
                        if (userType != null) userTypeText.setText(userType);

                        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                            Glide.with(MainActivity.this)
                                    .load(profileImageUrl)
                                    .placeholder(R.drawable.profile)
                                    .into(profileImageView);
                        }
                    } else {
                        Log.e("FIREBASE_DATA", "User data not found.");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("FIREBASE_DATA", "Error fetching user data: " + error.getMessage());
                    Snackbar.make(findViewById(android.R.id.content), "Failed to fetch user data", Snackbar.LENGTH_LONG).show();
                }
            });
        }


        profileImageView.setOnClickListener(view -> openImageChooser());


        profileImageView.post(() -> {
            profileImageView.setClipToOutline(true);
            profileImageView.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    int size = Math.min(view.getWidth(), view.getHeight());
                    outline.setOval(0, 0, size, size);
                }
            });
        });



        billingClient = BillingClient.newBuilder(this)
                .enablePendingPurchases()
                .setListener(new PurchasesUpdatedListener() {
                    @Override
                    public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {

                    }
                })
                .build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    Log.d("BILLING", "Billing client setup completed.");
                }
            }

            @Override
            public void onBillingServiceDisconnected() {

            }
        });


        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.action_logout) {
                FirebaseAuth.getInstance().signOut();

                SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                preferences.edit().clear().apply();

                Toast.makeText(MainActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

                return true;
            }
            if (id == R.id.nav_settings) {
                Intent intent = new Intent(MainActivity.this, khushi.example.greenmarket.ui.home.SettingsActivity.class);
                startActivity(intent);
                return true;
            }


            return NavigationUI.onNavDestinationSelected(item,
                    Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main))
                    || onOptionsItemSelected(item);
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();

            if (selectedImageUri != null) {
                uploadImageToFirebase();
            }
        }
    }

    private void uploadImageToFirebase() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return;

        String userId = currentUser.getUid();

        StorageReference fileRef = storageReference.child(userId + ".jpg");
        fileRef.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();

                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                            userRef.child("profileImageUrl").setValue(downloadUrl)
                                    .addOnSuccessListener(aVoid -> {
                                        Glide.with(MainActivity.this)
                                                .load(downloadUrl)
                                                .placeholder(R.drawable.profile)
                                                .into(profileImageView);

                                        Toast.makeText(MainActivity.this, "Profile image updated", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Failed to update profile image", Toast.LENGTH_SHORT).show());
                        }))
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}