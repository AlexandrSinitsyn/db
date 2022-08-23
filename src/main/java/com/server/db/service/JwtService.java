package com.server.db.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.server.db.Tools;
import com.server.db.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@SuppressWarnings("ClassCanBeRecord")
@Service
@RequiredArgsConstructor
public class JwtService {
    private static final String SECRET = Tools.PROPERTIES.getProperty(Tools.JWT_KEY);
    private final UserService userService;

    public String create(final User user) {
        try {
            return JWT.create()
                    .withClaim("userId", user.getId())
                    .sign(Algorithm.HMAC256(SECRET));
        } catch (final Exception e) {
            throw new RuntimeException("Cannot create JWT", e);
        }
    }

    public User findUser(final String jwt) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            return userService.findById(verifier.verify(jwt).getClaim("userId").asLong());
        } catch (final Exception ignored) {
            return null;
        }
    }
}
