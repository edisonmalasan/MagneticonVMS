package common.dao;

import common.models.Admin;
import common.models.Service;
import common.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {
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

    public boolean updateService(Service service) {
        String sql = "UPDATE SERVICE SET teamid = ?, sname = ? sdesc = ?, sstat = ? WHERE servid = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, service.getTeamid());
            statement.setString(2, service.getSname());
            statement.setString(3, service.getSdesc());
            statement.setString(4, service.getSstat());
            statement.setString(5, service.getServid());

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

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating service: " + e.getMessage(), e);
        }
    }
}
