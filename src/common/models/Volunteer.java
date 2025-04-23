package common.models;

import java.time.LocalDate;

public class Volunteer {
    private String volid;
    private String fname;
    private String lname;
    private String address;
    private String phone;
    private String email;
    private String password;
    private LocalDate birthday;
    private String sex;
    private String volstat;
    private String role;

    public Volunteer() {
    }

    public Volunteer(String volid, String fname, String lname, String address, String phone, String email, String password, LocalDate birthday, String sex, String volstat, String role) {
        this.volid = volid;
        this.fname = fname;
        this.lname = lname;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.birthday = birthday;
        this.sex = sex;
        this.volstat = volstat;
        this.role = role;
    }

    public String getVolid() {
        return volid;
    }

    public void setVolid(String volid) {
        this.volid = volid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getVolstat() {
        return volstat;
    }

    public void setVolstat(String volstat) {
        this.volstat = volstat;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Volunteer{" +
                "volid='" + volid + '\'' +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", birthday=" + birthday +
                ", sex='" + sex + '\'' +
                ", volstat='" + volstat + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
