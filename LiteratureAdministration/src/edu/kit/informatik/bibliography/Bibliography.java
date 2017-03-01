package edu.kit.informatik.bibliography;

import edu.kit.informatik.Terminal;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * A bibliography.
 * 
 * @author Frithjof Marquardt
 * @version 1.00, 10.02.2017
 */
public class Bibliography {
    private ArrayList<Author> authors;
    private ArrayList<Journal> journals;
    private ArrayList<Series> series;
    private ArrayList<Publication> publications;
    
    /**
     * Creates new bibliography with the styles provided in BibStyle.java.
     */
    public Bibliography() {
        authors = new ArrayList<Author>();
        journals = new ArrayList<Journal>();
        series = new ArrayList<Series>();
        publications = new ArrayList<Publication>();
    }
    
    /**
     * Adds a new author with the provided arguments to the list of authors in the bibliography.
     * 
     * @param firstname the forename
     * @param lastname the surname
     * 
     * @throws IllegalArgumentException if author already exists
     */
    public void addAuthor(String firstname, String lastname) {
        Author author = new Author(firstname, lastname);
        if (authors.contains(author)) throw new IllegalArgumentException("author with this name already exists.");
        authors.add(author);
    }
    
    /**
     * Adds a new journal with the provided arguments to the list of journals in the bibliography.
     * 
     * @param name its name
     * @param publisher its publisher
     * 
     * @throws IllegalArgumentException if journal already exists
     */
    public void addJournal(String name, String publisher) throws IllegalArgumentException {
        Journal journal = new Journal(name, publisher);
        if (journals.contains(journal)) throw new IllegalArgumentException("journal with this name already exists.");
        journals.add(journal);
    }
    
    /**
     * Adds a new conference series with the provided name to the list of conference series' the bibliography.
     * 
     * @param name the name
     * 
     * @throws IllegalArgumentException if series already exists
     */
    public void addSeries(String name) throws IllegalArgumentException {
        Series ser = new Series(name);
        if (series.contains(ser)) throw new IllegalArgumentException("series with this name already exists.");
        series.add(ser);
    }
    
    /**
     * Adds a new conference to an already existing conference series in the bibliography.
     * 
     * @param seriesName the name of the conference series
     * @param year the year in which the conference takes place
     * @param location the location of the conference
     * 
     * @throws NoSuchElementException if no conference series with the provided name exists
     * @throws IllegalArgumentException if conference in this series already exists
     */
    public void addConference(String seriesName, int year, String location) 
            throws NoSuchElementException, IllegalArgumentException {
        Series ser = (Series) findEntity("series", seriesName);
        Conference conf = new Conference(year, location);
        if (ser.getConferences().contains(conf)) 
            throw new IllegalArgumentException("conference in this year already exists.");
        ser.addConference(conf);
    }
    
    /**
     * Adds a new article with provided arguments to the specified venue (journal or conference).
     * 
     * @param articleType the type of the venue (journal or series)
     * @param venueName the venue's name
     * @param id the new article's unique id
     * @param year the publishing year
     * @param title the title of the article
     * 
     * @throws NoSuchElementException if there is no journal or conference matching the specified venue
     * @throws IllegalArgumentException if article already exists
     */
    public void addArticle(String articleType, String venueName, String id, int year, String title) 
            throws NoSuchElementException, IllegalArgumentException {
        if (!articleType.equals("series") && !articleType.equals("journal")) 
            throw new IllegalArgumentException("venue has to be a series or journal");
        Venue venue = (Venue) findEntity(articleType, venueName);
        Article newArticle = new Article(id, title, year, venue);
        if (publications.contains(newArticle)) throw new IllegalArgumentException("article already exists");
        newArticle.addKeywords(venue.getKeywords());
        venue.addArticle(newArticle);
        publications.add(newArticle);
    }
    
    /**
     * Sets the given publication's authors as the provided ones and adds the publication to the authors' publications.
     * 
     * @param id the publication's id
     * @param authorNames the authors' names
     * 
     * @throws NoSuchElementException if one of the authors does not exist
     * @throws IllegalArgumentException if publication has already one of the authors 
     *      or is about to add an author multiple times
     */
    public void writtenBy(String id, String[] authorNames) throws IllegalArgumentException, NoSuchElementException {
        Publication pub = (Publication) findEntity("pub", id);
        TreeSet<Author> authors = new TreeSet<Author>();
        for (String a : authorNames) {
            if (!authors.add(findAuthor(a))) 
                throw new IllegalArgumentException("list contains one author several times.");
        }
        pub.addAuthors(authors);
        for (Author a : authors) {
            a.addPub(pub);
        }
    }
    
    /**
     * Adds a reference of the citing publication to the list of citations of the quoted publication.
     * 
     * @param citesId the citing publication's id
     * @param quotedId the quoted publication's id
     * 
     * @throws NoSuchElementException if one of the articles does not exist
     */
    public void cites(String citesId, String quotedId) throws NoSuchElementException {
        Publication cites = (Publication) findEntity("pub", citesId);
        Publication quoted = (Publication) findEntity("pub", quotedId);
        if (cites.getYear() <= quoted.getYear()) 
            throw new IllegalArgumentException("the citing article has to be published after the quoted one!");
        quoted.addCitation(cites);
    }
    
    /**
     * Adds keywords to the entity with the provided id.
     * 
     * @param type describes the type of the entity (publication, journal, series, ...)
     * @param id the id
     * @param words the keywords to add
     * 
     * @throws NoSuchElementException if the entity with this id does not exist
     */
    public void addKeywords(String type, String id, Collection<String> words) throws NoSuchElementException {
        findEntity(type, id).addKeywords(words);
    }
    
    /**
     * Prints the unique id of every publication in the given list.
     * 
     * @param publications the list of publications
     */
    public void printPublications(Collection<Publication> publications) {
        for (Publication a : publications) {
            Terminal.printLine(a.toString());
        }
    }
    
    /**
     * Prints all articles that have all of the provided keywords.
     * 
     * @param words the keywords
     */
    public void findKeywords(Collection<String> words) {
        if (words.isEmpty()) return;
        TreeSet<Publication> pubs = new TreeSet<Publication>();
        for (Publication p : getPublications()) {
            if (p.hasKeywords(words)) pubs.add(p);
        }
        printPublications(pubs);
    }
    
    /**
     * Calculates the jaccard index of the two provided sets of keywords.
     * 
     * @param set1 the first set of words
     * @param set2 the second set of words
     * 
     * @return the jaccard index
     */
    public double jaccard(Set<String> set1, Set<String> set2) {
        if (set1.isEmpty() && set2.isEmpty()) return 1;
        TreeSet<String> union = new TreeSet<String>();
        union.addAll(set1);
        union.addAll(set2);
        set1.retainAll(set2);
        return (Math.floor(((double) set1.size()) / union.size() * 1000)) / 1000;
    }
    
    /**
     * Returns the jaccard index of the lists of keywords of the two articles specified by their id.
     * 
     * @param idArticle the first article's id
     * @param idCompare the id of the article the first one is compared to
     * 
     * @return their jaccard index
     * @throws NoSuchElementException if one of the articles does not exist
     */
    public double similarity(String idArticle, String idCompare) throws NoSuchElementException { 
        TreeSet<String> first = ((Article) findEntity("pub", idArticle)).getKeywords();
        TreeSet<String> compare = ((Article) findEntity("pub", idCompare)).getKeywords();
        return jaccard(first, compare);
    }
    
    /**
     * Calculates the hIndex for the provided numbers of citations for publications.
     * 
     * @param citations the numbers of citation
     * 
     * @return the hIndex
     * @throws IllegalArgumentException if @param is null
     */
    public int hIndex(int[] citations) throws IllegalArgumentException {
        if (citations == null) throw new IllegalArgumentException("no citations found.");
        Arrays.sort(citations);
        int result = 0;
        int current;
        for (int i = 0; i < citations.length; i++) {
            current = Math.min(citations[i], citations.length - i);
            result = Math.max(result, current);
        }
        return result;
    }
    
    /**
     * Calculates the given author's hIndex.
     * 
     * @param name the author's name
     * 
     * @return the author's hIndex
     * @throws NoSuchElementException if the author does not exist.
     */
    public int hIndexByAuthor(String name) throws NoSuchElementException {
        Author author = findAuthor(name);
        int[] citations = new int[author.getPublications().size()];
        for (int i = 0; i < citations.length; i++) {
            citations[i] = author.getPublications().get(i).getCitations().size();
        }
        return hIndex(citations);
    }
    
    /**
     * Prints all coauthors of the given author.
     * 
     * @param name the name of the author
     * 
     * @throws NoSuchElementException if the author does not exist
     */
    public void printCoauthors(String name) throws NoSuchElementException {
        Author author = findAuthor(name);
        TreeSet<Author> coauthors = author.getCoauthors();
        for (Author a : coauthors) {
            Terminal.printLine(a.getFullName());
        }
    }
    
    /**
     * Prints all foreign citations of the given author as specified in the task.
     * 
     * @param name the name of the author
     * 
     * @throws NoSuchElementException if author does not exist
     */
    public void printForeignCitations(String name) throws NoSuchElementException {
        Author author = findAuthor(name);
        TreeSet<Author> coauthors = author.getCoauthors();
        coauthors.add(author);
        TreeSet<Publication> foreignCitations = new TreeSet<Publication>();
        for (Publication pubs : author.getPublications()) {
            for (Publication citation : pubs.getCitations()) {
                boolean foreign = true;
                for (Author au : coauthors) {
                    if (citation.getAuthors().contains(au)) foreign = false;
                }
                if (foreign) foreignCitations.add(citation);
            }
        }
        for (Publication a : foreignCitations) {
            Terminal.printLine(a.toString());
        }
    }
    
    /**
     * Prints all the given publications in the specified style.
     * 
     * @param format the output format
     * @param ids the publications' ids
     * 
     * @throws NoSuchElementException if publication or style does not exist
     * @throws IllegalArgumentException if one publication has no authors
     */
    public void printBib(String format, Collection<String> ids) throws NoSuchElementException, 
        IllegalArgumentException {
        BibStyle style = null;
        for (BibStyle b : BibStyle.values()) {
            if (b.pattern().matcher(format).matches()) style = b;
        }
        if (style == null) throw new NoSuchElementException("specified style not found.");
        ArrayList<Article> articles = new ArrayList<Article>();
        Article pub;
        for (String id : ids) {
            pub = (Article) findEntity("pub", id);
            if (pub.getAuthors().isEmpty()) throw new IllegalArgumentException("one of the articles is invalid.");
            if (!articles.contains(pub)) articles.add(pub);
        }
        articles = sortArticles(articles);
        
        Venue venue;
        for (Article current : articles) {
            venue = current.getVenue();
            if (venue instanceof Series) {
                style.printConf(articles.indexOf(current) + 1, current.getAuthors(), current.getTitle(), 
                        venue.getName(), ((Series) venue).findConference(current.getYear()).getLocation(), 
                        current.getYear());
            } else {
                style.printJournal(articles.indexOf(current) + 1, current.getAuthors(), current.getTitle(), 
                        venue.getName(), current.getYear());
            }
        }
    }
    
    /**
     * Returns sorted version of the provided list.
     * 
     * @param list the list
     * @return the sorted list
     */
    public ArrayList<Article> sortArticles(ArrayList<Article> list) {
        list.sort(new ArticleComparator());
        return list;
    }
    
    /**
     * Returns the list of publications.
     * 
     * @return the list
     */
    public ArrayList<Publication> getPublications() {
        return publications;
    }
    
    /**
     * Returns the entity with the given unique identifier (id or name) if it is in the bibliography.
     * 
     * @param type the entity's supposed type (conference, series, journal or publication)
     * @param identifier the entity's supposed name
     * 
     * @return the entity
     * @throws NoSuchElementException if the bibliography does not contain the entity
     */
    public Entity findEntity(String type, String identifier) throws NoSuchElementException  {
        if (type.equals("pub")) {
            for (Publication p : publications) {
                if (p.toString().equals(identifier)) return p;
            }
            throw new NoSuchElementException("publication not found.");
        } else if (type.equals("journal")) {
            for (Journal j : journals) {
                if (j.toString().equals(identifier)) return j;
            }
            throw new NoSuchElementException("journal not found.");
        } else if (type.equals("series")) {
            for (Series s : series) {
                if (s.toString().equals(identifier)) return s;
            }
            throw new NoSuchElementException("series not found.");
        } else if (type.equals("conference")) {
            String[] id = identifier.split(",");
            for (Series s : series) {
                if (s.toString().equals(id[0])) {
                    Series ser = s;
                    return ser.findConference(Integer.parseInt(id[1]));
                }
            }
            throw new NoSuchElementException("series not found.");
        } else throw new NoSuchElementException("the provided type specifies no entity.");
        
    }
    
    /**
     * Returns the author with the given unique id if it is in the bibliography.
     * 
     * @param identifier the author's supposed name
     * 
     * @return the author
     * @throws NoSuchElementException if the bibliography does not contain the author
     */
    public Author findAuthor(String identifier) throws NoSuchElementException  {
        for (Author a : authors) {
            if (a.getFullName().equals(identifier)) return a;
        }
        throw new NoSuchElementException("author not found.");
    }
    
    /**
     * Private comparator to sort the list of articles in the bibliography.
     */
    private class ArticleComparator implements Comparator<Article> {
        @Override
        public int compare(Article o1, Article o2) {
            return o1.compareTo(o2);
        }
    }
}