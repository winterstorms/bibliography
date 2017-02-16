package edu.kit.informatik;

import java.util.regex.Matcher;
import java.util.regex.*;
import java.util.regex.Pattern;
import edu.kit.informatik.bibliography.Bibliography;

/**
 * Implementation of the commands for the task.
 * 
 * @author Frithjof Marquardt
 * @version 1.00, 11.02.2017
 */
public enum Command implements CommandHandling.Command<Bibliography> {
    
    /**
     * Implements the quit command as specified in the task.
     */
    QUIT("quit") {
        @Override
        public void apply(Bibliography bibliography, String string) {
            Terminal.printLine("Ok");
        }
        @Override
        public boolean isQuit() {
            return true;
        }
    },
    
    /**
     * Implements the command to add an author to the bibliography.
     */
//  ADD_AUTHOR("add author <firstname>,<lastname>")  
    ADD_AUTHOR("(add author )(.*)") {
        @Override
        public void apply(Bibliography bibliography, String string) throws FalseNumberOfParametersException, 
            IllegalArgumentException {
            Matcher matcher = pattern().matcher(string);
            matcher.matches();
            String parameters = string.substring(matcher.start(2), matcher.end(2));
            Matcher m = Pattern.compile("([^,]*),([^,]*)").matcher(parameters);
            if (!m.matches()) throw new FalseNumberOfParametersException();
            String firstname = parameters.substring(m.start(1), m.end(1));
            String lastname = parameters.substring(m.start(2), m.end(2));
            if (Tokens.NAME.matcher(firstname).matches() && Tokens.NAME.matcher(lastname).matches()) {
                bibliography.addAuthor(firstname, lastname);
                Terminal.printLine("Ok");
            } else throw new IllegalArgumentException("an author's name must include only letters.");
        }
    };
    
    private final Pattern pattern;
    
    /**
     * Creates new command of the provided format.
     * 
     * @param format the command's format
     */
    Command(String format) {
        pattern = Pattern.compile(format);
    }
    
    /**
     * Starts the program.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        final CommandHandling<Bibliography, Command> h 
            = new CommandHandling<Bibliography, Command>(new Bibliography(), Command.values());
        Command c;
        do {
           c = h.accept(Terminal.readLine());
        } while (c == null || !c.isQuit());
    }
    
    @Override
    public Pattern pattern() {
        return pattern;
    }
    
    @Override
    public void apply(Bibliography bibliography, String string) { }
    
    /**
     * Decides whether the program should be terminated.
     * 
     * @return the boolean value 
     */
    public boolean isQuit() {
        return false;
    }
    
    static final class Tokens {
        /**
         * Pattern for a name of an author.
         */
        static final Pattern NAME = Pattern.compile("[a-zA-Z]+");
        /**
         * Pattern for a keyword.
         */
        static final Pattern WORD = Pattern.compile("[a-z]+");
        /**
         * Pattern for an id of lower case letters and numbers.
         */
        static final Pattern ID = Pattern.compile("[a-z0-9]+");
        /**
         * Pattern for the title of a publication, conference, series, journal or a location.
         */
        static final Pattern TITLE = Pattern.compile("[^,;]+");
        /**
         * Pattern for a year.
         */
        static final Pattern YEAR = Pattern.compile("([1-9])([0-9]{3})");
        /**
         * Pattern for an arbitrary number.
         */
        static final Pattern NUMBER = Pattern.compile("[0-9]+");
        
    }
}
