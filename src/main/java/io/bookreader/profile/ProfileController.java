package io.bookreader.profile;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import io.bookreader.BooksByUser;
import io.bookreader.BooksByUserRepository;

@Controller
public class ProfileController {
	
	@Autowired
	BooksByUserRepository booksByUserRepository;
	 private final String COVER_IMAGE_ROOT = "http://covers.openlibrary.org/b/id/";

	@GetMapping(value = "/")
	public String login( @AuthenticationPrincipal OAuth2User principal,Model model) {
		
		
		if(principal==null || principal.getAttribute("login")==null) {
		return "index";
		}
		
		String userId = principal.getAttribute("login");
		Slice<BooksByUser> filterUserBook= booksByUserRepository.findAllById(userId, CassandraPageRequest.of(0,50));
		List<BooksByUser> filterUserContent = filterUserBook.getContent();
		filterUserContent = filterUserContent.stream().distinct().map(book -> {
			String coverid= book.getCoverIds().get(0);
			 if (StringUtils.hasText(coverid)) {
                   coverid = COVER_IMAGE_ROOT + coverid + "-M.jpg";
               } else {
                   coverid = "/images/no-image.jpeg";
               }
			 book.setCoverUrl(coverid);
			 return book;
		}).collect(Collectors.toList());
		
		model.addAttribute("books", filterUserContent);
		
		return "profile";
	}

}
