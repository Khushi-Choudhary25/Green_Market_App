package khushi.example.greenmarket;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class OtpTextWatcher implements TextWatcher {

    private EditText nextEditText;

    public OtpTextWatcher(EditText nextEditText) {
        this.nextEditText = nextEditText;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.length() == 1 && nextEditText != null) {
            nextEditText.requestFocus();
        }
    }
}
