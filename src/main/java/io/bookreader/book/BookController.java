package io.bookreader.book;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.bookreader.UserBook.UserBookInformation;
import io.bookreader.UserBook.UserBookRepository;
import io.bookreader.UserBook.UserBooksPrimaryKey;



@Controller
public class BookController {
	
	
	@Autowired
	BookService bookService;
	@Autowired
	UserBookRepository userBookRepository;
	
	 private final String COVER_IMAGE_ROOT = "http://covers.openlibrary.org/b/id/";

	@RequestMapping(value="/book/{id}")
	public String findById(@PathVariable String id, Model model, @AuthenticationPrincipal OAuth2User principal)
	{
		Optional<Book> data = bookService.findById(id);
		if(data.isPresent()) {
			Book book = data.get();
			String coverid= book.getCoverIds().get(0);
			 if (StringUtils.hasText(coverid)) {
                    coverid = COVER_IMAGE_ROOT + coverid + "-L.jpg";
                } else {
                    coverid = "/images/no-image.jpeg";
                }
			 model.addAttribute("coverImage", coverid);
			System.out.println("test"+data.get());
			model.addAttribute("book",book);
			
			if (principal != null && principal.getAttribute("login") != null) {
                String userId = principal.getAttribute("login");
                model.addAttribute("loginId", userId);
                UserBooksPrimaryKey key = new UserBooksPrimaryKey();
                key.setBookId(id);
                key.setUserId(userId);
                Optional<UserBookInformation> userBooks = userBookRepository.findById(key);
                if (userBooks.isPresent()) {
                    model.addAttribute("userBooks", userBooks.get());
                } else {
                    model.addAttribute("userBooks", new UserBookInformation());
                }
            }
			return "book";
			
		}
		return "booknotfound";
	}

}
