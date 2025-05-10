package common.dao;

import common.models.Team;

import common.models.Volunteer;
import common.utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TeamDAO {
    public static boolean createTeam(Team team){
        try{
            String query = "{ CALL addteam(?,?,?) }";
            CallableStatement statement = DatabaseConnection.getConnection().prepareCall(query);
            statement.setString(1, team.getTeamid());
            statement.setString(2, team.getTname());
            statement.setString(3, team.getTdesc());
            statement.execute(); //Execute Procedure

            return true;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public static List<Team> getTeamsForVolunteer(String volunteerId) throws SQLException {
        List<Team> teams = new ArrayList<>();

        // Corrected SQL with proper spacing and formatting
        String sql = "SELECT t.teamid, t.tname, t.tdesc " +
                "FROM team t " +
                "JOIN volunteer_team vt ON t.teamid = vt.teamid " +
                "JOIN volunteer v ON vt.volid = v.volid " +
                "WHERE v.volid = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, volunteerId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Team team = new Team();
                    team.setTeamid(rs.getString("teamid"));
                    team.setTname(rs.getString("tname"));
                    team.setTdesc(rs.getString("tdesc"));
                    teams.add(team);
                }
            }
        } catch (SQLException e) {
            // Log the error before rethrowing
            System.err.println("Error fetching teams for volunteer " + volunteerId + ": " + e.getMessage());
            throw e;
        }

        return teams;
    }

    public static List<Volunteer> getTeamMembers(String teamName) throws SQLException {
        List<Volunteer> members = new ArrayList<>();

        String sql = "SELECT v.volid, v.fname, v.lname, v.address, v.phone, " +
                "v.email, v.bday, v.sex, v.volstat " +
                "FROM volunteer v " +
                "JOIN volunteer_team vt ON v.volid = vt.volid " +
                "JOIN team t ON vt.teamid = t.teamid " +
                "WHERE t.tname = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, teamName);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Volunteer volunteer = new Volunteer();

                    volunteer.setVolid(rs.getString("volid"));
                    volunteer.setFname(rs.getString("fname"));
                    volunteer.setLname(rs.getString("lname"));
                    volunteer.setAddress(rs.getString("address"));
                    volunteer.setPhone(rs.getString("phone"));
                    volunteer.setEmail(rs.getString("email"));

                    String bday = rs.getString("bday");
                    if (bday != null) {
                        volunteer.setBirthday(LocalDate.parse(bday));
                    }

                    volunteer.setSex(rs.getString("sex"));
                    volunteer.setVolstat(rs.getString("volstat"));

                    members.add(volunteer);
                }
            }
        }
        return members;
    }

    public List<String> getTeamMembersName(String teamId) {
        List<String> members = new ArrayList<>();
        String sql = "SELECT v.volid, v.fname, v.lname FROM Volunteer v " +
                "JOIN Volunteer_Team vt ON v.volid = vt.volid " +
                "WHERE vt.teamid = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, teamId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String memberInfo = String.format("%s: %s %s",
                        rs.getString("volid"),
                        rs.getString("fname"),
                        rs.getString("lname"));
                members.add(memberInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

    public List<Team> getAllTeamsWithMembers() throws SQLException {
        List<Team> teams = getAllTeams();
        for (Team team : teams) {
            team.setMembers(getTeamMembersName(team.getTeamid()));
        }
        return teams;
    }



    public static List<Team> getAvailableTeams() throws SQLException {
        List<Team> teams = new ArrayList<>();
        String sql = "SELECT teamid, teamname, description FROM Team WHERE status = 'ACTIVE'";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Team team = new Team();
                team.setTeamid(rs.getString("teamid"));
                team.setTname(rs.getString("teamname"));rs.getString("teamname");
                team.setTdesc(rs.getString("description"));
                teams.add(team);
            }
        }
        return teams;
    }

    public static boolean addVolunteerToTeam(String volunteerId, String teamName) throws SQLException {
        String sql = "UPDATE Volunteer SET teamid = (SELECT teamid FROM Team WHERE teamname = ?) WHERE volid = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, teamName);
            stmt.setString(2, volunteerId);

            return stmt.executeUpdate() > 0;
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

    public String generateNewTeamID() {
        String sql = "SELECT MAX(CAST(SUBSTRING(teamid, 2) AS UNSIGNED)) FROM SERVICE";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            if (rs.next()) {
                int maxId = rs.getInt(1);

                //for up to 999 entries
                return String.format("T%03d", maxId + 1);
            }
            return "T001"; //default if no records exist
        } catch (SQLException e) {
            throw new RuntimeException("Error generating service ID", e);
        }
    }


}
