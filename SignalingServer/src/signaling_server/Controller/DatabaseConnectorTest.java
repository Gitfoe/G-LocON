package signaling_server.Controller;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import signaling_server.Model.UserInfo;
import signaling_server.Model.UserSettings;

class DatabaseConnectorTest {
    private final String userID = "100"; // user_id "100" is used as a testing user for database connections.

    @AfterEach
    void init() {
        DatabaseConnector.deleteUserSettingsFromDatabase(userID); // Delete the user from the database after executing
    }

    @org.junit.jupiter.api.Test
    void insertUserInfoInDatabase_ValidUserSettings_ReturnsTrue() {
        // Arrange
        UserInfo userInfo = new UserInfo("UnitTest", 0, "UnitTest", 0, 0.0, 0.0, userID);

        // Act
        boolean successful = DatabaseConnector.insertUserInfoInDatabase(userInfo);

        // Assert
        Assert.assertTrue(successful);
    }

    @org.junit.jupiter.api.Test
    void obtainUserSettings_NoUserSettingsExistYet_ReturnsDefaultSettings() {
        // Arrange
        UserInfo userInfo = new UserInfo("UnitTest", 0, "UnitTest", 0, 0.0, 0.0, userID);
        UserSettings userSettings;

        // Act
        userSettings = DatabaseConnector.obtainUserSettings(userInfo);

        // Assert
        Assert.assertNotNull(userSettings);
        Assert.assertFalse(userSettings.isLi_enabled());
        Assert.assertEquals(userID, userSettings.getPeer_id());
    }

    @org.junit.jupiter.api.Test
    void obtainUserSettings_UserSettingsExistAndLIenabledIsTrue_ReturnsCorrectUserSettings() {
        // Arrange
        UserInfo userInfo = new UserInfo("UnitTest", 0, "UnitTest", 0, 0.0, 0.0, userID);
        UserSettings userSettings = DatabaseConnector.obtainUserSettings(userInfo);
        userSettings.setLi_enabled(true);
        DatabaseConnector.updateUserSettingsInDatabase(userSettings);

        // Act
        userSettings = DatabaseConnector.obtainUserSettings(userInfo);

        // Assert
        Assert.assertNotNull(userSettings);
        Assert.assertTrue(userSettings.isLi_enabled());
        Assert.assertEquals(userID, userSettings.getPeer_id());
    }

    @org.junit.jupiter.api.Test
    void obtainUserSettings_UserDoesNotExist_ReturnsNull() {
        // Arrange
        UserInfo userInfo = new UserInfo("UnitTest", 0, "UnitTest", 0, 0.0, 0.0, "ThisPeerIDWillNeverExist");
        UserSettings userSettings;

        // Act
        userSettings = DatabaseConnector.obtainUserSettings(userInfo);

        // Assert
        Assert.assertNull(userSettings);
    }

    @org.junit.jupiter.api.Test
    void updateUserSettingsInDatabase_NewUserSettings_ReturnsTrue() {
        // Arrange
        UserInfo userInfo = new UserInfo("UnitTest", 0, "UnitTest", 0, 0.0, 0.0, userID);
        UserSettings userSettings = DatabaseConnector.obtainUserSettings(userInfo);
        userSettings.setLi_enabled(true);

        // Act
        boolean successful = DatabaseConnector.updateUserSettingsInDatabase(userSettings);

        // Assert
        Assert.assertTrue(successful);
    }
}