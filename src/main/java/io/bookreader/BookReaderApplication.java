package io.bookreader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.bookreader.author.Author;
import io.bookreader.author.AuthorRepository;
import io.bookreader.book.Book;
import io.bookreader.book.BookRepository;
import io.bookreader.connection.DataAstaxConfiguration;

import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.boot.autoconfigure.web.format.DateTimeFormatters;

@SpringBootApplication
@EnableConfigurationProperties(DataAstaxConfiguration.class)
@RestController
public class BookReaderApplication {

	@Autowired
	AuthorRepository authorRepository;
	@Autowired
	BookRepository bookRepository;

	@Value(value = "${datadumb.location.authors}")
	private String authorDumbLocation;
	@Value(value = "${datadumb.location.works}")
	private String worksDumbLocation;

	public static void main(String[] args) {
		SpringApplication.run(BookReaderApplication.class, args);
	}
	
	
//	@RequestMapping("/user")
//	public String user(@AuthenticationPrincipal OAuth2User principal) {
//		System.out.println(principal);
//		return principal.getAttribute("name");
//	}

	private void initauthors() {
		Path path = Paths.get(authorDumbLocation);
		try (Stream<String> lines = Files.lines(path)) {
			lines.forEach(line -> {
				// Read the parse line

				String jsonString = line.substring(line.indexOf("{"));
				try {
					JSONObject json = new JSONObject(jsonString);
					Author author = new Author();
					author.setId(json.optString("key").replace("/authors/", ""));
					author.setName(json.optString("name"));
					author.setPersonalName(json.optString("personal_name"));
					authorRepository.save(author);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Construct the author object

				// Persist using repository

			});

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	private void initworks() {
		Path path = Paths.get(worksDumbLocation);
		DateTimeFormatter df = DateTimeFormatter.ofPattern("YYYY-MM-DD'T'HH:mm:ss.SSSSSS");
		try (Stream<String> lines = Files.lines(path)) {
			lines.forEach(line -> {
				// Read the parse line

				String jsonString = line.substring(line.indexOf("{"));
				try {
					JSONObject json = new JSONObject(jsonString);
					Book book = new Book();
					book.setId(json.optString("key").replace("/works/", ""));
					book.setName(json.optString("title"));
					JSONObject descriptionObj = json.optJSONObject("description");

					if (descriptionObj != null) {
						book.setDescription(descriptionObj.optString("value"));

					}

					// parsing the json object into locat date and setting the object in book format
//					book.setPublishedDate(LocalDate.
//													parse(json.optJSONObject("created")
//																.optString("value")
//																.toString()));

					// or
//					JSONObject publishedDate = json.optJSONObject("created");
//					if (publishedDate != null) {
//						String pubdate = json.optString("value");
//						book.setPublishedDate(LocalDate.parse(pubdate,df));
//					}
					JSONArray coverIds = json.optJSONArray("covers");
					if (coverIds != null) {
						List<String> coverid = new ArrayList();
						for (int i = 0; i < coverIds.length(); i++) {
							coverid.add(coverIds.getString(i));
						}
						book.setCoverIds(coverid);

					}

					JSONArray authorIds = json.optJSONArray("authors");
					if (authorIds != null) {
						List<String> authorId = new ArrayList();
						for (int i = 0; i < authorIds.length(); i++) {
							authorId.add(authorIds.getJSONObject(i).getJSONObject("author").optString("key")
									.replace("/authors/", ""));
						}
						book.setAuthorIds(authorId);

						List<String> authorNames = authorId.stream()
															.map(authorid -> authorRepository.findById(authorid))
															.map(optionalAuthor -> {
																	return !optionalAuthor.isPresent() ? "Unknown Author" : optionalAuthor.get().getName();
																					}).collect(Collectors.toList());
						
						book.setAuthorNames(authorNames);
					}


					bookRepository.save(book);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Construct the author object
				// Persist using repository

			});

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	@PostConstruct
	public void start() {
//		initauthors();
//		initworks();
		System.out.println(authorDumbLocation);
//		Author author = new Author();
//		author.setId("one");
//		author.setName("Arpit Gangwar");
//		author.setPersonalName("Shiv");
//		authorRepository.save(author);

	}

	@Bean
	public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataAstaxConfiguration astraProperties) {
		Path bundle = astraProperties.getSecureConnectBundle().toPath();
		return builder -> builder.withCloudSecureConnectBundle(bundle);
	}
}
