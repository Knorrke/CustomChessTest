package modelTests;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import model.Board;
import model.PieceFactory;
import model.pieces.Bishop;
import model.pieces.Pawn;
import model.pieces.Piece;
import player.PlayerColor;

public class PieceFactoryTest {

	@Test
	public void acceptCustomClassesTest(){
		
		Piece piece = PieceFactory.newPiece(new Board(8,8),TestPieceDummy.class.getName(),PlayerColor.BLACK,new int[] {0,0});
		assertNotNull("Should not be null", piece);
		assertTrue("Should be a subclass of Piece", piece instanceof Piece);
		assertFalse("Shouldn't be a pawn", piece instanceof Pawn);
	}
	
	@Test
	public void corectClassTest() {
		Piece piece = PieceFactory.newPiece(new Board(8, 8), "Pawn", PlayerColor.BLACK, new int[] {0,0});
		assertNotNull("Should not be null", piece);
		assertTrue("Should be a subclass of Piece", piece instanceof Piece);
		assertTrue("Should be a pawn", piece instanceof Pawn);
		assertFalse("Shouldn't be a bishop", piece instanceof Bishop);
	}

	@Test
	public void correctStateTest() {
		PlayerColor testcolor = PlayerColor.BLACK;
		int[] testposition = new int[] {3,3};
		Piece piece = PieceFactory.newPiece(new Board(8,8),"Pawn",testcolor, testposition);
		assertEquals("Should be of color "+testcolor, piece.getColor(), testcolor);
		assertArrayEquals("Should be at position " + Arrays.toString(testposition), piece.getPosition(),testposition);
	}
}