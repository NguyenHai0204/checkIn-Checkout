package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.TimeSheet} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TimeSheetDTO implements Serializable {

    private Long id;

    private LocalDate date;

    private Instant checkIn;

    private Instant checkOut;

    private Instant overTime;

    private String user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Instant getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Instant checkIn) {
        this.checkIn = checkIn;
    }

    public Instant getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(Instant checkOut) {
        this.checkOut = checkOut;
    }

    public Instant getOverTime() {
        return overTime;
    }

    public void setOverTime(Instant overTime) {
        this.overTime = overTime;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TimeSheetDTO)) {
            return false;
        }

        TimeSheetDTO timeSheetDTO = (TimeSheetDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, timeSheetDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TimeSheetDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", checkIn='" + getCheckIn() + "'" +
            ", checkOut='" + getCheckOut() + "'" +
            ", overTime='" + getOverTime() + "'" +
            ", user='" + getUser() + "'" +
            "}";
    }
}
