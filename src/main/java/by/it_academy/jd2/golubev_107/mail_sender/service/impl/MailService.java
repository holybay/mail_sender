package by.it_academy.jd2.golubev_107.mail_sender.service.impl;

import by.it_academy.jd2.golubev_107.mail_sender.service.IMailService;
import by.it_academy.jd2.golubev_107.mail_sender.service.IRecipientAddressService;
import by.it_academy.jd2.golubev_107.mail_sender.service.dto.CreateEmailDto;
import by.it_academy.jd2.golubev_107.mail_sender.service.dto.RecipientAddressDto;
import by.it_academy.jd2.golubev_107.mail_sender.storage.IMailStorage;
import by.it_academy.jd2.golubev_107.mail_sender.storage.entity.Email;
import by.it_academy.jd2.golubev_107.mail_sender.storage.entity.Recipient;
import by.it_academy.jd2.golubev_107.mail_sender.storage.entity.RecipientAddress;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MailService implements IMailService {

    private final IMailStorage mailStorage;
    private final IRecipientAddressService addressService;

    public MailService(IMailStorage mailStorage, IRecipientAddressService addressService) {
        this.mailStorage = mailStorage;
        this.addressService = addressService;
    }

    @Override
    public void create(CreateEmailDto dto) {
        validate(dto);
        Email emailEntity = toEmailEntity(dto);
        mailStorage.create(emailEntity);
    }

    private void validate(CreateEmailDto dto) {
        Map<String, String[]> recipientsByType = new HashMap<>();
        recipientsByType.put(Recipient.RecipientType.TO.name(), dto.getRecipientsTo());
        recipientsByType.put(Recipient.RecipientType.CC.name(), dto.getRecipientsCC());
        recipientsByType.put(Recipient.RecipientType.BCC.name(), dto.getRecipientsBCC());

        Map<String, List<String>> duplicatesByType = new HashMap<>();

        recipientsByType.forEach((k, v) -> {
            List<String> duplicates = checkForEmailDuplicatesInType(v);
            if (!duplicates.isEmpty()) {
                duplicatesByType.put(k, duplicates);
            }
        });

        if (!duplicatesByType.isEmpty()) {
            throw new IllegalArgumentException("Email contains the following address duplicates: " + duplicatesByType);
        }
    }

    private List<String> checkForEmailDuplicatesInType(String[] addressList) {
        Set<String> addresses = new HashSet<>();
        Set<String> duplicates = new HashSet<>();
        Arrays.stream(addressList)
              .forEach(e -> {
                  if (addresses.contains(e)) {
                      duplicates.add(e);
                  } else {
                      addresses.add(e);
                  }
              });
        if (!duplicates.isEmpty()) {
            return new ArrayList<>(duplicates);
        }
        return Collections.emptyList();
    }

    private List<Recipient> accumulateRecptsByType(List<RecipientAddressDto> validatedAddresses, Recipient.RecipientType type) {
        List<RecipientAddress> recipientAddresses = getRecptsAddresses(validatedAddresses);
        return recipientAddresses.stream()
                                 .map(e -> toRecipient(e, type))
                                 .toList();
    }

    private List<RecipientAddress> getRecptsAddresses(List<RecipientAddressDto> addressList) {
        List<RecipientAddress> exist = new ArrayList<>();
        List<RecipientAddressDto> toCreate = new ArrayList<>();
        addressList.forEach(e -> {
            String email = e.getEmailAddress();
            if (addressService.exists(email)) {
                exist.add(addressService.getByEmail(email));
            } else {
                toCreate.add(e);
            }
        });
        if (!toCreate.isEmpty()) {
            List<RecipientAddress> created = addressService.create(toCreate);
            exist.addAll(created);
        }
        return exist;
    }

    private Email toEmailEntity(CreateEmailDto dto) {
        List<RecipientAddressDto> addressTo = Arrays.stream(dto.getRecipientsTo())
                                                    .map(this::toRecptAddressDto)
                                                    .toList();
        List<RecipientAddressDto> addressCC = Arrays.stream(dto.getRecipientsCC())
                                                    .map(this::toRecptAddressDto)
                                                    .toList();
        List<RecipientAddressDto> addressBCC = Arrays.stream(dto.getRecipientsBCC())
                                                     .map(this::toRecptAddressDto)
                                                     .toList();

        List<Recipient> recipientsTo = accumulateRecptsByType(addressTo, Recipient.RecipientType.TO);
        List<Recipient> recipientsCC = accumulateRecptsByType(addressCC, Recipient.RecipientType.CC);
        List<Recipient> recipientsBCC = accumulateRecptsByType(addressBCC, Recipient.RecipientType.BCC);
        return Email.builder()
                    .setRecipientsTo(recipientsTo)
                    .setRecipientsCC(recipientsCC)
                    .setRecipientsBCC(recipientsBCC)
                    .setTitle(dto.getTitle())
                    .setText(dto.getText())
                    .build();
    }

    private RecipientAddressDto toRecptAddressDto(String email) {
        RecipientAddressDto addressDto = new RecipientAddressDto();
        addressDto.setEmailAddress(email);
        return addressDto;
    }

    private Recipient toRecipient(RecipientAddress address, Recipient.RecipientType type) {
        return Recipient.builder()
                        .setAddress(address)
                        .setType(type)
                        .build();
    }
}
