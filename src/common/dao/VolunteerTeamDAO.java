package common.dao;

import common.models.Team;
import common.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VolunteerTeamDAO {
    // TODO: Query
    public List<Team> getAllVolunteerTeams() {
        String sql = "SELECT * FROM VOLUNTEER_TEAM";
        List<Team> teams = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Team team = new Team();
                team.setTeamid(rs.getString("teamid"));
                team.setTname(rs.getString("teamname"));
                team.setTdesc(rs.getString("tdesc"));
                teams.add(team);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return teams;
    }

}
