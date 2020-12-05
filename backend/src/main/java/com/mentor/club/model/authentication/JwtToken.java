package com.mentor.club.model.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mentor.club.model.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Getter
@Setter
public class JwtToken {
    @Id
    @Column(name = "id")
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "token")
    @JsonProperty("token")
    private String token;

    @JsonProperty("user")
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "userId", referencedColumnName = "id")
    private User user;

    @Column(name = "expirationDate")
    @JsonProperty("expirationDate")
    private Date expirationDate;

    public Boolean isExpired() {
        Calendar cal = Calendar.getInstance();

        return getExpirationDate().before(cal.getTime());
    }
}
