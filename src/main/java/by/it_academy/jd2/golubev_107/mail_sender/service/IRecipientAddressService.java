package by.it_academy.jd2.golubev_107.mail_sender.service;

import by.it_academy.jd2.golubev_107.mail_sender.service.dto.RecipientAddressDto;
import by.it_academy.jd2.golubev_107.mail_sender.storage.entity.RecipientAddress;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface IRecipientAddressService {

    RecipientAddress create(RecipientAddressDto dto);

    List<RecipientAddress> create(List<RecipientAddressDto> dtoList);

    RecipientAddress getById(UUID id);

    RecipientAddress getByEmail(String email);

    List<RecipientAddress> getAllByIds(Collection<UUID> idList);

    boolean exists(String email);
}
