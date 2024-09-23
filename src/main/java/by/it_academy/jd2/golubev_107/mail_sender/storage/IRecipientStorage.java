package by.it_academy.jd2.golubev_107.mail_sender.storage;

import by.it_academy.jd2.golubev_107.mail_sender.storage.entity.Recipient;

public interface IRecipientStorage {

    Recipient create(Recipient recipient);

    Recipient readById(Long id);
}
