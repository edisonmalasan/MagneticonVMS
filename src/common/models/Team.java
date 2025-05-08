package common.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Team {
    private String teamid;
    private String tname;
    private String tdesc;
    private List<String> members;

    public Team() {
        this.members = new ArrayList<>();
    }

    public Team(String teamid, String tname, String tdesc) {
        this.teamid = teamid;
        this.tname = tname;
        this.tdesc = tdesc;
        this.members = new ArrayList<>();
    }

    public Team(String teamid, String tname, String tdesc, List<String> members) {
        this.teamid = teamid;
        this.tname = tname;
        this.tdesc = tdesc;
        this.members = members != null ? members : new ArrayList<>();
    }

    public String getTeamid() { return teamid; }
    public void setTeamid(String teamid) { this.teamid = teamid; }
    public String getTname() { return tname; }
    public void setTname(String tname) { this.tname = tname; }
    public String getTdesc() { return tdesc; }
    public void setTdesc(String tdesc) { this.tdesc = tdesc; }
    public List<String> getMembers() { return members; }
    public void setMembers(List<String> members) {
        this.members = members != null ? members : new ArrayList<>();
    }


    // helper method for displaying hte members
    public String getMemberNames() {
        if (members == null || members.isEmpty()) {
            return "No members";
        }
        return String.join("\n", members); // join strings
    }
}
