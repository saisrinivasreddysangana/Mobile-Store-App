package org.mobilestoreapp.commons;

import org.mobilestoreapp.commons.email.OtpGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonBeansConfig {

    @Bean
    public OtpGenerator otpGenerator() {
        return new OtpGenerator();
    }
}
