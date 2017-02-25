package edu.kit.informatik.matchthree;

import java.util.Set;

import edu.kit.informatik.matchthree.framework.Position;
import edu.kit.informatik.matchthree.framework.interfaces.Board;
import edu.kit.informatik.matchthree.framework.interfaces.Move;

public class RotatingSquare implements Move {

    @Override
    public boolean canBeApplied(Board board) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void apply(Board board) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Move reverse() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<Position> getAffectedPositions(Board board) {
        // TODO Auto-generated method stub
        return null;
    }

}
