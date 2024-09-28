package by.it_academy.jd2.golubev_107.mail_sender.service.dto;

import java.util.Arrays;
import java.util.Objects;

public class CreateEmailDto {

    private String[] recipientsTo;
    private String[] recipientsCC;
    private String[] recipientsBCC;
    private String title;
    private String text;

    private CreateEmailDto(String[] recipientsTo, String[] recipientsCC, String[] recipientsBCC,
                           String title, String text) {
        this.recipientsTo = recipientsTo;
        this.recipientsCC = recipientsCC;
        this.recipientsBCC = recipientsBCC;
        this.title = title;
        this.text = text;
    }

    public static CreateEmailDtoBuilder builder() {
        return new CreateEmailDtoBuilder();
    }

    public String[] getRecipientsTo() {
        return recipientsTo;
    }

    public void setRecipientsTo(String[] recipientsTo) {
        this.recipientsTo = recipientsTo;
    }

    public String[] getRecipientsCC() {
        return recipientsCC;
    }

    public void setRecipientsCC(String[] recipientsCC) {
        this.recipientsCC = recipientsCC;
    }

    public String[] getRecipientsBCC() {
        return recipientsBCC;
    }

    public void setRecipientsBCC(String[] recipientsBCC) {
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
        CreateEmailDto that = (CreateEmailDto) o;
        return Objects.deepEquals(recipientsTo, that.recipientsTo) && Objects.deepEquals(recipientsCC, that.recipientsCC) && Objects.deepEquals(recipientsBCC, that.recipientsBCC) && Objects.equals(title, that.title) && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(recipientsTo), Arrays.hashCode(recipientsCC), Arrays.hashCode(recipientsBCC), title, text);
    }

    @Override
    public String toString() {
        return "CreateEmailDto{" +
                "recipientsTo=" + Arrays.toString(recipientsTo) +
                ", recipientsCC=" + Arrays.toString(recipientsCC) +
                ", recipientsBCC=" + Arrays.toString(recipientsBCC) +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    public static class CreateEmailDtoBuilder {
        private String[] recipientsTo;
        private String[] recipientsCC;
        private String[] recipientsBCC;
        private String title;
        private String text;

        private CreateEmailDtoBuilder() {
        }

        public CreateEmailDtoBuilder setRecipientsTo(String[] recipientsTo) {
            this.recipientsTo = recipientsTo;
            return this;
        }

        public CreateEmailDtoBuilder setRecipientsCC(String[] recipientsCC) {
            this.recipientsCC = recipientsCC;
            return this;
        }

        public CreateEmailDtoBuilder setRecipientsBCC(String[] recipientsBCC) {
            this.recipientsBCC = recipientsBCC;
            return this;
        }

        public CreateEmailDtoBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public CreateEmailDtoBuilder setText(String text) {
            this.text = text;
            return this;
        }

        public CreateEmailDto build() {
            return new CreateEmailDto(recipientsTo, recipientsCC, recipientsBCC, title, text);
        }
    }
}
