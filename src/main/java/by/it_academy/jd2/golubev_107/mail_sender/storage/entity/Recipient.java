package by.it_academy.jd2.golubev_107.mail_sender.storage.entity;

import java.util.Objects;

public class Recipient {

    private Long id;
    private String emailAddress;
    private RecipientType type;

    private Recipient(Long id, String emailAddress, RecipientType type) {
        this.id = id;
        this.emailAddress = emailAddress;
        this.type = type;
    }

    public static RecipientBuilder builder() {
        return new RecipientBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
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
        return Objects.equals(id, recipient.id) && Objects.equals(emailAddress, recipient.emailAddress) && type == recipient.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, emailAddress, type);
    }

    @Override
    public String toString() {
        return "Recipient{" +
                "id=" + id +
                ", emailAddress='" + emailAddress + '\'' +
                ", type=" + type +
                '}';
    }

    public enum RecipientType {
        TO, CC, BCC;
    }

    public static class RecipientBuilder {
        private Long id;
        private String emailAddress;
        private RecipientType type;

        public RecipientBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public RecipientBuilder setEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
            return this;
        }

        public RecipientBuilder setType(RecipientType type) {
            this.type = type;
            return this;
        }

        public Recipient build() {
            return new Recipient(id, emailAddress, type);
        }
    }
}
