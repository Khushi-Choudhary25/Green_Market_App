package khushi.example.greenmarket.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import khushi.example.greenmarket.R;
import khushi.example.greenmarket.ui.adapters.AddressAdapter;
import khushi.example.greenmarket.ui.model.AddressModel;

public class SelectAddressActivity extends AppCompatActivity {

    Button btnSetAddress;
    RecyclerView recyclerView;
    TextView tvAddNewAddress;
    AddressAdapter addressAdapter;
    ArrayList<AddressModel> addressList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);

        btnSetAddress = findViewById(R.id.btnSetAddress);
        recyclerView = findViewById(R.id.recyclerViewAddress);
        tvAddNewAddress = findViewById(R.id.tvAddNewAddress);

        addressList = new ArrayList<>();
        addressAdapter = new AddressAdapter(addressList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(addressAdapter);

        FirebaseDatabase.getInstance().getReference("Addresses")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnSuccessListener(snapshot -> {
                    addressList.clear();
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        String name = snap.child("name").getValue(String.class);
                        String address = snap.child("fullAddress").getValue(String.class);
                        String phone = snap.child("phone").getValue(String.class);
                        addressList.add(new AddressModel(name, address, phone));
                    }
                    addressAdapter.notifyDataSetChanged();
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load addresses", Toast.LENGTH_SHORT).show();
                });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        tvAddNewAddress.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddNewAddressActivity.class);
            startActivity(intent);
        });

        btnSetAddress.setOnClickListener(v -> {
            AddressModel selected = addressAdapter.getSelectedAddress();
            if (selected != null) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedAddress", selected.getFullAddress());
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(this, "Please Select a address", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
