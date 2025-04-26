package common.dao;

import common.models.TaskAssignment;
import common.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TaskAssignmentDAO {

    public boolean createTaskAssignment(TaskAssignment task) {
        String sql = "INSERT INTO TASK_ASSIGNMENT (servid, volid, taskstat) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, task.getServid());
            statement.setString(2, task.getVolid());
            statement.setString(3, task.getTaskstat());

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateTaskAssignment(String servid, String volid, String taskstat) {
        String sql = "UPDATE TASK_ASSIGNMENT SET taskstat = ? WHERE servid = ? AND volid = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, taskstat);
            statement.setString(2, servid);
            statement.setString(3, volid);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
