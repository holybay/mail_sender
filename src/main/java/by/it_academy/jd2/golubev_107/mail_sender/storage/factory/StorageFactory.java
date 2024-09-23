package by.it_academy.jd2.golubev_107.mail_sender.storage.factory;

import by.it_academy.jd2.golubev_107.mail_sender.storage.IRecipientStorage;
import by.it_academy.jd2.golubev_107.mail_sender.storage.connection.factory.ConnectionManagerFactory;
import by.it_academy.jd2.golubev_107.mail_sender.storage.impl.RecipientStorage;

public class StorageFactory {

    private static final StorageFactory INSTANCE = new StorageFactory(
            ConnectionManagerFactory.getInstance());
    private final IRecipientStorage recipientStorage;

    private StorageFactory(ConnectionManagerFactory cmf) {
        recipientStorage = new RecipientStorage(cmf.get());
    }

    public static StorageFactory getInstance() {
        return INSTANCE;
    }

    public IRecipientStorage getRecipientStorage() {
        return recipientStorage;
    }
}
