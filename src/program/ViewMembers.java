package program;

import core.DB;
import entity.DAO;
import entity.Member;
import entity.MemberDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import  com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class ViewMembers {
    private static DAO memberDAO;
    private JTextField memberIDTxt;
    private JTextField firstNameTxt;
    private JTextField lastNameTxt;
    private JButton memberDeleteBtn;
    private JButton MemberUpdateBtn;
    private JTable membersTable;
    private JTextField streetAddrTxt;
    private JTextField cityTxt;
    private JTextField zipTxt;
    private JComboBox stateComboBox;
    private JComboBox membershipCB;
    private JTextField phoneTxt;
    private JPanel ViewMembers;
    private JButton generatePassButton;

    public ViewMembers() {
        $$$setupUI$$$();
        refreshMemberTable();
        printMembers();
        MemberUpdateBtn.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            public void actionPerformed(ActionEvent e) {
                if (memberIDTxt.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "No member selected, try again");
                    return;
                }
                if (zipIsValid(zipTxt.getText())) {
                    System.out.println("valid zip");
                    if (isValid(phoneTxt.getText())) {
                        System.out.println("Valid Number");

                        try {
                            String fName = firstNameTxt.getText().toUpperCase().trim();
                            String lName = lastNameTxt.getText().toUpperCase().trim();
                            String streetA = streetAddrTxt.getText().toUpperCase().trim();
                            String city = cityTxt.getText().toUpperCase().trim();
                            String state = stateComboBox.getModel().getSelectedItem().toString();
                            String phon = (phoneTxt.getText());
                            String zc = zipTxt.getText();
                            String memSelection = membershipCB.getModel().getSelectedItem().toString();
                            String memberID = memberIDTxt.getText();

//                            Member(fName, lName, streetA, city, state, zc, phon, memSelection);

                            try {
                                DB db = DB.getInstance();
                                String sql = "UPDATE MEMBER SET FIRSTNAME=?, LASTNAME=?, STREETADDRESS=?, CITY=?, STATE=?, ZIP=?, PHONE=?, MEMBERSHIPID=? WHERE MEMBERID=?";
                                PreparedStatement stmt = db.getPreparedStatement(sql);
                                stmt.setString(1, fName);
                                stmt.setString(2, lName);
                                stmt.setString(3, streetA);
                                stmt.setString(4, city);
                                stmt.setString(5, state);
                                stmt.setString(6, zc);
                                stmt.setString(7, phoneTxt.getText());
                                stmt.setString(8, memSelection);
                                stmt.setInt(9, Integer.parseInt(memberID));
                                int rowInserted = stmt.executeUpdate();
                                if (rowInserted > 0) {
                                    System.out.println("Update Successful");
                                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Update Successful");
                                    refreshMemberTable();
                                    clearFields();


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
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            public void actionPerformed(ActionEvent e) {
                if (memberIDTxt.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "No member selected, try again");
                    return;
                }
                int option = JOptionPane.showConfirmDialog(JOptionPane.getRootFrame(), "Are you sure you want to delete " + firstNameTxt.getText() + " " + lastNameTxt.getText() + "?", "Delete confirmation", JOptionPane.YES_NO_OPTION);
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
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            public void actionPerformed(ActionEvent e) {
                if (memberIDTxt.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "No member selected, try again");
                    return;
                }

                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                final String QR_CODE_IMAGE_PATH = "./" + lastNameTxt.getText() + "QR.png";
                try {
                    generateQRImage(memberIDTxt.getText(), 350, 350, QR_CODE_IMAGE_PATH);
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Pass Available at " + QR_CODE_IMAGE_PATH);
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
    }

    public static void main(String[] args) {
        memberDAO = new MemberDAO();
        JFrame frame = new JFrame("ViewMembers");
        frame.setContentPane(new ViewMembers().ViewMembers);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        membersTable = new JTable();
        stateComboBox = new JComboBox();
        membershipCB = new JComboBox();

        // init member table
        InitMemberTable();
        InitComboBoxes();


    }

    private void InitComboBoxes() {
        stateComboBox.setModel(new DefaultComboBoxModel(new String[]{"AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "VW", "WI", "WY"}));
        // set membership level model
        membershipCB.setModel(new DefaultComboBoxModel(new String[]{"Basic", "Athlete", "Extreme"}));
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

    private void tblAuthorMouseClicked(MouseEvent evt) {
        int i = membersTable.getSelectedRow();
        TableModel model = membersTable.getModel();
        memberIDTxt.setText(model.getValueAt(i, 0).toString());
        firstNameTxt.setText(model.getValueAt(i, 1).toString());
        lastNameTxt.setText(model.getValueAt(i, 2).toString());
        streetAddrTxt.setText(model.getValueAt(i, 3).toString());
        cityTxt.setText(model.getValueAt(i, 4).toString());
        stateComboBox.setSelectedItem((model.getValueAt(i, 5).toString()));
        zipTxt.setText(model.getValueAt(i, 6).toString());
        phoneTxt.setText(model.getValueAt(i, 7).toString());
        membershipCB.setSelectedItem(model.getValueAt(i, 8).toString());

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

    static void printMembers() {
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
        Pattern p = Pattern.compile("(0/91)?[7-9][0-9]{9}");

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

    public void clearFields() {
        memberIDTxt.setText(null);
        firstNameTxt.setText(null);
        lastNameTxt.setText(null);
        streetAddrTxt.setText(null);
        cityTxt.setText(null);
        zipTxt.setText(null);
        phoneTxt.setText(null);
        stateComboBox.setSelectedIndex(0);
        membershipCB.setSelectedIndex(0);
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
        ViewMembers = new JPanel();
        ViewMembers.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(12, 2, new Insets(0, 0, 0, 0), -1, -1));
        ViewMembers.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("MemberID");
        panel1.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("First Name");
        panel1.add(label2, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Last Name");
        panel1.add(label3, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Street Address");
        panel1.add(label4, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("City");
        panel1.add(label5, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("State");
        panel1.add(label6, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Zip");
        panel1.add(label7, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Phone");
        panel1.add(label8, new com.intellij.uiDesigner.core.GridConstraints(7, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("Membership");
        panel1.add(label9, new com.intellij.uiDesigner.core.GridConstraints(8, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        memberIDTxt = new JTextField();
        memberIDTxt.setEditable(false);
        memberIDTxt.setToolTipText("You cannot edit this!");
        panel1.add(memberIDTxt, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        firstNameTxt = new JTextField();
        firstNameTxt.setText("");
        panel1.add(firstNameTxt, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        lastNameTxt = new JTextField();
        panel1.add(lastNameTxt, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        streetAddrTxt = new JTextField();
        panel1.add(streetAddrTxt, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        cityTxt = new JTextField();
        cityTxt.setText("");
        panel1.add(cityTxt, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        zipTxt = new JTextField();
        panel1.add(zipTxt, new com.intellij.uiDesigner.core.GridConstraints(6, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        phoneTxt = new JTextField();
        panel1.add(phoneTxt, new com.intellij.uiDesigner.core.GridConstraints(7, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        MemberUpdateBtn = new JButton();
        MemberUpdateBtn.setText("Update");
        panel1.add(MemberUpdateBtn, new com.intellij.uiDesigner.core.GridConstraints(9, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        memberDeleteBtn = new JButton();
        memberDeleteBtn.setText("Delete");
        panel1.add(memberDeleteBtn, new com.intellij.uiDesigner.core.GridConstraints(10, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel1.add(stateComboBox, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel1.add(membershipCB, new com.intellij.uiDesigner.core.GridConstraints(8, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        generatePassButton = new JButton();
        generatePassButton.setText("Generate Pass");
        panel1.add(generatePassButton, new com.intellij.uiDesigner.core.GridConstraints(11, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        ViewMembers.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        ViewMembers.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        membersTable.setAutoCreateRowSorter(false);
        membersTable.setAutoResizeMode(4);
        scrollPane1.setViewportView(membersTable);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return ViewMembers;
    }

}
