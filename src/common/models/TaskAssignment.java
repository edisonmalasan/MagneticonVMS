package common.models;

public class TaskAssignment {
    private String servid;
    private String volid;
    private String tadesc;
    private String taskstat;

    public TaskAssignment() {}

    public TaskAssignment(String servid, String volid, String tadesc, String taskstat) {
        this.servid = servid;
        this.volid = volid;
        this.tadesc = tadesc;
        this.taskstat = taskstat;
    }

    public String getServid() { return servid; }
    public void setServid(String servid) { this.servid = servid; }
    public String getVolid() { return volid; }
    public void setVolid(String volid) { this.volid = volid; }
    public String getTadesc() { return tadesc; }
    public void setTadesc(String tadesc) { this.tadesc = tadesc; }
    public String getTaskstat() { return taskstat; }
    public void setTaskstat(String taskstat) { this.taskstat = taskstat; }

    public boolean isEmpty() {return false;}

    public int get(int i) {return i;}
}
