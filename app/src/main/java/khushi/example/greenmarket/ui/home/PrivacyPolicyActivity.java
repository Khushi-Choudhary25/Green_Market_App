package khushi.example.greenmarket.ui.home;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import khushi.example.greenmarket.R;

public class PrivacyPolicyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        TextView privacyTextView = findViewById(R.id.privacyTextView);

        String privacyHtml = "<p>We value your privacy and are committed to protecting your personal information.</p>" +

                "<p><b>1. Data Collection</b>: We collect only necessary information like your name, contact, and location to improve your experience.</p>" +

                "<p><b>2. Use of Data</b>: Your data is used to offer personalized services, show nearby markets, and notify you of updates.</p>" +

                "<p><b>3. Data Security</b>: We use secure methods to keep your data safe. We don’t share it with third parties without your permission.</p>" +

                "<p><b>4. Permissions</b>: The app may request access to your location, camera, or storage to work properly. You can allow or deny these anytime.</p>" +

                "<p><b>5. User Control</b>: You can view or delete your data anytime by contacting our support.</p>" +

                "<p><b>6. Changes to Policy</b>: We may update this policy. Please check this page regularly for updates.</p>" +

                "<p>Thank you for trusting Green Market. We are committed to your safety and privacy.</p>";

        privacyTextView.setText(Html.fromHtml(privacyHtml, Html.FROM_HTML_MODE_COMPACT));
    }
}
