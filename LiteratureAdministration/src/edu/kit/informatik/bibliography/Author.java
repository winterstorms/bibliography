package edu.kit.informatik.bibliography;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An author in a bibliography.
 * 
 * @author Frithjof Marquardt
 * @version 1.00, 10.02.2017
 */
public class Author implements Comparable<Author> {
    private final String firstname;
    private final String lastname;
    private ArrayList<Article> publications;
    
    /**
     * Creates a new author with the provided arguments.
     *  
     * @param firstname the firstname
     * @param lastname the lastname
     */
    public Author(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }
    
    /**
     * Creates a new author with the provided argument.
     *  
     * @param fullname the name
     * 
     * @throws IllegalArgumentException if the argument is not a valid name for an author
     */
    public Author(String fullname) throws IllegalArgumentException {
        Matcher m = Pattern.compile("[a-zA-Z]+ [a-zA-Z]+").matcher(fullname);
        if (!m.matches()) throw new IllegalArgumentException("invalid name for an author.");
        String[] names = fullname.split(" ");
        this.firstname = names[0];
        this.lastname = names[1];
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
    
    /**
     * Returns the author's full name.
     * 
     * @return the name
     */
    public String getFullName() {
        return getFirstname() + " " + getLastname();
    }
    
    /**
     * Returns this author's list of publications.
     * 
     * @return the list
     */
    public ArrayList<Article> getPublications() {
        return publications;
    }
    
    @Override
    public int compareTo(Author other) {
        int result = lastname.compareTo(other.getLastname());
        if (result != 0) return result;
        
        result = firstname.compareTo(other.getFirstname());
        if (result != 0) return result;
        
        return 0;
    }
    
    @Override
    public boolean equals(Object other) {
        return (other instanceof Author) && getFullName().equals(((Author) other).getFullName());
    }
}
