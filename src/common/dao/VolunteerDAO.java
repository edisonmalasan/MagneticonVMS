package common.dao;

import common.exceptions.VolunteerNotFoundException;
import common.models.Volunteer;
import common.utils.DatabaseConnection;

import java.sql.*;
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

    public static Volunteer getVolunteerById(String volunteerId)
            throws SQLException, VolunteerNotFoundException {

        if (volunteerId == null || volunteerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Volunteer ID cannot be null or empty");
        }

        final String sql = "SELECT v.volid, v.fname, v.lname, v.email, t.teamname " +
                "FROM Volunteer v " +
                "LEFT JOIN Team t ON v.teamid = t.teamid " +
                "WHERE v.volid = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, volunteerId);

            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) {
                    throw new VolunteerNotFoundException(
                            "Volunteer not found with ID: " + volunteerId);
                }

                return mapResultSetToVolunteer(rs);
            }
        }
    }

    private static Volunteer mapResultSetToVolunteer(ResultSet rs) throws SQLException {
        Volunteer volunteer = new Volunteer();
        volunteer.setVolid(rs.getString("volid"));
        volunteer.setFname(rs.getString("fname"));
        volunteer.setLname(rs.getString("lname"));
        volunteer.setEmail(rs.getString("email"));
        return volunteer;
    }

    public static boolean createVolunteer(Volunteer volunteer) {
        String sql = "INSERT INTO Volunteer (volid, lname, fname, address, phone, email, password, bday, sex, volstat, role) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, volunteer.getVolid());
            statement.setString(2, volunteer.getLname());
            statement.setString(3, volunteer.getFname());
            statement.setString(4, volunteer.getAddress());
            statement.setString(5, volunteer.getPhone());
            statement.setString(6, volunteer.getEmail());
            statement.setString(7, volunteer.getPassword());
            statement.setDate(8, Date.valueOf(volunteer.getBirthday()));
            statement.setString(9, volunteer.getSex());
            statement.setString(10, volunteer.getVolstat());
            statement.setString(11, "Member");

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<Volunteer> getAllVolunteers() {
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

    public boolean updateVolunteer(Volunteer volunteer) {
        String sql = "UPDATE VOLUNTEER SET fname = ?, lname = ?, address = ?, phone = ?, " +
                "email = ?, password = ?, bday = ?, sex = ?, volstat = ?, role = ? " +
                "WHERE volid = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, volunteer.getFname());
            statement.setString(2, volunteer.getLname());
            statement.setString(3, volunteer.getAddress());
            statement.setString(4, volunteer.getPhone());
            statement.setString(5, volunteer.getEmail());
            statement.setString(6, volunteer.getPassword());
            statement.setDate(7, Date.valueOf(volunteer.getBirthday()));
            statement.setString(8, volunteer.getSex());
            statement.setString(9, volunteer.getVolstat());
            statement.setString(10, "Member");
            statement.setString(11, volunteer.getVolid());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating volunteer: " + e.getMessage(), e);
        }
    }


    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM VOLUNTEER WHERE email = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking email: " + e.getMessage(), e);
        }
        return false;
    }
}
