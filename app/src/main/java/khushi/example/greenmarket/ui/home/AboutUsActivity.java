package khushi.example.greenmarket.ui.home;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import khushi.example.greenmarket.R;

public class AboutUsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);


        findViewById(R.id.backButton).setOnClickListener(view -> onBackPressed());
    }
}
