package edu.kit.informatik.matchthree;

import java.util.Set;
import java.util.TreeSet;

import edu.kit.informatik.matchthree.framework.Position;
import edu.kit.informatik.matchthree.framework.Token;
import edu.kit.informatik.matchthree.framework.exceptions.BoardDimensionException;
import edu.kit.informatik.matchthree.framework.interfaces.Board;
import edu.kit.informatik.matchthree.framework.interfaces.Move;

/**
 * Implements a move that rotates a row right.
 * 
 * @author Frithjof Marquardt
 * @version 1.00, 25.02.2017
 */
public class RotateRowRight implements Move {
    private final int row;
    
    /**
     * Creates a new move that rotates the provided row right.
     * 
     * @param rowIndex the index of the row to rotate
     */
    public RotateRowRight(int rowIndex) {
        row = rowIndex;
    }

    @Override
    public boolean canBeApplied(Board board) {
        return (board.getRowCount() > row) && (row >= 0);
    }

    @Override
    public void apply(Board board) {
        if (!canBeApplied(board)) throw new BoardDimensionException("Move cannot be applied to this board.");
        Token tokenEnd = board.getTokenAt(Position.at(board.getColumnCount() - 1, row));
        for (int i = board.getColumnCount() - 1; i > 0; i--) {
            board.setTokenAt(Position.at(i, row), board.getTokenAt(Position.at(i - 1, row)));
        }
        board.setTokenAt(Position.at(0, row), tokenEnd);
    }

    @Override
    public Move reverse() {
        return new RotateRowLeft(row);
    }

    @Override
    public Set<Position> getAffectedPositions(Board board) {
        if (!canBeApplied(board)) throw new BoardDimensionException("Move cannot be applied to this board.");
        TreeSet<Position> affected = new TreeSet<Position>();
        for (int i = 0; i < board.getColumnCount(); i++) {
            affected.add(Position.at(i, row));
        }
        return affected;
    }
}