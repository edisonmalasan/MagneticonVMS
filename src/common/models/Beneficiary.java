package common.models;

public class Beneficiary {
    private String servid;
    private String benid;

    public Beneficiary() {}

    public Beneficiary(String servid, String benid) {
        this.servid = servid;
        this.benid = benid;
    }

    public String getServid() { return servid; }
    public void setServid(String servid) { this.servid = servid; }
    public String getBenid() { return benid; }
    public void setBenid(String benid) { this.benid = benid; }
}
