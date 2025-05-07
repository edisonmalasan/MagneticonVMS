package common.models;

public class Service {
    private String servid;
    private String sname;
    private String sdesc;
    private String sstat;
    private String teamid;

    public Service() {}

    public Service(String servid, String sname, String sdesc, String sstat, String teamid) {
        this.servid = servid;
        this.sname = sname;
        this.sdesc = sdesc;
        this.sstat = sstat;
        this.teamid = teamid;
    }

    public String getServid() {
        return servid;
    }

    public void setServid(String servid) {
        this.servid = servid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getSdesc() {
        return sdesc;
    }

    public void setSdesc(String sdesc) {
        this.sdesc = sdesc;
    }

    public String getSstat() {
        return sstat;
    }

    public void setSstat(String sstat) {
        this.sstat = sstat;
    }

    public String getTeamid() {
        return teamid;
    }

    public void setTeamid(String teamid) {
        this.teamid = teamid;
    }

    @Override
    public String toString() {
        return "Service{" +
                "servid='" + servid + '\'' +
                ", sname='" + sname + '\'' +
                ", sdesc='" + sdesc + '\'' +
                ", sstat='" + sstat + '\'' +
                ", teamid='" + teamid + '\'' +
                '}';
    }

    public boolean isEmpty() {return false;}
}
