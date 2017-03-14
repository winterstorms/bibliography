package edu.kit.informatik.matchthree;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import edu.kit.informatik.matchthree.framework.FillingStrategy;
import edu.kit.informatik.matchthree.framework.Position;
import edu.kit.informatik.matchthree.framework.Token;
import edu.kit.informatik.matchthree.framework.exceptions.BoardDimensionException;
import edu.kit.informatik.matchthree.framework.exceptions.IllegalTokenException;
import edu.kit.informatik.matchthree.framework.exceptions.NoFillingStrategyException;
import edu.kit.informatik.matchthree.framework.exceptions.TokenStringParseException;
import edu.kit.informatik.matchthree.framework.interfaces.Board;

/**
 * Implements the board for a match three game.
 * 
 * @author Frithjof Marquardt
 * @version 1.00, 01.03.2017
 */
public class MatchThreeBoard implements Board {
    private final Set<Token> tokens;
    private char[][] state;
    private FillingStrategy fillStrategy;
    
    /**
     * Creates a new rectangular board with the provided number of columns and rows and a set of valid tokens.
     * The board is initialized as an empty board.
     * 
     * @param tokens the set of tokens
     * @param columnCount the number of columns
     * @param rowCount the number of rows
     * @throws BoardDimensionException if board is smaller than 2x2
     */
    public MatchThreeBoard(Set<Token> tokens, int columnCount, int rowCount) throws BoardDimensionException {
        if (columnCount < 2 || rowCount < 2) 
            throw new BoardDimensionException("Board needs to have at least two columns and two rows.");
        if (tokens.size() < 2) throw new IllegalArgumentException("Board needs to have at least two valid tokens.");
        this.tokens = tokens;
        state = new char[rowCount][columnCount];
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                state[i][j] = ' ';
            }
        }
        fillStrategy = null;
    }

    /**
     * Creates a new board with the provided set of valid tokens.
     * It is initialized with a state matching the tokenString.
     * 
     * @param tokens the set of valid tokens
     * @param tokenString the String specifying the content of each field of the board
     * @throws TokenStringParseException 
     *      if the tokenString does not match the format for a board or contains an invalid token
     */
    public MatchThreeBoard(Set<Token> tokens, String tokenString) throws TokenStringParseException {
        if (tokens.size() < 2) throw new IllegalArgumentException("Board needs to have at least two valid tokens.");
        String[] rowsString = tokenString.split(";");
        int rowCount = rowsString.length;
        if (rowCount < 2) throw new BoardDimensionException("Board needs to have at least two rows.");
        int rowLength = rowsString[0].length();
        if (rowLength < 2) throw new BoardDimensionException("Board needs to have at least two columns.");
        for (int i = 0; i < rowCount; i++) {
            if (rowsString[i].length() != rowLength) 
                throw new TokenStringParseException("All rows of the board must have the same length.");
        }
        this.tokens = tokens;
        state = new char[rowCount][rowLength];
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[0].length; j++) {
                char currentChar = rowsString[i].charAt(j);
                if (currentChar != ' ' && !tokens.contains(new Token(currentChar))) 
                    throw new TokenStringParseException("One of the tokens in the tokenString is not valid.");
                state[i][j] = currentChar;
            }
        }
        fillStrategy = null;
    }

    @Override
    public Set<Token> getAllValidTokens() {
        return tokens;
    }

    @Override
    public int getColumnCount() {
        return state[0].length;
    }

    @Override
    public int getRowCount() {
        return state.length;
    }

    @Override
    public Token getTokenAt(Position position) throws BoardDimensionException {
        if (!containsPosition(position)) 
            throw new BoardDimensionException(position.toString() + " is not on the board.");
        if (state[position.y][position.x] != ' ') return new Token(state[position.y][position.x]);
        return null;
    }

    @Override
    public void setTokenAt(Position position, Token newToken) throws BoardDimensionException, IllegalTokenException {
        if (!containsPosition(position)) 
            throw new BoardDimensionException(position.toString() + " is not on the board.");
        if (newToken == null) {
            state[position.y][position.x] = ' ';
            return;
        }
        if (!tokens.contains(newToken)) throw new IllegalTokenException("Token is not valid.");
        else state[position.y][position.x] = newToken.toString().charAt(0);
    }

    @Override
    public boolean containsPosition(Position position) {
        return (position.x >= 0 && position.x < getColumnCount() && position.y >= 0 && position.y < getRowCount());
    }

    @Override
    public Set<Position> moveTokensToBottom() {
        Set<Position> changes = new HashSet<Position>();
        Set<Position> empty = new HashSet<Position>();
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                if (state[i][j] == ' ') empty.add(Position.at(j, i));
            }
        }
        for (int i = getRowCount() - 1; i > 0; i--) {
            for (int j = 0; j < getColumnCount(); j++) {
                if (state[i][j] == ' ') {
                    for (int k = i; k > 0; k--) {
                        state[k][j] = state[k - 1][j];
                        changes.add(Position.at(j, k));
                    }
                    state[0][j] = ' ';
                    changes.add(Position.at(j, 0));
                }
            }
        }
        for (Position pos : empty) {
            if (getTokenAt(pos) == null) changes.remove(pos);
        }
        return changes;
    }

    @Override
    public void swapTokens(Position positionA, Position positionB) throws BoardDimensionException {
        Token currentA = getTokenAt(positionA);
        setTokenAt(positionA, getTokenAt(positionB));
        setTokenAt(positionB, currentA);
    }

    @Override
    public void removeTokensAt(Set<Position> positions) throws BoardDimensionException {
        for (Position pos : positions) {
            if (!containsPosition(pos)) throw new BoardDimensionException(pos.toString() + " is not on the board.");
        }
        for (Position pos : positions) setTokenAt(pos, null);
    }

    @Override
    public void setFillingStrategy(FillingStrategy strategy) {
        Objects.requireNonNull(strategy, "Filling strategy must not be null.");
        fillStrategy = strategy;
    }

    @Override
    public void fillWithTokens() throws NoFillingStrategyException {
        if (fillStrategy != null) fillStrategy.fill(this);
        else throw new NoFillingStrategyException();
    }

    @Override
    public String toTokenString() {
        String result = "";
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                result += state[i][j];
            }
            if (i < getRowCount() - 1) result += ";";
        }
        return result;
    }
}
