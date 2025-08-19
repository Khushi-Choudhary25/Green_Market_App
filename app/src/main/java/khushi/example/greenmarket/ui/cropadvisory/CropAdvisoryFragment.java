package khushi.example.greenmarket.ui.cropadvisory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.*;

import java.util.*;

import khushi.example.greenmarket.R;

public class CropAdvisoryFragment extends Fragment {

    private Spinner categorySpinner, subcategorySpinner;
    private RecyclerView adviceRecyclerView;
    private AdvisoryAdapter adapter;
    private List<String> adviceList = new ArrayList<>();
    private Map<String, String[]> map = new LinkedHashMap<>();

    public CropAdvisoryFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crop_advisory, container, false);

        categorySpinner = view.findViewById(R.id.categorySpinner);
        subcategorySpinner = view.findViewById(R.id.subcategorySpinner);
        adviceRecyclerView = view.findViewById(R.id.adviceRecyclerView);

        adviceRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AdvisoryAdapter(adviceList);
        adviceRecyclerView.setAdapter(adapter);

        initializeMap();
        setupSpinners();

        return view;
    }

    private void initializeMap() {
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
    }

    private void setupSpinners() {
        List<String> categories = new ArrayList<>();
        categories.add("Select Category");
        categories.addAll(map.keySet());

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, categories);
        categorySpinner.setAdapter(categoryAdapter);

        setSubcategoryToDefault();

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String selectedCategory = categories.get(pos);
                if (pos == 0) {
                    setSubcategoryToDefault();
                    adviceList.clear();
                    adapter.notifyDataSetChanged();
                } else {
                    String[] subArray = map.get(selectedCategory);
                    List<String> subList = new ArrayList<>();
                    subList.add("Select Subcategory");
                    if (subArray != null) subList.addAll(Arrays.asList(subArray));
                    ArrayAdapter<String> subAdapter = new ArrayAdapter<>(requireContext(),
                            android.R.layout.simple_spinner_dropdown_item, subList);
                    subcategorySpinner.setAdapter(subAdapter);
                    subcategorySpinner.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        subcategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String selectedCategory = (String) categorySpinner.getSelectedItem();
                String selectedSub = (String) subcategorySpinner.getSelectedItem();

                if (!"Select Category".equals(selectedCategory) &&
                        !"Select Subcategory".equals(selectedSub)) {
                    loadAdvices(selectedCategory, selectedSub);
                } else {
                    adviceList.clear();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setSubcategoryToDefault() {
        List<String> defaultList = Collections.singletonList("Select Subcategory");
        ArrayAdapter<String> subAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, defaultList);
        subcategorySpinner.setAdapter(subAdapter);
    }

    private void loadAdvices(String category, String subcategory) {
        DatabaseReference adviceRef = FirebaseDatabase.getInstance()
                .getReference("advisory/advisories/" + category + "/" + subcategory + "/advices");

        adviceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adviceList.clear();
                for (DataSnapshot adviceSnap : snapshot.getChildren()) {
                    String advice = adviceSnap.getValue(String.class);
                    if (advice != null) {
                        adviceList.add(advice);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load advices", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
