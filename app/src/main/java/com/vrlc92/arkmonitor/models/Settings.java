package com.vrlc92.arkmonitor.models;

public class Settings {
    private String username;
    private String arkAddress;
    private String publicKey;
    private String ipAddress;
    private int port;
    private boolean sslEnabled;
    private boolean defaultServerEnabled;
    private long notificationInterval;

    public static final String USERNAME_ATTR = "settings.username";
    public static final String ARK_ADDRESS_ATTR = "settings.ark_address";
    public static final String PUBLIC_KEY_ATTR = "settings.public_key";
    public static final String IP_ATTR = "settings.ip_address";
    public static final String PORT_ATTR = "settings.port";
    public static final String SSL_ENABLED_ATTR = "settings.ssl_enabled";
    public static final String DEFAULT_SERVER_ENABLED_ATTR = "settings.default_server_enabled";
    public static final String NOTIFICATION_INTERVAL_ATTR = "settings.notification_interval_attr";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getArkAddress() {
        return arkAddress;
    }

    public void setArkAddress(String arkAddress) {
        this.arkAddress = arkAddress;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean getSslEnabled() {
        return sslEnabled;
    }

    public void setSslEnabled(boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
    }

    public boolean getDefaultServerEnabled() {
        return defaultServerEnabled;
    }

    public void setNotificationInterval(long notificationInterval) {
        this.notificationInterval = notificationInterval;
    }

    public long getNotificationInterval() {
        return notificationInterval;
    }

    public void setDefaultServerEnabled(boolean defaultServerEnabled) {
        this.defaultServerEnabled = defaultServerEnabled;
        if (defaultServerEnabled) {
            setIpAddress(null);
            setPort(-1);
            setSslEnabled(true);
        }
    }
}
