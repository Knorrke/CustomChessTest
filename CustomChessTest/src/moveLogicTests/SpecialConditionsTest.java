package moveLogicTests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import model.Board;
import model.pieces.Piece;
import moveLogic.specialMoveConditionStrategy.*;
import player.PlayerColor;

public class SpecialConditionsTest {
	Board boardmock;
	Piece piecemock;
	SpecialMoveCondition condition;
	private final PlayerColor ownColor = PlayerColor.WHITE, 
							  opponentColor = PlayerColor.BLACK;
	
	@Before public void initialize() {
		 boardmock = mock(Board.class);
		 piecemock = mock(Piece.class);
		 when(piecemock.getColor()).thenReturn(ownColor);
		 when(piecemock.getPosition()).thenReturn(new int[] {3,3});
	}
	
	@Test
	public void captureTest() {
		condition = new OnlyCapture();
		when(boardmock.isPieceOfColorOnSquare(opponentColor,new int[] {0,0})).thenReturn(true);
		assertTrue("Should work if a piece to capture is on square", 
				condition.isMatchingSpecialCondition(boardmock, piecemock, new int[] {0,0}));
		
		when(boardmock.isPieceOfColorOnSquare(opponentColor,new int[] {0,1})).thenReturn(false);
		assertFalse("Should work if a piece to capture is on square", 
				condition.isMatchingSpecialCondition(boardmock, piecemock, new int[] {0,1}));
	}
	
	@Test
	public void attackedTest() {
		condition = new OnlyIfAttacked();
		
		when(boardmock.isAttacked(opponentColor,new int[] {3,3})).thenReturn(true);
		assertTrue("Should work if piece is attacked", 
				condition.isMatchingSpecialCondition(boardmock, piecemock, new int[]{0,0}));

		when(boardmock.isAttacked(opponentColor,new int[] {3,3})).thenReturn(false);
		assertFalse("Should not work if piece is not attacked", 
				condition.isMatchingSpecialCondition(boardmock, piecemock, new int[] {0,0}));
	}
	
	@Test
	public void freeWayTest() {
		condition = new OnlyIfFreeWay();
		
		for(int i=0; i<8; i++){
			for(int j=0; j<9; j++){
				when(boardmock.isPieceOnSquare(new int[] {i,j})).thenReturn(false);
			}
		}
		when(boardmock.isPieceOnSquare(new int[] {3,3})).thenReturn(true);
		when(boardmock.isPieceOnSquare(new int[] {3,1})).thenReturn(true);
		when(boardmock.isPieceOnSquare(new int[] {2,3})).thenReturn(true);
		when(boardmock.isPieceOnSquare(new int[] {5,3})).thenReturn(true);
		when(boardmock.isPieceOnSquare(new int[] {7,3})).thenReturn(true);

		assertTrue("Should be able to move to adjacent square without piece on it",
				condition.isMatchingSpecialCondition(boardmock, piecemock, new int[] {3,4}));
		
		assertTrue("Should be able to move to adjacent square with piece on it",
				condition.isMatchingSpecialCondition(boardmock, piecemock, new int[] {2,3}));
		
		assertTrue("Should be able to move to distant square without piece in the way",
				condition.isMatchingSpecialCondition(boardmock, piecemock, new int[] {3,7}));
		
		assertFalse("Should not be able to move to distant square with piece in the way",
				condition.isMatchingSpecialCondition(boardmock, piecemock, new int[] {3,0}));
		
		assertTrue("Should be able to capture piece without piece in the way",
				condition.isMatchingSpecialCondition(boardmock, piecemock, new int[] {3,1}));
		
		assertFalse("Should not be able to capture piece with piece in the way",
				condition.isMatchingSpecialCondition(boardmock, piecemock, new int[] {7,3}));
	}
	
	@Test
	public void jumpOverPieceTest() {
		condition = new OnlyIfJumpOverPiece();
		for(int i=0; i<8; i++){
			for(int j=0; j<9; j++){
				when(boardmock.isPieceOnSquare(new int[] {i,j})).thenReturn(false);
			}
		}
		when(boardmock.isPieceOnSquare(new int[] {3,3})).thenReturn(true);
		when(boardmock.isPieceOnSquare(new int[] {3,1})).thenReturn(true);
		when(boardmock.isPieceOnSquare(new int[] {2,3})).thenReturn(true);
		when(boardmock.isPieceOnSquare(new int[] {5,3})).thenReturn(true);
		when(boardmock.isPieceOnSquare(new int[] {7,3})).thenReturn(true);

		assertFalse("Should not be able to move to adjacent square without piece on it",
				condition.isMatchingSpecialCondition(boardmock, piecemock, new int[] {3,4}));
		
		assertFalse("Should not be able to move to adjacent square with piece on it",
				condition.isMatchingSpecialCondition(boardmock, piecemock, new int[] {2,3}));
		
		assertFalse("Should not be able to move to distant square without piece in the way",
				condition.isMatchingSpecialCondition(boardmock, piecemock, new int[] {3,7}));
		
		assertTrue("Should be able to move to distant square with piece in the way",
				condition.isMatchingSpecialCondition(boardmock, piecemock, new int[] {3,0}));
		
		assertFalse("Should not be able to capture piece without piece in the way",
				condition.isMatchingSpecialCondition(boardmock, piecemock, new int[] {3,1}));
		
		assertTrue("Should be able to capture piece with piece in the way",
				condition.isMatchingSpecialCondition(boardmock, piecemock, new int[] {7,3}));
	}
	
	@Test
	public void neverMovedTest() {
		condition = new OnlyIfNeverMoved();
		when(piecemock.isMoved()).thenReturn(false);
		assertTrue("Should work if piece was never moved", 
				condition.isMatchingSpecialCondition(boardmock, piecemock, new int[] {0,0}));
		
		when(piecemock.isMoved()).thenReturn(true);
		assertFalse("Should not work if piece was moved", 
				condition.isMatchingSpecialCondition(boardmock, piecemock, new int[] {0,0}));
	}
	
	@Test
	public void notAttackedTest() {
		condition = new OnlyIfNotAttacked();
		
		when(boardmock.isAttacked(opponentColor,new int[] {3,3})).thenReturn(true);
		assertFalse("Should not work if piece is attacked", 
				condition.isMatchingSpecialCondition(boardmock, piecemock, new int[]{0,0}));

		when(boardmock.isAttacked(opponentColor,new int[] {3,3})).thenReturn(false);
		assertTrue("Should work if piece is not attacked", 
				condition.isMatchingSpecialCondition(boardmock, piecemock, new int[] {0,0}));
	}
	
	@Test
	public void onlyMoveTest() {
		condition = new OnlyMove();
		when(boardmock.isPieceOfColorOnSquare(PlayerColor.BLACK, new int[] {0,0})).thenReturn(true);
		when(boardmock.isPieceOfColorOnSquare(PlayerColor.WHITE, new int[] {0,0})).thenReturn(false);
		assertFalse("Should not be able to move on square with black piece",
				condition.isMatchingSpecialCondition(boardmock, piecemock, new int[] {0,0}));

		when(boardmock.isPieceOfColorOnSquare(PlayerColor.BLACK, new int[] {0,1})).thenReturn(false);
		when(boardmock.isPieceOfColorOnSquare(PlayerColor.WHITE, new int[] {0,1})).thenReturn(true);
		assertFalse("Should not be able to move on square with white piece",
				condition.isMatchingSpecialCondition(boardmock, piecemock, new int[] {0,1}));
		
		when(boardmock.isPieceOfColorOnSquare(PlayerColor.BLACK, new int[] {0,2})).thenReturn(false);
		when(boardmock.isPieceOfColorOnSquare(PlayerColor.WHITE, new int[] {0,2})).thenReturn(false);
		assertTrue("Should be able to move on free square",
				condition.isMatchingSpecialCondition(boardmock, piecemock, new int[] {0,2}));
	}
	
	@Test
	public void toNotAttackedSquareTest() {
		condition = new OnlyToNotAttackedSquare();
		
		when(boardmock.isAttacked(opponentColor,new int[] {0,0})).thenReturn(false);
		assertTrue("Should work if square is not attacked",
				condition.isMatchingSpecialCondition(boardmock, piecemock, new int[] {0,0}));
		
		when(boardmock.isAttacked(opponentColor,new int[] {0,1})).thenReturn(true);
		assertFalse("Should not work if square is attacked",
				condition.isMatchingSpecialCondition(boardmock, piecemock, new int[] {0,1}));
	}
}
