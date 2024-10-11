package by.it_academy.jd2.golubev_107.mail_sender.storage.entity;

import java.util.Objects;
import java.util.UUID;

public class Recipient {

    private UUID id;
    private RecipientAddress address;
    private RecipientType type;

    private Recipient(UUID id, RecipientAddress address, RecipientType type) {
        this.id = id;
        this.address = address;
        this.type = type;
    }

    public static RecipientBuilder builder() {
        return new RecipientBuilder();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public RecipientAddress getAddress() {
        return address;
    }

    public void setAddress(RecipientAddress address) {
        this.address = address;
    }

    public RecipientType getType() {
        return type;
    }

    public void setType(RecipientType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipient recipient = (Recipient) o;
        return Objects.equals(id, recipient.id) && Objects.equals(address, recipient.address) && type == recipient.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, address, type);
    }

    @Override
    public String toString() {
        return "Recipient{" +
                "id=" + id +
                ", address=" + address +
                ", type=" + type +
                '}';
    }

    public enum RecipientType {
        TO, CC, BCC;
    }

    public static class RecipientBuilder {
        private UUID id;
        private RecipientAddress address;
        private RecipientType type;

        private RecipientBuilder() {
        }

        public RecipientBuilder setId(UUID id) {
            this.id = id;
            return this;
        }

        public RecipientBuilder setAddress(RecipientAddress address) {
            this.address = address;
            return this;
        }

        public RecipientBuilder setType(RecipientType type) {
            this.type = type;
            return this;
        }

        public Recipient build() {
            return new Recipient(id, address, type);
        }
    }
}
