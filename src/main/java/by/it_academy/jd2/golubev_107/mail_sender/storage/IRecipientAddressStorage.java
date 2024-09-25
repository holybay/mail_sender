package by.it_academy.jd2.golubev_107.mail_sender.storage;

import by.it_academy.jd2.golubev_107.mail_sender.storage.entity.RecipientAddress;

import java.util.Collection;
import java.util.List;

public interface IRecipientAddressStorage {

    RecipientAddress create(RecipientAddress address);

    List<RecipientAddress> create(Collection<RecipientAddress> addresses);

    RecipientAddress readById(Long id);

    List<RecipientAddress> readAllByIds(Collection<Long> idList);

    RecipientAddress readByEmail(String emailAddress);
}
