package khushi.example.greenmarket.ui.subcategories;
import khushi.example.greenmarket.ui.adapters.GridAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

import khushi.example.greenmarket.R;
import khushi.example.greenmarket.ui.products.StaticProductsActivity;

public class SpicesSubcategoryActivity extends AppCompatActivity {

    GridView gridView;
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spices_subcategory);

        backButton = findViewById(R.id.backButton);
        gridView = findViewById(R.id.gridView);


        backButton.setOnClickListener(v -> onBackPressed());

        List<GridItem> items = new ArrayList<>();
        items.add(new GridItem("Haldi", R.drawable.haldi));
        items.add(new GridItem("Mirch", R.drawable.mirch));
        items.add(new GridItem("Dhania", R.drawable.dhania));
        items.add(new GridItem("Jeera", R.drawable.jeera));
        items.add(new GridItem("Ajwain", R.drawable.ajwain));
        items.add(new GridItem("Methi Dana", R.drawable.methi_dana));
        items.add(new GridItem("Rai", R.drawable.rai));
        items.add(new GridItem("Saunf", R.drawable.saunf));
        items.add(new GridItem("Kalaunji", R.drawable.kalaunji));
        items.add(new GridItem("Hing", R.drawable.hing));
        items.add(new GridItem("Tej Patta", R.drawable.tej_patta));
        items.add(new GridItem("Dalchini", R.drawable.dalchini));
        items.add(new GridItem("Laung", R.drawable.laung));
        items.add(new GridItem("Elaichi", R.drawable.elaichi));

        GridAdapter adapter = new GridAdapter(this, items);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener((adapterView, view, position, id) -> {
            GridItem selectedItem = (GridItem) adapterView.getItemAtPosition(position);

            if (selectedItem != null && selectedItem.getName() != null) {
                String subcategory = selectedItem.getName().trim();
                String category = "Spices";

                Log.d("SubcategoryClick", "Sending subcategory: " + subcategory);
                Log.d("SubcategoryClick", "Sending category: " + category);

                Intent intent = new Intent(SpicesSubcategoryActivity.this, StaticProductsActivity.class);
                intent.putExtra("subcategory", subcategory);
                intent.putExtra("category", category);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Selected subcategory is null", Toast.LENGTH_SHORT).show();
                Log.e("SubcategoryClick", "selectedItem or name is null");
            }
        });
    }
}
