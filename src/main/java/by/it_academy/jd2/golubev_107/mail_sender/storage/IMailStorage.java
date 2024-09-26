package by.it_academy.jd2.golubev_107.mail_sender.storage;

import by.it_academy.jd2.golubev_107.mail_sender.storage.dto.EmailStorageOutDto;

public interface IMailStorage {

    EmailStorageOutDto readById(Long id);
}
