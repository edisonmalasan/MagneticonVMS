package common.dao;

import common.models.ServiceSchedule;
import common.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ServiceScheduleDAO {
    // TODO: Query
    public ServiceSchedule getServiceSchedule(String servid, String schedid) {
        String sql = "SELECT * FROM SERVICE_SCHEDULE WHERE servid = ? AND schedid = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, servid);
            statement.setString(2, schedid);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                ServiceSchedule schedule = new ServiceSchedule();
                schedule.setServid(rs.getString("servid"));
                schedule.setSchedid(rs.getString("schedid"));
                schedule.setStart(LocalDateTime.parse(rs.getString("sstart")));
                schedule.setEnd(LocalDateTime.parse(rs.getString("ssend")));
                return schedule;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
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

}
