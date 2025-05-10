package common.dao;

import common.models.Attendance;
import common.utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {

    public List<Attendance> getAllAttendances() throws SQLException {
        List<Attendance> attendances = new ArrayList<>();
        String sql = "SELECT servid, volid, attendid, " +
                "NULLIF(date, '0000-00-00') as date, " +
                "NULLIF(timein, '00:00') as timein, NULLIF(timeout, '00:00') as timeout, attendstat " +
                "FROM Attendance ORDER BY COALESCE(date, '9999-12-31') DESC, timein DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                attendances.add(mapResultSetToAttendance(rs));
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

        Time timeIn = rs.getTime("timein");
        attendance.setTimein(timeIn != null ? timeIn.toLocalTime() : null);

        Time timeOut = rs.getTime("timeout");
        attendance.setTimeout(timeOut != null ? timeOut.toLocalTime() : null);

        attendance.setAttendstat(rs.getString("attendstat"));

        return attendance;
    }

    public boolean createAttendance(Attendance attendance) throws SQLException {
        if (attendance == null) {
            throw new IllegalArgumentException("Attendance cannot be null");
        }

        String sql = "INSERT INTO Attendance (servid, volid, attendid, date, timein, timeout, attendstat) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, attendance.getServid());
            statement.setString(2, attendance.getVolid());
            statement.setString(3, attendance.getAttendid());

            statement.setDate(4, Date.valueOf(attendance.getDate()));

            if (attendance.getTimein() != null) {
                statement.setTime(5, Time.valueOf(attendance.getTimein()));
            } else {
                statement.setNull(5, Types.TIME);
            }

            if (attendance.getTimeout() != null) {
                statement.setTime(6, Time.valueOf(attendance.getTimeout()));
            } else {
                statement.setNull(6, Types.TIME);
            }

            if (attendance.getAttendstat() != null) {
                statement.setString(7, attendance.getAttendstat());
            } else {
                statement.setNull(7, Types.VARCHAR);
            }

            return statement.executeUpdate() > 0;
        }
    }

    public boolean updateAttendance(Attendance attendance) throws SQLException {
        if (attendance == null) {
            throw new IllegalArgumentException("Attendance cannot be null");
        }

        String sql = "UPDATE Attendance SET date = ?, timein = ?, timeout = ?, attendstat = ? " +
                "WHERE servid = ? AND volid = ? AND attendid = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setDate(1, Date.valueOf(attendance.getDate()));

            if (attendance.getTimein() != null) {
                statement.setTime(2, Time.valueOf(attendance.getTimein()));
            } else {
                statement.setNull(2, Types.TIME);
            }

            if (attendance.getTimeout() != null) {
                statement.setTime(3, Time.valueOf(attendance.getTimeout()));
            } else {
                statement.setNull(3, Types.TIME);
            }

            if (attendance.getAttendstat() != null) {
                statement.setString(4, attendance.getAttendstat());
            } else {
                statement.setNull(4, Types.VARCHAR);
            }

            statement.setString(5, attendance.getServid());
            statement.setString(6, attendance.getVolid());
            statement.setString(7, attendance.getAttendid());

            return statement.executeUpdate() > 0;
        }
    }

    public List<Attendance> getAttendanceForVolunteer(String volid) throws SQLException {
        if (volid == null || volid.trim().isEmpty()) {
            throw new IllegalArgumentException("Volunteer ID cannot be null or empty");
        }

        List<Attendance> attendances = new ArrayList<>();
        String sql = "SELECT * FROM attendance WHERE volid = ? ORDER BY date DESC, timein DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, volid);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Attendance attendance = mapResultSetToAttendance(rs);
                    if (attendance != null) {
                        attendances.add(attendance);
                    }
                }
            }
        }
        return attendances;
    }

    public String generateNewAttendId() throws SQLException {
        String sql = "SELECT MAX(CAST(SUBSTRING(attendid, 2) AS UNSIGNED)) FROM Attendance";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            if (rs.next()) {
                int maxId = rs.getInt(1);
                return String.format("A%03d", maxId + 1);
            }
            return "A001";
        } catch (SQLException e) {
            throw new RuntimeException("Error generating Attendance ID", e);
        }
    }

}