package modelTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import model.Board;
import model.PieceFactory;
import model.pieces.Pawn;
import model.pieces.Piece;
import model.pieces.interfaces.MoveLogicInitializerInterface;
import player.PlayerColor;

public class PieceFactoryTest {

	@Test
	public void test() {
		PlayerColor testcolor = PlayerColor.BLACK;
		Piece piece = PieceFactory.newPiece(new Board(8, 8), "model.pieces.Pawn", testcolor, new int[] {0,0});
		assertTrue("Should be a subclass of Piece", piece instanceof MoveLogicInitializerInterface);
		assertTrue("Should be a pawn", piece instanceof Pawn);
		assertEquals("Should be of color "+testcolor, piece.getColor(), testcolor);
	}

}
