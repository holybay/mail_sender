package by.it_academy.jd2.golubev_107.mail_sender.storage.factory;

import by.it_academy.jd2.golubev_107.mail_sender.storage.IMailStorage;
import by.it_academy.jd2.golubev_107.mail_sender.storage.IRecipientAddressStorage;
import by.it_academy.jd2.golubev_107.mail_sender.storage.connection.factory.ConnectionManagerFactory;
import by.it_academy.jd2.golubev_107.mail_sender.storage.impl.MailStorage;
import by.it_academy.jd2.golubev_107.mail_sender.storage.impl.RecipientAddressStorage;

public class StorageFactory {

    private static final StorageFactory INSTANCE = new StorageFactory(
            ConnectionManagerFactory.getInstance());
    private final IRecipientAddressStorage recipientStorage;
    private final IMailStorage mailStorage;

    private StorageFactory(ConnectionManagerFactory cmf) {
        recipientStorage = new RecipientAddressStorage(cmf.get());
        mailStorage = new MailStorage(cmf.get());
    }

    public static StorageFactory getInstance() {
        return INSTANCE;
    }

    public IRecipientAddressStorage getRecipientStorage() {
        return recipientStorage;
    }

    public IMailStorage getMailStorage() {
        return mailStorage;
    }
}
