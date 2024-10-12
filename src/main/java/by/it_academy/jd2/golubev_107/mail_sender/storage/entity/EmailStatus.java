package by.it_academy.jd2.golubev_107.mail_sender.storage.entity;

import java.util.Objects;

public class EmailStatus {

    private Long id;

    private EStatus status;

    public EmailStatus() {
    }

    public EmailStatus(EStatus status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EStatus getStatus() {
        return status;
    }

    public void setStatus(EStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailStatus that = (EmailStatus) o;
        return Objects.equals(id, that.id) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status);
    }

    @Override
    public String toString() {
        return "EmailStatus{" +
                "id=" + id +
                ", status=" + status +
                '}';
    }

    public enum EStatus {
        LOADED,
        SENDING,
        FINISH,
        ERROR;
    }
}
