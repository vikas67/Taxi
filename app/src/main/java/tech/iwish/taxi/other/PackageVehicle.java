package tech.iwish.taxi.other;

public class PackageVehicle {
    private String  packid;
    private String  package_type;
    private String  vahicle_cat_id;
    private String  amount;

    public PackageVehicle(String packid, String package_type, String vahicle_cat_id, String amount) {
        this.packid = packid;
        this.package_type = package_type;
        this.vahicle_cat_id = vahicle_cat_id;
        this.amount = amount;
    }


    public String getPackid() {
        return packid;
    }

    public void setPackid(String packid) {
        this.packid = packid;
    }

    public String getPackage_type() {
        return package_type;
    }

    public void setPackage_type(String package_type) {
        this.package_type = package_type;
    }

    public String getVahicle_cat_id() {
        return vahicle_cat_id;
    }

    public void setVahicle_cat_id(String vahicle_cat_id) {
        this.vahicle_cat_id = vahicle_cat_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}