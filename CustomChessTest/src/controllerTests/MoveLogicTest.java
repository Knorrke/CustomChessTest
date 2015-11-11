package controllerTests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import controller.MoveLogic;
import model.Board;
import model.pieces.Piece;

public class MoveLogicTest {

	@Test
	public void test() {
		Board boardmock = mock(Board.class);
		Piece piecemock = mock(Piece.class);
		int[] position = new int[]{5,5};
		when(piecemock.getPosition()).thenReturn(position);
		
		MoveLogic testml = new MoveLogic(boardmock,piecemock,"1,0");

		assertTrue("Should be able to move one to the right", testml.moveCorrect(new int[]{6,5}));
		assertTrue("Should be able to move one to the left", testml.moveCorrect(new int[]{4,5}));
		assertFalse("Should not be able to move two to the left", testml.moveCorrect(new int[]{3,5}));
		assertFalse("Should not be able to move one up", testml.moveCorrect(new int[]{5,6}));
	}

}
