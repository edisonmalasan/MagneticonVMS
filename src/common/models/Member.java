package common.models;

public class Member {
    private String volid;
    private String availability;

    public Member() {}

    public Member(String volid, String availability) {
        this.volid = volid;
        this.availability = availability;
    }

    public String getVolid() { return volid; }
    public void setVolid(String volid) { this.volid = volid; }
    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }

    @Override
    public String toString() {
        return "Member{" +
                "volid='" + volid + '\'' +
                ", availability='" + availability + '\'' +
                '}';
    }
}
