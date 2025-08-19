package khushi.example.greenmarket.ui.model;

public class AddressModel {
    private String name, fullAddress, phone;

    public AddressModel(String name, String fullAddress, String phone) {
        this.name = name;
        this.fullAddress = fullAddress;
        this.phone = phone;
    }

    public String getName() { return name; }
    public String getFullAddress() { return fullAddress; }
    public String getPhone() { return phone; }
}
