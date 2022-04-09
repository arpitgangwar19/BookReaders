package io.bookreader.searchapi;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class SearchService {

	private final WebClient webClient;
	private final String COVER_IMAGE_ROOT = "http://covers.openlibrary.org/b/id/";
	public SearchService(WebClient.Builder webClientBuilder) {
		
		
		// creating the webclient and exchange  strategies are for buffering the data on the server 
		this.webClient= webClientBuilder.exchangeStrategies(ExchangeStrategies.builder()
				.codecs(configurer ->configurer
									.defaultCodecs()
									.maxInMemorySize(16 *1024 *1024)).build()).baseUrl("http://openlibrary.org/search.json").build();
		
	}
	
	public String getSearchResult(String query,Model model)
	{
		
	   Mono<SearchResult> resultMono= 	  this.webClient.get().uri("?q={query}",query).retrieve().bodyToMono(SearchResult.class);
	   SearchResult result = resultMono.block();
	   
	   
	   List<SearchResultElement> filterSearch =  result.getDocs()
			   										  .stream()
			   										 .map(bookData -> {
			   											  		bookData.setKey(bookData.getKey().replace("/works/",""));
			   											  	 String coverId = bookData.getCover_i();
			   								                if (StringUtils.hasText(coverId)) {
			   								                    coverId = COVER_IMAGE_ROOT + coverId + "-M.jpg";
			   								                } else {
			   								                    coverId = "/images/no-image.jpeg";
			   								                }
			   								                bookData.setCover_i(coverId);
			   											  		return bookData;
			   											  		
			   										  					}).collect(Collectors.toList());
//	   List<SearchResultElement> filterSearch = result.getDocs()
//	            .stream()
//	            .limit(10)
//	            .map(bookResult -> {
//	                bookResult.setKey(bookResult.getKey().replace("/works/", ""));
//	                String coverId = bookResult.getCover_i();
//	                if (StringUtils.hasText(coverId)) {
//	                    coverId = COVER_IMAGE_ROOT + coverId + "-M.jpg";
//	                } else {
//	                    coverId = "/images/no-image.jpeg";
//	                }
//	                bookResult.setCover_i(coverId);
//	                return bookResult;
//	            })
//	            .collect(Collectors.toList());
	   
	   model.addAttribute("searchResults", filterSearch);
	   
		
		return 	"search";
	}
}
