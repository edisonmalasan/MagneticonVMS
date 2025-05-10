package common.dao;

import common.models.Admin;
import common.models.Service;
import common.utils.DatabaseConnection;
import common.utils.LogManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {
    public ServiceDAO(String servname, String servdesc, String bengroupname, String bengroupdesc) {
    }

    public ServiceDAO() {

    }

    public List<Service> getAllServices() {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM Service";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                Service service = new Service();
                service.setServid(rs.getString("servid"));
                service.setSname(rs.getString("sname"));
                service.setSdesc(rs.getString("sdesc"));
                service.setSstat(rs.getString("sstat"));
                service.setTeamid(rs.getString("teamid"));
                services.add(service);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return services;
    }

    public static List<String> getServicesForVolunteer(String volunteerId) throws SQLException {
        List<String> services = new ArrayList<>();
        String sql = "SELECT s.sname " +
                "FROM service s " +
                "JOIN task_assignment vs ON s.servid = vs.servid " +
                "WHERE vs.volid = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, volunteerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    services.add(rs.getString("sname"));
                }
            }
        }
        return services;
    }

    public static Service getServiceDetails(String serviceName, String volunteerId) throws SQLException {
        Service service = null;
        String sql = "SELECT s.servid, s.sdesc " +
                "FROM service s " +
                "JOIN task_assignment vs ON s.servid = vs.servid " +
                "WHERE s.sname = ? AND vs.volid = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, serviceName);
            stmt.setString(2, volunteerId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    service = new Service();
                    service.setServid(rs.getString("servid"));
                    service.setSdesc(rs.getString("sdesc"));
                }
            }
        }
        return service;
    }

    public boolean updateService(Service service) {
        String sql = "UPDATE SERVICE SET teamid = ?, sname = ?, sdesc = ?, sstat = ? WHERE servid = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, service.getTeamid());
            statement.setString(2, service.getSname());
            statement.setString(3, service.getSdesc());
            statement.setString(4, service.getSstat());
            statement.setString(5, service.getServid());

            LogManager.insertToLogs("resources/adminlogs.txt", "Updated service: " + service);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);

        }
    }

    public boolean createService(Service service) {
        String sql = "INSERT INTO SERVICE (servid, sname, sdesc, sstat, teamid) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, service.getServid());
            statement.setString(2, service.getSname());
            statement.setString(3, service.getSdesc());
            statement.setString(4, service.getSstat());
            statement.setString(5, service.getTeamid());

            LogManager.insertToLogs("resources/adminlogs.txt", "Created new service: " + service);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating service: " + e.getMessage(), e);
        }
    }

    public String generateNewServiceId() {
        String sql = "SELECT MAX(CAST(SUBSTRING(servid, 2) AS UNSIGNED)) FROM SERVICE";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            if (rs.next()) {
                int maxId = rs.getInt(1);

                //for up to 999 entries
                return String.format("S%03d", maxId + 1);
            }
            return "S001"; //default if no records exist
        } catch (SQLException e) {
            throw new RuntimeException("Error generating service ID", e);
        }
    }
}
