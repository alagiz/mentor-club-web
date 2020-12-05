package com.mentor.club.model.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
public abstract class ExpirableToken {
    @Column(name = "expirationDate")
    @JsonProperty("expirationDate")
    private Date expirationDate;

    public Boolean isExpired(Date expirationDate) {
        Calendar cal = Calendar.getInstance();

        return expirationDate.before(cal.getTime());
    }
}
