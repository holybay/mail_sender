package by.it_academy.jd2.golubev_107.mail_sender.storage;

import by.it_academy.jd2.golubev_107.mail_sender.storage.dto.EmailStorageOutDto;
import by.it_academy.jd2.golubev_107.mail_sender.storage.entity.Email;

import java.util.List;

public interface IMailStorage {

    Long create(Email email);

    EmailStorageOutDto readById(Long id);

    List<EmailStorageOutDto> readAll();
}
