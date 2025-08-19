package khushi.example.greenmarket.ui.trends;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.database.*;

import java.text.SimpleDateFormat;
import java.util.*;

import khushi.example.greenmarket.R;

public class MarketTrendsFragment extends Fragment {

    private Spinner categorySpinner, subcategorySpinner;
    private LineChart lineChart;
    private TextView marketDataTextView;

    private final List<String> categories = Arrays.asList(
            "Cereals", "Vegetables", "Fruits", "Pulses", "Spices", "Dry Fruits", "Sugarcane",
            "Dairy Products", "Seeds", "Edible Oils", "Plants"
    );

    private final Map<String, List<String>> subcategoryMap = new HashMap<String, List<String>>() {{
        put("Cereals", Arrays.asList("Gehu", "Chawal", "Makka", "Bajra", "Jowar", "Jau", "Ragi", "Sama", "Kodo", "Kangni"));
        put("Vegetables", Arrays.asList("Aloo", "Pyaaz", "Tamatar", "Bhindi", "Baingan", "Gobi", "Patta Gobi", "Matar", "Lauki", "Tinda", "Karela", "Parwal", "Torai", "Shimla Mirch", "Hari Mirch", "Kaddu", "Palak", "Methi", "Sarso ka Saag", "Shalgam"));
        put("Fruits", Arrays.asList("Seb", "Kela", "Aam", "Santra", "Papita", "Anaar", "Angoor", "Nimbu", "Tarbooj", "Kharbooja", "Amrood", "Litchi", "Jamun", "Nariyal", "Bel"));
        put("Pulses", Arrays.asList("Arhar Dal", "Chana Dal", "Moong Dal", "Masoor Dal", "Urad Dal", "Rajma", "Kabuli Chana", "Lobia", "Kulthi Dal", "Masoor Sabut", "Moong Sabut", "Urad Sabut", "Moth Dal", "Soyabean", "Sukhe Matar", "Kala Chana", "Safed Matar"));
        put("Spices", Arrays.asList("Haldi", "Mirch", "Dhania", "Jeera", "Ajwain", "Methi Dana", "Rai", "Saunf", "Kalaunji", "Hing", "Tej Patta", "Dalchini", "Laung", "Elaichi"));
        put("Dry Fruits", Arrays.asList("Badam", "Akhrot", "Pista", "Kaju", "Chilgoza", "Makhana", "Kishmish", "Munakka", "Anjeer", "Sukha Nariyal", "Khajoor"));
        put("Sugarcane", Arrays.asList("Ganna", "Gud", "Khaand", "Cheeni", "Sirka"));
        put("Dairy Products", Arrays.asList("Doodh", "Dahi", "Makhan", "Paneer", "Ghee", "Chhachh", "Malai", "Rabri", "Mawa"));
        put("Seeds", Arrays.asList("Chia Beej", "Flax Beej", "Tulsi Beej", "Pyaaj Beej", "Tamatar Beej", "Palak Beej", "Methi Beej", "Dhaniya Beej", "Gajar Beej", "Mirch Beej", "Tinda Beej", "Karela Beej", "Lauki Beej", "Tori Beej", "Kaddu Beej"));
        put("Edible Oils", Arrays.asList("Sarso Ka Tel", "Til Ka Tel", "Saroj Tel", "Kharif Tel", "Nariyal Tel", "Soyabean Tel"));
        put("Plants", Arrays.asList("Gulab", "Surajmukhi", "Giloy", "Neem", "Aloe Vera", "Bamboo", "Ashwagandha", "Lemongrass", "Curry Patta", "Papita Paudha", "Amrood", "Aam", "Madhukamini", "Kela", "Eucalyptus", "Poplar"));
    }};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market_trends, container, false);

        categorySpinner = view.findViewById(R.id.category_spinner);
        subcategorySpinner = view.findViewById(R.id.subcategory_spinner);
        lineChart = view.findViewById(R.id.market_trend_chart);
        marketDataTextView = view.findViewById(R.id.market_data_text);

        setupSpinners();

        return view;
    }

    private void setupSpinners() {
        categorySpinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, categories));

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String selectedCategory = categories.get(pos);
                List<String> subcategories = subcategoryMap.get(selectedCategory);
                if (subcategories != null) {
                    subcategorySpinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, subcategories));
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        subcategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String category = categorySpinner.getSelectedItem().toString();
                String subcategory = subcategorySpinner.getSelectedItem().toString();
                Log.d("DEBUG_SPINNER", "Selected Category: " + category + ", Subcategory: " + subcategory);
                fetchMarketData(category, subcategory);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void fetchMarketData(String category, String subcategory) {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("market_trends")
                .child("market_trends")
                .child(category)
                .child(subcategory);

        List<String> last10Dates = getLastNDates(10);

        List<Entry> entries = new ArrayList<>();
        List<String> xAxisLabels = new ArrayList<>();

        Log.d("DEBUG_FIREBASE", "Fetching from path: market_trends/market_trends/" + category + "/" + subcategory);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("DEBUG_DATA", "Snapshot exists: " + snapshot.exists());

                int index = 0;
                entries.clear();
                xAxisLabels.clear();

                for (String date : last10Dates) {
                    DataSnapshot dateSnapshot = snapshot.child(date);
                    if (dateSnapshot.exists()) {
                        Long modalPrice = dateSnapshot.child("modal_price").getValue(Long.class);
                        if (modalPrice != null) {
                            entries.add(new Entry(index, modalPrice));
                            xAxisLabels.add(date);
                            Log.d("DEBUG_MODAL", "Date: " + date + ", Modal Price: " + modalPrice);
                            index++;
                        }
                    } else {
                        Log.d("DEBUG_SKIP", "No data for date: " + date);
                    }
                }

                LineDataSet dataSet = new LineDataSet(entries, "Modal Price (₹)");
                dataSet.setColor(getResources().getColor(R.color.purple_500));
                dataSet.setValueTextSize(12f);

                LineData lineData = new LineData(dataSet);
                lineChart.setData(lineData);

                XAxis xAxis = lineChart.getXAxis();
                xAxis.setGranularity(1f);
                xAxis.setValueFormatter(new com.github.mikephil.charting.formatter.ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        int i = (int) value;
                        return i < xAxisLabels.size() ? xAxisLabels.get(i).substring(5) : "";
                    }
                });

                lineChart.getDescription().setText("Last 10 Days");
                lineChart.invalidate();


                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < xAxisLabels.size(); i++) {
                    builder.append(xAxisLabels.get(i))
                            .append(" ➤ ₹")
                            .append((int) entries.get(i).getY())
                            .append("\n");
                }
                marketDataTextView.setText(builder.toString());
            }

            @Override public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DEBUG_CANCELLED", "Firebase error: " + error.getMessage());
            }
        });
    }

    private List<String> getLastNDates(int n) {
        List<String> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        for (int i = 0; i < n; i++) {
            dates.add(sdf.format(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }

        Collections.reverse(dates);
        return dates;
    }
}