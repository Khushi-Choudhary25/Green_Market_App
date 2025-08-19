package khushi.example.greenmarket.ui.subcategories;
import khushi.example.greenmarket.ui.adapters.GridAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

import khushi.example.greenmarket.R;
import khushi.example.greenmarket.ui.products.StaticProductsActivity;

public class SeedsSubcategoryActivity extends AppCompatActivity {

    GridView gridView;
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeds_subcategory);

        backButton = findViewById(R.id.backButton);
        gridView = findViewById(R.id.gridView);


        backButton.setOnClickListener(v -> onBackPressed());

        List<GridItem> items = new ArrayList<>();
        items.add(new GridItem("Chia Beej", R.drawable.chia_beej));
        items.add(new GridItem("Flax Beej", R.drawable.flax_beej));
        items.add(new GridItem("Tulsi Beej", R.drawable.tulsi_beej));
        items.add(new GridItem("Pyaaj Beej", R.drawable.pyaaj_beej));
        items.add(new GridItem("Tamatar Beej", R.drawable.tamatar_beej));
        items.add(new GridItem("Palak Beej", R.drawable.palak_beej));
        items.add(new GridItem("Methi Beej", R.drawable.methi_beej));
        items.add(new GridItem("Dhaniya Beej", R.drawable.dhaniya_beej));
        items.add(new GridItem("Gajar Beej", R.drawable.gajar_beej));
        items.add(new GridItem("Mirch Beej", R.drawable.mirch_beej));
        items.add(new GridItem("Tinda Beej", R.drawable.tinda_beej));
        items.add(new GridItem("Karela Beej", R.drawable.karela_beej));
        items.add(new GridItem("Lauki Beej", R.drawable.lauki_beej));
        items.add(new GridItem("Tori Beej", R.drawable.tori_beej));
        items.add(new GridItem("Kaddu Beej", R.drawable.kaddu_beej));


        GridAdapter adapter = new GridAdapter(this, items);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener((adapterView, view, position, id) -> {
            GridItem selectedItem = (GridItem) adapterView.getItemAtPosition(position);

            if (selectedItem != null && selectedItem.getName() != null) {
                String subcategory = selectedItem.getName().trim();
                String category = "Seeds";

                Log.d("SubcategoryClick", "Sending subcategory: " + subcategory);
                Log.d("SubcategoryClick", "Sending category: " + category);

                Intent intent = new Intent(SeedsSubcategoryActivity.this, StaticProductsActivity.class);
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
