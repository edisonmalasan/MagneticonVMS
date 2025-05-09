package common.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ServiceSchedule {
    private String servid;
    private String schedid;
    private String status;

    private LocalDate start;
    private LocalDate end;

    public ServiceSchedule() {}

    public ServiceSchedule(String servid, String schedid, String status, LocalDate start, LocalDate end) {
        this.servid = servid;
        this.schedid = schedid;
        this.status = status;
        this.start = start;
        this.end = end;
    }

    public String getServid() { return servid; }
    public void setServid(String servid) { this.servid = servid; }
    public String getSchedid() { return schedid; }
    public void setSchedid(String schedid) { this.schedid = schedid; }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public LocalDate getStart() { return start; }
    public void setStart(LocalDate start) { this.start = start; }
    public LocalDate getEnd() { return end; }
    public void setEnd(LocalDate end) { this.end = end; }
}