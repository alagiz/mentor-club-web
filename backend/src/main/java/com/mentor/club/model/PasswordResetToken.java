package com.mentor.club.model;

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
    private String token;

    @Setter
    @Getter
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "id")
    private User user;

    @Setter
    @Getter
    private Date expirationDate;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Token [String=").append(token).append("]").append("[Expires").append(expirationDate).append("]");

        return builder.toString();
    }
}
