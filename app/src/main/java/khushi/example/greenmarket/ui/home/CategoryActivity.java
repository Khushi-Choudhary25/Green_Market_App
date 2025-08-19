package khushi.example.greenmarket.ui.home;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import khushi.example.greenmarket.R;
import khushi.example.greenmarket.ui.adapters.ProductAdapter;
import khushi.example.greenmarket.ui.model.Product;

public class CategoryActivity extends AppCompatActivity {

    TextView categoryTitle;
    RecyclerView productRecycler;
    ProductAdapter adapter;
    List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        categoryTitle = findViewById(R.id.categoryTitle);
        productRecycler = findViewById(R.id.productRecycler);
        productRecycler.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();
        adapter = new ProductAdapter(this, productList);
        productRecycler.setAdapter(adapter);


        String category = getIntent().getStringExtra("category_name");
        String subcategory = getIntent().getStringExtra("subcategory_name");

        if (category == null || category.isEmpty()) {
            Toast.makeText(this, "Category not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (subcategory == null || subcategory.isEmpty()) {
            Toast.makeText(this, "Subcategory not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        categoryTitle.setText(category + " > " + subcategory);


        loadProductsFromRealtimeDB(category, subcategory);
    }

    private void loadProductsFromRealtimeDB(String category, String subcategory) {
        FirebaseDatabase.getInstance().getReference("Products")
                .child(category)
                .child(subcategory)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot productsSnapshot) {
                        productList.clear();
                        for (DataSnapshot productSnapshot : productsSnapshot.getChildren()) {
                            Product product = productSnapshot.getValue(Product.class);
                            if (product != null) {
                                productList.add(product);
                            }
                        }
                        adapter.notifyDataSetChanged();

                        if (productList.isEmpty()) {
                            Toast.makeText(CategoryActivity.this, "No products found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(CategoryActivity.this, "Failed to load products", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
