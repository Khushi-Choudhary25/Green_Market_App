package khushi.example.greenmarket.ui.products;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

public class StaticProductsActivity extends AppCompatActivity {

    private static final String TAG = "StaticProducts";

    RecyclerView recyclerView;
    TextView emptyMessage;
    ProductAdapter adapter;
    List<Product> productList;
    DatabaseReference dbRef;

    String category, subcategory;

    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_products);

        recyclerView = findViewById(R.id.recyclerViewProducts);
        emptyMessage = findViewById(R.id.emptyMessage);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        category = getIntent().getStringExtra("category");
        subcategory = getIntent().getStringExtra("subcategory");

        Log.d(TAG, "Received category: " + category + ", subcategory: " + subcategory);

        productList = new ArrayList<>();
        adapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(adapter);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        loadProductsFromFirebase();
    }

    private void loadProductsFromFirebase() {
        dbRef = FirebaseDatabase.getInstance().getReference("products");

        dbRef.orderByChild("subcategory").equalTo(subcategory)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        productList.clear();
                        Log.d(TAG, "Products snapshot children count: " + snapshot.getChildrenCount());

                        for (DataSnapshot productSnap : snapshot.getChildren()) {
                            Product product = productSnap.getValue(Product.class);
                            if (product != null) {
                                Log.d(TAG, "Product found: " + product.getDescription()
                                        + ", category: " + product.getCategory()
                                        + ", subcategory: " + product.getSubcategory());

                                if (product.getCategory() != null && product.getCategory().equalsIgnoreCase(category)) {
                                    productList.add(product);
                                } else {
                                    Log.d(TAG, "Product category mismatch: " + product.getCategory());
                                }
                            } else {
                                Log.d(TAG, "Failed to map product for snapshot key: " + productSnap.getKey());
                            }
                        }

                        adapter.notifyDataSetChanged();

                        if (productList.isEmpty()) {
                            emptyMessage.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            emptyMessage.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "Firebase load cancelled or failed: " + error.getMessage());
                        emptyMessage.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                });
    }
}
