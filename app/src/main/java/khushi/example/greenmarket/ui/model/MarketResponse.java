package khushi.example.greenmarket.ui.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MarketResponse {
    @SerializedName("records")
    private List<MarketData> records;

    public List<MarketData> getRecords() {
        return records;
    }
}
