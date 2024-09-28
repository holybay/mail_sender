package by.it_academy.jd2.golubev_107.mail_sender.service.impl;

import by.it_academy.jd2.golubev_107.mail_sender.service.IRecipientAddressService;
import by.it_academy.jd2.golubev_107.mail_sender.service.dto.RecipientAddressDto;
import by.it_academy.jd2.golubev_107.mail_sender.storage.IRecipientAddressStorage;
import by.it_academy.jd2.golubev_107.mail_sender.storage.entity.RecipientAddress;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

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

    @Override
    public List<RecipientAddress> create(List<RecipientAddressDto> dtoList) {
        List<RecipientAddress> addresses = dtoList.stream()
                                                  .map(this::toEntity)
                                                  .toList();
        return storage.create(addresses);
    }

    @Override
    public RecipientAddress getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Recipient Address id can't be null!");
        }
        RecipientAddress address = storage.readById(id);
        if (address == null) {
            throw new NoSuchElementException("There is no such email address with this id: " + id);
        }
        return address;
    }

    @Override
    public RecipientAddress getByEmail(String email) {
        return storage.readByEmail(email);
    }

    @Override
    public List<RecipientAddress> getAllByIds(Collection<Long> idList) {
        return storage.readAllByIds(idList);
    }

    @Override
    public boolean exists(String email) {
        return storage.readByEmail(email) != null;
    }
}
