package common.models;

public class BeneficiaryGroup {
    private String benid;
    private String bengroup;
    private String bendesc;

    public BeneficiaryGroup() {}

    public BeneficiaryGroup(String benid, String bengroup, String bendesc) {
        this.benid = benid;
        this.bengroup = bengroup;
        this.bendesc = bendesc;
    }

    public String getBenid() { return benid; }
    public void setBenid(String benid) { this.benid = benid; }
    public String getBengroup() { return bengroup; }
    public void setBengroup(String bengroup) { this.bengroup = bengroup; }
    public String getBendesc() { return bendesc; }
    public void setBendesc(String bendesc) { this.bendesc = bendesc; }

    @Override
    public String toString() {
        return "BeneficiaryGroup{" +
                "benid='" + benid + '\'' +
                ", bengroup='" + bengroup + '\'' +
                ", bendesc='" + bendesc + '\'' +
                '}';
    }
}
