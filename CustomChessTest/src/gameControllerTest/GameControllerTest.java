package gameControllerTest;

import static org.junit.Assert.*;

import org.junit.Test;

import gameController.GameController;
import gameController.StandardGameController;
import model.pieces.Pawn;
import model.pieces.Piece;
import player.PlayerColor;

public class GameControllerTest {

	@Test
	public void standardGameTest() {
		GameController gameController = new StandardGameController();
	    assertEquals("Should be whites turn", PlayerColor.WHITE, gameController.getCurrentPlayer());
	    
	    Piece pawn = gameController.getBoard().getPieceOfSquare(new int[] {4,1});
	    assertNotNull("Piece on e2 should be not null" , pawn);
	    assertTrue("Piece should be a pawn", pawn instanceof Pawn);
	    
	    assertTrue("Pawn should be able to move e2-e4", gameController.move(pawn , new int[] {4,3}));
	    assertEquals("Should be blacks turn after white move", PlayerColor.BLACK, gameController.getCurrentPlayer());
	}
}
