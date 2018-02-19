package edu.kit.informatik.matchthree;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import edu.kit.informatik.matchthree.framework.Delta;
import edu.kit.informatik.matchthree.framework.Position;
import edu.kit.informatik.matchthree.framework.Token;
import edu.kit.informatik.matchthree.framework.exceptions.BoardDimensionException;
import edu.kit.informatik.matchthree.framework.exceptions.MatcherInitializationException;
import edu.kit.informatik.matchthree.framework.interfaces.Board;
import edu.kit.informatik.matchthree.framework.interfaces.Matcher;

/**
 * Implementation of a matcher based on valid deltas.
 * 
 * @author Frithjof Marquardt
 * @version 1.00, 26.02.2017
 */
public class MaximumDeltaMatcher implements Matcher {
    private final Set<Delta> deltas;
    /**
     * Creates a new matcher with the provided set of valid deltas.
     * 
     * @param deltas the set of deltas used to find matches
     * @throws MatcherInitializationException if the set of deltas is not valid
     */
    public MaximumDeltaMatcher(Set<Delta> deltas) throws MatcherInitializationException {
        Objects.requireNonNull(deltas);
        if (deltas.size() == 0) throw new MatcherInitializationException("Matcher must have at least one valid delta.");
        Delta invalid = Delta.dxy(0, 0);
        Set<Delta> negatives = new HashSet<Delta>();
        for (Delta d : deltas) {
            if (d == null) throw new MatcherInitializationException("\"Null\" is not a valid delta.");
            if (d.equals(invalid)) 
                throw new MatcherInitializationException(invalid.toString() + " is not a valid delta.");
            negatives.add(d.negate());
        }
        deltas.addAll(negatives);
        this.deltas = deltas;
    }

    @Override
    public Set<Set<Position>> match(Board board, Position initial) throws BoardDimensionException {
        Objects.requireNonNull(board);
        Objects.requireNonNull(initial);
        if (!board.containsPosition(initial)) 
            throw new BoardDimensionException(initial.toString() + "is not on the board.");
        Token token = board.getTokenAt(initial);
        Set<Set<Position>> result = new HashSet<Set<Position>>();
        if (token.equals(null)) return result;
        Set<Position> match = new HashSet<Position>();
        match.add(initial);
        Set<Position> hits;
        do {
            hits = new HashSet<Position>();
            for (Position p : match) {
                for (Delta d : deltas) {
                    Position pos = p.plus(d);
                    if (board.containsPosition(pos) && board.getTokenAt(pos).equals(token)) hits.add(pos);
                }
            }
        } while (match.addAll(hits));
        result.add(match);
        return result;
    }

    @Override
    public Set<Set<Position>> matchAll(Board board, Set<Position> initial) throws BoardDimensionException {
        Objects.requireNonNull(board);
        Objects.requireNonNull(initial);
        for (Position p : initial) {
            if (!board.containsPosition(p)) 
                throw new BoardDimensionException(p.toString() + "is not on the board.");
        }
        Set<Set<Position>> result = new HashSet<Set<Position>>();
        for (Position p : initial) {
            result.addAll(match(board, p));
        }
        return result;
    }
}
