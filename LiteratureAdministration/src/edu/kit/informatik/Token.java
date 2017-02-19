package edu.kit.informatik;

import java.util.regex.Pattern;

public enum Token {
    /**
     * Pattern for a name of an author.
     */
    NAME("[a-zA-Z]+", "the name of a person must consist only of letters, but at least one."),
    /**
     * Pattern for the full name of a person.
     */
    FULLNAME("[a-zA-Z]+ [a-zA-Z]+", "the name of a person must consist only of letters, but at least one."),
    /**
     * Pattern for a keyword.
     */
    WORD("[a-z]+", "a keyword must consist only of lower case letters, but at least one."),
    /**
     * Pattern for an id of lower case letters and numbers.
     */
    ID("[a-z0-9]+", "an id must consist only of lower case letters and digits, but at least one."),
    /**
     * Pattern for the name of a publication, conference, series, journal, publisher or a location.
     */
    TITLE("[^,;]+", "the name of a venue, publication or publisher must not include a \", or ;\"."),
    /**
     * Pattern for a year.
     */
    YEAR("[1-9]\\d{3}", "the year has to be between 1000 and 9999"),
    /**
     * Pattern for an arbitrary number.
     */
    NUMBER("\\d+", "a number has to be a positive integer.");
    
    private final Pattern pattern;
    private final String message;
    
    /**
     * Creates a new Token with the provided pattern and message for exception handling.
     * 
     * @param regex the String for the pattern
     * @param msg the error message
     */
    Token(String regex, String msg) {
        pattern = Pattern.compile(regex);
        message = msg;
    }
    
    private Pattern pattern() {
        return pattern;
    }
    
    private String getMessage() {
        return message;
    }
    
    /**
     * Matches the input String against this token's pattern.
     * 
     * @param input the String
     * 
     * @throws IllegalArgumentException if they are not matching
     */
    public void matchToken(String input) throws IllegalArgumentException {
        if (!pattern().matcher(input).matches()) throw new IllegalArgumentException(getMessage());
    }
}