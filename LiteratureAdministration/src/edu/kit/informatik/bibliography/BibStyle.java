package edu.kit.informatik.bibliography;

import java.util.regex.Pattern;

import edu.kit.informatik.Terminal;

public enum BibStyle {
    /**
     * Implements the IEEE Simplified style.
     */
    IEEE("ieee") {
        @Override
        public void printConf(int bibId, Author[] authors, String title, 
                String nameSeries, String location, int year) throws IllegalArgumentException {
            if (authors.length == 0) throw new IllegalArgumentException("article has no authors.");
            String output = "[" + bibId + "] ";
            output.concat(formatAuthorNames(authors));
            output.concat(", \"" + title + ",\" in Proceedings of " + nameSeries + ", " + location + ", " + year + ".");
            Terminal.printLine(output);
        }
        
        @Override
        public void printJournal(int bibId, Author[] authors, String title, String nameJournal, int year) 
                throws IllegalArgumentException {
            if (authors.length == 0) throw new IllegalArgumentException("article has no authors.");
            String output = "[" + bibId + "] ";
            output.concat(formatAuthorNames(authors));
            output.concat(", \"" + title + ",\" " + nameJournal + ", " + year + ".");
            Terminal.printLine(output);
        }
        
        @Override
        public String formatAuthorNames(Author[] authors) {
            String result = authors[0].getFirstname().toUpperCase().charAt(0) + ". " + authors[0].getLastname();
            if (authors.length > 2) result += " et al.";
            if (authors.length == 1) result += authors[1].getFirstname().toUpperCase().charAt(0) 
                    + ". " + authors[1].getLastname();
            return result;
        }
    },
    
    /**
     * Implements the Chicago Simplified style.
     */
    CHIC("chicago") {
        @Override
        public void printConf(int bibId, Author[] authors, String title, 
                String nameSeries, String location, int year) throws IllegalArgumentException {
            if (authors.length == 0) throw new IllegalArgumentException("article has no authors.");
            String output = "(" + authors[0].getLastname() + ", " + year + ") ";
            output.concat(formatAuthorNames(authors));
            output.concat(". \"" + title + ".\" Paper presented at " 
                    + nameSeries + ", " + year + ", " + location + ".");
            Terminal.printLine(output);
        }
        
        @Override
        public void printJournal(int bibId, Author[] authors, String title, String nameJournal, int year) 
                throws IllegalArgumentException {
            if (authors.length == 0) throw new IllegalArgumentException("article has no authors.");
            String output = "(" + authors[0].getLastname() + ", " + year + ") ";
            output.concat(formatAuthorNames(authors));
            output.concat(". \"" + title + ".\" " + nameJournal + " (" + year + ").");
            Terminal.printLine(output);
        }
        
        @Override
        public String formatAuthorNames(Author[] authors) {
            String result = authors[0].getLastname() + ", " + authors[0].getFirstname();
            if (authors.length == 1) return result;
            if (authors.length > 2) {
                for (int i = 1; i < authors.length - 1; i++) {
                    result.concat(", " + authors[i].getLastname() + ", " + authors[i].getFirstname());
                }
            }
            result.concat(", and " + authors[authors.length - 1].getLastname() + ", " 
                    + authors[authors.length - 1].getFirstname());
            
            return result;
        }
        
    };
    
    private Pattern pattern;
    
    /**
     * Creates a new style.
     * 
     * @param style the style type
     */
    BibStyle(String style) {
        pattern = Pattern.compile(style);
    }
    
    /**
     * Returns the style's pattern;
     * 
     * @return the pattern
     */
    public Pattern pattern() {
        return pattern;
    }
    
    /**
     * Prints a conference article in the specified format of the enum.
     * 
     * @param bibId position of the article in the bibliography
     * @param authors the authors of the article
     * @param title the article's title
     * @param nameSeries the name of the conference's series
     * @param location the location of the conference
     * @param year the publishing year
     * 
     * @throws IllegalArgumentException if no authors are provided
     */
    public void printConf(int bibId, Author[] authors, String title, String nameSeries, String location, int year) 
            throws IllegalArgumentException { }
    
    /**
     * Prints a journal article in the specified format of the enum.
     * 
     * @param bibId position of the article in the bibliography
     * @param authors the authors of the article
     * @param title the article's title
     * @param nameJournal the name of the journal
     * @param year the publishing year
     * 
     * @throws IllegalArgumentException if no authors are provided
     */
    public void printJournal(int bibId, Author[] authors, String title, String nameJournal, int year) 
            throws IllegalArgumentException { }
    
    /**
     * Formats the given authors names to match the enums format.
     * 
     * @param authors the authors
     * 
     * @return a String with the output for the authors names
     */
    public String formatAuthorNames(Author[] authors) { 
        return null;
    }
}
