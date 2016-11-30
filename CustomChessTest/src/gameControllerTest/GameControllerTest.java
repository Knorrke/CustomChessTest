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
	
	@Test
	public void standardGameIntegrityTest() {
		GameController gameController = new StandardGameController();
		assertTrue(gameController.move(new int[] {4,1}, new int[] {4,3}));
		assertTrue(gameController.move(new int[] {4,6}, new int[] {4,4}));
		assertTrue(gameController.move(new int[] {5,1}, new int[] {5,3}));
		assertTrue(gameController.move(new int[] {3,7}, new int[] {7,3}));
		//check
		assertFalse("White can't move A-pawn in check", gameController.move(new int[] {0,1}, new int[] {0,3}));
		assertTrue("White can intermit the check", gameController.move(new int[] {6,1}, new int[] {6,2}));		
	}
	
	@Test
	public void moveResetTest() {
		GameController gameController = new StandardGameController();
		gameController.move(new int[] {4,1}, new int[] {4,3});
		gameController.move(new int[] {4,6}, new int[] {4,4});
		gameController.getBoard().draw();
		gameController.move(new int[] {5,1}, new int[] {5,3});
		gameController.getBoard().draw();
		gameController.move(new int[] {3,7}, new int[] {7,3});
		gameController.getBoardAtMove(1).draw();
		gameController.getBoardAtMove(2).draw();
		gameController.getBoard().draw();
	}
}
