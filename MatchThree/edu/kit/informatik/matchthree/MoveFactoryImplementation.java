package edu.kit.informatik.matchthree;

import edu.kit.informatik.matchthree.framework.Position;
import edu.kit.informatik.matchthree.framework.interfaces.Move;
import edu.kit.informatik.matchthree.framework.interfaces.MoveFactory;
import edu.kit.informatik.matchthree.moves.FlipDown;
import edu.kit.informatik.matchthree.moves.FlipRight;
import edu.kit.informatik.matchthree.moves.RotateColumnDown;
import edu.kit.informatik.matchthree.moves.RotateRowRight;
import edu.kit.informatik.matchthree.moves.RotateSquareClockwise;

/**
 * Implementation of a move factory.
 * 
 * @author Frithjof Marquardt
 * @version 1.00, 25.02.2017
 */
public class MoveFactoryImplementation implements MoveFactory {
    /**
     * Empty constructor that creates a new MoveFactory.
     */
    public MoveFactoryImplementation() { }

    @Override
    public Move flipRight(Position position) {
        return new FlipRight(position);
    }

    @Override
    public Move flipDown(Position position) {
        return new FlipDown(position);
    }

    @Override
    public Move rotateSquareClockwise(Position position) {
        return new RotateSquareClockwise(position);
    }

    @Override
    public Move rotateColumnDown(int columnIndex) {
        return new RotateColumnDown(columnIndex);
    }

    @Override
    public Move rotateRowRight(int rowIndex) {
        return new RotateRowRight(rowIndex);
    }
}