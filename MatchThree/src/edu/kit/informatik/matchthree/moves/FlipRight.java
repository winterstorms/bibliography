package edu.kit.informatik.matchthree.moves;

import java.util.HashSet;
import java.util.Set;

import edu.kit.informatik.matchthree.framework.Position;
import edu.kit.informatik.matchthree.framework.exceptions.BoardDimensionException;
import edu.kit.informatik.matchthree.framework.interfaces.Board;
import edu.kit.informatik.matchthree.framework.interfaces.Move;

/**
 * Implements a move swapping a field and its right neighbor.
 * 
 * @author Frithjof Marquardt
 * @version 1.00, 25.02.2017
 */
public class FlipRight implements Move {
    private final Position pos;
    
    /**
     * Creates a new move that flips the field at position and the field on the right.
     * 
     * @param position the position to flip
     * @throws BoardDimensionException if parameter is not valid for any board
     */
    public FlipRight(Position position) throws BoardDimensionException {
        if (position == null) throw new IllegalArgumentException("Parameters must not be null.");
        if (position.x < 0 || position.y < 0) throw new BoardDimensionException("Move affecting "
                + "a position with negative coordinates can never be applied to any board.");
        pos = position;
    }

    @Override
    public boolean canBeApplied(Board board) {
        if (board == null) throw new IllegalArgumentException("Parameters must not be null.");
        return (board.containsPosition(pos)) && (board.containsPosition(pos.plus(1, 0)));
    }

    @Override
    public void apply(Board board) throws BoardDimensionException {
        if (!canBeApplied(board)) throw new BoardDimensionException("Move cannot be applied to this board.");
        board.swapTokens(pos, pos.plus(1, 0));
    }

    @Override
    public Move reverse() {
        return this;
    }

    @Override
    public Set<Position> getAffectedPositions(Board board) throws BoardDimensionException {
        if (!canBeApplied(board)) throw new BoardDimensionException("Move cannot be applied to this board.");
        Set<Position> affected = new HashSet<Position>();
        affected.add(pos);
        affected.add(pos.plus(1, 0));
        return affected;
    }
}