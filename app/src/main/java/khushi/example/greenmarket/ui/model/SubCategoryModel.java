package khushi.example.greenmarket.ui.subcategories;

public class SubCategoryModel {
    private String name;
    private int imageResId;

    public SubCategoryModel(String name, int imageResId) {
        this.name = name;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }
}
