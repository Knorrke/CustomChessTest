package moveLogicTests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import static helper.Helper.*;

import gameController.GameController;
import gameController.StandardGameController;
import model.Board;
import model.PieceFactory;
import model.pieces.Pawn;
import model.pieces.Piece;
import moveLogic.specialMoveConditionStrategy.Castling;
import moveLogic.specialMoveConditionStrategy.EnPassant;
import moveLogic.specialMoveConditionStrategy.OnlyCapture;
import moveLogic.specialMoveConditionStrategy.OnlyIfAttacked;
import moveLogic.specialMoveConditionStrategy.OnlyIfFreeWay;
import moveLogic.specialMoveConditionStrategy.OnlyIfJumpOverPiece;
import moveLogic.specialMoveConditionStrategy.OnlyIfNeverMoved;
import moveLogic.specialMoveConditionStrategy.OnlyIfNotAttacked;
import moveLogic.specialMoveConditionStrategy.OnlyMove;
import moveLogic.specialMoveConditionStrategy.OnlyToNotAttackedSquare;
import moveLogic.specialMoveConditionStrategy.SpecialMoveCondition;
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
		 when(piecemock.getPosition()).thenReturn(pos(3,3));
	}
	
	@Test
	public void captureTest() {
		condition = new OnlyCapture();
		when(boardmock.isPieceOfColorOnSquare(opponentColor,pos(0,0))).thenReturn(true);
		assertTrue("Should work if a piece to capture is on square", 
				condition.isMatchingSpecialCondition(boardmock, piecemock, pos(0,0)));
		
		when(boardmock.isPieceOfColorOnSquare(opponentColor,pos(0,1))).thenReturn(false);
		assertFalse("Should work if a piece to capture is on square", 
				condition.isMatchingSpecialCondition(boardmock, piecemock, pos(0,1)));
	}
	
	@Test
	public void attackedTest() {
		condition = new OnlyIfAttacked();
		
		when(boardmock.isAttacked(opponentColor,pos(3,3))).thenReturn(true);
		assertTrue("Should work if piece is attacked", 
				condition.isMatchingSpecialCondition(boardmock, piecemock, pos(0,0)));

		when(boardmock.isAttacked(opponentColor,pos(3,3))).thenReturn(false);
		assertFalse("Should not work if piece is not attacked", 
				condition.isMatchingSpecialCondition(boardmock, piecemock, pos(0,0)));
	}
	
	@Test
	public void freeWayTest() {
		condition = new OnlyIfFreeWay();
		
		for(int i=0; i<8; i++){
			for(int j=0; j<9; j++){
				when(boardmock.isPieceOnSquare(pos(i,j))).thenReturn(false);
			}
		}
		when(boardmock.isPieceOnSquare(pos(3,3))).thenReturn(true);
		when(boardmock.isPieceOnSquare(pos(3,1))).thenReturn(true);
		when(boardmock.isPieceOnSquare(pos(2,3))).thenReturn(true);
		when(boardmock.isPieceOnSquare(pos(5,3))).thenReturn(true);
		when(boardmock.isPieceOnSquare(pos(7,3))).thenReturn(true);

		assertTrue("Should be able to move to adjacent square without piece on it",
				condition.isMatchingSpecialCondition(boardmock, piecemock, pos(3,4)));
		
		assertTrue("Should be able to move to adjacent square with piece on it",
				condition.isMatchingSpecialCondition(boardmock, piecemock, pos(2,3)));
		
		assertTrue("Should be able to move to distant square without piece in the way",
				condition.isMatchingSpecialCondition(boardmock, piecemock, pos(3,7)));
		
		assertFalse("Should not be able to move to distant square with piece in the way",
				condition.isMatchingSpecialCondition(boardmock, piecemock, pos(3,0)));
		
		assertTrue("Should be able to capture piece without piece in the way",
				condition.isMatchingSpecialCondition(boardmock, piecemock, pos(3,1)));
		
		assertFalse("Should not be able to capture piece with piece in the way",
				condition.isMatchingSpecialCondition(boardmock, piecemock, pos(7,3)));
	}
	
	@Test
	public void jumpOverPieceTest() {
		condition = new OnlyIfJumpOverPiece();
		for(int i=0; i<8; i++){
			for(int j=0; j<9; j++){
				when(boardmock.isPieceOnSquare(pos(i,j))).thenReturn(false);
			}
		}
		when(boardmock.isPieceOnSquare(pos(3,3))).thenReturn(true);
		when(boardmock.isPieceOnSquare(pos(3,1))).thenReturn(true);
		when(boardmock.isPieceOnSquare(pos(2,3))).thenReturn(true);
		when(boardmock.isPieceOnSquare(pos(5,3))).thenReturn(true);
		when(boardmock.isPieceOnSquare(pos(7,3))).thenReturn(true);

		assertFalse("Should not be able to move to adjacent square without piece on it",
				condition.isMatchingSpecialCondition(boardmock, piecemock, pos(3,4)));
		
		assertFalse("Should not be able to move to adjacent square with piece on it",
				condition.isMatchingSpecialCondition(boardmock, piecemock, pos(2,3)));
		
		assertFalse("Should not be able to move to distant square without piece in the way",
				condition.isMatchingSpecialCondition(boardmock, piecemock, pos(3,7)));
		
		assertTrue("Should be able to move to distant square with piece in the way",
				condition.isMatchingSpecialCondition(boardmock, piecemock, pos(3,0)));
		
		assertFalse("Should not be able to capture piece without piece in the way",
				condition.isMatchingSpecialCondition(boardmock, piecemock, pos(3,1)));
		
		assertTrue("Should be able to capture piece with piece in the way",
				condition.isMatchingSpecialCondition(boardmock, piecemock, pos(7,3)));
	}
	
	@Test
	public void neverMovedTest() {
		condition = new OnlyIfNeverMoved();
		when(piecemock.isMoved()).thenReturn(false);
		assertTrue("Should work if piece was never moved", 
				condition.isMatchingSpecialCondition(boardmock, piecemock, pos(0,0)));
		
		when(piecemock.isMoved()).thenReturn(true);
		assertFalse("Should not work if piece was moved", 
				condition.isMatchingSpecialCondition(boardmock, piecemock, pos(0,0)));
	}
	
	@Test
	public void notAttackedTest() {
		condition = new OnlyIfNotAttacked();
		
		when(boardmock.isAttacked(opponentColor,pos(3,3))).thenReturn(true);
		assertFalse("Should not work if piece is attacked", 
				condition.isMatchingSpecialCondition(boardmock, piecemock, pos(0,0)));

		when(boardmock.isAttacked(opponentColor,pos(3,3))).thenReturn(false);
		assertTrue("Should work if piece is not attacked", 
				condition.isMatchingSpecialCondition(boardmock, piecemock, pos(0,0)));
	}
	
	@Test
	public void onlyMoveTest() {
		condition = new OnlyMove();
		when(boardmock.isPieceOfColorOnSquare(PlayerColor.BLACK, pos(0,0))).thenReturn(true);
		when(boardmock.isPieceOfColorOnSquare(PlayerColor.WHITE, pos(0,0))).thenReturn(false);
		assertFalse("Should not be able to move on square with black piece",
				condition.isMatchingSpecialCondition(boardmock, piecemock, pos(0,0)));

		when(boardmock.isPieceOfColorOnSquare(PlayerColor.BLACK, pos(0,1))).thenReturn(false);
		when(boardmock.isPieceOfColorOnSquare(PlayerColor.WHITE, pos(0,1))).thenReturn(true);
		assertFalse("Should not be able to move on square with white piece",
				condition.isMatchingSpecialCondition(boardmock, piecemock, pos(0,1)));
		
		when(boardmock.isPieceOfColorOnSquare(PlayerColor.BLACK, pos(0,2))).thenReturn(false);
		when(boardmock.isPieceOfColorOnSquare(PlayerColor.WHITE, pos(0,2))).thenReturn(false);
		assertTrue("Should be able to move on free square",
				condition.isMatchingSpecialCondition(boardmock, piecemock, pos(0,2)));
	}
	
	@Test
	public void toNotAttackedSquareTest() {
		condition = new OnlyToNotAttackedSquare();
		
		when(boardmock.isAttacked(opponentColor,pos(0,0))).thenReturn(false);
		assertTrue("Should work if square is not attacked",
				condition.isMatchingSpecialCondition(boardmock, piecemock, pos(0,0)));
		
		when(boardmock.isAttacked(opponentColor,pos(0,1))).thenReturn(true);
		assertFalse("Should not work if square is attacked",
				condition.isMatchingSpecialCondition(boardmock, piecemock, pos(0,1)));
	}
	
	@Test
	public void castlingTest() {
		condition = new Castling();
		Board board = new Board(8, 8);
		Piece king = PieceFactory.newPiece(board, "Mighty King", ownColor, pos("e1"));
		Piece rook = PieceFactory.newPiece(board, "Rook", ownColor,pos("a1"));
		Piece rook2 = PieceFactory.newPiece(board, "Rook", ownColor,pos("h1"));
		Piece opponentDummy = PieceFactory.newPiece(board, "Dummy", opponentColor, pos("b1"));
		board.addPieces(king, rook, rook2, opponentDummy);
		
		assertTrue("Castling should be allowed if no piece inbetween", 
				condition.isMatchingSpecialCondition(board, king, pos("g1")));
		assertFalse("Castling should not be allowed if a piece inbetween", 
				condition.isMatchingSpecialCondition(board, king, pos("c1")));
		
		Piece opponentRook = PieceFactory.newPiece(board, "Rook", opponentColor, pos("e6"));
		board.addPiece(opponentRook);
		assertFalse("Castling should not work if king is in check", 
				condition.isMatchingSpecialCondition(board, king, pos("g1")));
		board.setPieceToNewPosition(opponentRook, pos("f6"));
		assertFalse("Castling should not be allowed if king moves through check", 
				condition.isMatchingSpecialCondition(board, king, pos("g1")));
		
		board.removePiece(opponentRook);
		assertTrue("Removing should work...", condition.isMatchingSpecialCondition(board, king, pos("g1")));
		rook2.setMoved(true);
		assertFalse("Can't castle if rook has already moved", condition.isMatchingSpecialCondition(board, king, pos("g1")));
		rook2.setMoved(false);
		king.setMoved(true);
		assertFalse("Can't castle if king has already moved", condition.isMatchingSpecialCondition(board, king, pos("g1")));
	}
	
	@Test
	public void enpassantTest() {
		condition = new EnPassant();
		GameController game = new StandardGameController();
		assertTrue(game.move(pos("e2"),pos("e4"))); //Weiss
		assertTrue(game.move(pos("c7"),pos("c5"))); //Schwarz
		assertTrue(game.move(pos("e4"), pos("e5"))); //Weiss
		assertTrue(game.move(pos("d7"), pos("d5"))); //Schwarz
		
		assertTrue("En passant should be allowed after move of pawn", game.move(pos("e5"),pos("d6"))); //Weiss
		assertNull("Pawn on d5 should be taken", game.getBoard().getPieceOfSquare(pos("d5")));
		
		assertTrue(game.move(pos("c5"), pos("c4"))); //Schwarz
		assertTrue(game.move(pos("d2"), pos("d4"))); //Weiss
		
		//intermediate move
		assertTrue(game.move(pos("b8"),pos("c6"))); //Schwarz
		assertTrue(game.move(pos("g1"),pos("f3"))); //Weiss
		
		assertFalse("En passant should not be allowed after intermediate move", game.move(pos("c4"),pos("d3"))); //(Schwarz)
		assertTrue("Pawn on d4 should still be there", game.getBoard().getPieceOfSquare(pos("d4")).getType().contains(Pawn.class));
	}
}
