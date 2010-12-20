/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utsa.cs.selfcheck;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author spivek
 */
public class Database {

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private static final String dbPass = "65X6Qq7";
    private static final String dbUser = "postgres";

    public static List<String> getActiveProjects() {
        ArrayList<String> list = new ArrayList<String>();
        try {
            PreparedStatement st = getPreparedStatement("SELECT project_display_name FROM projects WHERE active = true ORDER BY project_display_name");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("project_display_name"));
            }
        } catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }

    public static File getProjectMakefile(String project) {
        File make = null;
        try {
            PreparedStatement st = getPreparedStatement("SELECT makefile_path FROM projects WHERE project_display_name = ? AND active = ?");
            st.setString(1, project);
            st.setBoolean(2, true);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                make = new File(rs.getString("makefile_path"));
            }
        } catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
        }
        return make;
    }

    private static PreparedStatement getPreparedStatement(String statement) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:postgresql:selfcheck", dbUser, dbPass);
        PreparedStatement st = conn.prepareStatement(statement);
        return st;
    }
}
