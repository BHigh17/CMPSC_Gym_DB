package program;

import core.DB;
import entity.DAO;
import entity.Member;
import entity.MemberDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AddMember {
    private JPanel addMember;
    private JTextField firstNameTxt;
    private JTextField lastNameTxt;
    private JTextField addressTxt;
    private JTextField cityTxt;
    private JComboBox stateComboBox;
    private JTextField zipTxt;
    private JTextField phoneTxt;
    private JButton submitButton;
    private JButton resetButton;
    private JComboBox membershipLevel;
    private static DAO memberDAO;

    public JPanel getAddMember() {
        return addMember;
    }

    public AddMember() {
        $$$setupUI$$$();

        submitButton.addActionListener(new ActionListener() {
            /**
             * Invoked when submit action occurs.
             *
             * @param e the event to be processed
             */
            public void actionPerformed(ActionEvent e) {
                // check if zip text is not a number
                String zip = zipTxt.getText();
                int zipLength = zipTxt.getText().length();

                String phone = phoneTxt.getText().trim();
                int phoneLength = phoneTxt.getText().length();
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


                // TODO: Fix exception error
                if (zipIsValid(zip)) {
                    System.out.println("valid zip");
                    if (isValid(phone)) {
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
                                String sql = "INSERT INTO MEMBER(FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                                PreparedStatement stmt = db.getPreparedStatement(sql);
                                stmt.setString(1, fName);
                                stmt.setString(2, lName);
                                stmt.setString(3, streetA);
                                stmt.setString(4, city);
                                stmt.setString(5, state);
                                stmt.setString(6, zc);
                                stmt.setString(7, phoneTxt.getText());
                                stmt.setString(8, memSelection);
                                int rowInserted = stmt.executeUpdate();
                                if (rowInserted > 0) {
                                    System.out.println("A gym member was successful added!");
                                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), firstNameTxt.getText() + " " + lastNameTxt.getText() + " " + "has been added");
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
             * Invoked when reset button is clicked.
             *
             * @param e the event to be processed
             */
            public void actionPerformed(ActionEvent e) {
                clearFields();


            }
        });
        stateComboBox.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        membershipLevel.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            public void actionPerformed(ActionEvent e) {
            }
        });
    }

//    public static void main(String[] args) {
//        memberDAO = new MemberDAO();
//        JFrame frame = new JFrame("AddMember");
//        frame.setContentPane(new AddMember().addMember);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.setVisible(true);
//    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        addMember = new JPanel();
        addMember.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(11, 5, new Insets(0, 0, 0, 0), -1, -1));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        addMember.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(9, 1, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("First Name");
        addMember.add(label1, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        firstNameTxt = new JTextField();
        addMember.add(firstNameTxt, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Last Name");
        addMember.add(label2, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lastNameTxt = new JTextField();
        lastNameTxt.setText("");
        addMember.add(lastNameTxt, new com.intellij.uiDesigner.core.GridConstraints(2, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Address");
        addMember.add(label3, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addressTxt = new JTextField();
        addMember.add(addressTxt, new com.intellij.uiDesigner.core.GridConstraints(3, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("City");
        addMember.add(label4, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cityTxt = new JTextField();
        addMember.add(cityTxt, new com.intellij.uiDesigner.core.GridConstraints(4, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("State");
        addMember.add(label5, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Zip Code");
        addMember.add(label6, new com.intellij.uiDesigner.core.GridConstraints(6, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addMember.add(stateComboBox, new com.intellij.uiDesigner.core.GridConstraints(5, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        zipTxt = new JTextField();
        addMember.add(zipTxt, new com.intellij.uiDesigner.core.GridConstraints(6, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Phone");
        addMember.add(label7, new com.intellij.uiDesigner.core.GridConstraints(7, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        phoneTxt = new JTextField();
        addMember.add(phoneTxt, new com.intellij.uiDesigner.core.GridConstraints(7, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        submitButton = new JButton();
        submitButton.setText("Submit ");
        addMember.add(submitButton, new com.intellij.uiDesigner.core.GridConstraints(9, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        resetButton = new JButton();
        resetButton.setText("Reset");
        addMember.add(resetButton, new com.intellij.uiDesigner.core.GridConstraints(10, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Add Member");
        addMember.add(label8, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        addMember.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        addMember.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
        addMember.add(spacer4, new com.intellij.uiDesigner.core.GridConstraints(1, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("Membership Level");
        addMember.add(label9, new com.intellij.uiDesigner.core.GridConstraints(8, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addMember.add(membershipLevel, new com.intellij.uiDesigner.core.GridConstraints(8, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return addMember;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        stateComboBox = new JComboBox();
        membershipLevel = new JComboBox();
        addMember = new JPanel();


        // Setup state comboBox
        stateComboBox.setModel(new DefaultComboBoxModel(new String[]{"AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "VW", "WI", "WY"}));
        // set membership level model
        membershipLevel.setModel(new DefaultComboBoxModel(new String[]{"Basic", "Athlete", "Extreme"}));
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
        firstNameTxt.setText(null);
        lastNameTxt.setText(null);
        addressTxt.setText(null);
        cityTxt.setText(null);
        zipTxt.setText(null);
        phoneTxt.setText(null);
        stateComboBox.setSelectedIndex(0);
        membershipLevel.setSelectedIndex(0);
    }

//    private static void Member(String firstName, String lastName, String streetAddr, String city, String state, int zip, int phone, int membershipID) {
//        Member member;
//        member = new Member(firstName, lastName, streetAddr, city, state, zip, phone, membershipID);
//        memberDAO.insert(member);
//    }
}
