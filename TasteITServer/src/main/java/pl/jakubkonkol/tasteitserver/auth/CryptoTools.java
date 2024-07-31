package pl.jakubkonkol.tasteitserver.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CryptoTools {

    private static final String SECRET = System.getenv("TASTEIT_SECRET_PHRASE");

    public String generateSalt(){
        return BCrypt.gensalt();
    }
    public String authentication(String password, String salt){
        return BCrypt.hashpw(password + SECRET, salt);
    }
}
