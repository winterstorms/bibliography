package edu.kit.informatik.bibliography;

import java.util.ArrayList;

/**
 * An author in a bibliography.
 * 
 * @author Frithjof Marquardt
 * @version 1.00, 10.02.2017
 */
public class Author {
    private final String firstname;
    private final String lastname;
    private ArrayList<Article> publications;
    
    /**
     * Creates a new author with the provided arguments.
     *  
     * @param firstname the firstname
     * @param lastname the lastname
     * 
     * @throws SpellingException if names are not in lower case
     */
    public Author(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }
    
    /**
     * Adds provided article to this author's publications.
     * 
     * @param article the article
     */
    public void addPub(Article article) {
        publications.add(article);
    }
    
    /**
     * Returns the firstname.
     * 
     * @return the firstname
     */
    public String getFirstname() {
        return firstname;
    }
    
    /**
     * Returns the lastname.
     * 
     * @return the lastname
     */
    public String getLastname() {
        return lastname;
    }
    
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Author) && firstname.equals(((Author) obj).getFirstname()) 
                && lastname.equals(((Author) obj).getLastname());
    }
}
