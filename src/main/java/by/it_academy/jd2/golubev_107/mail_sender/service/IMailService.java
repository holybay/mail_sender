package by.it_academy.jd2.golubev_107.mail_sender.service;

import by.it_academy.jd2.golubev_107.mail_sender.service.dto.CreateEmailDto;
import by.it_academy.jd2.golubev_107.mail_sender.service.dto.EmailOutDto;
import by.it_academy.jd2.golubev_107.mail_sender.storage.entity.EmailStatus;

import java.util.List;
import java.util.UUID;

public interface IMailService {

    void create(CreateEmailDto dto);

    List<EmailOutDto> getAll();

    void updateStatus(UUID id, EmailStatus.EStatus newStatus);

    List<EmailOutDto> getAllByStatus(EmailStatus.EStatus newStatus);
}
