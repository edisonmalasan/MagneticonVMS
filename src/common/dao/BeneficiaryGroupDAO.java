package common.dao;

import common.models.BeneficiaryGroup;
import common.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BeneficiaryGroupDAO {

    public boolean createBeneficiaryGroup(BeneficiaryGroup group) {
        String sql = "INSERT INTO BENEFICIARY_GROUPS (benid, bengroup, bendesc) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, group.getBenid());
            statement.setString(2, group.getBengroup());
            statement.setString(3, group.getBendesc());
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
