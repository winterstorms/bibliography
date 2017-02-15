package edu.kit.informatik.bibliography;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import edu.kit.informatik.Terminal;

/**
 * A bibliography.
 * 
 * @author Frithjof Marquardt
 * @version 1.00, 10.02.2017
 */
public class Bibliography {
    private ArrayList<Author> authors;
    private ArrayList<Journal> journals;
    private ArrayList<ConferenceSeries> series;
    private ArrayList<Article> publications;
    private BibStyle[] styles;
    
    /**
     * Creates new bibliography with the styles provided in BibStyle.java.
     */
    public Bibliography() {
        styles = BibStyle.values();
    }
    
    /**
     * Adds a new author with the provided arguments to the list of authors in the bibliography.
     * 
     * @param firstname the forename
     * @param lastname the surname
     */
    public void addAuthor(String firstname, String lastname) {
        authors.add(new Author(firstname, lastname));
    }
    
    /**
     * Adds a new journal with the provided arguments to the list of journals in the bibliography.
     * 
     * @param name its name
     * @param publisher its publisher
     */
    public void addJournal(String name, String publisher) {
        journals.add(new Journal(name, publisher));
    }
    
    /**
     * Adds a new conference series with the provided name to the list of conference series' the bibliography.
     * 
     * @param name the name
     */
    public void addConferenceSeries(String name) {
        series.add(new ConferenceSeries(name));
    }
    
    /**
     * Adds a new conference to an already existing conference series in the bibliography.
     * 
     * @param seriesName the name of the conference series
     * @param location the location of the conference
     * @param year the year in which the conference takes place
     * 
     * @throws NoSuchElementException if no conference series with the provided name exists
     */
    public void addConference(String seriesName, String location, int year) throws NoSuchElementException {
        int index = series.indexOf(new ConferenceSeries(seriesName));
        if (index == -1) {
            throw new NoSuchElementException("conference series not found.");
        } else {
            series.get(index).addConference(new Conference(year, location));
        }
    }
    
    /**
     * Adds a new article with provided arguments to the specified venue (journal or conference).
     * 
     * @param venue the venue
     * @param id the new article's unique id
     * @param year the publishing year
     * @param title the title of the article
     * 
     * @throws NoSuchElementException if there is no journal or conference matching the specified venue
     */
    public void addArticle(String venue, String id, int year, String title) throws NoSuchElementException {

        //if there is a journal matching the venue add the article to it
        int index = journals.indexOf(new Journal(venue, ""));
        if (index != -1) {
            Article newArticle = new Article(id, title, year, ArticleType.JOUR);
            journals.get(index).addArticle(newArticle);
            publications.add(newArticle);
            return;
        }
        
        /*
         * if there is a conference series matching the venue with a conference 
         * in the given year add the article to this conference.
         */
        index = series.indexOf(new ConferenceSeries(venue));
        if (index == -1) throw new NoSuchElementException("there is no journal or conference series with this name.");
        else {
            int confIndex = series.get(index).getConferences().indexOf(new Conference(year, ""));
            if (confIndex == -1) throw new NoSuchElementException("there is no conference in the given year.");
            else {
                Article newArticle = new Article(id, title, year, ArticleType.CONF);
                series.get(index).getConferences().get(confIndex).addArticle(newArticle);
                publications.add(newArticle);
            }
        }
    }
    
    /**
     * Determines the given article's authors as the provided ones and adds the article too the authors' publications.
     * 
     * @param id the article's id
     * @param authors the authors
     */
    public void writtenBy(String id, ArrayList<Author> authors) {
        Article article = findPublication(id);
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
        Article cites = findPublication(citesId);
        Article quoted = findPublication(quotedId);
        
        if (cites.getYear() <= quoted.getYear()) 
            throw new IllegalArgumentException("the citing article has to be published after the quoted one!");
        
        quoted.addCitation(cites);
    }
    
    /**
     * Adds keywords to the entity with the provided id.
     * 
     * @param entity describes the type of the entity (publication, journal, series, ...)
     * @param id the id
     * @param words the keywords to add
     * 
     * @throws NoSuchElementException if the entity with this id does not exist
     */
    public void addKeywords(String entity, String id, ArrayList<String> words) throws NoSuchElementException {
        int index;
        if (entity.equals("pub")) {
            Article article = findPublication(id);
            article.addKeywords(words);
        } else if (entity.equals("series")) {
            ConferenceSeries ser = findSeries(id);
            ser.addKeywords(words);
        } else if (entity.equals("journal")) {
            Journal journal = findJournal(id);
            journal.addKeywords(words);
        } else if (entity.equals("conference")) {
            ConferenceSeries ser = findSeries(id.split(",")[0]);
            Conference conf = findConference(ser, Integer.parseInt(id.split(",")[1]));
            conf.addKeywords(words);
        }
    }
    
    /**
     * Prints the unique id of every publication in the given list.
     * 
     * @param articles the list of publications
     */
    public void printPublications(Collection<Article> articles) {
        for (Article a : articles) {
            Terminal.printLine(a.getId());
        }
    }
    
    /**
     * Prints all articles published by at least one of the provided authors.
     *  
     * @param authors the authors' names
     */
    public void printByAuthors(Collection<String> authors) {
        TreeSet<Article> articles = new TreeSet<Article>();
        Author author;
        for (String name : authors) {
            author = findAuthor(name);
            articles.addAll(author.getPublications());
        }
        printPublications(articles);
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
        return (set1.size() / union.size());
    }
    
    /**
     * Returns the jaccard index of the lists of keywords of the two articles specified bay their id
     * @param idArticle the first article's id
     * @param idCompare the id of the article the first one is compared to
     * 
     * @return their jaccard index
     * 
     * @throws NoSuchElementException if one of the articles does not exist
     */
    public double similarity(String idArticle, String idCompare) throws NoSuchElementException { 
        TreeSet<String> first = findPublication(idArticle).getKeywords();
        TreeSet<String> compare = findPublication(idCompare).getKeywords();
        return jaccard(first, compare);
    }
    
    /**
     * Calculates the hIndex for the provided numbers of citations for publications.
     * 
     * @param citations the numbers of citation
     * 
     * @return the hIndex
     * 
     * @throws IllegalArgumentException if @param is null
     */
    public int hIndex(int[] citations) throws IllegalArgumentException {
        if (citations == null) throw new IllegalArgumentException("no sitations found.");
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
     * @return his/her hIndex
     * 
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
            Terminal.printLine(a.getId());
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
    public void printConference(String style, int bibId, Author[] authors, String title, 
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
    public void printJournal(String style, int bibId, Author[] authors, String title, String journal, int year) 
            throws NoSuchElementException, IllegalArgumentException {
        for (BibStyle b : styles) {
            if (b.pattern().matcher(style).matches()) b.printJournal(bibId, authors, title, journal, year);
        }
        throw new NoSuchElementException("specified style not found.");
    }
    
    /**
     * Prints all the given articles in the specified style
     * 
     * @param stylePattern the output format
     * @param ids the articles' ids
     * 
     * @throws NoSuchElementException if article or style does not exist
     */
    public void printBib(String stylePattern, Collection<String> ids) throws NoSuchElementException {
        BibStyle style;
        if (BibStyle.IEEE.pattern().matcher(stylePattern).matches()) style = BibStyle.IEEE;
        else if (BibStyle.CHIC.pattern().matcher(stylePattern).matches()) style = BibStyle.CHIC;
        else throw new NoSuchElementException("specified style not found.");
        
        Article current;
        ArrayList<Article> articles = new ArrayList<Article>();
        for (String id : ids) {
            current = findPublication(id);
            if (current.getAuthors().isEmpty()) 
                throw new IllegalArgumentException("one of the articles has no authors.");
            articles.add(current);
        }
        
        //sortiere articles noch 
        
        
        
        for (int i = 0; i < articles.size(); i++) {
            current = articles.get(i);
            if (current.getType().equals(ArticleType.CONF)) printConference(style.toString().toLowerCase(), i, 
                    (Author[]) current.getAuthors().toArray(), current.getTitle(), "platzhalter", "location", 
                    current.getYear());
        
        //füge article series/journal + location hinzu (interface für die beiden) schaffe dafür articleType ab.
        }
    }
    
    /**
     * Returns set of all coauthors of the author with the given name excluding the author himself.
     * 
     * @param name the author's name
     * 
     * @return the coauthors
     * 
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
     * Returns the publication with the given unique id if it is in the bibliography.
     * 
     * @param identifier the publication's supposed id
     * 
     * @return the publication
     * 
     * @throws NoSuchElementException if the bibliography does not contain the publication
     */
    private Article findPublication(String identifier) throws NoSuchElementException  {
        for (Article a : publications) {
            if (a.getId().equals(identifier)) return a;
        }
        throw new NoSuchElementException("publication not found.");
    }
    
    /**
     * Returns the conference series with the given unique id if it is in the bibliography.
     * 
     * @param identifier the series' supposed name
     * 
     * @return the series
     * 
     * @throws NoSuchElementException if the bibliography does not contain the series
     */
    private ConferenceSeries findSeries(String identifier) throws NoSuchElementException  {
        for (ConferenceSeries s : series) {
            if (s.getName().equals(identifier)) return s;
        }
        throw new NoSuchElementException("series not found.");
    }
    
    /**
     * Returns the journal with the given unique id if it is in the bibliography.
     * 
     * @param identifier the journal's supposed name
     * 
     * @return the journal
     * 
     * @throws NoSuchElementException if the bibliography does not contain the journal
     */
    private Journal findJournal(String identifier) throws NoSuchElementException  {
        for (Journal j : journals) {
            if (j.getName().equals(identifier)) return j;
        }
        throw new NoSuchElementException("journal not found.");
    }
    
    /**
     * Returns the author with the given unique id if it is in the bibliography.
     * 
     * @param identifier the author's supposed name
     * 
     * @return the author
     * 
     * @throws NoSuchElementException if the bibliography does not contain the author
     */
    private Author findAuthor(String identifier) throws NoSuchElementException  {
        for (Author a : authors) {
            if (a.getFullName().equals(identifier)) return a;
        }
        throw new NoSuchElementException("author not found.");
    }
    
    /**
     * Returns the conference with the given unique id if it is in the bibliography.
     * 
     * @param ser the series of this conference
     * @param identifier the year in which the conference held place
     * 
     * @return the conference
     * 
     * @throws NoSuchElementException if the bibliography does not contain the series
     */
    private Conference findConference(ConferenceSeries ser, int identifier) throws NoSuchElementException  {
        for (Conference c : ser.getConferences()) {
            if (c.getYear() == identifier) return c;
        }
        throw new NoSuchElementException("conference not found.");
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
