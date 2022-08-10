package signaling_server;

import java.sql.*;

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
        }
    }
}