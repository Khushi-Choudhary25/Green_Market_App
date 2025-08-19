package khushi.example.greenmarket.ui.model;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {
    private String description;
    private String quantity;
    private String basePrice;
    private List<String> imageUrls;
    private String subcategory;
    private String category;
    private String unit;
    private String address;
    private String expectedDelivery;
    private String farmingType;

    public Product() {

    }

    public String getDescription() {
        return description;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getBasePrice() {
        return basePrice;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public String getImageUrl() {
        if (imageUrls != null && !imageUrls.isEmpty()) {
            return imageUrls.get(0);
        }
        return null;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public String getCategory() {
        return category;
    }

    public String getUnit() {
        return unit;
    }

    public String getAddress() {
        return address;
    }

    public String getExpectedDelivery() {
        return expectedDelivery;
    }

    public String getFarmingType() {
        return farmingType;
    }
}
