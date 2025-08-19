package khushi.example.greenmarket.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import khushi.example.greenmarket.R;

public class SettingsActivity extends AppCompatActivity {

    private ImageView backButton;
    private LinearLayout changeLanguageLayout, helpSupportLayout, termsLayout,
            shareAppLayout, aboutUsLayout, privacyPolicyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        changeLanguageLayout = findViewById(R.id.changeLanguageLayout);
        helpSupportLayout = findViewById(R.id.helpSupportLayout);
        termsLayout = findViewById(R.id.termsLayout);
        shareAppLayout = findViewById(R.id.shareAppLayout);
        aboutUsLayout = findViewById(R.id.aboutUsLayout);
        privacyPolicyLayout = findViewById(R.id.privacyPolicyLayout);

        changeLanguageLayout.setOnClickListener(v -> {
            startActivity(new Intent(this, ChangeLanguageActivity.class));
        });

        helpSupportLayout.setOnClickListener(v -> {
            startActivity(new Intent(this, HelpSupportActivity.class));
        });

        termsLayout.setOnClickListener(v -> {
            startActivity(new Intent(this, TermsConditionsActivity.class));
        });

        shareAppLayout.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Green Market");
            intent.putExtra(Intent.EXTRA_TEXT, "Download this app: [Your App Link]");
            startActivity(Intent.createChooser(intent, "Share App via"));
        });

        aboutUsLayout.setOnClickListener(v -> {
            startActivity(new Intent(this, AboutUsActivity.class));
        });

        privacyPolicyLayout.setOnClickListener(v -> {
            startActivity(new Intent(this, PrivacyPolicyActivity.class));
        });
    }
}
