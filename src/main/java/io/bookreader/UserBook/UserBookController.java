package io.bookreader.UserBook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserBookController {
	
	
	
	@Autowired
	UserBookService userBookService;
	
	@PostMapping(value = "/addUserBook")
	public ModelAndView addUserStatus(@RequestBody MultiValueMap<String, String> formData, @AuthenticationPrincipal OAuth2User principal,Model model) {
		
		
		System.out.println("aa");
	String url= userBookService.addUserStatus(formData,principal,model);	
		return url!=null?new ModelAndView("redirect:/book/" + url):new ModelAndView("redirect:/");
 	}

}
