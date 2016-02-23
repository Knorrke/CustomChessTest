package gameControllerTest;

import static org.junit.Assert.*;
import org.junit.Test;

import gameController.GameController;
import player.PlayerColor;

public class GameControllerTest {

	@Test
	public void test() {
		GameController gameController = new GameController(PlayerColor.WHITE);
	    assertEquals(PlayerColor.WHITE, gameController.getCurrentPlayer());
	    assertEquals(PlayerColor.BLACK, gameController.nextPlayer());
	}
}
