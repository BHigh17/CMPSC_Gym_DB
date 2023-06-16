package program;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import core.DB;
import entity.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class main {
    private JPanel Main;
    private JTabbedPane tabbedPane1;
    private JTextField firstNameTxt;
    private JTextField lastNameTxt;
    private JTextField addressTxt;
    private JTextField cityTxt;
    private JComboBox stateComboBox;
    private JTextField zipTxt;
    private JTextField phoneTxt;
    private JComboBox membershipLevel;
    private JButton submitButton;
    private JButton resetButton;
    private JTextField memberIDTxt;
    private JTextField view_streetAddrTxt;
    private JButton memberUpdateBtn;
    private JButton memberDeleteBtn;
    private JComboBox view_membershipLevel;
    private JButton generatePassButton;
    private JTable membersTable;
    private JComboBox view_stateComboBox;
    private JTextField view_firstNameTxt;
    private JTextField view_lastNameTxt;
    private JTextField view_cityTxt;
    private JTextField view_zipTxt;
    private JTextField view_phoneTxt;
    private JTextField membershipIDTxt;
    private JTextField membershipNTxt;
    private JTextField membershipPriceTxt;
    private JTable membershipTable;
    private JButton membUpdateButton;
    private JButton membRemoveButton;
    private JButton membCreateButton;
    private JButton membClearButton;
    private static DAO memberDAO;
    private static DAO membershipDAO;

    public main() {
        $$$setupUI$$$();
        printMembers();
        refreshMemberTable();
        refreshMembershipTable();
        updateMemCB();
        memberUpdateBtn.addActionListener(new ActionListener() {
            /**
             * Invoked when the update button is clicked occurs.
             *
             * @param e the event to be processed
             */
            public void actionPerformed(ActionEvent e) {
                if (memberIDTxt.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "No member selected, try again", null, JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (view_firstNameTxt.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "First Name cannot be empty, try again", null, JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (view_lastNameTxt.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Last name cannot be empty, try again", null, JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (view_streetAddrTxt.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Address cannot be empty, try again", null, JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (view_cityTxt.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "City cannot be empty, try again", null, JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (zipIsValid(view_zipTxt.getText())) {
                    System.out.println("valid zip");
                    if (isValid(view_phoneTxt.getText())) {
                        System.out.println("Valid Number");

                        try {
                            String fName = view_firstNameTxt.getText().toUpperCase().trim();
                            String lName = view_lastNameTxt.getText().toUpperCase().trim();
                            String streetA = view_streetAddrTxt.getText().toUpperCase().trim();
                            String city = view_cityTxt.getText().toUpperCase().trim();
                            String state = view_stateComboBox.getModel().getSelectedItem().toString();
                            String phon = (view_phoneTxt.getText());
                            String zc = view_zipTxt.getText();
                            String memSelection = view_membershipLevel.getModel().getSelectedItem().toString();
                            String memberID = memberIDTxt.getText();

//                            Member(fName, lName, streetA, city, state, zc, phon, memSelection);

                            try {
                                DB db = DB.getInstance();
                                String sql =
                                        "UPDATE MEMBER SET FIRSTNAME=?, LASTNAME=?, STREETADDRESS=?, CITY=?, STATE=?, ZIP=?, PHONE=?, MEMBERSHIPID = (SELECT MEMBERSHIPID FROM MEMBERSHIP WHERE MEMNAME=?) WHERE MEMBERID=?";
                                PreparedStatement stmt = db.getPreparedStatement(sql);
                                stmt.setString(1, fName);
                                stmt.setString(2, lName);
                                stmt.setString(3, streetA);
                                stmt.setString(4, city);
                                stmt.setString(5, state);
                                stmt.setString(6, zc);
                                stmt.setString(7, phon);
                                stmt.setString(8, memSelection);
                                stmt.setInt(9, Integer.parseInt(memberID));
                                int rowInserted = stmt.executeUpdate();
                                if (rowInserted > 0) {
                                    System.out.println("Update Successful");
                                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Update Successful");
                                    refreshMemberTable();
                                    clearViewFields();


                                }
                            } catch (SQLException ex) {
                                System.err.println(ex.toString());
                                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), ex.getLocalizedMessage());
                                System.out.println(Arrays.toString(ex.getStackTrace()));
                                System.out.println(ex.getErrorCode());
                            }


                        } catch (NumberFormatException exc) {
                            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), " Nice try...but there was an error: " + exc.getMessage());
                        }

                    } else {
                        System.out.println("invalid number");
                        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Invalid Phone Number entered", null, JOptionPane.ERROR_MESSAGE);


                    }

                } else {
                    System.out.println("not vaild zip");
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Invalid Zip Code entered", null, JOptionPane.ERROR_MESSAGE);

                }

            }
        });
        memberDeleteBtn.addActionListener(new ActionListener() {
            /**
             * Invoked when delete button action occurs.
             *
             * @param e the event to be processed
             */
            public void actionPerformed(ActionEvent e) {
                if (memberIDTxt.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "No member selected, try again");
                    return;
                }
                int option = JOptionPane.showConfirmDialog(JOptionPane.getRootFrame(), "Are you sure you want to delete " + view_firstNameTxt.getText() + " " + view_lastNameTxt.getText() + "?", "Delete confirmation", JOptionPane.YES_NO_OPTION);
                DB db = DB.getInstance();
                if (option == 0) {
                    try {
                        String sql = "DELETE FROM MEMBER WHERE MEMBERID = ?";
                        PreparedStatement stmt = db.getPreparedStatement(sql);
                        stmt.setInt(1, Integer.parseInt(memberIDTxt.getText()));
                        int rowsDeleted = stmt.executeUpdate();
                        if (rowsDeleted > 0) {
                            System.out.println("A Member was deleted deleted!");
                            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Member was successfully deleted", "Delete Successful", JOptionPane.INFORMATION_MESSAGE);
                            refreshMemberTable();
                            clearViewFields();
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(
                                JOptionPane.getRootFrame(),
                                ex.getLocalizedMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        System.err.println(ex.toString());
                    }

                }

            }
        });
        generatePassButton.addActionListener(new ActionListener() {
            /**
             * Invoked generate pass action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if (memberIDTxt.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "No member selected, try again");
                    return;
                }

                final String QR_CODE_IMAGE_PATH = String.format("./ %sQR.png", view_lastNameTxt.getText());
                try {
                    generateQRImage(memberIDTxt.getText() + " " + view_lastNameTxt.getText(), 200, 200, QR_CODE_IMAGE_PATH);
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), String.format("Pass Available at %s", QR_CODE_IMAGE_PATH));
                    File qrFile = new File(QR_CODE_IMAGE_PATH);
                    if (!Desktop.isDesktopSupported()) {
                        return;
                    }
                    Desktop desktop = Desktop.getDesktop();
                    desktop.open(qrFile);

                } catch (WriterException wExc) {
                    System.out.println("Could not generate QR Code, WriterException :: " + wExc.getMessage());
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Could not generate QR Code, WriterException :: " + wExc.getMessage(), null, JOptionPane.ERROR_MESSAGE);
                } catch (IOException ioExc) {
                    System.out.println("Could not generate QR Code, IOException :: " + ioExc.getMessage());
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Could not generate QR Code, WriterException :: " + ioExc.getMessage(), null, JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        submitButton.addActionListener(new ActionListener() {
            /**
             * Invoked when submit action to database occurs.
             *
             * @param e the event to be processed
             */
            public void actionPerformed(ActionEvent e) {
                if (firstNameTxt.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "First Name cannot be empty");
                    return;
                }
                if (lastNameTxt.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Last Name cannot be empty");
                    return;
                }
                if (addressTxt.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Address cannot be empty");
                    return;
                }
                if (cityTxt.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "City cannot be empty");
                    return;
                }


                if (zipIsValid(zipTxt.getText().trim())) {
                    System.out.println("valid zip");
                    if (isValid(phoneTxt.getText().trim())) {
                        System.out.println("Valid Number");

                        try {
                            String fName = firstNameTxt.getText().toUpperCase().trim();
                            String lName = lastNameTxt.getText().toUpperCase().trim();
                            String streetA = addressTxt.getText().toUpperCase().trim();
                            String city = cityTxt.getText().toUpperCase().trim();
                            String state = stateComboBox.getModel().getSelectedItem().toString();
                            String phon = (phoneTxt.getText());
                            String zc = zipTxt.getText();
                            String memSelection = membershipLevel.getModel().getSelectedItem().toString();

//                            Member(fName, lName, streetA, city, state, zc, phon, memSelection);

                            try {
                                DB db = DB.getInstance();
                                String sql = "INSERT INTO MEMBER(FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID ) VALUES (?, ?, ?, ?, ?, ?, ?, (SELECT MEMBERSHIPID FROM MEMBERSHIP WHERE MEMNAME=?))";
                                PreparedStatement stmt = db.getPreparedStatement(sql);
                                stmt.setString(1, fName);
                                stmt.setString(2, lName);
                                stmt.setString(3, streetA);
                                stmt.setString(4, city);
                                stmt.setString(5, state);
                                stmt.setString(6, zc);
                                stmt.setString(7, phon);
                                stmt.setString(8, memSelection);
                                int rowInserted = stmt.executeUpdate();
                                if (rowInserted > 0) {
                                    System.out.println("A gym member was successful added!");
                                    String d = String.format("%s %s has been added!", fName, lName);
                                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), d);
                                    clearAddFields();
                                    refreshMemberTable();


                                }
                            } catch (SQLException ex) {
                                System.err.println(ex.toString());
                                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), ex.getLocalizedMessage());
                                System.out.println(Arrays.toString(ex.getStackTrace()));
                                System.out.println(ex.getErrorCode());
                            }


                        } catch (NumberFormatException exc) {
                            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), " Nice try...but there was an error: " + exc.getMessage());
                        }

                    } else {
                        System.out.println("invalid number");
                        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Please enter valid phone number", null, JOptionPane.ERROR_MESSAGE);


                    }

                } else {
                    System.out.println("not valid zip");
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Please enter valid zip code", null, JOptionPane.ERROR_MESSAGE);

                }

            }
        });
        resetButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            public void actionPerformed(ActionEvent e) {
                clearAddFields();

            }
        });

        membUpdateButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if (membershipIDTxt.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "No Membership selected, try again", null, JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (membershipNTxt.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Membership Name Empty, try again", null, JOptionPane.ERROR_MESSAGE);
                    return;

                }
                if (membershipPriceTxt.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Membership Price Empty, try again", null, JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    DB db = DB.getInstance();
                    String sql = "UPDATE MEMBERSHIP SET MEMPRICE=?, MEMNAME=? WHERE MEMBERSHIPID=?";
                    PreparedStatement stmt = db.getPreparedStatement(sql);
                    stmt.setInt(1, Integer.parseInt(membershipPriceTxt.getText()));
                    stmt.setString(2, membershipNTxt.getText().trim());
                    stmt.setInt(3, Integer.parseInt(membershipIDTxt.getText()));
                    int rowInserted = stmt.executeUpdate();
                    if (rowInserted > 0) {
                        System.out.println("Update Successful");
                        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Update Successful");
                        refreshMembershipTable();
                        clearMembershipFields();


                    }
                } catch (SQLException ex) {
                    System.err.println(ex.toString());
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), ex.getLocalizedMessage());
                    System.out.println(Arrays.toString(ex.getStackTrace()));
                    System.out.println(ex.getErrorCode());
                }


            }
        });
        membCreateButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the e
             */

            public void actionPerformed(ActionEvent e) {
                if (membershipPriceTxt.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Membership Price Empty, try again", null, JOptionPane.ERROR_MESSAGE);
                    return;

                }

                if (membershipNTxt.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Membership Name Empty, try again", null, JOptionPane.ERROR_MESSAGE);
                    return;
                }


                try {
                    Integer.parseInt(membershipPriceTxt.getText());
                    try {
                        DB db = DB.getInstance();
                        String sql = "INSERT INTO MEMBERSHIP(MEMPRICE, MEMNAME) VALUES (?, ?)";
                        PreparedStatement stmt = db.getPreparedStatement(sql);
                        stmt.setInt(1, Integer.parseInt(membershipPriceTxt.getText()));
                        stmt.setString(2, membershipNTxt.getText());

                        int rowInserted = stmt.executeUpdate();
                        if (rowInserted > 0) {
                            System.out.println("A gym member was successful added!");
                            String d = String.format("A new membership: %s has been added!", membershipNTxt.getText());
                            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), d);
                            clearMembershipFields();
                            refreshMembershipTable();
                            updateMemCB();
                        }
                    } catch (SQLException ex) {
                        System.err.println(ex.toString());
                        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), ex.getLocalizedMessage());
                        System.out.println(Arrays.toString(ex.getStackTrace()));
                        System.out.println(ex.getErrorCode());
                    }

                } catch (NumberFormatException exception) {
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Enter valid number for Price", null, JOptionPane.ERROR_MESSAGE);
                }


            }


        });
        membRemoveButton.addActionListener(new

                                                   ActionListener() {
                                                       /**
                                                        * Invoked when an action occurs.
                                                        *
                                                        * @param e the event to be processed
                                                        */
                                                       public void actionPerformed(ActionEvent e) {
                                                           if (membershipIDTxt.getText().isEmpty()) {
                                                               JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "No membership selected, try again");
                                                               return;
                                                           }
                                                           String d = String.format("Are you sure you want to delete %s?", membershipNTxt.getText());
                                                           int option = JOptionPane.showConfirmDialog(JOptionPane.getRootFrame(), d, null, JOptionPane.YES_NO_CANCEL_OPTION);

                                                           DB db = DB.getInstance();
                                                           if (option == 0) {
                                                               try {
                                                                   String sql = "DELETE FROM MEMBERSHIP WHERE MEMBERSHIPID = ?";
                                                                   PreparedStatement stmt = db.getPreparedStatement(sql);
                                                                   stmt.setInt(1, Integer.parseInt(membershipIDTxt.getText()));
                                                                   int rowsDeleted = stmt.executeUpdate();
                                                                   if (rowsDeleted > 0) {
                                                                       System.out.println("A membership was deleted deleted!");
                                                                       JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), String.format("Membership %s was deleted", membershipNTxt.getText()), "Delete Successful", JOptionPane.INFORMATION_MESSAGE);
                                                                       refreshMembershipTable();
                                                                       clearMembershipFields();
                                                                       updateMemCB();
                                                                   }
                                                               } catch (SQLException ex) {
                                                                   JOptionPane.showMessageDialog(
                                                                           JOptionPane.getRootFrame(),
                                                                           ex.getLocalizedMessage(),
                                                                           "Error",
                                                                           JOptionPane.ERROR_MESSAGE);
                                                                   System.err.println(ex.toString());
                                                               }

                                                           }


                                                       }
                                                   });
        membClearButton.addActionListener(new

                                                  ActionListener() {
                                                      /**
                                                       * Invoked when an action occurs.
                                                       *
                                                       * @param e the event to be processed
                                                       */
                                                      public void actionPerformed(ActionEvent e) {
                                                          clearMembershipFields();

                                                      }
                                                  });
    }

    public static void main(String[] args) {
        memberDAO = new MemberDAO();
        membershipDAO = new MembershipDAO();
        // Set look and feel before calling frame
        setupLookAndFeel();
        setupFrame();
    }

    private void clearMembershipFields() {
        membershipIDTxt.setText(null);
        membershipPriceTxt.setText(null);
        membershipNTxt.setText(null);
        membCreateButton.setEnabled(true);
        membUpdateButton.setEnabled(false);
        membRemoveButton.setEnabled(false);
        membershipTable.getSelectionModel().clearSelection();
    }


    private static void setupFrame() {
        JFrame frame = new JFrame("main");
        frame.setContentPane(new main().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setVisible(true);
    }

    public void updateMemCB() {
        DB db = DB.getInstance();
        ResultSet rs = null;
        membershipLevel.removeAllItems();
        view_membershipLevel.removeAllItems();

        try {
            String sql = "SELECT * FROM MEMBERSHIP";
            PreparedStatement stmt = db.getPreparedStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {

                membershipLevel.addItem(rs.getString("MEMNAME"));
                view_membershipLevel.addItem(rs.getString("MEMNAME"));
            }
        } catch (SQLException ex) {
            System.err.println(ex.toString());

        }
    }

    private static void setupLookAndFeel() {
        // set look and feel of application
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Metal".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        membersTable = new JTable();
        membershipTable = new JTable();

        view_stateComboBox = new JComboBox();
        view_membershipLevel = new JComboBox();
        stateComboBox = new JComboBox();
        membershipLevel = new JComboBox();
        InitComboBoxes();
        InitMemberTable();
        InitMembershipTable();


    }


    private void InitComboBoxes() {
        stateComboBox.setModel(new DefaultComboBoxModel(new String[]{"AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "VW", "WI", "WY"}));
        view_stateComboBox.setModel(new DefaultComboBoxModel(new String[]{"AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "VW", "WI", "WY"}));

        // set membershipTable level model
        updateMemCB();
//        membershipLevel.setModel(new DefaultComboBoxModel(new String[]{"Basic", "Athlete", "Extreme"}));
        view_membershipLevel.setModel(new DefaultComboBoxModel(new String[]{"Basic", "Athlete", "Extreme"}));

    }

    private void InitMemberTable() {
        membersTable.setModel(new DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "MemberID", "First Name", "Last Name", "Street Address", "City", "State", "Zip", "Phone Number", "Membership"
                }
        ) {
            boolean[] canEdit = new boolean[]{
                    false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });
        membersTable.setColumnSelectionAllowed(false);
        membersTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                tblAuthorMouseClicked(evt);
            }
        });
    }

    private void InitMembershipTable() {
        membershipTable.setModel(new DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "Membership ID", "Membership Price", "Membership Name"
                }
        ) {
            boolean[] canEdit = new boolean[]{
                    false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });
        membershipTable.setColumnSelectionAllowed(false);
        membershipTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                tblMembershipMouseClicked(evt);
            }
        });

    }

    private void tblMembershipMouseClicked(MouseEvent evt) {
        int i = membershipTable.getSelectedRow();
        TableModel model = membershipTable.getModel();
        membershipIDTxt.setText(model.getValueAt(i, 0).toString());
        membershipNTxt.setText(model.getValueAt(i, 2).toString());
        membershipPriceTxt.setText(model.getValueAt(i, 1).toString());
        membCreateButton.setEnabled(false);
        membUpdateButton.setEnabled(true);
        membRemoveButton.setEnabled(true);

    }

    private void tblAuthorMouseClicked(MouseEvent evt) {
        int i = membersTable.getSelectedRow();
        TableModel model = membersTable.getModel();
        memberIDTxt.setText(model.getValueAt(i, 0).toString());
        view_firstNameTxt.setText(model.getValueAt(i, 1).toString());
        view_lastNameTxt.setText(model.getValueAt(i, 2).toString());
        view_streetAddrTxt.setText(model.getValueAt(i, 3).toString());
        view_cityTxt.setText(model.getValueAt(i, 4).toString());
        view_stateComboBox.setSelectedItem((model.getValueAt(i, 5).toString()));
        view_zipTxt.setText(model.getValueAt(i, 6).toString());
        view_phoneTxt.setText(model.getValueAt(i, 7).toString());

    }

    private void refreshMemberTable() {
        List<Member> members = memberDAO.getAll();
        DefaultTableModel model = (DefaultTableModel) membersTable.getModel();
        for (int i = model.getRowCount() - 1; i >= 0; i--) {
            model.removeRow(i);

        }
        for (Member member : members) {
            Object[] row = new Object[9];
            row[0] = member.getMemberID();
            row[1] = member.getFirstName();
            row[2] = member.getLastName();
            row[3] = member.getStreetAddress();
            row[4] = member.getCity();
            row[5] = member.getState();
            row[6] = member.getZip();
            row[7] = member.getPhone();
            row[8] = member.getMembershipID();
            model.addRow(row);
        }
    }

    private void refreshMembershipTable() {
        List<Membership> memberships = membershipDAO.getAll();
        DefaultTableModel model = (DefaultTableModel) membershipTable.getModel();
        for (int i = model.getRowCount() - 1; i >= 0; i--) {
            model.removeRow(i);

        }
        for (Membership membership : memberships) {
            Object[] row = new Object[3];
            row[0] = membership.getMembershipID();
            row[1] = membership.getMemPrice();
            row[2] = membership.getMemName();
            model.addRow(row);
        }
    }

    private void printMembers() {
        List<String> headers;
        headers = memberDAO.getColumnNames();
        int numberCols = headers.size();
        //Print column names as header
        for (int i = 0; i < numberCols; i++) {
            String header = headers.get(i);
            System.out.printf("%17s", header);
        }
        System.out.println();
        //Print the results
        List<Member> members = memberDAO.getAll();
        int numberRows = members.size();
        for (int i = 0; i < numberRows; i++) {
            System.out.printf("%17s%17s%17s%17s%17s%17s%17s%17s", members.get(i).getMemberID(), members.get(i).getFirstName(), members.get(i).getLastName(), members.get(i).getStreetAddress(), members.get(i).getCity(), members.get(i).getState(), members.get(i).getZip(), members.get(i).getPhone(), members.get(i).getMembershipID());
            System.out.println();
        }
    }

    public static boolean isValid(String s) {
        // The given argument to compile() method
        // is regular expression. With the help of
        // regular expression we can validate mobile
        // number.
        // 1) Begins with 0 or 91
        // 2) Then contains 7 or 8 or 9.
        // 3) Then contains 9 digits
        Pattern p = Pattern.compile("\\\\d{3}-\\\\d{7}");

        // Pattern class contains matcher() method
        // to find matching between given number
        // and regular expression
        Matcher m = p.matcher(s);
        return (m.find() && m.group().equals(s));
    }

    public static boolean zipIsValid(String s) {

        Pattern p = Pattern.compile("^[0-9]{5}(?:-[0-9]{4})?$");
        Matcher m = p.matcher(s);
        return (m.find() && m.group().equals(s));

    }

    public void clearViewFields() {
        memberIDTxt.setText(null);
        view_firstNameTxt.setText(null);
        view_lastNameTxt.setText(null);
        view_streetAddrTxt.setText(null);
        view_cityTxt.setText(null);
        view_zipTxt.setText(null);
        view_phoneTxt.setText(null);
        view_stateComboBox.setSelectedIndex(0);
        view_membershipLevel.setSelectedIndex(0);
    }

    public void clearAddFields() {
        memberIDTxt.setText(null);
        firstNameTxt.setText(null);
        lastNameTxt.setText(null);
        addressTxt.setText(null);
        cityTxt.setText(null);
        zipTxt.setText(null);
        phoneTxt.setText(null);
        stateComboBox.setSelectedIndex(0);
        membershipLevel.setSelectedIndex(0);
    }


    private static void generateQRImage(String text, int width, int height, String filePath)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        Main = new JPanel();
        Main.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setIcon(new ImageIcon(getClass().getResource("/images/p1.png")));
        label1.setText("Welcome to Hightower Gym Portal");
        Main.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tabbedPane1 = new JTabbedPane();
        Main.add(tabbedPane1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(10, 3, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Add Member", panel1);
        final JLabel label2 = new JLabel();
        label2.setText("First Name");
        panel1.add(label2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        firstNameTxt = new JTextField();
        panel1.add(firstNameTxt, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Last Name");
        panel1.add(label3, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lastNameTxt = new JTextField();
        panel1.add(lastNameTxt, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Address");
        panel1.add(label4, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addressTxt = new JTextField();
        panel1.add(addressTxt, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("City");
        panel1.add(label5, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("State");
        panel1.add(label6, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Zip Code");
        panel1.add(label7, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Phone Number");
        panel1.add(label8, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("Membership Level");
        panel1.add(label9, new com.intellij.uiDesigner.core.GridConstraints(7, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cityTxt = new JTextField();
        panel1.add(cityTxt, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        panel1.add(stateComboBox, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        zipTxt = new JTextField();
        panel1.add(zipTxt, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        phoneTxt = new JTextField();
        panel1.add(phoneTxt, new com.intellij.uiDesigner.core.GridConstraints(6, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        panel1.add(membershipLevel, new com.intellij.uiDesigner.core.GridConstraints(7, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        submitButton = new JButton();
        submitButton.setText("Submit");
        panel1.add(submitButton, new com.intellij.uiDesigner.core.GridConstraints(8, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        resetButton = new JButton();
        resetButton.setText("Reset");
        panel1.add(resetButton, new com.intellij.uiDesigner.core.GridConstraints(9, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("View/Update Memebrs", panel2);
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(12, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("MemberID");
        panel3.add(label10, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label11 = new JLabel();
        label11.setText("First Name");
        panel3.add(label11, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label12 = new JLabel();
        label12.setText("Last Name");
        panel3.add(label12, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label13 = new JLabel();
        label13.setText("Street Address");
        panel3.add(label13, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label14 = new JLabel();
        label14.setText("City");
        panel3.add(label14, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label15 = new JLabel();
        label15.setText("State");
        panel3.add(label15, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label16 = new JLabel();
        label16.setText("Zip");
        panel3.add(label16, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label17 = new JLabel();
        label17.setText("Phone");
        panel3.add(label17, new com.intellij.uiDesigner.core.GridConstraints(7, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label18 = new JLabel();
        label18.setText("Membership");
        panel3.add(label18, new com.intellij.uiDesigner.core.GridConstraints(8, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        memberIDTxt = new JTextField();
        memberIDTxt.setEditable(false);
        memberIDTxt.setToolTipText("You cannot edit this!");
        panel3.add(memberIDTxt, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        view_firstNameTxt = new JTextField();
        view_firstNameTxt.setText("");
        panel3.add(view_firstNameTxt, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        view_lastNameTxt = new JTextField();
        panel3.add(view_lastNameTxt, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        view_streetAddrTxt = new JTextField();
        panel3.add(view_streetAddrTxt, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        view_cityTxt = new JTextField();
        view_cityTxt.setText("");
        panel3.add(view_cityTxt, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        view_zipTxt = new JTextField();
        panel3.add(view_zipTxt, new com.intellij.uiDesigner.core.GridConstraints(6, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        view_phoneTxt = new JTextField();
        panel3.add(view_phoneTxt, new com.intellij.uiDesigner.core.GridConstraints(7, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        memberUpdateBtn = new JButton();
        memberUpdateBtn.setText("Update");
        panel3.add(memberUpdateBtn, new com.intellij.uiDesigner.core.GridConstraints(9, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        memberDeleteBtn = new JButton();
        memberDeleteBtn.setText("Delete");
        panel3.add(memberDeleteBtn, new com.intellij.uiDesigner.core.GridConstraints(10, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel3.add(view_stateComboBox, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel3.add(view_membershipLevel, new com.intellij.uiDesigner.core.GridConstraints(8, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        generatePassButton = new JButton();
        generatePassButton.setText("Generate Pass");
        panel3.add(generatePassButton, new com.intellij.uiDesigner.core.GridConstraints(11, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel2.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(500, 500), null, 0, false));
        membersTable.setAutoCreateRowSorter(false);
        membersTable.setAutoResizeMode(4);
        scrollPane1.setViewportView(membersTable);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(8, 3, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Manage Membreships", panel4);
        final JLabel label19 = new JLabel();
        label19.setText("Membership ID");
        panel4.add(label19, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label20 = new JLabel();
        label20.setText("Membership Price");
        panel4.add(label20, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label21 = new JLabel();
        label21.setText("Membership Name");
        panel4.add(label21, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        membershipIDTxt = new JTextField();
        membershipIDTxt.setEditable(false);
        membershipIDTxt.setToolTipText("You cannot edit this!");
        panel4.add(membershipIDTxt, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        membershipPriceTxt = new JTextField();
        panel4.add(membershipPriceTxt, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        membershipNTxt = new JTextField();
        panel4.add(membershipNTxt, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        panel4.add(scrollPane2, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 5, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane2.setViewportView(membershipTable);
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        panel4.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        membRemoveButton = new JButton();
        membRemoveButton.setEnabled(false);
        membRemoveButton.setText("Remove");
        panel4.add(membRemoveButton, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        membUpdateButton = new JButton();
        membUpdateButton.setEnabled(false);
        membUpdateButton.setText("Update");
        panel4.add(membUpdateButton, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        membCreateButton = new JButton();
        membCreateButton.setEnabled(true);
        membCreateButton.setText("Create");
        panel4.add(membCreateButton, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        membClearButton = new JButton();
        membClearButton.setText("Clear");
        panel4.add(membClearButton, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return Main;
    }


}
