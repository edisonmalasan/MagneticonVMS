package common.dao;

import common.models.Team;

import common.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeamDAO {
    // TODO: Query
    public boolean createTeam(Team team) {
        String sql = "INSERT INTO TEAM (teamid, tname, tdesc) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, team.getTeamid());
            statement.setString(2, team.getTname());
            statement.setString(3, team.getTdesc());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating team: " + e.getMessage(), e);
        }
    }

    public boolean updateTeam(Team team) {
        String sql = "UPDATE TEAM SET tname = ?, tdesc = ? WHERE teamid = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, team.getTname());
            statement.setString(2, team.getTdesc());
            statement.setString(3, team.getTeamid());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating team: " + e.getMessage(), e);
        }
    }

    public List<Team> getAllTeams() {
        List<Team> teams = new ArrayList<>();
        String sql = "SELECT * FROM TEAM";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                Team team = new Team();
                team.setTeamid(rs.getString("teamid"));
                team.setTname(rs.getString("tname"));
                team.setTdesc(rs.getString("tdesc"));
                teams.add(team);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving teams: " + e.getMessage(), e);
        }
        return teams;
    }

    public boolean teamNameExists(String tname) {
        String sql = "SELECT COUNT(*) FROM TEAM WHERE tname = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, tname);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking team name: " + e.getMessage(), e);
        }
        return false;
    }




}
