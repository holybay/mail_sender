package by.it_academy.jd2.golubev_107.mail_sender.service.dto;

import java.util.Objects;

public class RecipientAddressDto {

    private String emailAddress;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipientAddressDto that = (RecipientAddressDto) o;
        return Objects.equals(emailAddress, that.emailAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(emailAddress);
    }

    @Override
    public String toString() {
        return "RecipientAddressDto{" +
                "emailAddress='" + emailAddress + '\'' +
                '}';
    }
}
