package khushi.example.greenmarket.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import khushi.example.greenmarket.R;
import khushi.example.greenmarket.ui.adapters.ImageAdapter;

import java.util.*;

public class AddProductActivity extends AppCompatActivity {

    RadioGroup radioGroupFarmingType;
    Spinner spinnerCategory, spinnerSubcategory, spinnerUnit;
    EditText editQuantity, editBasePrice, editExpectedDelivery, editProductDescription;
    Button btnSelectImages, btnPostProduct;
    RecyclerView recyclerViewImages;
    TextView txtSelectedAddress;
    ImageAdapter imageAdapter;
    ArrayList<Uri> imageUriList = new ArrayList<>();

    private static final int ADDRESS_REQUEST_CODE = 100;
    ActivityResultLauncher<Intent> imagePickerLauncher;

    DatabaseReference productDatabaseRef;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        productDatabaseRef = FirebaseDatabase.getInstance().getReference("products");
        storageRef = FirebaseStorage.getInstance().getReference("product_images");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Add Product");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        radioGroupFarmingType = findViewById(R.id.radioGroupFarmingType);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerSubcategory = findViewById(R.id.spinnerSubcategory);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        editQuantity = findViewById(R.id.editQuantity);
        editBasePrice = findViewById(R.id.editBasePrice);
        editExpectedDelivery = findViewById(R.id.editExpectedDelivery);
        editProductDescription = findViewById(R.id.editProductDescription);
        btnSelectImages = findViewById(R.id.buttonSelectImage);
        btnPostProduct = findViewById(R.id.btnPostProduct);
        recyclerViewImages = findViewById(R.id.recyclerViewImages);
        txtSelectedAddress = findViewById(R.id.txtAddress);

        recyclerViewImages.setLayoutManager(new GridLayoutManager(this, 2));


        List<String> stringUriList = new ArrayList<>();
        for (Uri uri : imageUriList) {
            stringUriList.add(uri.toString());
        }

        imageAdapter = new ImageAdapter(stringUriList);
        recyclerViewImages.setAdapter(imageAdapter);

        txtSelectedAddress.setOnClickListener(v -> {
            Intent intent = new Intent(AddProductActivity.this, SelectAddressActivity.class);
            startActivityForResult(intent, ADDRESS_REQUEST_CODE);
        });

        Map<String, String[]> categorySubcategoryMap = getCategoryCommodityMap();
        String[] categories = categorySubcategoryMap.keySet().toArray(new String[0]);

        setSpinnerAdapter(spinnerCategory, categories);
        setSpinnerAdapter(spinnerSubcategory, new String[]{});
        setSpinnerAdapter(spinnerUnit, new String[]{"GM", "KG", "Quintal", "Ton", "PCS"});

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = categories[position];
                String[] subcategories = categorySubcategoryMap.get(selectedCategory);
                setSpinnerAdapter(spinnerSubcategory, subcategories != null ? subcategories : new String[]{});
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                if (result.getData().getClipData() != null) {
                    int count = result.getData().getClipData().getItemCount();
                    for (int i = 0; i < count && imageUriList.size() < 5; i++) {
                        Uri uri = result.getData().getClipData().getItemAt(i).getUri();
                        imageUriList.add(uri);
                    }
                } else if (result.getData().getData() != null && imageUriList.size() < 5) {
                    imageUriList.add(result.getData().getData());
                }


                List<String> newStringList = new ArrayList<>();
                for (Uri uri : imageUriList) {
                    newStringList.add(uri.toString());
                }
                imageAdapter = new ImageAdapter(newStringList);
                recyclerViewImages.setAdapter(imageAdapter);
            }
        });

        btnSelectImages.setOnClickListener(v -> selectImages());

        btnPostProduct.setOnClickListener(v -> {
            if (imageUriList.isEmpty()) {
                Toast.makeText(this, "⚠️ Select at least one image", Toast.LENGTH_SHORT).show();
                return;
            }
            uploadImagesAndSaveProduct();
        });
    }

    private void uploadImagesAndSaveProduct() {
        List<String> imageUrls = new ArrayList<>();
        String productId = productDatabaseRef.push().getKey();

        if (productId == null) {
            Toast.makeText(this, "❌ Failed to generate product ID", Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; i < imageUriList.size(); i++) {
            Uri imageUri = imageUriList.get(i);
            StorageReference imageRef = storageRef.child(productId + "/image_" + i);

            int finalI = i;
            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                imageUrls.add(uri.toString());
                                if (imageUrls.size() == imageUriList.size()) {
                                    saveProductToDatabase(productId, imageUrls);
                                }
                            }).addOnFailureListener(e ->
                                    Toast.makeText(this, "❌ URL fetch failed: " + e.getMessage(), Toast.LENGTH_SHORT).show())
                    )
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "❌ Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
        }
    }

    private void saveProductToDatabase(String productId, List<String> imageUrls) {
        Map<String, Object> productData = new HashMap<>();
        productData.put("category", spinnerCategory.getSelectedItem().toString());
        productData.put("subcategory", spinnerSubcategory.getSelectedItem().toString());
        productData.put("unit", spinnerUnit.getSelectedItem().toString());
        productData.put("quantity", editQuantity.getText().toString());
        productData.put("basePrice", editBasePrice.getText().toString());
        productData.put("expectedDelivery", editExpectedDelivery.getText().toString());
        productData.put("address", txtSelectedAddress.getText().toString());
        productData.put("description", editProductDescription.getText().toString());

        int selectedRadioId = radioGroupFarmingType.getCheckedRadioButtonId();
        if (selectedRadioId != -1) {
            RadioButton selectedRadio = findViewById(selectedRadioId);
            productData.put("farmingType", selectedRadio.getText().toString());
        } else {
            Toast.makeText(this, "⚠️ Please select Farming Type", Toast.LENGTH_SHORT).show();
            return;
        }

        productData.put("imageUrls", imageUrls);

        productDatabaseRef.child(productId).setValue(productData)
                .addOnSuccessListener(unused -> Toast.makeText(this, "✅ Product Posted Successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "❌ Failed to Post Product: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADDRESS_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            txtSelectedAddress.setText(data.getStringExtra("selectedAddress"));
        } else {
            Toast.makeText(this, "⚠️ Address not selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectImages() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        imagePickerLauncher.launch(Intent.createChooser(intent, "Select up to 5 images"));
    }

    private void setSpinnerAdapter(Spinner spinner, String[] items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
    }

    private Map<String, String[]> getCategoryCommodityMap() {
        Map<String, String[]> map = new HashMap<>();
        map.put("Cereals", new String[]{"Gehu", "Chawal", "Makka", "Bajra", "Jowar", "Jau", "Ragi", "Sama", "Kodo", "Kangni"});
        map.put("Vegetables", new String[]{"Aloo", "Pyaaz", "Tamatar", "Bhindi", "Baingan", "Gobi", "Patta Gobi", "Matar", "Lauki", "Tinda", "Karela", "Parwal", "Torai", "Shimla Mirch", "Hari Mirch", "Kaddu", "Palak", "Methi", "Sarso ka Saag", "Shalgam"});
        map.put("Fruits", new String[]{"Seb", "Kela", "Aam", "Santra", "Papita", "Anaar", "Angoor", "Nimbu", "Tarbooj", "Kharbooja", "Amrood", "Litchi", "Jamun", "Nariyal", "Bel"});
        map.put("Pulses", new String[]{"Arhar Dal", "Chana Dal", "Moong Dal", "Masoor Dal", "Urad Dal", "Rajma", "Kabuli Chana", "Lobia", "Kulthi Dal", "Masoor Sabut", "Moong Sabut", "Urad Sabut", "Moth Dal", "Soyabean", "Sukhe Matar", "Kala Chana", "Safed Matar"});
        map.put("Spices", new String[]{"Haldi", "Mirch", "Dhania", "Jeera", "Ajwain", "Methi Dana", "Rai", "Saunf", "Kalaunji", "Hing", "Tej Patta", "Dalchini", "Laung", "Elaichi"});
        map.put("Dry Fruits", new String[]{"Badam", "Akhrot", "Pista", "Kaju", "Chilgoza", "Makhana", "Kishmish", "Munakka", "Anjeer", "Sukha Nariyal", "Khajoor"});
        map.put("Sugarcane", new String[]{"Ganna", "Gud", "Khaand", "Cheeni", "Sirka"});
        map.put("Dairy Products", new String[]{"Doodh", "Dahi", "Makhan", "Paneer", "Ghee", "Chhachh", "Malai", "Rabri", "Mawa"});
        map.put("Seeds", new String[]{"Chia Beej", "Flax Beej", "Tulsi Beej", "Pyaaj Beej", "Tamatar Beej", "Palak Beej", "Methi Beej", "Dhaniya Beej", "Gajar Beej", "Mirch Beej", "Tinda Beej", "Karela Beej", "Lauki Beej", "Tori Beej", "Kaddu Beej"});
        map.put("Edible Oils", new String[]{"Sarso Ka Tel", "Til Ka Tel", "Saroj Tel", "Kharif Tel", "Nariyal Tel", "Soyabean Tel"});
        map.put("Plants", new String[]{"Gulab", "Surajmukhi", "Giloy", "Neem", "Aloe Vera", "Bamboo", "Ashwagandha", "Lemongrass", "Curry Patta", "Papita Paudha", "Amrood", "Aam", "Madhukamini", "Kela", "Eucalyptus", "Poplar"});
        return map;
    }
}
