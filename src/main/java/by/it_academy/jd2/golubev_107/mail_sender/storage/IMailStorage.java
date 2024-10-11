package by.it_academy.jd2.golubev_107.mail_sender.storage;

import by.it_academy.jd2.golubev_107.mail_sender.storage.dto.EmailStorageOutDto;
import by.it_academy.jd2.golubev_107.mail_sender.storage.entity.Email;

import java.util.List;
import java.util.UUID;

public interface IMailStorage {

    void create(Email email);

    EmailStorageOutDto readById(UUID id);

    List<EmailStorageOutDto> readAll();
}
