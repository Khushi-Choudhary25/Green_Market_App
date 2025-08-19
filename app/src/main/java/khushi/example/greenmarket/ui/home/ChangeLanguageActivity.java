package khushi.example.greenmarket.ui.home;
import khushi.example.greenmarket.MainActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;

import khushi.example.greenmarket.LoginActivity;
import khushi.example.greenmarket.R;

public class ChangeLanguageActivity extends AppCompatActivity {

    private RadioGroup radioGroupLanguages;
    private RadioButton radioEnglish, radioHindi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_selection);

        FirebaseApp.initializeApp(this);

        radioGroupLanguages = findViewById(R.id.radioGroupLanguages);
        radioEnglish = findViewById(R.id.radioEnglish);
        radioHindi = findViewById(R.id.radioHindi);

        radioGroupLanguages.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioEnglish) {
                Toast.makeText(this, "English Selected", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else if (checkedId == R.id.radioHindi) {
                Toast.makeText(this, "हिंदी चुनी गई", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        });
    }


    public void onBackPressed(android.view.View view) {
        super.onBackPressed();
    }
}
