package modelTests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.util.Arrays;

import org.junit.Test;

import model.Board;
import model.PieceFactory;
import model.pieces.Pawn;
import model.pieces.Piece;
import model.pieces.interfaces.MoveLogicInitializerInterface;
import player.PlayerColor;

public class PieceFactoryTest {

	@Test
	public void corectClassTest() {
		Piece piece = PieceFactory.newPiece(new Board(8, 8), "model.pieces.Pawn", PlayerColor.BLACK, new int[] {0,0});
		assertTrue("Should be a subclass of Piece", piece instanceof MoveLogicInitializerInterface);
		assertTrue("Should be a pawn", piece instanceof Pawn);
	}

	@Test
	public void correctStateTest() {
		PlayerColor testcolor = PlayerColor.BLACK;
		int[] testposition = new int[] {3,3};
		Piece piece = PieceFactory.newPiece(new Board(8,8),"model.pieces.Pawn",testcolor, testposition);
		assertEquals("Should be of color "+testcolor, piece.getColor(), testcolor);
		assertArrayEquals("Should be at position " + Arrays.toString(testposition), piece.getPosition(),testposition);
	}
}
