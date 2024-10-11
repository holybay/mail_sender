package by.it_academy.jd2.golubev_107.mail_sender.storage;

import by.it_academy.jd2.golubev_107.mail_sender.storage.entity.RecipientAddress;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface IRecipientAddressStorage {

    RecipientAddress create(RecipientAddress address);

    List<RecipientAddress> create(Collection<RecipientAddress> addresses);

    RecipientAddress readById(UUID id);

    List<RecipientAddress> readAllByIds(Collection<UUID> idList);

    RecipientAddress readByEmail(String emailAddress);
}
