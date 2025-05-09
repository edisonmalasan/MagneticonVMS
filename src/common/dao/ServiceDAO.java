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
        if (volunteerId == null || volunteerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Volunteer ID cannot be null or empty");
        }

        List<String> services = new ArrayList<>();
        String sql = "SELECT DISTINCT s.servname " +
                "FROM Service s " +
                "JOIN VolunteerService vs ON s.servid = vs.servid " +
                "WHERE vs.volid = ? " +
                "ORDER BY s.servname";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, volunteerId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    services.add(rs.getString("servname"));
                }
            }
        }
        return services;
    }
    public static Service getServiceDetails(String serviceName, String volunteerId) throws SQLException {
        if (serviceName == null || serviceName.trim().isEmpty() ||
                volunteerId == null || volunteerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Parameters cannot be null or empty");
        }

        String sql = "SELECT s.servid, s.sname, s.sdesc, s.sstat, s.teamid, " +
                "b.bengroupname, b.bengroupdesc " +
                "FROM Service s " +
                "LEFT JOIN BeneficiaryGroup b ON s.bengroupid = b.bengroupid " +
                "JOIN VolunteerService vs ON s.servid = vs.servid " +
                "WHERE s.sname = ? AND vs.volid = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, serviceName);
            statement.setString(2, volunteerId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    Service service = new Service();
                    service.setServid(rs.getString("servid"));
                    service.setSname(rs.getString("sname"));
                    service.setSdesc(rs.getString("sdesc"));
                    service.setSstat(rs.getString("sstat"));
                    service.setTeamid(rs.getString("teamid"));

                    return service;
                }
            }
        }
        return null;
    }

    public List<Service> getAllServicesForVolunteer(String volunteerId) throws SQLException {
        if (volunteerId == null || volunteerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Volunteer ID cannot be null or empty");
        }

        List<Service> services = new ArrayList<>();
        String sql = "SELECT s.servid, s.sname, s.sdesc, s.sstat, s.teamid " +
                "FROM Service s " +
                "JOIN VolunteerService vs ON s.servid = vs.servid " +
                "WHERE vs.volid = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, volunteerId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Service service = new Service();
                    service.setServid(rs.getString("servid"));
                    service.setSname(rs.getString("sname"));
                    service.setSdesc(rs.getString("sdesc"));
                    service.setSstat(rs.getString("sstat"));
                    service.setTeamid(rs.getString("teamid"));
                    services.add(service);
                }
            }
        }
        return services;
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
