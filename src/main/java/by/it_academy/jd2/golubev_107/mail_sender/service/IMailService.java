package by.it_academy.jd2.golubev_107.mail_sender.service;

import by.it_academy.jd2.golubev_107.mail_sender.service.dto.CreateEmailDto;
import by.it_academy.jd2.golubev_107.mail_sender.service.dto.EmailOutDto;

import java.util.List;

public interface IMailService {

    void create(CreateEmailDto dto);

    List<EmailOutDto> getAll();
}
