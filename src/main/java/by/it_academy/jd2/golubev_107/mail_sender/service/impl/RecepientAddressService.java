package by.it_academy.jd2.golubev_107.mail_sender.service.impl;

import by.it_academy.jd2.golubev_107.mail_sender.service.IRecipientAddressService;
import by.it_academy.jd2.golubev_107.mail_sender.service.dto.RecipientAddressDto;
import by.it_academy.jd2.golubev_107.mail_sender.storage.IRecipientAddressStorage;
import by.it_academy.jd2.golubev_107.mail_sender.storage.entity.RecipientAddress;

public class RecepientAddressService implements IRecipientAddressService {

    private final IRecipientAddressStorage storage;

    public RecepientAddressService(IRecipientAddressStorage storage) {
        this.storage = storage;
    }

    @Override
    public RecipientAddress create(RecipientAddressDto dto) {
        return storage.create(toEntity(dto));
    }

    private RecipientAddress toEntity(RecipientAddressDto dto) {
        RecipientAddress entity = new RecipientAddress();
        entity.setEmailAddress(dto.getEmailAddress());
        return entity;
    }
}
