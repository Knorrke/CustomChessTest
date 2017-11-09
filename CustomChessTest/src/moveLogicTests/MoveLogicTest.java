package moveLogicTests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.Mockito;

import static helper.Helper.*;

import model.Board;
import model.pieces.Piece;
import moveLogic.MoveLogic;

public class MoveLogicTest {

	@Test
	public void movementConditionTest() {
		Board boardmock = Mockito.mock(Board.class);
		Piece piecemock = Mockito.mock(Piece.class);
		int[] position = pos(5,5);
		Mockito.when(piecemock.getPosition()).thenReturn(position);
		Mockito.when(boardmock.getPieceOfSquare(position)).thenReturn(piecemock);
		
		MoveLogic testml = new MoveLogic(boardmock,piecemock,"1,0");
		
		assertTrue("Should be able to move one to the right", testml.getPossibleMoves(pos(6,5)).size()>0);
		assertTrue("Should be able to move one to the left", testml.getPossibleMoves(pos(4,5)).size()>0);
		assertFalse("Should not be able to move two to the left", testml.getPossibleMoves(pos(3,5)).size()>0);
		assertFalse("Should not be able to move one up", testml.getPossibleMoves(pos(5,6)).size()>0);
	}
}
