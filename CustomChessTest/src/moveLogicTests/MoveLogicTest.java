package moveLogicTests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.Mockito;

import model.Board;
import model.pieces.Piece;
import moveLogic.MoveLogic;

public class MoveLogicTest {

	@Test
	public void movementConditionTest() {
		Board boardmock = Mockito.mock(Board.class);
		Piece piecemock = Mockito.mock(Piece.class);
		int[] position = new int[]{5,5};
		Mockito.when(piecemock.getPosition()).thenReturn(position);
		
		MoveLogic testml = new MoveLogic(boardmock,piecemock,"1,0");
		
		assertTrue("Should be able to move one to the right", testml.moveCorrect(new int[]{6,5}));
		Mockito.verify(piecemock).getPosition();
		assertTrue("Should be able to move one to the left", testml.moveCorrect(new int[]{4,5}));
		assertFalse("Should not be able to move two to the left", testml.moveCorrect(new int[]{3,5}));
		assertFalse("Should not be able to move one up", testml.moveCorrect(new int[]{5,6}));
	}
}
