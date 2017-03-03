package edu.kit.informatik.matchthree.moves;

import java.util.HashSet;
import java.util.Set;

import edu.kit.informatik.matchthree.framework.Position;
import edu.kit.informatik.matchthree.framework.Token;
import edu.kit.informatik.matchthree.framework.exceptions.BoardDimensionException;
import edu.kit.informatik.matchthree.framework.interfaces.Board;
import edu.kit.informatik.matchthree.framework.interfaces.Move;

/**
 * Implements a move rotating the square of fields with the position at its upper left corner counterclockwise.
 * 
 * @author Frithjof Marquardt
 * @version 1.00, 25.02.2017
 */
public class RotateSquareCounterClockwise implements Move {
    private final Position pos;
    
    /**
     * Creates a new move that rotates a square of fields counterclockwise.
     * 
     * @param position the position of the upper left corner of the square
     * @throws BoardDimensionException if parameter is not valid for any board
     */
    public RotateSquareCounterClockwise(Position position) {
        if (position.x < 0 || position.y < 0) throw new BoardDimensionException("Move affecting "
                + "a position with negative coordinates can never be applied to any board.");
        pos = position;
    }

    @Override
    public boolean canBeApplied(Board board) {
        return (board.containsPosition(pos)) && (board.containsPosition(pos.plus(1, 1)));
    }

    @Override
    public void apply(Board board) {
        if (!canBeApplied(board)) throw new BoardDimensionException("Move cannot be applied to this board.");
        Token tokenA = board.getTokenAt(pos);
        board.setTokenAt(pos, board.getTokenAt(pos.plus(1, 0)));
        board.setTokenAt(pos.plus(1, 0), board.getTokenAt(pos.plus(1, 1)));
        board.setTokenAt(pos.plus(1, 1), board.getTokenAt(pos.plus(0, 1)));
        board.setTokenAt(pos.plus(0, 1), tokenA);
    }

    @Override
    public Move reverse() {
        return new RotateSquareClockwise(pos);
    }

    @Override
    public Set<Position> getAffectedPositions(Board board) {
        if (!canBeApplied(board)) throw new BoardDimensionException("Move cannot be applied to this board.");
        Set<Position> affected = new HashSet<Position>();
        affected.add(pos);
        affected.add(pos.plus(1, 0));
        affected.add(pos.plus(1, 1));
        affected.add(pos.plus(0, 1));
        return affected;
    }
}