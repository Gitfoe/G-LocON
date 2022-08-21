package signaling_server.Controller;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import signaling_server.Model.UserInfo;
import signaling_server.Model.UserSettings;

import static org.junit.jupiter.api.Assertions.*;

class SignalingServerSendTest {
    private final String userID = "100"; // user_id "100" is used as a testing user.

    @Test
    void anonymizeUser_UserID100_AnonymizedCorrectly() {
        // Arrange
        UserInfo userInfo = new UserInfo("UnitTest", 0, "UnitTest", 0, 0.0, 0.0, userID);
        SignalingServerSend signalingServerSend = new SignalingServerSend();

        // Act
        userInfo = signalingServerSend.anonymizeUser(userInfo);

        // Assert
        Assert.assertEquals("Length of peerId", 40, userInfo.getPeerId().length());
        Assert.assertNotEquals("Contents of peerId", userID, userInfo.getPeerId());
    }

    @Test
    void removePersonalDataFromUserInfoAccordingToUserSettings_SettingsDisabled_LatitudeLongitudeNull() {
        // Arrange
        UserInfo userInfo = new UserInfo("UnitTest", 0, "UnitTest", 0, 0.0, 0.0, userID);
        DatabaseConnector.obtainUserSettings(userInfo); // Create new user settings
        SignalingServerSend signalingServerSend = new SignalingServerSend();

        // Act
        userInfo = signalingServerSend.removePersonalDataFromUserInfoAccordingToUserSettings(userInfo, false);

        // Assert
        Assert.assertNull("Latitude", userInfo.getLatitude());
        Assert.assertNull("Longitude", userInfo.getLongitude());

        // Clean-up
        DatabaseConnector.deleteUserSettingsFromDatabase(userID); // Delete the user from the database after executing
    }

    @Test
    void removePersonalDataFromUserInfoAccordingToUserSettings_SettingsEnabled_LatitudeLongitudeNotNull() {
        // Arrange
        UserInfo userInfo = new UserInfo("UnitTest", 0, "UnitTest", 0, 0.0, 0.0, userID);
        UserSettings userSettings = DatabaseConnector.obtainUserSettings(userInfo); // Create new user settings
        userSettings.setLi_enabled(true);
        DatabaseConnector.updateUserSettingsInDatabase(userSettings); // Sets li_enabled to 1
        SignalingServerSend signalingServerSend = new SignalingServerSend();

        // Act
        userInfo = signalingServerSend.removePersonalDataFromUserInfoAccordingToUserSettings(userInfo, false);

        // Assert
        Assert.assertNotNull("Latitude", userInfo.getLatitude());
        Assert.assertNotNull("Longitude", userInfo.getLongitude());

        // Clean-up
        DatabaseConnector.deleteUserSettingsFromDatabase(userID); // Delete the user from the database after executing
    }
}