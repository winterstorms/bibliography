package edu.kit.informatik.matchthree;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import edu.kit.informatik.matchthree.framework.Position;
import edu.kit.informatik.matchthree.framework.exceptions.BoardDimensionException;
import edu.kit.informatik.matchthree.framework.interfaces.Board;
import edu.kit.informatik.matchthree.framework.interfaces.Game;
import edu.kit.informatik.matchthree.framework.interfaces.Matcher;
import edu.kit.informatik.matchthree.framework.interfaces.Move;

/**
 * Implementation of the game logic.
 * 
 * @author Frithjof Marquardt
 * @version 1.00, 26.02.2017
 */
public class MatchThreeGame implements Game {
    private final Board board;
    private Matcher matcher;
    private int score;
    
    /**
     * Creates new game with the provided board and a matcher for the scoring system.
     * 
     * @param board the board to play on
     * @param matcher the matcher for identifying hits on the board
     */
    public MatchThreeGame(Board board, Matcher matcher) {
        setMatcher(matcher);
        this.board = board;
        score = 0;
    }

    @Override
    public void initializeBoardAndStart() {
        board.moveTokensToBottom();
        board.fillWithTokens();
        Set<Position> affected = new HashSet<Position>();
        for (int i = 0; i < board.getColumnCount(); i++) {
            for (int j = 0; j < board.getRowCount(); j++) {
                affected.add(Position.at(i, j));
            }
        }
        analyseBoard(matcher.matchAll(board, affected));
    }

    @Override
    public void acceptMove(Move move) throws BoardDimensionException {
        move.apply(board);
        analyseBoard(matcher.matchAll(board, move.getAffectedPositions(board)));
    }

    @Override
    public int getScore() {
        return score;
    }
    /**
     * Returns the board.
     * 
     * @return the board.
     */
    public Board getBoard() {
        return board;
    }
    @Override
    public void setMatcher(Matcher matcher) {
        Objects.requireNonNull(matcher);
        this.matcher = matcher;
    }
    
    private void analyseBoard(Set<Set<Position>> matches) {
        Set<Set<Position>> copy = validateMatches(matches);
        Set<Position> affected;
        int factor = 1;
        int row;
        Position pos;
        while (!copy.isEmpty()) {
            calculateScore(copy, factor++);
            affected = board.moveTokensToBottom();
            for (int i = 0; i < board.getColumnCount(); i++) {
                row = 0;
                pos = Position.at(i, row);
                while (board.getTokenAt(pos) == null) {
                    affected.add(pos);
                    pos = Position.at(i, ++row);
                }
            }
            board.fillWithTokens();
            copy = validateMatches(matcher.matchAll(board, affected));
        }
    }
    
    private void calculateScore(Set<Set<Position>> matches, int factor) {
        int sum = 0;
        for (Set<Position> match : matches) {
            sum += 3 + (match.size() - 3) * 2;
            board.removeTokensAt(match);
        }
        score += sum * factor;
    }
    
    private Set<Set<Position>> validateMatches(Set<Set<Position>> matches) {
        Set<Set<Position>> copy = new HashSet<Set<Position>>();
        copy.addAll(matches);
        Set<Set<Position>> remove = new HashSet<Set<Position>>();
        boolean valid;
        for (Set<Position> match : matches) {
            copy.remove(match);
            if (match.size() > 2) {
                valid = true;
                for (Set<Position> other : copy) {
                    if (other.containsAll(match)) valid = false;
                }
                if (valid) copy.add(match);
                else remove.add(match);
            } else remove.add(match);
        }
        matches.removeAll(remove);
        return matches;
    }
}
