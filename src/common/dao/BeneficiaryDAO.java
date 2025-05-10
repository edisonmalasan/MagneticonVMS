package common.dao;


import common.models.Beneficiary;
import common.utils.DatabaseConnection;
import common.utils.LogManager;

import java.sql.*;

public class BeneficiaryDAO {
    public boolean assignBeneficiaryToService(String servid, String benid) {
        String sql = "INSERT INTO BENEFICIARY (servid, benid) VALUES (?, ?)";
        Beneficiary beneficiary = new Beneficiary(servid, benid);

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, servid);
            statement.setString(2, benid);

            LogManager.insertToLogs("resources/adminlogs.txt", "Created new beneficiary: " + beneficiary);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error assigning beneficiary to service: " + e.getMessage(), e);
        }
    }

    public static boolean hasBeneficiaryGroups(String servid) {
        String sql = "SELECT COUNT(*) FROM beneficiary WHERE servid = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, servid);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking for beneficiary groups", e);
        }
        return false;
    }

    public boolean removeBeneficiaryFromService(String servid, String benid) {
        String sql = "DELETE FROM BENEFICIARY WHERE servid = ? AND benid = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, servid);
            statement.setString(2, benid);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error removing beneficiary from service: " + e.getMessage(), e);
        }
    }
}
