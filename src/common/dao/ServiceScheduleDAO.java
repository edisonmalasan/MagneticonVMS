package common.dao;

import common.models.ServiceSchedule;
import common.utils.DatabaseConnection;
import common.utils.LogManager;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ServiceScheduleDAO {
    public List<ServiceSchedule> getAllServiceSchedule() {
        List<ServiceSchedule> schedules = new ArrayList<>();
        //handle zero dates by converting them to null
        String sql = "SELECT servid, schedid, sstat, " +
                "NULLIF(sstart, '0000-00-00') as sstart, " +
                "NULLIF(send, '0000-00-00') as send " +
                "FROM SERVICE_SCHEDULE ORDER BY COALESCE(sstart, '9999-12-31')";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                ServiceSchedule schedule = new ServiceSchedule();
                schedule.setServid(rs.getString("servid"));
                schedule.setSchedid(rs.getString("schedid"));
                schedule.setStatus(rs.getString("sstat") != null ? rs.getString("sstat") : "Not Assigned");

                Date startDate = rs.getDate("sstart");
                schedule.setStart(startDate != null ? startDate.toLocalDate() : null);

                Date endDate = rs.getDate("send");
                schedule.setEnd(endDate != null ? endDate.toLocalDate() : null);

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

            if (schedule.getStatus() != null) {
                statement.setString(3, schedule.getStatus());
            } else {
                statement.setNull(3, Types.VARCHAR);
            }

            if (schedule.getStart() != null) {
                statement.setDate(4, Date.valueOf(schedule.getStart()));
            } else {
                statement.setNull(4, Types.DATE);
            }

            if (schedule.getEnd() != null) {
                statement.setDate(5, Date.valueOf(schedule.getEnd()));
            } else {
                statement.setNull(5, Types.DATE);
            }

            LogManager.insertToLogs("resources/adminlogs.txt", "Created new service schedule: " + schedule);

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

            if (schedule.getStatus() != null) {
                statement.setString(2, schedule.getStatus());
            } else {
                statement.setNull(2, Types.VARCHAR);
            }

            if (schedule.getStart() != null) {
                statement.setDate(3, Date.valueOf(schedule.getStart()));
            } else {
                statement.setNull(3, Types.DATE);
            }

            if (schedule.getEnd() != null) {
                statement.setDate(4, Date.valueOf(schedule.getEnd()));
            } else {
                statement.setNull(4, Types.DATE);
            }

            statement.setString(5, schedule.getSchedid());

            LogManager.insertToLogs("resources/adminlogs.txt", "Updated service schedule: " + schedule);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating service schedule: " + e.getMessage(), e);
        }
    }

    public boolean hasScheduleConflict(String servid, String status, LocalDate newStart, LocalDate newEnd, String excludeSchedid) {
        if (newStart == null || newEnd == null) {
            return false;
        }

        String sql = "SELECT COUNT(*) FROM SERVICE_SCHEDULE " +
                "WHERE servid = ? " +
                "AND sstart IS NOT NULL " +  // Only check schedules with dates
                "AND send IS NOT NULL " +    // Only check schedules with dates
                "AND ((sstart <= ? AND send >= ?) OR " +  // New event overlaps existing
                "(sstart >= ? AND sstart <= ?) OR " +     // New start during existing
                "(send >= ? AND send <= ?)) " +           // New end during existing
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
                return String.format("SS%03d", maxId + 1);
            }
            return "SS001";
        } catch (SQLException e) {
            throw new RuntimeException("Error generating service ID", e);
        }
    }
}