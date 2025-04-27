package common.dao;

import common.models.Service;
import common.models.TaskAssignment;
import common.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    private List<TaskAssignment> getAllTaskAssignments() {
        List<TaskAssignment> tasks = new ArrayList<>();
        String sql = "SELECT * FROM TASK_ASSIGNMENT";
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                tasks.add(new TaskAssignment(rs.getString("servid"),
                        rs.getString("volid"),
                        rs.getString("tadesc"),
                        rs.getString("taskstat")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

}
