package common.dao;

import common.models.ServiceSchedule;
import common.utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceScheduleDAO {
    // TODO: Query
    public List<ServiceSchedule> getServiceSchedule(String servid) {
        List<ServiceSchedule> schedules = new ArrayList<>();
        String sql = "SELECT * FROM SERVICE_SCHEDULE ORDER BY sstart";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, servid);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    ServiceSchedule schedule = new ServiceSchedule();
                    schedule.setServid(rs.getString("servid"));
                    schedule.setSchedid(rs.getString("schedid"));
                    schedule.setStart(rs.getTimestamp("sstart").toLocalDateTime());
                    schedule.setEnd(rs.getTimestamp("send").toLocalDateTime());
                    schedules.add(schedule);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving service schedules: " + e.getMessage(), e);
        }
        return schedules;
    }

    public boolean createServiceSchedule(ServiceSchedule schedule) {
        String sql = "INSERT INTO SERVICE_SCHEDULE (servid, schedid, sstart, ssend) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, schedule.getServid());
            statement.setString(2, schedule.getSchedid());
            statement.setString(3, String.valueOf(schedule.getStart()));
            statement.setString(4, String.valueOf(schedule.getEnd()));
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateServiceSchedule(ServiceSchedule schedule) {
        String sql = "UPDATE SERVICE_SCHEDULE SET sstart = ?, send = ? WHERE schedid = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setTimestamp(1, Timestamp.valueOf(schedule.getStart()));
            statement.setTimestamp(2, Timestamp.valueOf(schedule.getEnd()));
            statement.setString(3, schedule.getSchedid());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating service schedule: " + e.getMessage(), e);


        }
    }

    public boolean hasScheduleConflict(String servid, LocalDateTime newStart, LocalDateTime newEnd, String excludeSchedid) {
        String sql = "SELECT COUNT(*) FROM SERVICE_SCHEDULE " +
                "WHERE servid = ? " +
                "AND ((sstart < ? AND send > ?) OR " +  //new sched overlaps exisitng sched
                "(sstart >= ? AND sstart < ?) OR " +      //new start dureing existing sched
                "(send > ? AND send <= ?)) " +            //new end during existing
                (excludeSchedid != null ? "AND schedid != ?" : "");

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            int paramIndex = 1;
            statement.setString(paramIndex++, servid);
            statement.setTimestamp(paramIndex++, Timestamp.valueOf(newEnd));
            statement.setTimestamp(paramIndex++, Timestamp.valueOf(newStart));
            statement.setTimestamp(paramIndex++, Timestamp.valueOf(newStart));
            statement.setTimestamp(paramIndex++, Timestamp.valueOf(newEnd));
            statement.setTimestamp(paramIndex++, Timestamp.valueOf(newStart));
            statement.setTimestamp(paramIndex++, Timestamp.valueOf(newEnd));

            if (excludeSchedid != null) {
                statement.setString(paramIndex, excludeSchedid);
            }

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking schedule conflicts: " + e.getMessage(), e);
        }
        return false;
    }
}
