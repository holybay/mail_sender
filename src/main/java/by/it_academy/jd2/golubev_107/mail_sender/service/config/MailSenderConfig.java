package by.it_academy.jd2.golubev_107.mail_sender.service.config;

import java.util.Objects;
import java.util.Properties;

public class MailSenderConfig {

    private Properties mailProps;
    private String from;
    private String user;
    private String password;
    private boolean isDebugModeOn;

    public Properties getMailProps() {
        return mailProps;
    }

    public void setMailProps(Properties mailProps) {
        this.mailProps = mailProps;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isDebugModeOn() {
        return isDebugModeOn;
    }

    public void setDebugModeOn(boolean debugModeOn) {
        isDebugModeOn = debugModeOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MailSenderConfig config = (MailSenderConfig) o;
        return isDebugModeOn == config.isDebugModeOn && Objects.equals(mailProps, config.mailProps) && Objects.equals(from, config.from) && Objects.equals(user, config.user) && Objects.equals(password, config.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mailProps, from, user, password, isDebugModeOn);
    }

    @Override
    public String toString() {
        return "MailSenderConfig{" +
                "mailProps=" + mailProps +
                ", from='" + from + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", isDebugModeOn=" + isDebugModeOn +
                '}';
    }
}
