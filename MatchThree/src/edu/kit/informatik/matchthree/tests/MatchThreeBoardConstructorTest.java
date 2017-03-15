package edu.kit.informatik.matchthree.tests;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import edu.kit.informatik.matchthree.MatchThreeBoard;
import edu.kit.informatik.matchthree.framework.Position;
import edu.kit.informatik.matchthree.framework.Token;
import edu.kit.informatik.matchthree.framework.interfaces.Board;
import edu.kit.informatik.matchthree.framework.interfaces.Move;
import edu.kit.informatik.matchthree.moves.FlipDown;

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
     * Tests the constructor and getters of {@link Board} on the instance
     * created by {@code new MatchThreeBoard(Token.set("AB"), 2, 3);}.
     */
    @Test
    public void testToString() {
        String s = "ABA;AAA;BCC;AAA";
        Board b = new MatchThreeBoard(Token.set("ABC"), s);
        assertEquals(b.toTokenString(), s);
        Move m = new FlipDown(Position.at(0, 1));
        m.apply(b);
        assertEquals(b.toTokenString(), "ABA;BAA;ACC;AAA");
        assertEquals(b.getColumnCount(), 3);
        assertEquals(b.getRowCount(), 4);
    }
    
    /**
     * Tests getter-methods.
     */
    @Test
    public void testGetter() {
        String s = "ABA;AAA;BCC;AAA";
        Board b = new MatchThreeBoard(Token.set("ABC"), s);
        assertEquals(b.getColumnCount(), 3);
        assertEquals(b.getRowCount(), 4);
        
        b = new MatchThreeBoard(Token.set("ABC"), s + ";CCC");
        assertEquals(b.getColumnCount(), 3);
        assertEquals(b.getRowCount(), 5);
        assertEquals(b.getAllValidTokens(), Token.set("BAC"));
        b = new MatchThreeBoard(Token.set("*dFsxaW"), 5, 2);
        assertEquals(b.getColumnCount(), 5);
        assertEquals(b.getRowCount(), 2);
        assertEquals(b.toTokenString(), "     ;     ");
        assertEquals(b.getAllValidTokens(), Token.set("sxadFW*"));
    }
    
    /**
     * Test {@link MatchThreeBoard#moveTokensToBottom()}
     */
    @Test
    public void moveBottom() {
        Board b = new MatchThreeBoard(Token.set("AB"), 3, 3);
        assertEquals(b.toTokenString(), "   ;   ;   ");
        b.setTokenAt(Position.at(0, 2), new Token('A'));
        assertEquals(b.toTokenString(), "   ;   ;A  ");
        Set<Position> pos = b.moveTokensToBottom();
        assertEquals(pos.size(), 0);
        b.setTokenAt(Position.at(1, 1), new Token('A'));
        assertEquals(b.toTokenString(), "   ; A ;A  ");
        b.setTokenAt(Position.at(2, 2), new Token('A'));
        assertEquals(b.toTokenString(), "   ; A ;A A");
        pos = b.moveTokensToBottom();
        assertEquals(pos.size(), 2);
        assertEquals(b.toTokenString(), "   ;   ;AAA");
        assertEquals(pos.contains(Position.at(1, 1)), true);
        assertEquals(pos.contains(Position.at(1, 2)), true);
        assertEquals(pos.contains(Position.at(1, 0)), false);
    }
}
