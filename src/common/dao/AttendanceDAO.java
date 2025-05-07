package common.dao;

import common.models.Attendance;
import common.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {

    public Attendance getAttendance(String servid, String volid, String attendid) {
        String sql = "SELECT * FROM Attendance WHERE servid = ? AND volid = ? AND attendid = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, servid);
            statement.setString(2, volid);
            statement.setString(3, attendid);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Attendance attendance = new Attendance();
                attendance.setServid(rs.getString("servid"));
                attendance.setVolid(rs.getString("volid"));
                attendance.setAttendid(rs.getString("attendid"));
                attendance.setDate(rs.getDate("date").toLocalDate());
                attendance.setTimein(rs.getTime("timein").toLocalTime());
                attendance.setTimeout(rs.getTime("timeout").toLocalTime());
                attendance.setAttendstat(rs.getString("attendstat"));
                return attendance;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static List<Attendance> getAttendanceForVolunteer(String volid) throws SQLException {
        if (volid == null) {
            throw new IllegalArgumentException("Volunteer ID cannot be null");
        }

        List<Attendance> attendances = new ArrayList<>();
        String sql = "SELECT * FROM Attendance WHERE volid = ? ORDER BY date DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, volid);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    attendances.add(mapResultSetToAttendance(rs));
                }
            }
        }
        return attendances;
    }

    private static Attendance mapResultSetToAttendance(ResultSet rs) throws SQLException {
        Attendance attendance = new Attendance();
        attendance.setServid(rs.getString("servid"));
        attendance.setVolid(rs.getString("volid"));
        attendance.setAttendid(rs.getString("attendid"));

        Date date = rs.getDate("date");
        attendance.setDate(date != null ? date.toLocalDate() : null);

        Time timein = rs.getTime("timein");
        attendance.setTimein(timein != null ? timein.toLocalTime() : null);

        Time timeout = rs.getTime("timeout");
        attendance.setTimeout(timeout != null ? timeout.toLocalTime() : null);

        attendance.setAttendstat(rs.getString("attendstat"));
        return attendance;
    }



    public boolean createAttendance(Attendance attendance) {
        String sql = "INSERT INTO Attendance (servid, volid, attendid, date, timein, timeout, attendstat) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, attendance.getServid());
            statement.setString(2, attendance.getVolid());
            statement.setString(3, attendance.getAttendid());
            statement.setDate(4, Date.valueOf(attendance.getDate()));
            statement.setTime(5, Time.valueOf(attendance.getTimein()));
            statement.setTime(6, Time.valueOf(attendance.getTimeout()));
            statement.setString(7, attendance.getAttendstat());
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateAttendance(String attendid, String attendstat, Time timeout) {
        String sql = "UPDATE Attendance SET attendstat = ?, timeout = ? WHERE attendid = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, attendstat);
            statement.setTime(2, timeout);
            statement.setString(3, attendid);
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
