package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.*;

/**
 * A TimeSheet.
 */
@Entity
@Table(name = "time_sheet")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TimeSheet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "check_in")
    private Instant checkIn;

    @Column(name = "check_out")
    private Instant checkOut;

    @Column(name = "over_time")
    private Instant overTime;

    @Column(name = "user")
    private String user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TimeSheet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public TimeSheet date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Instant getCheckIn() {
        return this.checkIn;
    }

    public TimeSheet checkIn(Instant checkIn) {
        this.setCheckIn(checkIn);
        return this;
    }

    public void setCheckIn(Instant checkIn) {
        this.checkIn = checkIn;
    }

    public Instant getCheckOut() {
        return this.checkOut;
    }

    public TimeSheet checkOut(Instant checkOut) {
        this.setCheckOut(checkOut);
        return this;
    }

    public void setCheckOut(Instant checkOut) {
        this.checkOut = checkOut;
    }

    public Instant getOverTime() {
        return this.overTime;
    }

    public TimeSheet overTime(Instant overTime) {
        this.setOverTime(overTime);
        return this;
    }

    public void setOverTime(Instant overTime) {
        this.overTime = overTime;
    }

    public String getUser() {
        return this.user;
    }

    public TimeSheet user(String user) {
        this.setUser(user);
        return this;
    }

    public void setUser(String user) {
        this.user = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TimeSheet)) {
            return false;
        }
        return id != null && id.equals(((TimeSheet) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TimeSheet{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", checkIn='" + getCheckIn() + "'" +
            ", checkOut='" + getCheckOut() + "'" +
            ", overTime='" + getOverTime() + "'" +
            ", user='" + getUser() + "'" +
            "}";
    }
}
