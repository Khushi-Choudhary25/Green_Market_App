package khushi.example.greenmarket;
public class UserModel {
    public String fullName, idType, idNumber, userType, state, villageName, fullAddress, pincode, farmingType;

    public UserModel() {

    }

    public UserModel(String fullName, String idType, String idNumber, String userType, String state,
                     String villageName, String fullAddress, String pincode, String farmingType) {
        this.fullName = fullName;
        this.idType = idType;
        this.idNumber = idNumber;
        this.userType = userType;
        this.state = state;
        this.villageName = villageName;
        this.fullAddress = fullAddress;
        this.pincode = pincode;
        this.farmingType = farmingType;
    }
}
