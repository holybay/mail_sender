package by.it_academy.jd2.golubev_107.mail_sender.storage.dto;

import java.util.List;
import java.util.Objects;

public class EmailStorageOutDto {

    private Long id;
    private List<RecipientOutDto> recipientsTo;
    private List<RecipientOutDto> recipientsCC;
    private List<RecipientOutDto> recipientsBCC;
    private String title;
    private String text;

    private EmailStorageOutDto(Long id, List<RecipientOutDto> recipientsTo, List<RecipientOutDto> recipientsCC,
                               List<RecipientOutDto> recipientsBCC, String title, String text) {
        this.id = id;
        this.recipientsTo = recipientsTo;
        this.recipientsCC = recipientsCC;
        this.recipientsBCC = recipientsBCC;
        this.title = title;
        this.text = text;
    }

    public static EmailStorageOutDtoBuilder builder() {
        return new EmailStorageOutDtoBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<RecipientOutDto> getRecipientsTo() {
        return recipientsTo;
    }

    public void setRecipientsTo(List<RecipientOutDto> recipientsTo) {
        this.recipientsTo = recipientsTo;
    }

    public List<RecipientOutDto> getRecipientsCC() {
        return recipientsCC;
    }

    public void setRecipientsCC(List<RecipientOutDto> recipientsCC) {
        this.recipientsCC = recipientsCC;
    }

    public List<RecipientOutDto> getRecipientsBCC() {
        return recipientsBCC;
    }

    public void setRecipientsBCC(List<RecipientOutDto> recipientsBCC) {
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
        EmailStorageOutDto that = (EmailStorageOutDto) o;
        return Objects.equals(id, that.id) && Objects.equals(recipientsTo, that.recipientsTo)
                && Objects.equals(recipientsCC, that.recipientsCC)
                && Objects.equals(recipientsBCC, that.recipientsBCC)
                && Objects.equals(title, that.title) && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, recipientsTo, recipientsCC, recipientsBCC, title, text);
    }

    @Override
    public String toString() {
        return "EmailStorageOutDto{" +
                "id=" + id +
                ", recipientsTo=" + recipientsTo +
                ", recipientsCC=" + recipientsCC +
                ", recipientsBCC=" + recipientsBCC +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    public static class EmailStorageOutDtoBuilder {
        private Long id;
        private List<RecipientOutDto> recipientsTo;
        private List<RecipientOutDto> recipientsCC;
        private List<RecipientOutDto> recipientsBCC;
        private String title;
        private String text;

        private EmailStorageOutDtoBuilder() {
        }

        public EmailStorageOutDtoBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public EmailStorageOutDtoBuilder setRecipientsTo(List<RecipientOutDto> recipientsTo) {
            this.recipientsTo = recipientsTo;
            return this;
        }

        public EmailStorageOutDtoBuilder setRecipientsCC(List<RecipientOutDto> recipientsCC) {
            this.recipientsCC = recipientsCC;
            return this;
        }

        public EmailStorageOutDtoBuilder setRecipientsBCC(List<RecipientOutDto> recipientsBCC) {
            this.recipientsBCC = recipientsBCC;
            return this;
        }

        public EmailStorageOutDtoBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public EmailStorageOutDtoBuilder setText(String text) {
            this.text = text;
            return this;
        }

        public EmailStorageOutDto build() {
            return new EmailStorageOutDto(id, recipientsTo, recipientsCC, recipientsBCC, title, text);
        }
    }

}
