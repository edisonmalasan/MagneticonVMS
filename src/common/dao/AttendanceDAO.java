package common.dao;

import common.models.Attendance;
import common.utils.DatabaseConnection;
import common.utils.LogManager;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {

    public List<Attendance> getAllAttendances() throws SQLException {
        List<Attendance> attendances = new ArrayList<>();
        String sql = "SELECT * FROM Attendance ORDER BY date DESC, timein DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                attendances.add(mapResultSetToAttendance(rs));
            }
        }
        return attendances;
    }

    public Attendance getAttendance(String servid, String volid, String attendid) throws SQLException {
        if (servid == null || volid == null || attendid == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }

        String sql = "SELECT * FROM Attendance WHERE servid = ? AND volid = ? AND attendid = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, servid);
            statement.setString(2, volid);
            statement.setString(3, attendid);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAttendance(rs);
                }
            }
        }
        return null;
    }

    public List<Attendance> getAttendanceForVolunteer(String volid) throws SQLException {
        if (volid == null) {
            throw new IllegalArgumentException("Volunteer ID cannot be null");
        }

        List<Attendance> attendances = new ArrayList<>();
        String sql = "SELECT * FROM Attendance WHERE volid = ? ORDER BY date DESC, timein DESC";

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
            statement.setString(3, generateNewAttendId());
            statement.setDate(4, attendance.getDate() != null ? Date.valueOf(attendance.getDate()) : null);
            statement.setTime(5, attendance.getTimein() != null ? Time.valueOf(attendance.getTimein()) : null);
            statement.setTime(6, attendance.getTimeout() != null ? Time.valueOf(attendance.getTimeout()) : null);
            statement.setString(7, attendance.getAttendstat());

            LogManager.insertToLogs("resources/adminlogs.txt", "Created new attendance: " + attendance);

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

            statement.setDate(1, attendance.getDate() != null ? Date.valueOf(attendance.getDate()) : null);
            statement.setTime(2, attendance.getTimein() != null ? Time.valueOf(attendance.getTimein()) : null);
            statement.setTime(3, attendance.getTimeout() != null ? Time.valueOf(attendance.getTimeout()) : null);
            statement.setString(4, attendance.getAttendstat());
            statement.setString(5, attendance.getServid());
            statement.setString(6, attendance.getVolid());
            statement.setString(7, attendance.getAttendid());

            LogManager.insertToLogs("resources/adminlogs.txt", "Updated attendance: " + attendance);

            return statement.executeUpdate() > 0;
        }
    }

    private static Attendance mapResultSetToAttendance(ResultSet rs) throws SQLException {
        Attendance attendance = new Attendance();
        String dateFormat = "yyyy-MM-dd";

        attendance.setServid(rs.getString("servid"));
        attendance.setVolid(rs.getString("volid"));
        attendance.setAttendid(rs.getString("attendid"));

        String date = rs.getString("date");
        if (date.equals("0000-00-00")) {
            attendance.setDate(null);
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
            LocalDate localDate = LocalDate.parse(date, formatter);
            attendance.setDate(localDate);
        }

        Time timeIn = rs.getTime("timein");
        attendance.setTimein(timeIn != null ? timeIn.toLocalTime() : null);

        Time timeOut = rs.getTime("timeout");
        attendance.setTimeout(timeOut != null ? timeOut.toLocalTime() : null);

        attendance.setAttendstat(rs.getString("attendstat"));
        return attendance;
    }

    private String generateNewAttendId() throws SQLException {
        String sql = "SELECT MAX(CAST(SUBSTRING(attendid, 2) AS UNSIGNED)) FROM Attendance";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            if (rs.next()) {
                int maxId = rs.getInt(1);
                return String.format("A%02d", maxId + 1);
            }
            return "A001";
        }
    }
}