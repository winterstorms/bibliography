package edu.kit.informatik;

import edu.kit.informatik.bibliography.*;

import java.util.NoSuchElementException;
import java.util.regex.Pattern;

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
        try {
            for (C c : commands) {
                if (c.pattern().matcher(string).matches()) {
                    c.apply(target, string);
                    return c;
                }
            }
            if (!string.equals("")) throw new NoSuchElementException("this command doesn't exist.");
        } catch (FalseNumberOfParametersException e) {
            Terminal.printError("the number of parameters for this command is wrong.");
        } catch (IllegalArgumentException e) {
            Terminal.printError(e.getMessage());
        } catch (NoSuchElementException e) {
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
         */
        void apply(T target, String string);
    }
    
    
    
    
    
    
    
    
    
    
    
 /*   
    private static Bibliography bibliography;
    private static boolean isQuit;
    
    /**
     * Starts the program.
     * 
     * @param args arguments of command line.
     */
  /*  public static void main(String[] args) {
        isQuit = false;
        do {
            analyseCommand(Terminal.readLine());
        } while (!isQuit);
    }
    
    /**
     * Checks if provided String is a valid command and calls the according method.
     * 
     * @param command input command
     */
  /*  private static void analyseCommand(String command) {
        String[] commandParts = command.split(" ");
        
        if (commandParts[0].equals("quit")) {
            Terminal.printLine("Ok");
            isQuit = true;
        } else if (commandParts[0].equals("add")) {
            if (commandParts[1].equals("author")) {
                
            }
        } else if (commandParts[0].equals("written-by")) {
            
        } else if (commandParts[0].equals("cites")) {
            
        } else if (commandParts[0].equals("all")) {
            
        } else if (commandParts[0].equals("publications")) {
            
        } else if (commandParts[0].equals("in")) {
            
        } else if (commandParts[0].equals("find")) {
            
        } else if (commandParts[0].equals("jaccard")) {
            
        } else if (commandParts[0].equals("similarity")) {
            
        } else if (commandParts[0].equals("direct")) {
            
        } else if (commandParts[0].equals("h-index")) {
            
        } else if (commandParts[0].equals("coauthors")) {
            
        } else if (commandParts[0].equals("foreign")) {
            
        } else if (commandParts[0].equals("print")) {
            
        } else {
            
        }
    }
    */
}
