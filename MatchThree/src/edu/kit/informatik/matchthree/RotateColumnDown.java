package edu.kit.informatik.matchthree;

import java.util.Set;
import java.util.TreeSet;

import edu.kit.informatik.matchthree.framework.Position;
import edu.kit.informatik.matchthree.framework.Token;
import edu.kit.informatik.matchthree.framework.exceptions.BoardDimensionException;
import edu.kit.informatik.matchthree.framework.interfaces.Board;
import edu.kit.informatik.matchthree.framework.interfaces.Move;

/**
 * Implements a move that rotates a column down.
 * 
 * @author Frithjof Marquardt
 * @version 1.00, 25.02.2017
 */
public class RotateColumnDown implements Move {
    private final int column;
    
    /**
     * Creates a new move that rotates the provided column down.
     * 
     * @param columnIndex the index of the column to rotate
     */
    public RotateColumnDown(int columnIndex) {
        column = columnIndex;
    }

    @Override
    public boolean canBeApplied(Board board) {
        return (board.getColumnCount() > column) && (column >= 0);
    }

    @Override
    public void apply(Board board) {
        if (!canBeApplied(board)) throw new BoardDimensionException("Move cannot be applied to this board.");
        Token tokenEnd = board.getTokenAt(Position.at(column, board.getRowCount() - 1));
        for (int i = board.getRowCount() - 1; i > 0; i--) {
            board.setTokenAt(Position.at(column, i), board.getTokenAt(Position.at(column, i - 1)));
        }
        board.setTokenAt(Position.at(column, 0), tokenEnd);
    }

    @Override
    public Move reverse() {
        return new RotateColumnUp(column);
    }

    @Override
    public Set<Position> getAffectedPositions(Board board) {
        if (!canBeApplied(board)) throw new BoardDimensionException("Move cannot be applied to this board.");
        TreeSet<Position> affected = new TreeSet<Position>();
        for (int i = 0; i < board.getRowCount(); i++) {
            affected.add(Position.at(column, i));
        }
        return affected;
    }
}