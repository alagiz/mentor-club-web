package com.mentor.club.model.authentication.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mentor.club.model.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
public abstract class JwtToken {
    @Id
    @Column(name = "id", columnDefinition = "uuid")
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "token", length = 1024)
    @JsonProperty("token")
    private String token;

    @JsonProperty("user")
    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "userId", referencedColumnName = "id")
    private User user;

    @Column(name = "expirationDate")
    @JsonProperty("expirationDate")
    private Date expirationDate;

    @Column(name = "tokenType")
    @JsonProperty("tokenType")
    private JwtTokenType jwtTokenType;

    public Boolean isExpired() {
        Calendar cal = Calendar.getInstance();

        return getExpirationDate().before(cal.getTime());
    }

    public JwtToken(JwtTokenType jwtTokenType) {
        this.jwtTokenType = jwtTokenType;
    }
}
