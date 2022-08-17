package signaling_server.Controller;

import signaling_server.Model.UserInfo;
import signaling_server.Model.UserSettings;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * Responsible for interacting with the MySQL database.
 */
public final class DatabaseConnector {
    private static final String ipAddressOfDatabase = "127.0.0.1";
    private static final String portNumberOfDatabase = "3306";
    private static final String databaseName = "GLocON";
    private static final String databaseConnectionUrl = "jdbc:mysql://" + ipAddressOfDatabase + ":" + portNumberOfDatabase + "/" + databaseName;
    private static final String databaseUserName = "root";
    private static final String databasePassword = "Sql-1337!";

    private DatabaseConnector() { }

    /**
     * Inserts new UserInfo into the database.
     * @param userInfo The UserInfo that needs to be inserted.
     */
    public static void insertUserInfoInDatabase(UserInfo userInfo) {
        // Open a connection
        try(Connection conn = DriverManager.getConnection(databaseConnectionUrl, databaseUserName, databasePassword); Statement stmt = conn.createStatement()) {
            // Build the string for inserting
            String insertSql = "INSERT INTO user_info(user_id, public_ip, public_port, private_ip, private_port, latitude, longitude) "
                             + "VALUES('" + userInfo.getPeerId() + "', '" +
                                            userInfo.getPublicIP() + "', '" +
                                            userInfo.getPublicPort() + "', '" +
                                            userInfo.getPrivateIP() + "', '" +
                                            userInfo.getPrivatePort() + "', '" +
                                            userInfo.getLatitude() + "', '" +
                                            userInfo.getLongitude() + "')";
            // Execute the string on the database
            stmt.executeUpdate(insertSql);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Inserting of user info for peer " + userInfo.getPeerId() + " failed");
        }
    }

    /**
     * Obtains the configured userSettings of a userInfo from the database.
     * @param userInfo The user that wants to know their settings.
     * @return Their settings.
     */
    public static UserSettings obtainUserSettings(UserInfo userInfo) {
        int counter = 0;
        while (counter <= 1) {
            // Open a connection
            try(Connection conn = DriverManager.getConnection(databaseConnectionUrl, databaseUserName, databasePassword); Statement stmt = conn.createStatement()) {
                // Build the string for selecting
                String selectSql = "SELECT user_id, li_enabled FROM user_settings WHERE user_id = '" + userInfo.getPeerId() + "'";
                // Execute the string on the database
                ResultSet resultSet = stmt.executeQuery(selectSql);

                String user_id = null;
                boolean li_enabled = false;
                while (resultSet.next()) {
                    // Retrieve by column name
                    user_id = resultSet.getString("user_id");
                    li_enabled = resultSet.getBoolean("li_enabled");
                }
                UserSettings userSettings = new UserSettings(user_id, li_enabled);

                if (userSettings.getPeer_id() == null) {
                    System.out.println("Peer " + userInfo.getPeerId() + " not in user settings table, returning default values and inserting into the database");
                    UserSettings defaultUserSettings = new UserSettings(userInfo.getPeerId(), false);
                    insertUserSettingsInDatabase(defaultUserSettings);
                    return defaultUserSettings;
                }
                else {
                    System.out.println("User settings for peer " + userInfo.getPeerId() + " successfully obtained");
                    return userSettings;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Trying to obtain the user settings for peer " + userInfo.getPeerId() + " failed");
            }
        }
        System.out.println("Null returned for user settings of peer " + userInfo.getPeerId());
        return null;
    }

    /**
     * Inserts new UserSettings into the database.
     * @param userSettings The UserSettings that needs to be inserted.
     */
    private static void insertUserSettingsInDatabase(UserSettings userSettings) {
        int userSettingsLiEnabled = userSettings.isLi_enabled() ? 1 : 0;
        // Open a connection
        try(Connection conn = DriverManager.getConnection(databaseConnectionUrl, databaseUserName, databasePassword); Statement stmt = conn.createStatement()) {
            // Build the string for inserting
            String insertSql = "INSERT INTO user_settings(user_id, li_enabled) "
                             + "VALUES('" + userSettings.getPeer_id() + "', '" +
                                            userSettingsLiEnabled + "')";
            // Execute the string on the database
            stmt.executeUpdate(insertSql);
            System.out.println("User settings for peer " + userSettings.getPeer_id() + " successfully inserted");
        } catch (SQLException e) {
            System.out.println("Inserting of user settings for peer " + userSettings.getPeer_id() + " failed");
            e.printStackTrace();
        }
    }

    /**
     * Updates the UserSettings of a user in the database.
     * @param userSettings The UserSettings that needs to be updated.
     */
    public static void updateUserSettingsInDatabase(UserSettings userSettings) {
        int userSettingsLiEnabled = userSettings.isLi_enabled() ? 1 : 0;
        // Open a connection
        try(Connection conn = DriverManager.getConnection(databaseConnectionUrl, databaseUserName, databasePassword); Statement stmt = conn.createStatement()) {
            // Build the string for inserting
            String insertSql = "UPDATE user_settings " +
                               "SET li_enabled = '" + userSettingsLiEnabled + "', last_updated = '" + LocalDateTime.now() + "' " +
                               "WHERE user_id = '" + userSettings.getPeer_id() + "'";
            // Execute the string on the database
            stmt.executeUpdate(insertSql);
            System.out.println("User settings for peer " + userSettings.getPeer_id() + " successfully updated");
        } catch (SQLException e) {
            System.out.println("Updating of user settings for peer " + userSettings.getPeer_id() + " failed");
            e.printStackTrace();
        }
    }
}