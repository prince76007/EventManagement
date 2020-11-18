package model;

public class Guest {
   private String email;
    private String fullName;
    private String address;
    private String mobileNo;
    private String company;
    private String desc;
    private String designation;

    public Guest(String email, String fullName, String address, String mobileNo, String company, String desc, String designation) {
        this.email = email;
        this.fullName = fullName;
        this.address = address;
        this.mobileNo = mobileNo;
        this.company = company;
        this.desc = desc;
        this.designation = designation;
    }

    public Guest(){

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
