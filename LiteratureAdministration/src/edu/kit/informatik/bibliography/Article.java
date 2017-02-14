package edu.kit.informatik.bibliography;

import java.util.ArrayList;

/**
 * An article in a bibliography.
 * 
 * @author Frithjof Marquardt
 * @version 1.00, 10.02.2017
 */
public class Article implements Comparable<Article> {
    private final String id;
    private final String title;
    private final int year;
    private final ArticleType type;
    private ArrayList<Article> citatedBy;
    private ArrayList<Author> authors;
    private ArrayList<String> keywords;
    
    /**
     * Creates e new article with the provided arguments.
     * 
     * @param id the id
     * @param title the title
     * @param year the publishing year
     * @param type the venue (journal or conference)
     */
    public Article(String id, String title, int year, ArticleType type) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.type = type;
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
        if (!citatedBy.contains(article)) {
            citatedBy.add(article);
        }     
    }
    
    /**
     * Adds provided words to this article's list of keywords.
     * 
     * @param words the new keywords
     */
    public void addKeywords(ArrayList<String> words) {
        keywords.addAll(words);
    }
    
    /**
     * Returns this article's id.
     * 
     * @return the id
     */
    public String getId() {
        return id;
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
        return id.compareTo(other.getId());
    }
    
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Article) && this.id.equals(((Article) obj).getId());
    }
}
