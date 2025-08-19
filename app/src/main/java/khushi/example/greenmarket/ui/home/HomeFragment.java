package khushi.example.greenmarket.ui.home;

import khushi.example.greenmarket.SliderAdapter;
import khushi.example.greenmarket.ui.subcategories.CerealsSubcategoryActivity;
import khushi.example.greenmarket.ui.subcategories.FruitsSubcategoryActivity;
import khushi.example.greenmarket.ui.subcategories.VegetablesSubcategoryActivity;
import khushi.example.greenmarket.ui.subcategories.DairyProductsSubcategoryActivity;
import khushi.example.greenmarket.ui.subcategories.DryFruitsSubcategoryActivity;
import khushi.example.greenmarket.ui.subcategories.PulsesSubcategoryActivity;
import khushi.example.greenmarket.ui.subcategories.SpicesSubcategoryActivity;
import khushi.example.greenmarket.ui.subcategories.SugarcaneSubcategoryActivity;
import khushi.example.greenmarket.ui.subcategories.EdibleOilsSubcategoryActivity;
import khushi.example.greenmarket.ui.subcategories.SeedsSubcategoryActivity;
import khushi.example.greenmarket.ui.subcategories.PlantsSubcategoryActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;
import khushi.example.greenmarket.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private ViewPager2 viewPager;
    private Handler sliderHandler = new Handler();
    private Runnable sliderRunnable;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        viewPager = view.findViewById(R.id.viewPagerSlider);
        WormDotsIndicator dotsIndicator = view.findViewById(R.id.dotsIndicator);

        List<Integer> sliderImages = new ArrayList<>();
        sliderImages.add(R.drawable.slider1);
        sliderImages.add(R.drawable.slider2);
        sliderImages.add(R.drawable.slider3);
        sliderImages.add(R.drawable.slider4);
        sliderImages.add(R.drawable.slider5);
        sliderImages.add(R.drawable.slider6);
        sliderImages.add(R.drawable.slider7);

        SliderAdapter adapter = new SliderAdapter(sliderImages);
        viewPager.setAdapter(adapter);

        dotsIndicator.setViewPager2(viewPager);

        sliderRunnable = () -> {
            int next = viewPager.getCurrentItem() + 1;
            viewPager.setCurrentItem(next % sliderImages.size(), true);
            sliderHandler.postDelayed(sliderRunnable, 3000);
        };
        sliderHandler.postDelayed(sliderRunnable, 3000);

        setupCategoryClickListeners(view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void setupCategoryClickListeners(View view) {
        setupClick(view, R.id.cerealsLayout, CerealsSubcategoryActivity.class);
        setupClick(view, R.id.vegetablesLayout, VegetablesSubcategoryActivity.class);
        setupClick(view, R.id.fruitsLayout, FruitsSubcategoryActivity.class);
        setupClick(view, R.id.pulsesLayout, PulsesSubcategoryActivity.class);
        setupClick(view, R.id.spicesLayout, SpicesSubcategoryActivity.class);
        setupClick(view, R.id.dryFruitsLayout, DryFruitsSubcategoryActivity.class);
        setupClick(view, R.id.sugarcaneLayout, SugarcaneSubcategoryActivity.class);
        setupClick(view, R.id.dairyproductsLayout, DairyProductsSubcategoryActivity.class);
        setupClick(view, R.id.seedsLayout, SeedsSubcategoryActivity.class);
        setupClick(view, R.id.edibleoilsLayout, EdibleOilsSubcategoryActivity.class);
        setupClick(view, R.id.plantsLayout, PlantsSubcategoryActivity.class);
    }

    private void setupClick(View view, int layoutId, Class<?> activityClass) {
        LinearLayout layout = view.findViewById(layoutId);
        layout.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), activityClass);
            startActivity(intent);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sliderHandler.removeCallbacks(sliderRunnable);
    }
}
