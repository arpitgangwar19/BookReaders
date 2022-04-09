package io.bookreader.searchapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {
	
	
	
	@Autowired
	SearchService searchService;
	@GetMapping(value = "/search")
	public void search(@RequestParam String query,Model model) {
		
	searchService.getSearchResult(query, model);	
		
		
	}

}
