package gameControllerTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import gameController.gameConditionsStrategy.IntegrityConditions.GameIntegrityCondition;
import gameController.gameConditionsStrategy.IntegrityConditions.MightyNotAttacked;
import model.Board;
import model.PieceFactory;
import model.pieces.Piece;
import player.PlayerColor;

public class GameConditionTest {

	GameIntegrityCondition cond;
	Board board;
	PlayerColor playerColor;
	PlayerColor opponentColor;
	
	@Before
	public void initialize() {
		cond = new MightyNotAttacked();
		board = new Board(8, 8);
		playerColor = PlayerColor.BLACK;
		opponentColor = playerColor.getOppositColor();
	}
	

	@Test
	public void migthyNotAttackedCheckAllowedTest() {
		Piece king = PieceFactory.newPiece(board, "Mighty King", playerColor, new int[] {4,4});
		Piece attacker = PieceFactory.newPiece(board, "Rook", opponentColor, new int[] {6,5});
		
		board.addPieces(king, attacker);
		
		assertTrue("Check is allowed", 
				cond.isGameIntegrityEnsured(board, attacker.getPossibleMoves(new int[] {6,4}).get(0)));
		assertFalse("Move into check is not allowed", king.getPossibleMoves(new int[] {4,5}).size()>0 
				&& cond.isGameIntegrityEnsured(board, king.getPossibleMoves(new int[] {4,5}).get(0)));
	}
	
	@Test
	public void mightyNotAttackedPinnedTest() {
		Piece king = PieceFactory.newPiece(board, "Mighty King", playerColor, new int[] {4,4});
		Piece pinnedPiece = PieceFactory.newPiece(board, "Bishop", playerColor, new int[] {5,4});
		Piece attacker = PieceFactory.newPiece(board, "Rook", opponentColor, new int[] {6,4});
		
		board.addPieces(king, pinnedPiece, attacker);

		assertFalse("Pinned bishop should not be able to move", 
				cond.isGameIntegrityEnsured(board, pinnedPiece.getPossibleMoves(new int[] {6,5}).get(0)));
	}
	
	@Test
	public void mightyNotAttackedIntermittingTest() {
		Piece king = PieceFactory.newPiece(board, "Mighty King", playerColor, new int[] {4,4});
		Piece intermittingPiece = PieceFactory.newPiece(board, "Bishop", playerColor, new int[] {4,3});
		Piece attacker = PieceFactory.newPiece(board, "Rook", opponentColor, new int[] {6,4});
		
		board.addPieces(king, intermittingPiece, attacker);

		assertFalse("Bishop should not be able to move normally if king is attacked",
				cond.isGameIntegrityEnsured(board, intermittingPiece.getPossibleMoves(new int[] {6,5}).get(0)));
		assertTrue("Bishop should be able move between king and attacker", 
				cond.isGameIntegrityEnsured(board, intermittingPiece.getPossibleMoves(new int[] {5,4}).get(0)));
	}
	
	@Test
	public void mightyNotAttackedMultipleKingsCaptureAttackerTest() {
		Piece king = PieceFactory.newPiece(board, "Mighty King", playerColor, new int[] {4,4});
		Piece king2 = PieceFactory.newPiece(board, "Mighty King", playerColor, new int[] {6,6});
		
		Piece intermittingPiece = PieceFactory.newPiece(board, "Queen", playerColor, new int[] {5,5});
		Piece attacker = PieceFactory.newPiece(board, "Rook", opponentColor, new int[] {6,4});

		board.addPieces(king, king2, intermittingPiece, attacker);

		assertFalse("Queen should not be able to move normally if kings are attacked",
				cond.isGameIntegrityEnsured(board, intermittingPiece.getPossibleMoves(new int[] {6,5}).get(0)));
		assertFalse("Queen should not be able move between king and attacker king2 attacked", 
				cond.isGameIntegrityEnsured(board, intermittingPiece.getPossibleMoves(new int[] {5,4}).get(0)));
		assertTrue("Queen should be able to capture attacker", 
				cond.isGameIntegrityEnsured(board, intermittingPiece.getPossibleMoves(new int[] {6,4}).get(0)));
	}
	
	@Test
	public void mightyNotAttackedMultipleKingsPinnedPieceTest() {
		Piece king = PieceFactory.newPiece(board, "Mighty King", playerColor, new int[] {4,4});
		Piece attackedKing = PieceFactory.newPiece(board, "Mighty King", playerColor, new int[] {6,6});
		Piece intermittingPiece = PieceFactory.newPiece(board, "Queen", playerColor, new int[] {5,4});
		Piece attacker = PieceFactory.newPiece(board, "Rook", opponentColor, new int[] {6,4});

		board.addPieces(king, attackedKing, intermittingPiece, attacker);

		assertTrue("Queen should not be able to move normally if kings are attacked",
				intermittingPiece.getPossibleMoves(new int[] {6,5}).isEmpty() ||
				!cond.isGameIntegrityEnsured(board, intermittingPiece.getPossibleMoves(new int[] {6,5}).get(0)));
		assertTrue("Pinned Queen should not be able move between other king and attacker", 
				intermittingPiece.getPossibleMoves(new int[] {6,5}).isEmpty() ||
				!cond.isGameIntegrityEnsured(board, intermittingPiece.getPossibleMoves(new int[] {6,5}).get(0)));
		assertTrue("Queen should be able to capture attacker", 
				cond.isGameIntegrityEnsured(board, intermittingPiece.getPossibleMoves(new int[] {6,4}).get(0)));
		assertTrue("Attacked king should be able to move", 
				cond.isGameIntegrityEnsured(board, attackedKing.getPossibleMoves(new int[] {7,7}).get(0)));
		assertTrue("Attacked king should not be able to move to attacked square", 
				attackedKing.getPossibleMoves(new int[] {6,7}).isEmpty() ||
				!cond.isGameIntegrityEnsured(board, attackedKing.getPossibleMoves(new int[] {6,7}).get(0)));
		assertTrue("Not attacked king should not be able to move", 
				king.getPossibleMoves(new int[] {4,3}).isEmpty() ||
				!cond.isGameIntegrityEnsured(board, king.getPossibleMoves(new int[] {4,3}).get(0)));
	}
}
