package common.models;

import java.time.LocalDateTime;

public class ServiceSchedule {
    private String servid;
    private String schedid;
    private LocalDateTime start;
    private LocalDateTime end;

    public ServiceSchedule() {}

    public ServiceSchedule(String servid, String schedid, LocalDateTime start, LocalDateTime end) {
        this.servid = servid;
        this.schedid = schedid;
        this.start = start;
        this.end = end;
    }

    public String getServid() { return servid; }
    public void setServid(String servid) { this.servid = servid; }
    public String getSchedid() { return schedid; }
    public void setSchedid(String schedid) { this.schedid = schedid; }
    public LocalDateTime getStart() { return start; }
    public void setStart(LocalDateTime start) { this.start = start; }
    public LocalDateTime getEnd() { return end; }
    public void setEnd(LocalDateTime end) { this.end = end; }
}