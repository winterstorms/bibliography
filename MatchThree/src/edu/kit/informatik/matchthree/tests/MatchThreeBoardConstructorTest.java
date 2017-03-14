package edu.kit.informatik.matchthree.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.kit.informatik.matchthree.MatchThreeBoard;
import edu.kit.informatik.matchthree.framework.Token;
import edu.kit.informatik.matchthree.framework.interfaces.Board;

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
}
