package common.models;

public class Team {
    private String teamid;
    private String tname;
    private String tdesc;

    public Team() {}

    public Team(String teamid, String tname, String tdesc) {
        this.teamid = teamid;
        this.tname = tname;
        this.tdesc = tdesc;
    }

    public String getTeamid() { return teamid; }
    public void setTeamid(String teamid) { this.teamid = teamid; }
    public String getTname() { return tname; }
    public void setTname(String teamname) { this.tname = tname; }
    public String getTdesc() { return tdesc; }
    public void setTdesc(String tdesc) { this.tdesc = tdesc; }
}
