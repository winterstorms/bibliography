package edu.kit.informatik.bibliography;

import java.util.ArrayList;
import java.util.TreeSet;

public class Publication extends Entity {
    private final String id;
    private final String title;
    private final int year;
    private ArrayList<Author> authors;
    private TreeSet<Publication> citatedBy;

    
    /**
     * Creates new publication with the provided arguments.
     * 
     * @param id the publication id
     * @param title the title
     * @param year the publishing year
     */
    public Publication(String id, String title, int year) {
        super();
        this.id = id;
        this.title = title;
        this.year = year;
        authors = new ArrayList<Author>();
        citatedBy = new TreeSet<Publication>();
    }
    
    /**
     * Adds provided authors to this publication's list of authors.
     * 
     * @param newAuthors the authors to add
     * @throws IllegalArgumentException if publication already has one of the authors 
     */
    public void addAuthors(ArrayList<Author> newAuthors) throws IllegalArgumentException {
        ArrayList<Author> copy = new ArrayList<Author>();
        copy.addAll(getAuthors());
        copy.retainAll(authors);
        if (!copy.isEmpty()) throw new IllegalArgumentException("article already had one of the authors added.");
        authors.addAll(newAuthors);
    }
    
    /**
     * Adds provided publication that cites this publication to its list of citations.
     * 
     * @param pub the publication that cites this article
     */
    public void addCitation(Publication pub) {
        citatedBy.add(pub); 
    }
    
    /**
     * Returns this publication's the list of citations.
     * 
     * @return the citations
     */
    public TreeSet<Publication> getCitations() {
        return citatedBy;
    }
    
    /**
     * Returns the publication's id.
     * 
     * @return the id
     */
    public String getId() {
        return id;
    }
    
    /**
     * Returns the publication's title
     * 
     * @return the title
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Returns the publication's publishing year.
     * 
     * @return the year
     */
    public int getYear() {
        return year;
    }
    
    /**
     * Returns the ArrayList of authors of this publication.
     * 
     * @return the list
     */
    public ArrayList<Author> getAuthors() {
        return authors;
    }
    
    @Override
    public String toString() {
        return getId();
    }
}
