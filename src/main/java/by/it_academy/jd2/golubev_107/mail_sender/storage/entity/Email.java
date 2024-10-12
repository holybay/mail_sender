package by.it_academy.jd2.golubev_107.mail_sender.storage.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Email {

    private UUID id;
    private List<Recipient> recipientsTo;
    private List<Recipient> recipientsCC;
    private List<Recipient> recipientsBCC;
    private String title;
    private String text;
    private EmailStatus emailStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Email(UUID id, List<Recipient> recipientsTo, List<Recipient> recipientsCC, List<Recipient> recipientsBCC,
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

    public static EmailBuilder builder() {
        return new EmailBuilder();
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
        Email email = (Email) o;
        return Objects.equals(id, email.id) && Objects.equals(recipientsTo, email.recipientsTo) && Objects.equals(recipientsCC, email.recipientsCC) && Objects.equals(recipientsBCC, email.recipientsBCC) && Objects.equals(title, email.title) && Objects.equals(text, email.text) && Objects.equals(emailStatus, email.emailStatus) && Objects.equals(createdAt, email.createdAt) && Objects.equals(updatedAt, email.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, recipientsTo, recipientsCC, recipientsBCC, title, text, emailStatus, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "Email{" +
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

    public static class EmailBuilder {
        private UUID id;
        private List<Recipient> recipientsTo;
        private List<Recipient> recipientsCC;
        private List<Recipient> recipientsBCC;
        private String title;
        private String text;
        private EmailStatus emailStatus;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        private EmailBuilder() {
        }

        public EmailBuilder setId(UUID id) {
            this.id = id;
            return this;
        }

        public EmailBuilder setRecipientsTo(List<Recipient> recipientsTo) {
            this.recipientsTo = recipientsTo;
            return this;
        }

        public EmailBuilder setRecipientsCC(List<Recipient> recipientsCC) {
            this.recipientsCC = recipientsCC;
            return this;
        }

        public EmailBuilder setRecipientsBCC(List<Recipient> recipientsBCC) {
            this.recipientsBCC = recipientsBCC;
            return this;
        }

        public EmailBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public EmailBuilder setText(String text) {
            this.text = text;
            return this;
        }

        public EmailBuilder setEmailStatus(EmailStatus emailStatus) {
            this.emailStatus = emailStatus;
            return this;
        }

        public EmailBuilder setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public EmailBuilder setUpdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Email build() {
            return new Email(id, recipientsTo, recipientsCC, recipientsBCC,
                    title, text, emailStatus, createdAt, updatedAt);
        }
    }
}
