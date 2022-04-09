package io.bookreader.UserBook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.ModelAndView;

import io.bookreader.BooksByUser;
import io.bookreader.BooksByUserRepository;
import io.bookreader.book.Book;
import io.bookreader.book.BookRepository;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserBookService {

	
	@Autowired
	BookRepository bookRepository;
	@Autowired
	UserBookRepository userBookRepository;
	@Autowired
	BooksByUserRepository booksByUserRepository;
	public String addUserStatus(MultiValueMap<String, String> formData,OAuth2User principal,Model model) {

		if(principal==null || principal.getAttribute("login")==null)
		{
			return null;
		}
		String bookid = formData.getFirst("bookid");
		Optional<Book> data = bookRepository.findById(bookid);
		if(!data.isPresent())
		{
			return "";
		}
		  Book book = data.get();

	        UserBookInformation userBooks  = new UserBookInformation();
	        UserBooksPrimaryKey key = new UserBooksPrimaryKey();
	        String userId = principal.getAttribute("login");
	        key.setUserId(userId);
	        key.setBookId(bookid);

	        userBooks.setKey(key);

	        int ratings = Integer.parseInt(formData.getFirst("rating"));

	        userBooks.setStartedDate(LocalDate.parse(formData.getFirst("startDate")));
	        userBooks.setCompletedDate(LocalDate.parse(formData.getFirst("completedDate")));
	        userBooks.setRating(ratings);
	        userBooks.setReadingStatus(formData.getFirst("readingStatus"));

	        userBookRepository.save(userBooks);

	        BooksByUser booksByUser = new BooksByUser();	
	        booksByUser.setId(userId);
	        booksByUser.setBookId(bookid);
	        booksByUser.setBookName(book.getName());
	        booksByUser.setCoverIds(book.getCoverIds());
	        booksByUser.setAuthorNames(book.getAuthorNames());
	        booksByUser.setReadingStatus(formData.getFirst("readingStatus"));
	        booksByUser.setRating(ratings);
	        booksByUserRepository.save(booksByUser);


	        return bookid;
		
		
		
	}
	
	
	

}
