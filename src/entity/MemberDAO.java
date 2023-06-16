package entity;

import core.DB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MemberDAO implements DAO<Member> {

    public MemberDAO() {

    }

    List<Member> members;

    @Override
    public Optional<Member> get(int memberID) {
        DB db = DB.getInstance();
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM MEMBER WHERE MEMBERID = ?";
            PreparedStatement stmt = db.getPreparedStatement(sql);
            stmt.setInt(1, memberID);
            rs = stmt.executeQuery();
            Member member = null;
            while (rs.next()) {
                member = new Member(
                        rs.getInt("MEMBERID"),
                        rs.getString("FIRSTNAME"),
                        rs.getString("LASTNAME"),
                        rs.getString("STREETADDRESS"),
                        rs.getString("CITY"),
                        rs.getString("STATE"),
                        rs.getString("ZIP"),
                        rs.getString("PHONE"),
                        rs.getString("MEMBERSHIPID"));

            }
            return Optional.ofNullable(member);
        } catch (SQLException ex) {
            System.err.println(ex.toString());
            return null;
        }

    }

    @Override
    public List<Member> getAll() {
        DB db = DB.getInstance();
        ResultSet rs = null;
        members = new ArrayList<>();
        try {
            String sql = "SELECT * FROM MEMBER";
            rs = db.executeQuery(sql);
            Member member = null;
            while (rs.next()) {
                member = new Member(
                        rs.getInt("MEMBERID"),
                        rs.getString("FIRSTNAME"),
                        rs.getString("LASTNAME"),
                        rs.getString("STREETADDRESS"),
                        rs.getString("CITY"),
                        rs.getString("STATE"),
                        rs.getString("ZIP"),
                        rs.getString("PHONE"),
                        rs.getString("MEMBERSHIPID"));

                members.add(member);
            }
            return members;
        } catch (SQLException ex) {
            System.err.println(ex.toString());
            return null;
        }


    }

    @Override
    public void insert(Member member) {
        DB db = DB.getInstance();
        try {
            String sql = "INSERT INTO MEMBER(FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) VALUES (?, ?, ?, ?, ?, ?, ?, SELECT MEMBERSHIPID FROM WHERE MEMNAME=?)";
            PreparedStatement stmt = db.getPreparedStatement(sql);
            stmt.setString(1, member.getFirstName());
            stmt.setString(2, member.getLastName());
            stmt.setString(3, member.getStreetAddress());
            stmt.setString(4, member.getCity());
            stmt.setString(5, member.getState());
            stmt.setString(6, member.getZip());
            stmt.setString(7, member.getPhone());
            stmt.setString(8, member.getMembershipID());
            int rowInserted = stmt.executeUpdate();
            if (rowInserted > 0) {
                System.out.println("A gym member was successful added!");
            }
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }

    }

    @Override
    public void update(Member member) {
        DB db = DB.getInstance();
        try {
            String sql = "UPDATE MEMBER SET FIRSTNAME=?, LASTNAME=?, STREETADDRESS=?, CITY=?, STATE=?, ZIP=?, PHONE=? WHERE MEMBERID=?";
            PreparedStatement stmt = db.getPreparedStatement(sql);
            stmt.setInt(1, member.getMemberID());
            stmt.setString(2, member.getFirstName());
            stmt.setString(3, member.getLastName());
            stmt.setString(4, member.getStreetAddress());
            stmt.setString(5, member.getCity());
            stmt.setString(6, member.getState());
            stmt.setString(7, member.getZip());
            stmt.setString(8, member.getPhone());
            stmt.setString(9, member.getMembershipID());
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("An existing gym member was updated successfully!");
            }
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
    }



    @Override
    public void delete(Member member) {
        DB db = DB.getInstance();
        try {
            String sql = "DELETE FROM MEMBER WHERE MEMBERID = ?";
            PreparedStatement stmt = db.getPreparedStatement(sql);
            stmt.setInt(1, member.getMemberID());
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("A gym member was successfully deleted!");
            }
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }

    }

    @Override
    public List<String> getColumnNames() {
        DB db = DB.getInstance();
        ResultSet rs = null;
        List<String> headers = new ArrayList<>();
        try {
            String sql = "SELECT * FROM MEMBER WHERE MEMBERID = -1";//We just need this sql query to get the column headers
            rs = db.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            //Get number of columns in the result set
            int numberCols = rsmd.getColumnCount();
            for (int i = 1; i <= numberCols; i++) {
                headers.add(rsmd.getColumnLabel(i));//Add column headers to the list
            }
            return headers;
        } catch (SQLException ex) {
            System.err.println(ex.toString());
            return null;
        }
    }
}
