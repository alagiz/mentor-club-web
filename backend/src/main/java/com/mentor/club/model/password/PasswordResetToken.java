package com.mentor.club.model.password;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mentor.club.model.authentication.ExpirableToken;
import com.mentor.club.model.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
public class PasswordResetToken extends ExpirableToken {
    public static final Long PASSWORD_RESET_TOKEN_LIFESPAN_IN_SECONDS = 24L * 3600L; // 24 hours

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonProperty("token")
    private String token;

    @JsonProperty("user")
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "userId", referencedColumnName = "id")
    private User user;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Token [String=").append(token).append("]").append("[Expires").append(super.getExpirationDate()).append("]");

        return builder.toString();
    }
}
