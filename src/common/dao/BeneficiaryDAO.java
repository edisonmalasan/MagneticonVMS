package common.dao;


import common.models.Beneficiary;
import common.utils.DatabaseConnection;

import java.sql.*;

public class BeneficiaryDAO {
    public boolean assignBeneficiaryToService(String servid, String benid) {
        String sql = "INSERT INTO BENEFICIARY (servid, benid) VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, servid);
            statement.setString(2, benid);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error assigning beneficiary to service: " + e.getMessage(), e);
        }
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
