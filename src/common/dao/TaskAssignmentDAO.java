package common.dao;

import common.models.Service;
import common.models.TaskAssignment;
import common.utils.DatabaseConnection;
import common.utils.LogManager;
import javafx.concurrent.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskAssignmentDAO {

    public boolean createTaskAssignment(TaskAssignment task) {
        String sql = "INSERT INTO TASK_ASSIGNMENT (servid, volid, tadesc, taskstat) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, task.getServid());
            statement.setString(2, task.getVolid());
            statement.setString(3, task.getTadesc());
            statement.setString(4, task.getTaskstat());

            LogManager.insertToLogs("resources/adminlogs.txt", "Created new task assignment: " + task);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating task assignment: " + e.getMessage(), e);
        }
    }

    public boolean updateTaskAssignment(TaskAssignment task) {
        String sql = "UPDATE TASK_ASSIGNMENT SET tadesc = ?, taskstat = ? WHERE servid = ? AND volid = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, task.getTadesc());
            statement.setString(2, task.getTaskstat());
            statement.setString(3, task.getServid());
            statement.setString(4, task.getVolid());

            LogManager.insertToLogs("resources/adminlogs.txt", "Updated task assignment: " + task);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating task assignment: " + e.getMessage(), e);
        }
    }

    public List<TaskAssignment> getAllTasks() {
        List<TaskAssignment> tasks = new ArrayList<>();
        String sql = "SELECT * FROM TASK_ASSIGNMENT";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery(sql)) {

                while (rs.next()) {
                    TaskAssignment task = new TaskAssignment();
                    task.setServid(rs.getString("servid"));
                    task.setVolid(rs.getString("volid"));
                    task.setTaskstat(rs.getString("taskstat"));
                    task.setTadesc(rs.getString("tadesc"));
                    tasks.add(task);
                }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving tasks for service: " + e.getMessage(), e);
        }
        return tasks;
    }

    public boolean isVolunteerAvailable(String volid) {
        String sql = "SELECT availability FROM MEMBER WHERE volid = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, volid);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return "Available".equalsIgnoreCase(rs.getString("availability"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking volunteer availability: " + e.getMessage(), e);
        }
        return false;
    }

    public static List<TaskAssignment> getTasksForVolunteerService(String volunteerId, String serviceName) throws SQLException {
        List<TaskAssignment> tasks = new ArrayList<>();
        String sql = "SELECT ta.servid, ta.taskstat " +
                "FROM task_assignment ta " +
                "JOIN service s ON ta.servid = s.servid " +
                "WHERE ta.volid = ? AND s.sname = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, volunteerId);
            stmt.setString(2, serviceName);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    TaskAssignment task = new TaskAssignment();
                    task.setServid(rs.getString("servid"));
                    task.setTaskstat(rs.getString("taskstat"));
                    tasks.add(task);
                }
            }
        }
        return tasks;
    }

    // In TaskAssignmentDAO
    public static int updateTaskStatus(String servid, String volid, String status) throws SQLException {
        String sql = "UPDATE task_assignment SET taskstat = ? WHERE servid = ? AND volid = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setString(2, servid);
            stmt.setString(3, volid);

            return stmt.executeUpdate(); // Returns number of rows affected
        }
    }

    public boolean isVolunteerAssignedToService(String volid, String servid) {
        String sql = "SELECT COUNT(*) FROM TASK_ASSIGNMENT WHERE volid = ? AND servid = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, volid);
            statement.setString(2, servid);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking volunteer assignment: " + e.getMessage(), e);
        }
        return false;
    }

}
