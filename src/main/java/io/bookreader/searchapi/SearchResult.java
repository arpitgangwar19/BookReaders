package io.bookreader.searchapi;

import java.util.List;

public class SearchResult {

	private int numFound;
	private List<SearchResultElement> docs;
	public int getNumFound() {
		return numFound;
	}
	public void setNumFound(int numFound) {
		this.numFound = numFound;
	}
	public List<SearchResultElement> getDocs() {
		return docs;
	}
	public void setDocs(List<SearchResultElement> docs) {
		this.docs = docs;
	}
	
	
}
