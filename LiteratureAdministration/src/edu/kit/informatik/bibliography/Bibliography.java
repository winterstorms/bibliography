package edu.kit.informatik.bibliography;

import java.util.ArrayList;
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
    private ArrayList<ConferenceSeries> series;
    private ArrayList<Article> publications;
    
    /**
     * Creates new bibliography with no content.
     */
    public Bibliography() { }
    
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
     * Determines the given article's authors as the provided ones.
     * 
     * @param id the article's id
     * @param authors the authors
     */
    public void writtenBy(String id, ArrayList<Author> authors) {
        int index = publications.indexOf(new Article(id, "", 0, ArticleType.CONF));
        publications.get(index).addAuthors(authors);
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
        int index = publications.indexOf(new Article(citesId, "", 0, ArticleType.CONF));
        if (index == -1) throw new NoSuchElementException("the citing article does not exist.");
        Article cites = publications.get(index);
        
        index = publications.indexOf(new Article(quotedId, "", 0, ArticleType.CONF));
        if (index == -1) throw new NoSuchElementException("the cited article does not exist.");
        Article quoted = publications.get(index);
        
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
            index = publications.indexOf(new Article(id, "", 0, ArticleType.CONF));
            if (index == -1) throw new NoSuchElementException("publication not found.");
            publications.get(index).addKeywords(words);
        } else if (entity.equals("series")) {
            index = series.indexOf(new ConferenceSeries(id));
            if (index == -1) throw new NoSuchElementException("series not found.");
            series.get(index).addKeywords(words);
        } else if (entity.equals("journal")) {
            index = journals.indexOf(new Journal(id, ""));
            if (index == -1) throw new NoSuchElementException("journal not found.");
            journals.get(index).addKeywords(words);
        } else if (entity.equals("conference")) {
            index = series.indexOf(new ConferenceSeries(id.split(",")[0]));
            if (index == -1) throw new NoSuchElementException("series of the conference not found.");
            int confIndex = series.get(index).getConferences()
                    .indexOf(new Conference(Integer.parseInt(id.split(",")[1]), ""));
            if (confIndex == -1) throw new NoSuchElementException("conference not found.");
            series.get(index).getConferences().get(confIndex).addKeywords(words);
        }
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
