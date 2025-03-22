package co.com.consumer.api.security.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Component
public class PBKDF2Encoder implements PasswordEncoder {

    private final static String ALGORITHM = "PBKDF2WithHmacSHA512";

    private final String secret;

    private final int iterations;

    private final int keyLength;

    public PBKDF2Encoder(@Value("${jwt.password.encoder.secret}") String secret, @Value("${jwt.password.encoder.iteration}") int iterations, @Value("${jwt.password.encoder.keyLength}") int keyLength) {
        this.secret = secret;
        this.iterations = iterations;
        this.keyLength = keyLength;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        try {
            byte[] result = SecretKeyFactory.getInstance(ALGORITHM)
                    .generateSecret(new PBEKeySpec(rawPassword.toString().toCharArray(), secret.getBytes(StandardCharsets.UTF_8), iterations, keyLength))
                    .getEncoded();
            return Base64.getEncoder().encodeToString(result);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }

    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        return PasswordEncoder.super.upgradeEncoding(encodedPassword);
    }
}
