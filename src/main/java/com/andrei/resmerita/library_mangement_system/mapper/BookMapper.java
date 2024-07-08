package com.andrei.resmerita.library_mangement_system.mapper;

import com.andrei.resmerita.library_mangement_system.dto.BookDTO;
import com.andrei.resmerita.library_mangement_system.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    BookDTO toBookDTO(Book book);

    Book toBook(BookDTO bookDTO);
}