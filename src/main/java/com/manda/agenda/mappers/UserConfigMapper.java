package com.manda.agenda.mappers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfigMapper {

    @Bean
    public UserMapper userMapper() {
        return new UserImpl();
    }

}
