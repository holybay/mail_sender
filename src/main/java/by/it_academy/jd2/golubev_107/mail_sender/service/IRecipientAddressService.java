package by.it_academy.jd2.golubev_107.mail_sender.service;

import by.it_academy.jd2.golubev_107.mail_sender.service.dto.RecipientAddressDto;
import by.it_academy.jd2.golubev_107.mail_sender.storage.entity.RecipientAddress;

import java.util.List;

public interface IRecipientAddressService {

    RecipientAddress create(RecipientAddressDto dto);

    List<RecipientAddress> create(RecipientAddressDto[] dtos);

    RecipientAddress getById(Long id);
}
