package com.example.pc.main;

/**
 * Saves all the settings the user might have configured for his profile.
 */
public class UserSettings {
    private String peer_id;
    private boolean li_enabled;

    public UserSettings() { }

    public String getPeer_id() {
        return peer_id;
    }

    public void setPeer_id(String peer_id) {
        this.peer_id = peer_id;
    }

    public boolean isLi_enabled() { return li_enabled; }

    public void setLi_enabled(boolean li_enabled) {
        this.li_enabled = li_enabled;
    }
}
