package edu.kit.informatik;


import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Handles commands for the bibliography.
 * 
 * @author Frithjof Marquardt
 * @version 1.00, 10.02.2017
 */
public class CommandHandling<T, C extends CommandHandling.Command<T>> {
    private T target;
    private C[] commands;
    
    /**
     * Creates new CommandHandling object.
     * 
     * @param target the target system of the commands
     * @param commands the commands available for the target system
     */
    public CommandHandling(T target, C[] commands) {
        this.target = target;
        this.commands = commands;
    }
    
    /**
     * Checks whether provided String matches the pattern of a command 
     * and eventually calls the method to perform the command.
     * 
     * @param string the string
     * 
     * @return the matching command or null
     */
    public C accept(String string) {
        Matcher m;
        try {
            if (string == null) throw new IllegalArgumentException("no command entered.");
            for (C c : commands) {
                m = c.pattern().matcher(string);
                if (m.matches()) {
                    c.apply(target, m.group(2));
                    return c;
                }
            }
            if (!string.trim().equals("")) throw new NoSuchElementException("this command doesn't exist.");
            throw new IllegalArgumentException("no command entered.");
        } catch (NoSuchElementException e) {
            Terminal.printError(e.getMessage());
        } catch (PatternSyntaxException e) {
            Terminal.printError(e.getMessage());
        } catch (IllegalArgumentException e) {
            Terminal.printError(e.getMessage());
        }
        
        return null;
    }
    
    
    public interface Command<T> {
        /**
         * Return the pattern of the command.
         * 
         * @return the pattern
         */
        Pattern pattern();
        
        /**
         * Implement the command's task.
         * 
         * @param target the target system of the command
         * @param string the command
         * 
         * @throws IllegalArgumentException if the string is not valid for this command
         */
        void apply(T target, String string) throws IllegalArgumentException;
        
        /**
         * Matches the input String against the provided pattern and after success returns an array of Strings 
         * with the individual tokens (parameters) of the input.
         * 
         * @param regex the pattern to match
         * @param input the input String
         * 
         * @return the parameters
         * @throws IllegalArgumentException if the number of tokens does not match the expected one
         */
        String[] matchParameters(String regex, String input) throws IllegalArgumentException;
    }
}
