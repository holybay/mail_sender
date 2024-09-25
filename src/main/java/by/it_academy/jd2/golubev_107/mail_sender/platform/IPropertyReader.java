package by.it_academy.jd2.golubev_107.mail_sender.platform;

import java.util.Properties;

public interface IPropertyReader {

    Properties getAll();

    String getProperty(String key);
}