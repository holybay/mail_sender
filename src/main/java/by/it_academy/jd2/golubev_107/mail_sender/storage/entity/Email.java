package by.it_academy.jd2.golubev_107.mail_sender.storage.entity;

import java.util.List;
import java.util.Objects;

public class Email {

    private Long id;
    private List<Recipient> recipientsTo;
    private List<Recipient> recipientsCC;
    private List<Recipient> recipientsBCC;
    private String title;
    private String text;

    private Email(Long id, List<Recipient> recipientsTo, List<Recipient> recipientsCC, List<Recipient> recipientsBCC, String title, String text) {
        this.id = id;
        this.recipientsTo = recipientsTo;
        this.recipientsCC = recipientsCC;
        this.recipientsBCC = recipientsBCC;
        this.title = title;
        this.text = text;
    }

    public static EmailBuilder builder() {
        return new EmailBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
        Email email = (Email) o;
        return Objects.equals(id, email.id) && Objects.equals(recipientsTo, email.recipientsTo) && Objects.equals(recipientsCC, email.recipientsCC) && Objects.equals(recipientsBCC, email.recipientsBCC) && Objects.equals(title, email.title) && Objects.equals(text, email.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, recipientsTo, recipientsCC, recipientsBCC, title, text);
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
                '}';
    }

    public static class EmailBuilder {
        private Long id;
        private List<Recipient> recipientsTo;
        private List<Recipient> recipientsCC;
        private List<Recipient> recipientsBCC;
        private String title;
        private String text;

        private EmailBuilder() {
        }

        public EmailBuilder setId(Long id) {
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

        public Email build() {
            return new Email(id, recipientsTo, recipientsCC, recipientsBCC, title, text);
        }
    }
}
