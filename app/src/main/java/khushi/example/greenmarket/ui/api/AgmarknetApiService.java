package khushi.example.greenmarket.ui.api;

import khushi.example.greenmarket.ui.model.MarketResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AgmarknetApiService {
    @GET("api/APMCPrices")
    Call<MarketResponse> getMarketPrices();
}
