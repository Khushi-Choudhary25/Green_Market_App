package khushi.example.greenmarket.ui.home;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import khushi.example.greenmarket.R;

public class TermsConditionsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        TextView termsTextView = findViewById(R.id.termsTextView);

        String termsHtml = "<p>By using the Green Market app, you agree to follow these terms. Please read them carefully.</p>" +

                "<p><b>1. Using the App</b>: When you use Green Market, you agree to follow our rules. These rules apply to everyone.</p>" +

                "<p><b>2. Changes to Rules</b>: We can update these rules anytime. If you keep using the app after changes, it means you accept them.</p>" +

                "<p><b>3. Your Responsibility</b>: Always give correct information. Misuse or false info may lead to account ban.</p>" +

                "<p><b>4. Your Privacy</b>: We care about your privacy. Please read our privacy policy to know how we use your data.</p>" +

                "<p><b>5. Our Content</b>: All logos, content, and features belong to Green Market. Don’t copy or misuse them.</p>" +

                "<p><b>6. Account Termination</b>: We may block your access anytime if you break the rules.</p>" +

                "<p><b>7. Indian Laws</b>: These rules follow the laws of India.</p>" +

                "<p>Thank you for using Green Market – your partner in smart and sustainable farming!</p>";


        termsTextView.setText(Html.fromHtml(termsHtml, Html.FROM_HTML_MODE_COMPACT));
    }
}
