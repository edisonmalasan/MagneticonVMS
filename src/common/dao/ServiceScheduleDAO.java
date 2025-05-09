package common.dao;

import common.models.ServiceSchedule;
import common.utils.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ServiceScheduleDAO {
    public List<ServiceSchedule> getAllServiceSchedule() {
        List<ServiceSchedule> schedules = new ArrayList<>();
        String sql = "SELECT * FROM SERVICE_SCHEDULE ORDER BY sstart";
        String dateFormat = "yyyy-MM-dd";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                ServiceSchedule schedule = new ServiceSchedule();
                schedule.setServid(rs.getString("servid"));
                schedule.setSchedid(rs.getString("schedid"));
                schedule.setStatus(rs.getString("sstat"));
                String startString = rs.getString("sstart");

                if (startString.equals("0000-00-00")) {
                    schedule.setStart(null);
                } else {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
                    LocalDate date = LocalDate.parse(startString, formatter);
                    schedule.setStart(date);
                }

                String endString = rs.getString("send");

                if (endString.equals("0000-00-00")) {
                    schedule.setEnd(null);
                } else {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
                    LocalDate date = LocalDate.parse(endString, formatter);
                    schedule.setEnd(date);
                }

                schedules.add(schedule);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving service schedules: " + e.getMessage(), e);
        }
        return schedules;
    }

    public boolean createServiceSchedule(ServiceSchedule schedule) {
        String sql = "INSERT INTO SERVICE_SCHEDULE (servid, schedid, sstat, sstart, send) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, schedule.getServid());
            statement.setString(2, schedule.getSchedid());
            statement.setString(3, schedule.getStatus());
            statement.setDate(4, Date.valueOf(schedule.getStart()));
            statement.setDate(5, Date.valueOf(schedule.getEnd()));
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating service schedule: " + e.getMessage(), e);
        }
    }

    public boolean updateServiceSchedule(ServiceSchedule schedule) {
        String sql = "UPDATE SERVICE_SCHEDULE SET servid = ?, sstat = ?, sstart = ?, send = ? WHERE schedid = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, schedule.getServid());
            statement.setString(2, schedule.getStatus());
            statement.setDate(3, Date.valueOf(schedule.getStart()));
            statement.setDate(4, Date.valueOf(schedule.getEnd()));
            statement.setString(5, schedule.getSchedid());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating service schedule: " + e.getMessage(), e);
        }
    }

    public boolean hasScheduleConflict(String servid, String status, LocalDate newStart, LocalDate newEnd, String excludeSchedid) {
        String sql = "SELECT COUNT(*) FROM SERVICE_SCHEDULE " +
                "WHERE servid = ? " +
                "AND ((sstart <= ? AND send >= ?) OR " +  // New event overlaps existing
                "(sstart >= ? AND sstart <= ?) OR " +      // New start during existing
                "(send >= ? AND send <= ?)) " +            // New end during existing
                (excludeSchedid != null ? "AND schedid != ?" : "");

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            int paramIndex = 1;
            statement.setString(paramIndex++, servid);
            statement.setDate(paramIndex++, Date.valueOf(newEnd));
            statement.setDate(paramIndex++, Date.valueOf(newStart));
            statement.setDate(paramIndex++, Date.valueOf(newStart));
            statement.setDate(paramIndex++, Date.valueOf(newEnd));
            statement.setDate(paramIndex++, Date.valueOf(newStart));
            statement.setDate(paramIndex++, Date.valueOf(newEnd));

            if (excludeSchedid != null) {
                statement.setString(paramIndex, excludeSchedid);
            }

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking schedule conflicts: " + e.getMessage(), e);
        }
    }

    public String generateNewScheduleID() {
        String sql = "SELECT MAX(CAST(SUBSTRING(schedid, 2) AS UNSIGNED)) FROM SERVICE_SCHEDULE";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            if (rs.next()) {
                int maxId = rs.getInt(1);

                //for up to 999 entries
                return String.format("SS%03d", maxId + 1);
            }
            return "SS001"; //default if no records exist
        } catch (SQLException e) {
            throw new RuntimeException("Error generating service ID", e);
        }
    }
}