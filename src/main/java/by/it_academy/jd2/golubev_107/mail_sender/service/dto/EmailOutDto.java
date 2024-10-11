package by.it_academy.jd2.golubev_107.mail_sender.service.dto;

import by.it_academy.jd2.golubev_107.mail_sender.storage.entity.Recipient;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class EmailOutDto {

    private UUID id;
    private List<Recipient> recipientsTo;
    private List<Recipient> recipientsCC;
    private List<Recipient> recipientsBCC;
    private String title;
    private String text;

    private EmailOutDto(UUID id, List<Recipient> recipientsTo, List<Recipient> recipientsCC, List<Recipient> recipientsBCC, String title, String text) {
        this.id = id;
        this.recipientsTo = recipientsTo;
        this.recipientsCC = recipientsCC;
        this.recipientsBCC = recipientsBCC;
        this.title = title;
        this.text = text;
    }

    public static EmailOutDtoBuilder builder() {
        return new EmailOutDtoBuilder();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<Recipient> getRecipientsTo() {
        return recipientsTo;
    }

    public void setRecipientsTo(List<Recipient> recipientsTo) {
        this.recipientsTo = recipientsTo;
    }

    public List<Recipient> getRecipientsCC() {
        return recipientsCC;
    }

    public void setRecipientsCC(List<Recipient> recipientsCC) {
        this.recipientsCC = recipientsCC;
    }

    public List<Recipient> getRecipientsBCC() {
        return recipientsBCC;
    }

    public void setRecipientsBCC(List<Recipient> recipientsBCC) {
        this.recipientsBCC = recipientsBCC;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailOutDto that = (EmailOutDto) o;
        return Objects.equals(id, that.id) && Objects.equals(recipientsTo, that.recipientsTo) && Objects.equals(recipientsCC, that.recipientsCC) && Objects.equals(recipientsBCC, that.recipientsBCC) && Objects.equals(title, that.title) && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, recipientsTo, recipientsCC, recipientsBCC, title, text);
    }

    @Override
    public String toString() {
        return "EmailOutDto{" +
                "id=" + id +
                ", recipientsTo=" + recipientsTo +
                ", recipientsCC=" + recipientsCC +
                ", recipientsBCC=" + recipientsBCC +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    public static class EmailOutDtoBuilder {
        private UUID id;
        private List<Recipient> recipientsTo;
        private List<Recipient> recipientsCC;
        private List<Recipient> recipientsBCC;
        private String title;
        private String text;

        private EmailOutDtoBuilder() {
        }

        public EmailOutDtoBuilder setId(UUID id) {
            this.id = id;
            return this;
        }

        public EmailOutDtoBuilder setRecipientsTo(List<Recipient> recipientsTo) {
            this.recipientsTo = recipientsTo;
            return this;
        }

        public EmailOutDtoBuilder setRecipientsCC(List<Recipient> recipientsCC) {
            this.recipientsCC = recipientsCC;
            return this;
        }

        public EmailOutDtoBuilder setRecipientsBCC(List<Recipient> recipientsBCC) {
            this.recipientsBCC = recipientsBCC;
            return this;
        }

        public EmailOutDtoBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public EmailOutDtoBuilder setText(String text) {
            this.text = text;
            return this;
        }

        public EmailOutDto build() {
            return new EmailOutDto(id, recipientsTo, recipientsCC, recipientsBCC, title, text);
        }
    }

}
