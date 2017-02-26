package edu.kit.informatik.matchthree.moves;

import java.util.HashSet;
import java.util.Set;

import edu.kit.informatik.matchthree.framework.Position;
import edu.kit.informatik.matchthree.framework.Token;
import edu.kit.informatik.matchthree.framework.exceptions.BoardDimensionException;
import edu.kit.informatik.matchthree.framework.interfaces.Board;
import edu.kit.informatik.matchthree.framework.interfaces.Move;

/**
 * Implements a move swapping a field and the field below.
 * 
 * @author Frithjof Marquardt
 * @version 1.00, 25.02.2017
 */
public class FlipDown implements Move {
    private final Position pos;
    
    /**
     * Creates a new move that flips the field at position and the field below.
     * 
     * @param position the position to flip
     */
    public FlipDown(Position position) {
        pos = position;
    }

    @Override
    public boolean canBeApplied(Board board) {
        return (board.containsPosition(pos)) && (board.containsPosition(pos.plus(0, 1)));
    }

    @Override
    public void apply(Board board) {
        if (!canBeApplied(board)) throw new BoardDimensionException("Move cannot be applied to this board.");
        Token tokenA = board.getTokenAt(pos);
        board.setTokenAt(pos, board.getTokenAt(pos.plus(0, 1)));
        board.setTokenAt(pos.plus(0, 1), tokenA);

    }

    @Override
    public Move reverse() {
        return this;
    }

    @Override
    public Set<Position> getAffectedPositions(Board board) {
        if (!canBeApplied(board)) throw new BoardDimensionException("Move cannot be applied to this board.");
        Set<Position> affected = new HashSet<Position>();
        affected.add(pos);
        affected.add(pos.plus(0, 1));
        return affected;
    }
}