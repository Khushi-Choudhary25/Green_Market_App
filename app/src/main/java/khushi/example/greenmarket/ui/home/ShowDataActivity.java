package khushi.example.greenmarket.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

import khushi.example.greenmarket.R;
import khushi.example.greenmarket.ui.adapters.ProductAdapter;
import khushi.example.greenmarket.ui.model.Product;

public class ShowDataActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Product> productList;
    ProductAdapter adapter;

    String category, subcategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);

        recyclerView = findViewById(R.id.productRecyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        productList = new ArrayList<>();
        adapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(adapter);

        category = getIntent().getStringExtra("category");
        subcategory = getIntent().getStringExtra("subcategory");

        fetchProductsFromFirebase();
    }

    private void fetchProductsFromFirebase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Products");

        ref.orderByChild("subcategory").equalTo(subcategory)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        productList.clear();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Product product = dataSnapshot.getValue(Product.class);

                            if (product != null && product.getCategory().equals(category)) {
                                productList.add(product);
                            }
                        }

                        if (productList.isEmpty()) {
                            Toast.makeText(ShowDataActivity.this, "No products found", Toast.LENGTH_SHORT).show();
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("FirebaseError", error.getMessage());
                        Toast.makeText(ShowDataActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
