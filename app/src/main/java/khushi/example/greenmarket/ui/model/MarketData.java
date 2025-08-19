package khushi.example.greenmarket.ui.model;

import com.google.gson.annotations.SerializedName;

public class MarketData {

    @SerializedName("state")
    private String state;

    @SerializedName("district")
    private String district;

    @SerializedName("market")
    private String market;

    @SerializedName("commodity")
    private String commodity;

    @SerializedName("min_price")
    private String minPrice;

    @SerializedName("max_price")
    private String maxPrice;

    public String getState() {
        return state;
    }

    public String getDistrict() {
        return district;
    }

    public String getMarket() {
        return market;
    }

    public String getCommodity() {
        return commodity;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }
}
