package common.dao;

import common.models.BeneficiaryGroup;
import common.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public boolean updateBeneficiaryGroup(BeneficiaryGroup group) {
        String sql = "UPDATE BENEFICIARY_GROUPS SET bengroup = ?, bendesc = ? WHERE benid = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, group.getBengroup());
            statement.setString(2, group.getBendesc());
            statement.setString(3, group.getBenid());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating beneficiary group: " + e.getMessage(), e);
        }
    }

    public List<BeneficiaryGroup> getAllBeneficiaryGroups() {
        List<BeneficiaryGroup> groups = new ArrayList<>();
        String sql = "SELECT * FROM BENEFICIARY_GROUPS";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                BeneficiaryGroup group = new BeneficiaryGroup();
                group.setBenid(rs.getString("benid"));
                group.setBengroup(rs.getString("bengroup"));
                group.setBendesc(rs.getString("bendesc"));
                groups.add(group);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving beneficiary groups: " + e.getMessage(), e);
        }
        return groups;
    }

    public List<BeneficiaryGroup> getBeneficiariesByService(String servid) {
        List<BeneficiaryGroup> groups = new ArrayList<>();
        String sql = "SELECT bg.* FROM BENEFICIARY_GROUPS bg " +
                "JOIN BENEFICIARY b ON bg.benid = b.benid " +
                "WHERE b.servid = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, servid);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    BeneficiaryGroup group = new BeneficiaryGroup();
                    group.setBenid(rs.getString("benid"));
                    group.setBengroup(rs.getString("bengroup"));
                    group.setBendesc(rs.getString("bendesc"));
                    groups.add(group);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving beneficiaries for service: " + e.getMessage(), e);
        }
        return groups;
    }


    public String generateNewBeneficiaryID() {
        String sql = "SELECT MAX(CAST(SUBSTRING(benid, 2) AS UNSIGNED)) FROM BENEFICIARY_GROUPS";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            if (rs.next()) {
                int maxId = rs.getInt(1);

                //for up to 999 entries
                return String.format("B%03d", maxId + 1);
            }
            return "B001"; //default if no records exist
        } catch (SQLException e) {
            throw new RuntimeException("Error generating beneficiary ID", e);
        }
    }
}
