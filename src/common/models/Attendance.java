package common.models;

import java.time.LocalDate;
import java.time.LocalTime;

public class Attendance {
    private String servid;
    private String volid;
    private String attendid;
    private LocalDate date;
    private LocalTime timein;
    private LocalTime timeout;
    private String attendstat;

    public Attendance() {}

    public Attendance(String servid, String volid, String attendid, LocalDate date,
                      LocalTime timein, LocalTime timeout, String attendstat) {
        this.servid = servid;
        this.volid = volid;
        this.attendid = attendid;
        this.date = date;
        this.timein = timein;
        this.timeout = timeout;
        this.attendstat = attendstat;
    }

    public String getServid() { return servid; }
    public void setServid(String servid) { this.servid = servid; }
    public String getVolid() { return volid; }
    public void setVolid(String volid) { this.volid = volid; }
    public String getAttendid() { return attendid; }
    public void setAttendid(String attendid) { this.attendid = attendid; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public LocalTime getTimein() { return timein; }
    public void setTimein(LocalTime timein) { this.timein = timein; }
    public LocalTime getTimeout() { return timeout; }
    public void setTimeout(LocalTime timeout) { this.timeout = timeout; }
    public String getAttendstat() { return attendstat; }
    public void setAttendstat(String attendstat) { this.attendstat = attendstat; }
}
