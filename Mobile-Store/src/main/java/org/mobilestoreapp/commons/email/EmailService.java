package org.mobilestoreapp.commons.email;

public interface EmailService {
    void send(String to, String subject, String body);
}
