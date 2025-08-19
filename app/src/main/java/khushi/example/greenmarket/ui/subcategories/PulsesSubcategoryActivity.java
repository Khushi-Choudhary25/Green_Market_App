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

public class PulsesSubcategoryActivity extends AppCompatActivity {

    GridView gridView;
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pulses_subcategory);

        backButton = findViewById(R.id.backButton);
        gridView = findViewById(R.id.gridView);


        backButton.setOnClickListener(v -> onBackPressed());

        List<GridItem> items = new ArrayList<>();
        items.add(new GridItem("Arhar Dal", R.drawable.arhar_dal));
        items.add(new GridItem("Chana Dal", R.drawable.chana_dal));
        items.add(new GridItem("Moong Dal", R.drawable.moong_dal));
        items.add(new GridItem("Masoor Dal", R.drawable.masoor_dal));
        items.add(new GridItem("Urad Dal", R.drawable.urad_dal));
        items.add(new GridItem("Rajma", R.drawable.rajma));
        items.add(new GridItem("Kabuli Chana", R.drawable.kabuli_chana));
        items.add(new GridItem("Lobia", R.drawable.lobia));
        items.add(new GridItem("Kulthi Dal", R.drawable.kulthi_dal));
        items.add(new GridItem("Masoor Sabut", R.drawable.masoor_sabut));
        items.add(new GridItem("Moong Sabut", R.drawable.moong_sabut));
        items.add(new GridItem("Urad Sabut", R.drawable.urad_sabut));
        items.add(new GridItem("Moth Dal", R.drawable.moth_dal));
        items.add(new GridItem("Soyabean", R.drawable.soyabean));
        items.add(new GridItem("Sukhe Matar", R.drawable.sukhe_matar));
        items.add(new GridItem("Kala Chana", R.drawable.kala_chana));
        items.add(new GridItem("Safed Matar", R.drawable.safed_matar));


        GridAdapter adapter = new GridAdapter(this, items);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener((adapterView, view, position, id) -> {
            GridItem selectedItem = (GridItem) adapterView.getItemAtPosition(position);

            if (selectedItem != null && selectedItem.getName() != null) {
                String subcategory = selectedItem.getName().trim();
                String category = "Pulses";

                Log.d("SubcategoryClick", "Sending subcategory: " + subcategory);
                Log.d("SubcategoryClick", "Sending category: " + category);

                Intent intent = new Intent(PulsesSubcategoryActivity.this, StaticProductsActivity.class);
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
