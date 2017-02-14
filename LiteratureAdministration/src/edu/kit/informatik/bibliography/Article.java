package edu.kit.informatik.bibliography;

import java.util.ArrayList;

/**
 * An article in a bibliography.
 * 
 * @author Frithjof Marquardt
 * @version 1.00, 10.02.2017
 */
public class Article {
    private final String id;
    private final String title;
    private final int year;
    private final ArticleType type;
    private ArrayList<Article> citations;
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
        if (!citations.contains(article)) {
            citations.add(article);
        }     
    }
    
    /**
     * Adds provided words to this article's list of keywords.
     * 
     * @param words the new keywords
     */
    public void addKeyword(ArrayList<String> words) {
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
    
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Article) && this.id.equals(((Article) obj).getId());
    }
}
