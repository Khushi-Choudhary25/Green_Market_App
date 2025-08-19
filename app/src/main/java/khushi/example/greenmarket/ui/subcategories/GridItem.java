package khushi.example.greenmarket.ui.subcategories;

public class GridItem {
    private String name;
    private int imageResId;

    public GridItem(String name, int imageResId) {
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
