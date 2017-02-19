package edu.kit.informatik.bibliography;

import edu.kit.informatik.Terminal;
import java.util.Collection;
import java.util.ArrayList;
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
    private ArrayList<Article> publications;
    private BibStyle[] styles;
    
    /**
     * Creates new bibliography with the styles provided in BibStyle.java.
     */
    public Bibliography() {
        styles = BibStyle.values();
        authors = new ArrayList<Author>();
        journals = new ArrayList<Journal>();
        series = new ArrayList<Series>();
        publications = new ArrayList<Article>();
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
        if (authors.contains(new Author(firstname, lastname))) 
            throw new IllegalArgumentException("author with this name already exists");
        authors.add(new Author(firstname, lastname));
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
        if (journals.contains(new Journal(name, publisher))) 
            throw new IllegalArgumentException("journal with this name already exists");
        journals.add(new Journal(name, publisher));
    }
    
    /**
     * Adds a new conference series with the provided name to the list of conference series' the bibliography.
     * 
     * @param name the name
     * 
     * @throws IllegalArgumentException if series already exists
     */
    public void addSeries(String name) throws IllegalArgumentException {
        if (series.contains(new Series(name))) 
            throw new IllegalArgumentException("series with this name already exists");
        series.add(new Series(name));
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
        if (ser.getConferences().contains(new Conference(year, location))) 
            throw new IllegalArgumentException("conference in this year already exists");
        ser.addConference(new Conference(year, location));
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
        Venue venue = (Venue) findEntity(articleType, venueName);
        Article newArticle = new Article(id, title, year, venue);
        if (publications.contains(newArticle)) throw new IllegalArgumentException("article already exists");
        venue.addArticle(newArticle);
        publications.add(newArticle);
    }
    
    /**
     * Determines the given article's authors as the provided ones and adds the article too the authors' publications.
     * 
     * @param id the article's id
     * @param authorNames the authors' names
     * 
     * @throws NoSuchElementException if one of the authors does not exist
     * @throws IllegalArgumentException if article has already one of the authors
     */
    public void writtenBy(String id, String[] authorNames) throws IllegalArgumentException, NoSuchElementException {
        Article article = (Article) findEntity("pub", id);
        ArrayList<Author> authors = new ArrayList<Author>();
        for (String a : authorNames) {
            authors.add(findAuthor(a));
        }
        ArrayList<Author> copy = new ArrayList<Author>();
        copy.addAll(article.getAuthors());
        copy.retainAll(authors);
        if (!copy.isEmpty()) throw new IllegalArgumentException("article already had one of the authors added."); 
        article.addAuthors(authors);
        for (Author a : authors) {
            a.addPub(article);
        }
    }
    
    /**
     * Adds a reference of the citing article to the list of citations of the quoted article.
     * 
     * @param citesId the citing article's id
     * @param quotedId the quoted article's id
     * 
     * @throws NoSuchElementException if one of the articles does not exist
     */
    public void cites(String citesId, String quotedId) throws NoSuchElementException {
        Article cites = (Article) findEntity("pub", citesId);
        Article quoted = (Article) findEntity("pub", quotedId);
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
     * @param articles the list of publications
     */
    public void printPublications(Collection<Article> articles) {
        for (MyEntity a : articles) {
            Terminal.printLine(a.toString());
        }
    }
    
    /**
     * Prints all articles that have all of the provided keywords.
     * 
     * @param words the keywords
     */
    public void findKeywords(Collection<String> words) {
        TreeSet<Article> articles = new TreeSet<Article>();
        for (Article a : publications) {
            if (a.hasKeywords(words)) articles.add(a);
        }
        printPublications(articles);
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
        return Math.round((set1.size() / union.size()) * 1000) / 1000;
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
        TreeSet<Author> coauthors = getCoauthors(name);
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
        TreeSet<Author> coauthors = getCoauthors(name);
        Author author = findAuthor(name);
        coauthors.add(author);
        TreeSet<Article> foreignCitations = new TreeSet<Article>();
        for (Article article : author.getPublications()) {
            for (Article citation : article.getCitations()) {
                boolean foreign = true;
                for (Author au : coauthors) {
                    if (citation.getAuthors().contains(au)) foreign = false;
                }
                if (foreign) foreignCitations.add(citation);
            }
        }
        for (Article a : foreignCitations) {
            Terminal.printLine(a.toString());
        }
    }
    
    /**
     * Prints an article published on a conference in the specified format.
     * 
     * @param style the output format
     * @param bibId the number of the article in its bibliography
     * @param authors the article's authors
     * @param title the title of the article
     * @param series the series of the conference
     * @param location location of the conference
     * @param year year of the conference
     * 
     * @throws NoSuchElementException if style does not exist
     * @throws IllegalArgumentException if no authors are provided
     */
    public void printConference(String style, int bibId, ArrayList<Author> authors, String title, 
            String series, String location, int year) throws NoSuchElementException, IllegalArgumentException {
        for (BibStyle b : styles) {
            if (b.pattern().matcher(style).matches()) b.printConf(bibId, authors, title, series, location, year);
        }
        throw new NoSuchElementException("specified style not found.");
    }
    
    /**
     * Prints a journal article in the specified format.
     * 
     * @param style the output format
     * @param bibId position of the article in the bibliography
     * @param authors the authors of the article
     * @param title the article's title
     * @param journal the name of the journal
     * @param year the publishing year
     * 
     * @throws NoSuchElementException if style does not exist
     * @throws IllegalArgumentException if no authors are provided
     */
    public void printJournal(String style, int bibId, ArrayList<Author> authors, String title, 
            String journal, int year) throws NoSuchElementException, IllegalArgumentException {
        for (BibStyle b : styles) {
            if (b.pattern().matcher(style).matches()) b.printJournal(bibId, authors, title, journal, year);
        }
        throw new NoSuchElementException("specified style not found.");
    }
    
    /**
     * Prints all the given articles in the specified style.
     * 
     * @param style the output format
     * @param ids the articles' ids
     * 
     * @throws NoSuchElementException if article or style does not exist
     * @throws IllegalArgumentException if one article has no authors
     */
    public void printBib(String style, Collection<String> ids) throws NoSuchElementException, IllegalArgumentException {
        if (!BibStyle.IEEE.pattern().matcher(style).matches() && !BibStyle.CHIC.pattern()
                .matcher(style).matches()) throw new NoSuchElementException("specified style not found.");
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
                printConference(style, articles.indexOf(current), current.getAuthors(), current.getTitle(), 
                        venue.getName(), ((Series) venue).findConference(current.getYear()).getLocation(), 
                        current.getYear());
            } else {
                printJournal(style, articles.indexOf(current), current.getAuthors(), 
                        current.getTitle(), venue.getName(), current.getYear());
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
    public ArrayList<Article> getPublications() {
        return publications;
    }
    
    /**
     * Returns the entity with the given unique identifier (id or name) if it is in the bibliography.
     * 
     * @param type the entity's supposed type (conference, series, journal or article)
     * @param identifier the entity's supposed name
     * 
     * @return the entity
     * @throws NoSuchElementException if the bibliography does not contain the entity
     */
    public MyEntity findEntity(String type, String identifier) throws NoSuchElementException  {
        if (type.equals("pub")) {
            for (Article a : publications) {
                if (a.toString().equals(identifier)) return a;
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
     * Returns set of all coauthors of the author with the given name excluding the author himself.
     * 
     * @param name the author's name
     * 
     * @return the coauthors
     * @throws NoSuchElementException if author does not exist
     */
    private TreeSet<Author> getCoauthors(String name) throws NoSuchElementException {
        Author author = findAuthor(name);
        TreeSet<Author> names = new TreeSet<Author>();
        for (Article article : author.getPublications()) {
            names.addAll(article.getAuthors());
        }
        names.remove(author);
        return names;
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