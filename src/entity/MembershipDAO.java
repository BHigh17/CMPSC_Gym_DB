package entity;

import core.DB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MembershipDAO implements DAO<Membership>{

    public MembershipDAO() {

    }

    List<Membership> memberships;
    @Override
    public Optional<Membership> get(int membershipID) {
        DB db = DB.getInstance();
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM MEMBERSHIP WHERE MEMBERSHIPID = ?";
            PreparedStatement stmt = db.getPreparedStatement(sql);
            stmt.setInt(1, membershipID);
            rs = stmt.executeQuery();
            Membership membership = null;
            while (rs.next()) {
                membership = new Membership(
                        rs.getInt("MEMBERSHIPID"),
                        rs.getInt("MEMPRICE"),
                        rs.getString("MEMNAME")
                );

            }
            return Optional.ofNullable(membership);
        } catch (SQLException ex) {
            System.err.println(ex.toString());
            return null;
        }
    }

    @Override
    public void insert(Membership membership) {
        DB db = DB.getInstance();
        try {
            String sql = "INSERT INTO MEMBER(MEMBERSHIPID, MEMPRICE, MEMBERID) VALUES (?, ?, ?)";
            PreparedStatement stmt = db.getPreparedStatement(sql);
            stmt.setInt(1, membership.getMembershipID());
            stmt.setInt(2, membership.getMemPrice());
            stmt.setInt(3, membership.getMemberID());
            int rowInserted = stmt.executeUpdate();
            if (rowInserted > 0) {
                System.out.println("A gym member's membership was successful added!");
            }
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }

    }

    @Override
    public void update(Membership membership) {
        DB db = DB.getInstance();
        try {
            String sql = "UPDATE MEMBERSHIP SET MEMPRICE=? WHERE MEMBERSHIPID=?";
            PreparedStatement stmt = db.getPreparedStatement(sql);
            stmt.setInt(1, membership.getMemPrice());
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("A gym member's membership was updated successfully!");
            }
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }

    }

    @Override
    public void delete(Membership membership) {
        DB db = DB.getInstance();
        try {
            String sql = "DELETE FROM MEMBER WHERE MEMBERID = ?";
            PreparedStatement stmt = db.getPreparedStatement(sql);
            stmt.setInt(1, membership.getMemberID());
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("A gym member's membership was successfully deleted!");
            }
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }

    }

    @Override
    public List<String> getColumnNames() {
        return null;
    }

    @Override
    public List<Membership> getAll() {
        DB db = DB.getInstance();
        ResultSet rs = null;
        memberships = new ArrayList<>();
        try {
            String sql = "SELECT * FROM MEMBERSHIP";
            rs = db.executeQuery(sql);
            Membership membership = null;
            while (rs.next()) {
                membership = new Membership(
                        rs.getInt("MEMBERSHIPID"),
                        rs.getInt("MEMPRICE"),
                        rs.getString("MEMNAME"));

                memberships.add(membership);
            }
            return memberships;
        } catch (SQLException ex) {
            System.err.println(ex.toString());
            return null;
        }
    }
}
