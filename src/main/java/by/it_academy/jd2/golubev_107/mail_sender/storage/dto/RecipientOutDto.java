package by.it_academy.jd2.golubev_107.mail_sender.storage.dto;

import by.it_academy.jd2.golubev_107.mail_sender.storage.entity.Recipient;

import java.util.Objects;

public class RecipientOutDto {

    private Long id;
    private Long address_id;
    private Recipient.RecipientType type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAddress_id() {
        return address_id;
    }

    public void setAddress_id(Long address_id) {
        this.address_id = address_id;
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
        return Objects.equals(id, that.id) && Objects.equals(address_id, that.address_id) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, address_id, type);
    }

    @Override
    public String toString() {
        return "RecipientOutDto{" +
                "id=" + id +
                ", address_id=" + address_id +
                ", type=" + type +
                '}';
    }
}
