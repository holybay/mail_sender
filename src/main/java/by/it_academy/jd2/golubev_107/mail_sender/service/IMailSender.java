package by.it_academy.jd2.golubev_107.mail_sender.service;

import by.it_academy.jd2.golubev_107.mail_sender.service.dto.EmailOutDto;

public interface IMailSender {

    void send(EmailOutDto dto);

}
