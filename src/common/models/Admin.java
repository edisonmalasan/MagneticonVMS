package common.models;

public class Admin {
    private String volid;
    private String certification;
    private String skills;

    public Admin() {}

    public Admin(String volid, String certification, String skills) {
        this.volid = volid;
        this.certification = certification;
        this.skills = skills;
    }

    public String getVolid() { return volid; }
    public void setVolid(String volid) { this.volid = volid; }
    public String getCertification() { return certification; }
    public void setCertification(String certification) { this.certification = certification; }
    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    @Override
    public String toString() {
        return "Admin{" +
                "volid='" + volid + '\'' +
                ", certification='" + certification + '\'' +
                ", skills='" + skills + '\'' +
                '}';
    }
}
