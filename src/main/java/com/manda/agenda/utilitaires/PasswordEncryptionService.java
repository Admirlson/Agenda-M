package com.manda.agenda.utilitaires;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordEncryptionService {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String encrypPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
