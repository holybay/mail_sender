package by.it_academy.jd2.golubev_107.mail_sender.service.factory;

import by.it_academy.jd2.golubev_107.mail_sender.platform.IPropertyReader;
import by.it_academy.jd2.golubev_107.mail_sender.platform.impl.PropertyReader;
import by.it_academy.jd2.golubev_107.mail_sender.service.IMailSender;
import by.it_academy.jd2.golubev_107.mail_sender.service.IMailService;
import by.it_academy.jd2.golubev_107.mail_sender.service.IRecipientAddressService;
import by.it_academy.jd2.golubev_107.mail_sender.service.config.MailSenderConfig;
import by.it_academy.jd2.golubev_107.mail_sender.service.impl.MailSender;
import by.it_academy.jd2.golubev_107.mail_sender.service.impl.MailService;
import by.it_academy.jd2.golubev_107.mail_sender.service.impl.RecepientAddressService;
import by.it_academy.jd2.golubev_107.mail_sender.storage.factory.StorageFactory;

import java.util.Properties;

public class ServiceFactory {

    public static final String USER_PROP = "mail.smtp.user";
    public static final String PASSWORD_PROP = "user.password";
    public static final String DEBUG_MODE_PROP = "debug.mode";
    private static final ServiceFactory INSTANCE = new ServiceFactory(StorageFactory.getInstance(),
            "/smtpMailRu.properties");
    private final IRecipientAddressService recipientAddressService;
    private final IMailService mailService;
    private final IMailSender mailSender;

    private ServiceFactory(StorageFactory storageFactory, String mailPropertiesFile) {
        recipientAddressService = new RecepientAddressService(storageFactory.getRecipientStorage());
        mailService = new MailService(storageFactory.getMailStorage(), recipientAddressService);
        MailSenderConfig config = setMailConfig(mailPropertiesFile);
        mailSender = new MailSender(config);
    }

    public static ServiceFactory getInstance() {
        return INSTANCE;
    }

    public IRecipientAddressService getRecipientAddressService() {
        return recipientAddressService;
    }

    public IMailService getMailService() {
        return mailService;
    }

    public IMailSender getMailSender() {
        return mailSender;
    }

    private MailSenderConfig setMailConfig(String mailPropertiesFile) {
        IPropertyReader reader = new PropertyReader(mailPropertiesFile);
        Properties props = reader.getAll();

        MailSenderConfig config = new MailSenderConfig();

        config.setFrom(props.getProperty(USER_PROP));
        config.setUser(props.getProperty(USER_PROP));
        props.remove(USER_PROP);
        config.setPassword(props.getProperty(PASSWORD_PROP));
        props.remove(PASSWORD_PROP);
        config.setDebugModeOn(Boolean.parseBoolean(props.getProperty(DEBUG_MODE_PROP)));
        props.remove(DEBUG_MODE_PROP);

        config.setMailProps(props);
        return config;
    }
}
