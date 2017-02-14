package edu.kit.informatik;

import java.util.regex.Matcher;
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
    ADD_AUTHOR("(add author )(*)") {
        @Override
        public void apply(Bibliography bibliography, String string) throws FalseNumberOfParametersException {
            Matcher m = pattern().matcher(string);
            if (m.matches()) {
                bibliography.addAuthor(string.substring(m.start(1), m.end(1)), string.substring(m.start(2), m.end(2)));
            }
            Terminal.printLine("Ok");
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
        final CommandHandling<Bibliography, Command> h = new CommandHandling(new Bibliography(), Command.values());
        Command c;
        
        do {
           c = h.accept(Terminal.readLine());
        } while (c == null || !c.isQuit());
    }
    
    
    /**
     * Returns the command's pattern.
     * 
     * @return the pattern
     */
    @Override
    public Pattern pattern() {
        return pattern;
    }
    
    /**
     * Implements the task of this command for the bibliography.
     * 
     * @param bibliography the bibliography
     * @param string String which contains the parameters of the command
     */
  /*  @Override
    public void apply(Bibliograohy bibliography, String string) { }
    */
    /**
     * Decides whether the program should be terminated.
     * 
     * @return the boolean value 
     */
    public boolean isQuit() {
        return false;
    }
}
