package com.manda.agenda.mappers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EvenementConfigMapper {
    @Bean
    EvenementMapper evenementMapper() {
        return new EvenementMapperImpl();
    }

}
