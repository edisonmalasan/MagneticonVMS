package common.dao;

import common.models.Volunteer;
import common.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VolunteerDAO {

    public Volunteer authenticate(String email, String password) {
        String sql = "SELECT * FROM Volunteer WHERE email = ? AND password = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                Volunteer volunteer = new Volunteer();
                volunteer.setVolid(rs.getString("volid"));
                volunteer.setFname(rs.getString("fname"));
                volunteer.setLname(rs.getString("lname"));
                volunteer.setEmail(rs.getString("email"));
                volunteer.setRole(rs.getString("role"));

                return volunteer;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean createVolunteer(Volunteer volunteer) {
        String sql = "INSERT INTO Volunteer (volid, fname, lname, email, password, role) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, volunteer.getVolid());
            statement.setString(2, volunteer.getFname());
            statement.setString(3, volunteer.getLname());
            statement.setString(4, volunteer.getEmail());
            statement.setString(5, volunteer.getPassword());
            statement.setString(6, volunteer.getRole());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static List<Volunteer> getAllVolunteers() {
        List<Volunteer> volunteers = new ArrayList<>();
        String sql = "SELECT * FROM Volunteer";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                Volunteer volunteer = new Volunteer();
                volunteer.setVolid(rs.getString("volid"));
                volunteer.setFname(rs.getString("fname"));
                volunteer.setLname(rs.getString("lname"));
                volunteer.setEmail(rs.getString("email"));
                volunteer.setPassword(rs.getString("password"));
                volunteer.setRole(rs.getString("role"));

                volunteers.add(volunteer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return volunteers;
    }
}
