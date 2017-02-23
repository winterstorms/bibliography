package edu.kit.informatik;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.NoSuchElementException;

import edu.kit.informatik.bibliography.Bibliography;
import edu.kit.informatik.bibliography.Publication;
import edu.kit.informatik.bibliography.Author;
import edu.kit.informatik.bibliography.Series;

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
    QUIT("(quit)()") {
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
        public void apply(Bibliography bibliography, String string) throws IllegalArgumentException {
            String[] params = matchParameters("[^,;]*,[^,;]*", string);
            Token.NAME.matchToken(params[0]);
            Token.NAME.matchToken(params[1]);
            bibliography.addAuthor(params[0], params[1]);
            Terminal.printLine("Ok");
        }
    },
    /**
     * Implements the command to add a journal to the bibliography.
     */
//  ADD_JOURNAL("add journal <name>,<publisher>")
    ADD_JOURNAL("(add journal )(.*)") {
        @Override
        public void apply(Bibliography bibliography, String string) throws IllegalArgumentException {
            String[] params = matchParameters("[^,;]*,[^,;]*", string);
            Token.TITLE.matchToken(params[0]);
            Token.TITLE.matchToken(params[1]);
            bibliography.addJournal(params[0], params[1]);
            Terminal.printLine("Ok");
        }
    },
    /**
     * Implements the command to add a conference series to the bibliography.
     */
//  ADD_SERIES("add conference series <name>")
    ADD_SERIES("(add conference series )(.*)") {
        @Override
        public void apply(Bibliography bibliography, String string) throws IllegalArgumentException {
            String[] params = matchParameters("[^,;]*", string);
            Token.TITLE.matchToken(params[0]);
            bibliography.addSeries(params[0]);
            Terminal.printLine("Ok");
        }
    },
    /**
     * Implements the command to add a conference to a series in the bibliography.
     */
//  ADD_CONFERENCE("add conference <series>,<year>,<location>")
    ADD_CONFERENCE("(add conference )(.*)") {
        @Override
        public void apply(Bibliography bibliography, String string) throws IllegalArgumentException, 
            NoSuchElementException {
            String[] params = matchParameters("[^,;]*,[^,;]*,[^,;]*", string);
            Token.TITLE.matchToken(params[0]);
            Token.YEAR.matchToken(params[1]);
            Token.TITLE.matchToken(params[2]);
            bibliography.addConference(params[0], Integer.parseInt(params[1]), params[2]);
            Terminal.printLine("Ok");
        }
    },
    /**
     * Implements the command to add an article to an entity.
     */
//  ADD_ARTICLE("add article to <series/journal>:<id>,<year>,<title>")
    ADD_ARTICLE("(add article to )(.*)") {
        @Override
        public void apply(Bibliography bibliography, String string) throws IllegalArgumentException,
            NoSuchElementException {
            Matcher matcher = Pattern.compile("([^,;]*):(.*)").matcher(string);
            if (!matcher.matches()) throw new IllegalArgumentException("parameters falsely delimited.");
            String rest = matcher.group(2);
            matcher = Pattern.compile("([^,;]*?) ([^,;]*)").matcher(matcher.group(1));
            if (!matcher.matches()) throw new IllegalArgumentException("entity has wrong format.");
            String[] params = matchParameters("[^,;]*,[^,;]*,[^,;]*", rest);
            Token.TITLE.matchToken(matcher.group(2));
            Token.ID.matchToken(params[0]);
            Token.YEAR.matchToken(params[1]);
            Token.TITLE.matchToken(params[2]);
            bibliography.addArticle(matcher.group(1), matcher.group(2), params[0],
                    Integer.parseInt(params[1]), params[2]);
            Terminal.printLine("Ok");
        }
    },
    /**
     * Implements the command to add authors to a publication.
     */
//  WRITTEN_BY("written-by <publication>,<list of author names>")
    WRITTEN_BY("(written-by )(.*)") {
        @Override
        public void apply(Bibliography bibliography, String string) throws IllegalArgumentException,
            NoSuchElementException {
            String[] params = matchParameters("[^,;]*,([^,;]*)((;[^,;]*)*)", string);
            String[] names = new String[params.length - 1];
            Token.TITLE.matchToken(params[0]);
            for (int i = 1; i < params.length; i++) {
                Token.FULLNAME.matchToken(params[i]);
                names[i - 1] = params[i];
            }
            bibliography.writtenBy(params[0], names);
            Terminal.printLine("Ok");
        }
    },
    /**
     * Implements the command to add a publication to another publication's citations.
     */
//  CITES("cites <publication 1>,<publication 2>")
    CITES("(cites )(.*)") {
        @Override
        public void apply(Bibliography bibliography, String string) throws IllegalArgumentException,
            NoSuchElementException {
            String[] params = matchParameters("[^,;]*,[^,;]*", string);
            Token.ID.matchToken(params[0]);
            Token.ID.matchToken(params[1]);
            bibliography.cites(params[0], params[1]);
            Terminal.printLine("Ok");
        }
    },
    /**
     * Implements the command to add keywords to an entity.
     */
//  ADD_KEYWORDS("add keywords to <entity>:<list of keywords>")
    ADD_KEYWORDS("(add keywords to )(.*)") {
        @Override
        public void apply(Bibliography bibliography, String string) throws IllegalArgumentException,
            NoSuchElementException {
            Matcher matcher = Pattern.compile("([^;,]*) ([^;]*):(.*)").matcher(string);
            if (!matcher.matches()) throw new IllegalArgumentException("parameters are falsely delimited.");
            String entity = matcher.group(1);
            String entityId = matcher.group(2);
            String[] params = matchParameters("([^,;]*)((;[^,;]*)*)", matcher.group(3));
            TreeSet<String> words = new TreeSet<String>();
            int size = params.length;
            for (int i = 0; i < size; i++) {
                Token.WORD.matchToken(params[i]);
                if (!params[i].isEmpty()) words.add(params[i]);
            }
            matcher = Pattern.compile("(([^,]*)(,(.*))?)").matcher(entityId);
            if (!matcher.matches()) throw new IllegalArgumentException("entity has wrong format.");
            Token.TITLE.matchToken(matcher.group(2));
            if (matcher.group(4) != null) Token.YEAR.matchToken(matcher.group(4));
            bibliography.addKeywords(entity, matcher.group(1), words);
            Terminal.printLine("Ok");
        }
    },
    /**
     * Implements the command to list all publications in the bibliography.
     */
    ALL_PUBLICATIONS("(all publications)()") {
        @Override
        public void apply(Bibliography bibliography, String string) throws IllegalArgumentException {
            bibliography.printPublications(bibliography.getPublications());
        }
    },
    /**
     * Implements the command to list all invalid publications in the bibliography.
     */
    INVALID_PUBLICATIONS("(list invalid publications)()") {
        @Override
        public void apply(Bibliography bibliography, String string) throws IllegalArgumentException {
            ArrayList<Publication> pubs = new ArrayList<Publication>();
            for (Publication a : bibliography.getPublications()) {
                if (a.getAuthors().isEmpty()) pubs.add(a);
            }
            bibliography.printPublications(pubs);
        }
    },
    /**
     * Implements the command to list all publications written by the provided author.
     */
//  PUB_BY_AUTHOR("publications by <list of authors>")
    PUB_BY_AUTHOR("(publications by )(.*)") {
        @Override
        public void apply(Bibliography bibliography, String string) throws IllegalArgumentException,
            NoSuchElementException {
            String[] params = matchParameters("[^,;]*((;[^,;]*)*)", string);
            TreeSet<Publication> pubs = new TreeSet<Publication>();
            Author author;
            for (String name : params) {
                Token.FULLNAME.matchToken(name);
                author = bibliography.findAuthor(name);
                pubs.addAll(author.getPublications());
            }
            bibliography.printPublications(pubs);
        }
    },
    /**
     * Implements the command to list all articles of a conference.
     */
//  IN_PROCEEDINGS("in proceedings <series>,<year>")
    IN_PROCEEDINGS("(in proceedings )(.*)") {
        @Override
        public void apply(Bibliography bibliography, String string) throws IllegalArgumentException, 
            NoSuchElementException {
            String[] params = matchParameters("[^,;]*,[^,;]*", string);
            Series ser = ((Series) bibliography.findEntity("series", params[0]));
            bibliography.printPublications(ser.findConference(Integer.parseInt(params[1])).getPublications());
        }
    },
    /**
     * Implements the command to find publications with the provided keywords.
     */
//  FIND_KEYWORDS("find keywords <list of keywords>")
    FIND_KEYWORDS("(find keywords )(.*)") {
        @Override
        public void apply(Bibliography bibliography, String string) throws IllegalArgumentException {
            String[] params = matchParameters("[^,;]*((;[^,;]*)*)", string);
            ArrayList<String> words = new ArrayList<String>();
            int size = params.length;
            for (int i = 0; i < size; i++) {
                Token.WORD.matchToken(params[i]);
                if (!params[i].isEmpty()) words.add(params[i]);
            }
            bibliography.findKeywords(words);
        }
    },
    /**
     * Implements the command to calculate the jaccard index of two sets.
     */
//  JACCARD("jaccard <list of words 1> <list of words 2>")
    JACCARD("(jaccard )(.*)") {
        @Override
        public void apply(Bibliography bibliography, String string) throws IllegalArgumentException {
            Matcher matcher = Pattern.compile("(.*) (.*)").matcher(string);
            if (!matcher.matches()) throw new IllegalArgumentException("this command needs exactly two sets as "
                    + "parameters to work");
            String[] set = matchParameters("[^,;]*((;[^,;]*)*)", string.substring(matcher.start(1), matcher.end(1)));
            TreeSet<String> words = new TreeSet<String>();
            int size = set.length;
            for (int i = 0; i < size; i++) {
                Token.WORD.matchToken(set[i]);
                words.add(set[i]);
            }
            String[] otherSet = matchParameters("[^,;]*((;[^,;]*)*)", 
                    string.substring(matcher.start(2), matcher.end(2)));
            TreeSet<String> otherWords = new TreeSet<String>();
            size = otherSet.length;
            for (int i = 0; i < size; i++) {
                Token.WORD.matchToken(otherSet[i]);
                otherWords.add(otherSet[i]);
            }
            Terminal.printLine(bibliography.jaccard(words, otherWords));
        }
    },
    /**
     * Implements the command calculate the similarity of two provided publications.
     */
//  SIMILARITY("similarity <publication 1>,<publiction 2>")
    SIMILARITY("(similarity )(.*)") {
        @Override
        public void apply(Bibliography bibliography, String string) throws IllegalArgumentException,
            NoSuchElementException {
            String[] params = matchParameters("[^,;]*,[^,;]*", string);
            Terminal.printLine(bibliography.similarity(params[0], params[1]));
        }
    },
    /**
     * Implements the command to determine the hIndex of the provided numbers.
     */
//  DIR_HINDEX("direct h-index <list of numbers>")
    DIR_HINDEX("(direct h-index )(.*)") {
        @Override
        public void apply(Bibliography bibliography, String string) throws IllegalArgumentException {
            String[] params = matchParameters("[^,;]*((;[^,;]*)*)", string);
            int size = params.length;
            int[] numbers = new int[size];
            for (int i = 0; i < size; i++) {
                Token.NUMBER.matchToken(params[i]);
                try {
                    numbers[i] = Integer.parseInt(params[i]);
                } catch (NumberFormatException e) {
                    Terminal.printError("one of the numbers exceeds the limits of \"Integer\"");
                }
            }
            Terminal.printLine(bibliography.hIndex(numbers));
        }
    },
    /**
     * Implements the command to determine the hIndex of the provided author.
     */
//  HINDEX("h-index <firstname> <lastname>")
    HINDEX("(h-index )(.*)") {
        @Override
        public void apply(Bibliography bibliography, String string) throws IllegalArgumentException,
            NoSuchElementException {
            String[] params = matchParameters("[^,;]*", string);
            Token.FULLNAME.matchToken(params[0]);
            Terminal.printLine(bibliography.hIndexByAuthor(params[0]));
        }
    },
    /**
     * Implements the command to list the coauthors of the provided author.
     */
//  COAUTHIORS("coauthors of <firstname> <lastname>")
    COAUTHORS("(coauthors of )(.*)") {
        @Override
        public void apply(Bibliography bibliography, String string) throws IllegalArgumentException,
            NoSuchElementException {
            String[] params = matchParameters("[^,;]*", string);
            Token.FULLNAME.matchToken(params[0]);
            bibliography.printCoauthors(params[0]);
        }
    },
    /**
     * Implements the command to list the foreign citations of the provided author.
     */
//  FOREIGN_CIT("foreign citations of <firstname> <lastname>")
    FOREIGN_CIT("(foreign citations of )(.*)") {
        @Override
        public void apply(Bibliography bibliography, String string) throws IllegalArgumentException, 
            NoSuchElementException {
            String[] params = matchParameters("[^,;]*", string);
            Token.FULLNAME.matchToken(params[0]);
            bibliography.printForeignCitations(params[0]);
        }
    },
    /**
     * Implements the command to directly print a conference article.
     */
//  PRINT_CONF("direct print conference <style>:<author 1>,<author 2>,<author 3>,<title>,←
//      <conference series name>,<location>,<year>")
    PRINT_CONF("(direct print conference )(.*)") {
        @Override
        public void apply(Bibliography bibliography, String string) throws IllegalArgumentException {
            Matcher matcher = Pattern.compile("([^,;:]*):(.*)").matcher(string);
            if (!matcher.matches()) throw new IllegalArgumentException("parameters falsely delimited.");
            String style = matcher.group(1);
            String[] params = matchParameters("[^,;]*,[^,;]*,[^,;]*,[^,;]*,[^,;]*,[^,;]*,[^,;]*", matcher.group(2));
            ArrayList<Author> authors = new ArrayList<Author>();
            Token.FULLNAME.matchToken(params[0]);
            authors.add(new Author(params[0]));
            if (!params[1].isEmpty()) {
                Token.FULLNAME.matchToken(params[1]);
                authors.add(new Author(params[1]));
            }
            if (!params[2].isEmpty()) {
                Token.FULLNAME.matchToken(params[2]);
                authors.add(new Author(params[2]));
            }
            Token.TITLE.matchToken(params[3]);
            Token.TITLE.matchToken(params[4]);
            Token.TITLE.matchToken(params[5]);
            Token.YEAR.matchToken(params[6]);
            bibliography.printConference(style, 1, authors, params[3], params[4], params[5],
                    Integer.parseInt(params[6]));
        }
    },
    /**
     * Implements the command to directly print a journal article.
     */
//  PRINT_JOURNAL("direct print journal <style>:<author 1>,<author 2>,<author 3>,<title>,<journal name>,<year>")
    PRINT_JOURNAL("(direct print journal )(.*)") {
        @Override
        public void apply(Bibliography bibliography, String string) throws IllegalArgumentException {
            Matcher matcher = Pattern.compile("([^,;:]*):(.*)").matcher(string);
            if (!matcher.matches()) throw new IllegalArgumentException("parameters falsely delimited.");
            String style = matcher.group(1);
            String[] params = matchParameters("[^,;]*,[^,;]*,[^,;]*,[^,;]*,[^,;]*,[^,;]*", matcher.group(2));
            ArrayList<Author> authors = new ArrayList<Author>();
            Token.FULLNAME.matchToken(params[0]);
            authors.add(new Author(params[0]));
            if (!params[1].isEmpty()) {
                Token.FULLNAME.matchToken(params[1]);
                authors.add(new Author(params[1]));
            }
            if (!params[2].isEmpty()) {
                Token.FULLNAME.matchToken(params[2]);
                authors.add(new Author(params[2]));
            }
            Token.TITLE.matchToken(params[3]);
            Token.TITLE.matchToken(params[4]);
            Token.TITLE.matchToken(params[5]);
            bibliography.printJournal(style, 1, authors, params[3], params[4], Integer.parseInt(params[5]));
        }
    },
    /**
     * Implements the command to print the provided publications as a bibliography.
     */
//  PRINT_BIB("print bibliography <style>:<list of publications>")
    PRINT_BIB("(print bibliography )(.*)") {
        @Override
        public void apply(Bibliography bibliography, String string) throws IllegalArgumentException {
            Matcher matcher = Pattern.compile("([^,;:]*):(.*)").matcher(string);
            if (!matcher.matches()) throw new IllegalArgumentException("parameters falsely delimited.");
            String style = matcher.group(1);
            String[] params = matchParameters("([^,;]*)((;[^,;]*)*)", matcher.group(2));
            TreeSet<String> pubIds = new TreeSet<String>();
            for (int i = 0; i < params.length; i++) {
                Token.ID.matchToken(params[i]);
                pubIds.add(params[i]);
            }
            bibliography.printBib(style, pubIds);
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
        try {
            InputStream in = new FileInputStream(new File("/Users/Frithjof/Documents/workspacejava/"
                    + "LiteratureAdministration/src/edu/kit/informatik/testfile.txt"));
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            
            do {
               String input = reader.readLine();
               Terminal.printLine(input);
               c = h.accept(input);
            } while (c == null || !c.isQuit());
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        /*
        do {
            c = h.accept(Terminal.readLine());
         } while (c == null || !c.isQuit());
         */
    }
    
    @Override
    public Pattern pattern() {
        return pattern;
    }
    
    @Override
    public void apply(Bibliography bibliography, String string) { }
    
    @Override
    public String[] matchParameters(String regex, String input) throws IllegalArgumentException {
        Matcher m = Pattern.compile(regex).matcher(input);
        if (!m.matches()) 
            throw new IllegalArgumentException("wrong number of parameters or they are falsely delimited.");
        return input.split("[,;]", -1);
    }
    
    /**
     * Decides whether the program should be terminated.
     * 
     * @return the boolean value 
     */
    public boolean isQuit() {
        return false;
    }
}