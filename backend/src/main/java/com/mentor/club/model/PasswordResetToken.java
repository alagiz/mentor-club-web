package com.mentor.club.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
public class PasswordResetToken {
    public static final Long PASSWORD_RESET_TOKEN_LIFESPAN_IN_SECONDS = 24L * 3600L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Setter
    @Getter
    @JsonProperty("token")
    private String token;

    @Setter
    @Getter
    @JsonProperty("user")
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "userId", referencedColumnName = "id")
    private User user;

    @Setter
    @Getter
    @JsonProperty("expirationDate")
    private Date expirationDate;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Token [String=").append(token).append("]").append("[Expires").append(expirationDate).append("]");

        return builder.toString();
    }
}
