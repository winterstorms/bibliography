package edu.kit.informatik.matchthree.tests;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;

import org.junit.Test;

import edu.kit.informatik.matchthree.MatchThreeBoard;
import edu.kit.informatik.matchthree.MatchThreeGame;
import edu.kit.informatik.matchthree.MaximumDeltaMatcher;
import edu.kit.informatik.matchthree.framework.Delta;
import edu.kit.informatik.matchthree.framework.DeterministicStrategy;
import edu.kit.informatik.matchthree.framework.Token;
import edu.kit.informatik.matchthree.framework.interfaces.Board;
import edu.kit.informatik.matchthree.framework.interfaces.Game;
import edu.kit.informatik.matchthree.framework.interfaces.Matcher;

/**
 * An exemplary test for {@link Board}.
 * <p>
 * Tests whether the constructor of {@link MatchThreeBoard} creates an instance
 * that returns correct values for:
 * <ul>
 * <li>{@link Board#getColumnCount()},</li>
 * <li>{@link Board#getRowCount()},</li>
 * <li>{@link Board#getAllValidTokens()}.</li>
 * </ul>
 * 
 * @author IPD Koziolek
 */
public class MatchThreeBoardConstructorTest {
    /**
     * Tests the constructor and getters of {@link Board} on the instance
     * created by {@code new MatchThreeBoard(Token.set("AB"), 2, 3);}.
     */
    @Test
    public void testAB23() {
        Board b = new MatchThreeBoard(Token.set("AB"), 2, 3);
        assertEquals(b.getColumnCount(), 2);
        assertEquals(b.getRowCount(), 3);
        assertEquals(b.getAllValidTokens(), Token.set("BA"));
    }
    
    /**
     * Tests some other things
     */
    public void testPublicOpt() {
        Board b = new MatchThreeBoard(Token.set("AXO*"), "O*O;***;O*O;O*O");
        b.setFillingStrategy(new DeterministicStrategy(
                Token.iterator("AOA**"), Token.iterator("AXAXA"), Token.iterator("A**A*")));
        HashSet<Delta> set = new HashSet<Delta>();
        set.add(Delta.dxy(1, 0));
        set.add(null);
        Matcher m = new MaximumDeltaMatcher(set);
        Game g = new MatchThreeGame(b, m);
        g.initializeBoardAndStart();
        assertEquals(b.toTokenString(), "*A*;*XA;AA*;OX*");
        assertEquals(g.getScore(), 49);
    }
}
