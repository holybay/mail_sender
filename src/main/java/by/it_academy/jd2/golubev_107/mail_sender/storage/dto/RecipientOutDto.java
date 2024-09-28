package by.it_academy.jd2.golubev_107.mail_sender.storage.dto;

import by.it_academy.jd2.golubev_107.mail_sender.storage.entity.Recipient;

import java.util.Objects;

public class RecipientOutDto {

    private Long id;
    private Long addressId;
    private Recipient.RecipientType type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Recipient.RecipientType getType() {
        return type;
    }

    public void setType(Recipient.RecipientType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipientOutDto that = (RecipientOutDto) o;
        return Objects.equals(id, that.id) && Objects.equals(addressId, that.addressId) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, addressId, type);
    }

    @Override
    public String toString() {
        return "RecipientOutDto{" +
                "id=" + id +
                ", address_id=" + addressId +
                ", type=" + type +
                '}';
    }
}
