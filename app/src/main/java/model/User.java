package model;

public class User {
    private String department, designation, fullName, img, mobileNo, role;

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public User(String department, String designation, String fullName, String img, String mobileNo, String role) {
        this.department = department;
        this.designation = designation;
        this.fullName = fullName;
        this.img = img;
        this.mobileNo = mobileNo;
        this.role = role;
    }

    public User() {
    }
}
