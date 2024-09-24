package by.it_academy.jd2.golubev_107.mail_sender.storage;

import by.it_academy.jd2.golubev_107.mail_sender.storage.entity.RecipientAddress;

public interface IRecipientAddressStorage {

    RecipientAddress create(RecipientAddress address);

    RecipientAddress readById(Long id);

    RecipientAddress readByEmail(String emailAddress);
}
