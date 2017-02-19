package edu.kit.informatik.bibliography;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

/**
 * An article in a bibliography.
 * 
 * @author Frithjof Marquardt
 * @version 1.00, 10.02.2017
 */
public class Article implements Comparable<Article>, MyEntity {
    private final String id;
    private final String title;
    private final int year;
    private final Venue venue;
    private TreeSet<Article> citatedBy;
    private ArrayList<Author> authors;
    private TreeSet<String> keywords;
    
    /**
     * Creates e new article with the provided arguments.
     * 
     * @param id the id
     * @param title the title
     * @param year the publishing year
     * @param venue the venue (journal or conference series)
     */
    public Article(String id, String title, int year, Venue venue) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.venue = venue;
        citatedBy = new TreeSet<Article>();
        authors = new ArrayList<Author>();
        keywords = new TreeSet<String>();
    }
    /**
     * Adds provided authors to this article's list of authors.
     * 
     * @param newAuthors the authors to add
     */
    public void addAuthors(ArrayList<Author> newAuthors) {
        authors.addAll(newAuthors);
    }
    
    /**
     * Adds provided publication that cites this article to its list of citations.
     * 
     * @param article the publication that cites this article
     */
    public void addCitation(Article article) {
        citatedBy.add(article); 
    }
    
    @Override
    public void addKeywords(Collection<String> words) {
        keywords.addAll(words);
    }
    
    /**
     * Checks whether this article has all the provided keywords.
     * 
     * @param words the keywords
     * 
     * @return true if list of keywords includes all words 
     */
    public boolean hasKeywords(Collection<String> words) {
        return keywords.containsAll(words);
    }
    
    /**
     * Returns the article's title
     * 
     * @return the title
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Returns the article's publishing year.
     * 
     * @return the year
     */
    public int getYear() {
        return year;
    }
    
    /**
     * Returns the ArrayList of authors of this article.
     * 
     * @return the list
     */
    public ArrayList<Author> getAuthors() {
        return authors;
    }
    
    /**
     * Returns the article's set of keywords.
     * 
     * @return the keywords
     */
    public TreeSet<String> getKeywords() {
        return keywords;
    }
    
    /**
     * Returns this article's the list of citations.
     * 
     * @return the citations
     */
    public TreeSet<Article> getCitations() {
        return citatedBy;
    }
    
    /**
     * Returns the article type.
     * 
     * @return the type
     */
    public Venue getVenue() {
        return venue;
    }
    
    @Override
    public String toString() {
        return id;
    }
    
    @Override
    public int compareTo(Article other) {
        int result;
        
        //compare the authors
        int counter = 0;
        int size = authors.size();
        while (counter < size) {
            if (!other.getAuthors().isEmpty()) {
                result = authors.get(counter).compareTo(other.getAuthors().get(counter));
                if (result != 0) return result;
                counter++;
            } else return 1;
        }
        if (other.getAuthors().size() > counter) return -1;
        
        //compare the title
        result = title.compareTo(other.getTitle());
        if (result != 0) return result;
        
        //compare the publishing year
        result = ((Integer) year).compareTo(other.getYear());
        if (result != 0) return result;
        
        //compare the id
        return id.compareTo(other.toString());
    }
    
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Article) && this.id.equals(((Article) obj).toString());
    }
}
