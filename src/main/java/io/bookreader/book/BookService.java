package io.bookreader.book;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {
	
	@Autowired
	BookRepository bookRepo;

	public Optional<Book> findById(String id) {
		// TODO Auto-generated method stub
		
	return	bookRepo.findById(id);
		
	}

}
