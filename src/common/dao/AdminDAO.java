package common.dao;

import common.exceptions.VolunteerNotFoundException;
import common.models.Admin;
import common.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {
    public static boolean createAdmin(Admin admin) {
        String sql = "INSERT INTO Admin (volid, certification, skills) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, admin.getVolid());
            statement.setString(2, admin.getCertification());
            statement.setString(3, admin.getSkills());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (VolunteerNotFoundException e) {
            System.out.println("NOT FOUND: " + e.getMessage());
            return false;
        }
    }

    public static Admin getAdminByVolunteerId(String volid) {
        String sql = "SELECT * FROM Admin WHERE volid = ?";

        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1,volid);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                Admin admin = new Admin();
                admin.setVolid(rs.getString("volid"));
                admin.setCertification(rs.getString("certification"));
                admin.setSkills(rs.getString("skills"));
                return admin;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (VolunteerNotFoundException e) {
            System.out.println("NOT FOUND: " + e.getMessage());
        }
        return null;
    }

    public static boolean updateAdmin(Admin admin) {
        String sql = "UPDATE ADMIN SET certification = ?, skills = ? WHERE volid = ?";

        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, admin.getCertification());
            statement.setString(2, admin.getSkills());
            statement.setString(3, admin.getVolid());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);

        }
    }

    public static List<Admin> getAllAdmins() {
        List<Admin> admins = new ArrayList<>();
        String sql = "SELECT * FROM Admin";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                Admin admin = new Admin();
                admin.setVolid(rs.getString("volid"));
                admin.setCertification(rs.getString("certification"));
                admin.setSkills(rs.getString("skills"));
                admins.add(admin);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return admins;
    }
}
