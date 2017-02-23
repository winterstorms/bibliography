package edu.kit.informatik.bibliography;

/**
 * An article in a bibliography.
 * 
 * @author Frithjof Marquardt
 * @version 1.00, 10.02.2017
 */
public class Article extends Publication implements Comparable<Article> {
    private final Venue venue;
    
    /**
     * Creates e new article with the provided arguments.
     * 
     * @param id the id
     * @param title the title
     * @param year the publishing year
     * @param venue the venue (journal or conference series)
     */
    public Article(String id, String title, int year, Venue venue) {
        super(id, title, year);
        this.venue = venue;
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
    public int compareTo(Article other) {
        int result;
        
        //compare the authors
        int counter = 0;
        int size = getAuthors().size();
        while (counter < size) {
            if (!other.getAuthors().isEmpty()) {
                result = getAuthors().get(counter).compareTo(other.getAuthors().get(counter));
                if (result != 0) return result;
                counter++;
            } else return 1;
        }
        if (other.getAuthors().size() > counter) return -1;
        
        //compare the title
        result = getTitle().compareTo(other.getTitle());
        if (result != 0) return result;
        
        //compare the publishing year
        result = ((Integer) getYear()).compareTo(other.getYear());
        if (result != 0) return result;
        
        //compare the id
        return getId().compareTo(other.toString());
    }
    
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Article) && getId().equals(((Article) obj).toString());
    }
}
