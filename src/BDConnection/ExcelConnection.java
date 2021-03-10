package BDConnection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;
import javax.swing.JOptionPane;

/**
 *
 * @author Gabriel Cabredo <202000210 at @est.umms.edu>
 */
public class ExcelConnection {

    private static Connection bdConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost/bd_tkteusers", "root", "");
    }

    public static ArrayList<String> userList() throws SQLException {
        //int u = 0;
        ArrayList<String> users = new ArrayList<>();

        Connection cn = bdConnection();
        PreparedStatement pst = cn.prepareStatement("SELECT `Name`, `LastName` FROM `users`");
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            users.add(rs.getNString(1).toUpperCase() + " " + rs.getString(2).toUpperCase());
        }

        return users;
    }

    private static String getRowTestofUser(int idUser) throws SQLException {
        String rowData = "";
        Connection cn = bdConnection();
        PreparedStatement pst = cn.prepareStatement("SELECT `Tests` FROM `users` where `ID` = " + idUser);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            rowData = rs.getString(1);
        }
        return rowData;
    }

    public static String[][] getTestsofUser(int idUser) throws SQLException {
        String[] rowData = getRowTestofUser(idUser).split("/");
        String[] stories = {};
        String[] savvy = {};
        String[] pm = {};
        if (!rowData[0].isEmpty()) {
            stories = new String[rowData.length];
            savvy = new String[rowData.length];
            pm = new String[rowData.length];
            for (int i = 0; i < rowData.length; i++) {
                String[] rowTest = rowData[i].split("-");
                stories[i] = rowTest[0];
                savvy[i] = Integer.parseInt(rowTest[1]) + "%";
                pm[i] = rowTest[2];
            }
            stories = getStorieTittles(stories);
        }
        String[][] table = new String[3][];
        table[0] = stories;
        table[1] = pm;
        table[2] = savvy;
        return table;

    }

    public static int getAge(int id) throws SQLException {
        Connection cn = bdConnection();
        PreparedStatement pst = cn.prepareStatement("SELECT `Age` FROM `users` where ID = " + id);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }

        return 0;
    }

    private static String[] getStorieTittles(String[] A) throws SQLException {
        String[] tittles = new String[A.length];
        ArrayList stories = getStoriesTittles();
        for (int i = 0; i < A.length; i++) {
            tittles[i] = (String) stories.get(Integer.parseInt(A[i]) - 1);
        }
        return tittles;
    }

    public static ArrayList<String> getStoriesTittles() throws SQLException {
        ArrayList stories = new ArrayList<String>();
        Connection cn = bdConnection();
        PreparedStatement pst = cn.prepareStatement("Select Tittle from stories");
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            stories.add(rs.getString(1));
        }
        return stories;
    }

    public static boolean addUser(String name, String lastName, int age) throws SQLException {
        ArrayList<String> userList = userList();
        String fullName = name.toUpperCase() + " " + lastName.toUpperCase();
        boolean exist = false;
        for (String s : userList) {
            if (s.equals(fullName)) {
                exist = true;
                break;
            }
        }
        if (!exist) {
            Connection cn = bdConnection();
            PreparedStatement ps = cn.prepareStatement("insert into users values('0','" + name + "','" + lastName + "','" + age + "',' ')");
            ps.executeUpdate();
        } else {
            return false;
        }
        return true;
    }

    public static void upStories() throws SQLException, FileNotFoundException, IOException {
        String contraseña = JOptionPane.showInputDialog("Contraseña");
        if (contraseña != null) {
            String line;
            Connection cn = bdConnection();
            PreparedStatement ps = cn.prepareStatement("insert into stories values (0,?,?,?,?)");
            File file = new File("data\\stories.txt");
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            boolean a = contraseña.equals("edm190202");
            do {
                line = br.readLine();
                if (a = line != null && !line.isBlank()) {
                    String[] data = line.split("-");
                    ps.setString(1, data[0]);
                    ps.setString(2, data[1]);
                    ps.setString(3, data[2]);
                    ps.setString(4, data[3]);
                    ps.executeUpdate();

                }
            } while (a);
            JOptionPane.showMessageDialog(null, "Base de datos llenada con exito!");
        }

    }

    public static String[] getAnwerWords(String title) {
        String[] answerWords = new String[2];
        Connection cn;
        try {
            cn = bdConnection();
            PreparedStatement pst = cn.prepareStatement("Select Words , Answers from stories where Tittle = '" + title + "'");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                answerWords[0] = rs.getString(1);
                answerWords[1] = rs.getString(2);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error contactate con el desarrollador" + "\n" + " Mensaje de error: " + e.getMessage());
        }

        return answerWords;
    }

    public static ArrayList<String> getStoriesTittlesinRange(String age) {
        ArrayList stories = new ArrayList<String>();
        try {
            Connection cn = bdConnection();
            PreparedStatement pst;
            pst = cn.prepareStatement("SELECT `Tittle` FROM `stories` where Age = '" + age + "'");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                stories.add(rs.getString(1));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error contactate con el desarrollador" + "\n" + " Mensaje de error: " + e.getMessage());
        }
        return stories;
    }

    public static void updateTest(int id, int idStorie, int ppm, int comp) throws SQLException {
        String test = idStorie + "-" + comp + "-" + ppm + "";

        String rowData = getRowTestofUser(id);
        if (rowData.isBlank()) {
            rowData = test;
        } else {
            rowData += "/" + test;
        }
        Connection cn = bdConnection();
        PreparedStatement pst = cn.prepareStatement("UPDATE `users` SET `Tests`= '" + rowData + "' WHERE `ID` = " + id);
        pst.executeUpdate();
    }

    public static void main(String[] args) throws SQLException {
        updateTest(3, 1, 670, 100);
    }
}
