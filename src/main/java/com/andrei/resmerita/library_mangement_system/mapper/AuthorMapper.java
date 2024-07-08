package com.andrei.resmerita.library_mangement_system.mapper;

import com.andrei.resmerita.library_mangement_system.dto.AuthorDTO;
import com.andrei.resmerita.library_mangement_system.model.Author;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuthorMapper {
    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    AuthorDTO toAuthorDTO(Author author);

    Author toAuthor(AuthorDTO authorDTO);
}