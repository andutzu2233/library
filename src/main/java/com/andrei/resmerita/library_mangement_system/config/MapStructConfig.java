package com.andrei.resmerita.library_mangement_system.config;

import com.andrei.resmerita.library_mangement_system.mapper.AuthorMapper;
import com.andrei.resmerita.library_mangement_system.mapper.BookMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapStructConfig {

    @Bean
    public AuthorMapper authorMapper() {
        return AuthorMapper.INSTANCE;
    }

    @Bean
    public BookMapper bookMapper() {
        return BookMapper.INSTANCE;
    }
}