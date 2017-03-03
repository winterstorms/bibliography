package edu.kit.informatik.matchthree.moves;

import java.util.HashSet;
import java.util.Set;

import edu.kit.informatik.matchthree.framework.Position;
import edu.kit.informatik.matchthree.framework.Token;
import edu.kit.informatik.matchthree.framework.exceptions.BoardDimensionException;
import edu.kit.informatik.matchthree.framework.interfaces.Board;
import edu.kit.informatik.matchthree.framework.interfaces.Move;

/**
 * Implements a move that rotates a column up.
 * 
 * @author Frithjof Marquardt
 * @version 1.00, 25.02.2017
 */
public class RotateColumnUp implements Move {
    private final int column;
    
    /**
     * Creates a new move that rotates the provided column up.
     * 
     * @param columnIndex the index of the column to rotate
     * @throws BoardDimensionException if parameter is not valid for any board
     */
    public RotateColumnUp(int columnIndex) {
        if (columnIndex < 0) 
            throw new BoardDimensionException("Columns with a negative index do not exist on any board.");
        column = columnIndex;
    }

    @Override
    public boolean canBeApplied(Board board) {
        return (board.getColumnCount() > column);
    }

    @Override
    public void apply(Board board) {
        if (!canBeApplied(board)) throw new BoardDimensionException("Move cannot be applied to this board.");
        Token tokenA = board.getTokenAt(Position.at(column, 0));
        for (int i = 0; i < board.getRowCount() - 1; i++) {
            board.setTokenAt(Position.at(column, i), board.getTokenAt(Position.at(column, i + 1)));
        }
        board.setTokenAt(Position.at(column, board.getRowCount() - 1), tokenA);
    }

    @Override
    public Move reverse() {
        return new RotateColumnDown(column);
    }

    @Override
    public Set<Position> getAffectedPositions(Board board) {
        if (!canBeApplied(board)) throw new BoardDimensionException("Move cannot be applied to this board.");
        Set<Position> affected = new HashSet<Position>();
        for (int i = 0; i < board.getRowCount(); i++) {
            affected.add(Position.at(column, i));
        }
        return affected;
    }
}