package by.it_academy.jd2.golubev_107.mail_sender.service.factory;

import by.it_academy.jd2.golubev_107.mail_sender.service.IMailService;
import by.it_academy.jd2.golubev_107.mail_sender.service.IRecipientAddressService;
import by.it_academy.jd2.golubev_107.mail_sender.service.impl.MailService;
import by.it_academy.jd2.golubev_107.mail_sender.service.impl.RecepientAddressService;
import by.it_academy.jd2.golubev_107.mail_sender.storage.factory.StorageFactory;

public class ServiceFactory {

    private static final ServiceFactory INSTANCE = new ServiceFactory(StorageFactory.getInstance());

    private final IRecipientAddressService recipientAddressService;
    private final IMailService mailService;

    private ServiceFactory(StorageFactory storageFactory) {
        recipientAddressService = new RecepientAddressService(storageFactory.getRecipientStorage());
        mailService = new MailService(storageFactory.getMailStorage(), recipientAddressService);
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
}
