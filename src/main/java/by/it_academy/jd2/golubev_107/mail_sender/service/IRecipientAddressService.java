package by.it_academy.jd2.golubev_107.mail_sender.service;

import by.it_academy.jd2.golubev_107.mail_sender.service.dto.RecipientAddressDto;
import by.it_academy.jd2.golubev_107.mail_sender.storage.entity.RecipientAddress;

public interface IRecipientAddressService {

    RecipientAddress create(RecipientAddressDto dto);

    RecipientAddress getById(Long id);
}
