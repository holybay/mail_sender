package by.it_academy.jd2.golubev_107.mail_sender.service.dto;

import by.it_academy.jd2.golubev_107.mail_sender.storage.entity.EmailStatus;
import by.it_academy.jd2.golubev_107.mail_sender.storage.entity.Recipient;

import java.time.LocalDateTime;
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
    private EmailStatus emailStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public EmailOutDto(UUID id, List<Recipient> recipientsTo, List<Recipient> recipientsCC, List<Recipient> recipientsBCC,
                       String title, String text, EmailStatus emailStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.recipientsTo = recipientsTo;
        this.recipientsCC = recipientsCC;
        this.recipientsBCC = recipientsBCC;
        this.title = title;
        this.text = text;
        this.emailStatus = emailStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public EmailStatus getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(EmailStatus emailStatus) {
        this.emailStatus = emailStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailOutDto that = (EmailOutDto) o;
        return Objects.equals(id, that.id) && Objects.equals(recipientsTo, that.recipientsTo) && Objects.equals(recipientsCC, that.recipientsCC) && Objects.equals(recipientsBCC, that.recipientsBCC) && Objects.equals(title, that.title) && Objects.equals(text, that.text) && Objects.equals(emailStatus, that.emailStatus) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, recipientsTo, recipientsCC, recipientsBCC, title, text, emailStatus, createdAt, updatedAt);
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
                ", emailStatus=" + emailStatus +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public static class EmailOutDtoBuilder {
        private UUID id;
        private List<Recipient> recipientsTo;
        private List<Recipient> recipientsCC;
        private List<Recipient> recipientsBCC;
        private String title;
        private String text;
        private EmailStatus emailStatus;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

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

        public EmailOutDtoBuilder setEmailStatus(EmailStatus emailStatus) {
            this.emailStatus = emailStatus;
            return this;
        }

        public EmailOutDtoBuilder setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public EmailOutDtoBuilder setUpdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public EmailOutDto build() {
            return new EmailOutDto(id, recipientsTo, recipientsCC, recipientsBCC, title, text, emailStatus,
                    createdAt, updatedAt);
        }
    }

}
