package modelTests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

import model.Board;
import model.PieceFactory;
import model.pieces.Dummy;
import model.pieces.King;
import model.pieces.Piece;
import model.pieces.decorator.AbstractDecorator;
import model.pieces.decorator.AxeSwinging;
import model.pieces.decorator.KnightRiding;
import model.pieces.decorator.Mighty;
import player.PlayerColor;

public class DecoratorTest {

	private PlayerColor playerColor, opponentColor;
	private Board boardmock;
	
	@Before
	public void initialize() {
		playerColor = PlayerColor.BLACK;
		opponentColor = playerColor.getOppositColor();
		boardmock = mock(Board.class);
	}
	
	@Test
	public void factoryTest() {
		Piece piece = PieceFactory.newPiece(boardmock, "Mighty KnightRiding King", playerColor, new int[] {2,2});
		assertTrue("Piece should be of class Mighty", piece.getType().contains(Mighty.class));
		assertTrue("Piece should be of class KnightRiding", piece.getType().contains(KnightRiding.class));
		assertTrue("Piece should be of class King", piece.getType().contains(King.class));
		assertFalse("Piece should not be of class Dummy", piece.getType().contains(Dummy.class));
		assertFalse("Piece should not be of class AxeSwinging", piece.getType().contains(AxeSwinging.class));
	}
	
	@Test
	public void mightyTest(){
		int x = 2, y = 2;
		
		int[][] testcases = {{x+1,y+1}, {x-1,y-1}, {x+2,y+2}};
		boolean[] expectedValue = {true, false, false};
		
		String[] description = {
				"King should be able to move to adjacent square thats not attacked",
				"King should not be able to move to attacked square even though adjacent",
				"King should not be able to move to not adjacent square even though not attacked"
		};

		when(boardmock.isAttacked(opponentColor,testcases[0])).thenReturn(false);
		when(boardmock.isAttacked(opponentColor,testcases[1])).thenReturn(true);
		when(boardmock.isAttacked(opponentColor,testcases[2])).thenReturn(false);
		
		Piece king = PieceFactory.newPiece(boardmock, "Mighty King",playerColor,new int[] {x,y});
		when(boardmock.getPieceOfSquare(new int[] {x,y})).thenReturn(king);
		
		for(int i=0; i<testcases.length; i++){
			assertEquals(description[i], expectedValue[i], king.getPossibleMoves(testcases[i]).size()>0);
		}
	}
	
	@Test
	public void knightRidingNoKnightTest() {
		int x = 2, y = 2;

		Piece king = PieceFactory.newPiece(boardmock, "KnightRiding King",playerColor,new int[] {x,y});

		for(int i=-1; i<=1; i++) {
			for(int j=-1; j<=1; j++) {				
				if(i==0 && j==0) {
					when(boardmock.getPieceOfSquare(new int[]{x,y})).thenReturn(king);
				} else {					
					when(boardmock.getPieceOfSquare(new int[]{x+i,y+j})).thenReturn(null);
				}
			}
		}
		assertTrue("King should be able to move to adjacent square without horse next to him",
				king.getPossibleMoves(new int[] {x+1,y+1}).size()>0);
		assertFalse("King should not be able to move like knight, if not adjacent to knight",
				king.getPossibleMoves(new int[] {x+1,y+2}).size()>0);
	}
	
	@Test
	public void knightRidingWithKnightTest() {
		int x = 2, y = 2;

		Piece knight = PieceFactory.newPiece(boardmock, "Knight", playerColor, new int[] {x+1,y+1});
		Piece king = PieceFactory.newPiece(boardmock, "KnightRiding King",playerColor,new int[] {x,y});

		for(int i=-1; i<=1; i++) {
			for(int j=-1; j<=1; j++) {				
				if(i==0 && j==0){
					when(boardmock.getPieceOfSquare(new int[]{x+i,y+j})).thenReturn(king);
				} else if(i==-1 && j==-1) {					
					when(boardmock.getPieceOfSquare(new int[]{x+i,y+j})).thenReturn(knight);
				} else {
					when(boardmock.getPieceOfSquare(new int[]{x+i,y+j})).thenReturn(null);
				}
			}
		}
		assertTrue("King should be able to move to adjacent square with horse next to him",
				king.getPossibleMoves(new int[] {x+1,y+1}).size()>0);
		assertTrue("King should be able to move like knight, if adjacent to knight",
				king.getPossibleMoves(new int[] {x+1,y+2}).size()>0);
		assertFalse("King should not be able to move weird, even if adjacent to knight",
				king.getPossibleMoves(new int[] {x+1,y+3}).size()>0);
	}
	
	@Test
	public void knightRidingWithDecoratedKnightTest() {
		int x = 2, y = 2;

		Piece knight = new AbstractDecorator(
				PieceFactory.newPiece(boardmock, "Knight", playerColor, new int[] {x+1,y+1})) {};
				
		Piece king = PieceFactory.newPiece(boardmock, "KnightRiding King", playerColor, new int[] {x,y});

		for(int i=-1; i<=1; i++) {
			for(int j=-1; j<=1; j++) {				
				if(i==0 && j==0) {
					when(boardmock.getPieceOfSquare(new int[]{x+i,y+j})).thenReturn(king);
				} else if(i==-1 && j==-1) {					
					when(boardmock.getPieceOfSquare(new int[]{x+i,y+j})).thenReturn(knight);
				} else {
					when(boardmock.getPieceOfSquare(new int[]{x+i,y+j})).thenReturn(null);
				}
			}
		}
		assertTrue("King should be able to move to adjacent square with horse next to him",
				king.getPossibleMoves(new int[] {x+1,y+1}).size()>0);
		assertTrue("King should be able to move like knight, if adjacent to knight",
				king.getPossibleMoves(new int[] {x+1,y+2}).size()>0);
		assertFalse("King should not be able to move weird, even if adjacent to knight",
				king.getPossibleMoves(new int[] {x+1,y+3}).size()>0);
	}
	
	@Test
	public void AxeSwingingTest() {
		//AxeSwinging Piece can additionally move 2,0 or 0,2 for attacking, but can't jump.
		int[] piecePos = {2,2}, opponentVPos = {2,4}, opponentHPos = {4,2};
		
		Piece axeBishop = PieceFactory.newPiece(boardmock, "AxeSwinging Bishop", playerColor, piecePos);
		Piece opponent = PieceFactory.newPiece(boardmock, "Dummy", opponentColor, opponentVPos);
		Piece opponent2 = PieceFactory.newPiece(boardmock, "Dummy", opponentColor, opponentHPos);
		
		when(boardmock.isPieceOfColorOnSquare(opponentColor, opponentVPos)).thenReturn(true);
		when(boardmock.isPieceOfColorOnSquare(opponentColor, opponentHPos)).thenReturn(true);
		doReturn(false).when(boardmock).isPieceOfColorOnSquare(
				ArgumentMatchers.eq(opponentColor), 
				ArgumentMatchers.argThat((int[] pos) -> !pos.equals(opponentVPos) && !pos.equals(opponentHPos)));
		when(boardmock.getPieceOfSquare(piecePos)).thenReturn(axeBishop);
		when(boardmock.getPieceOfSquare(opponentVPos)).thenReturn(opponent);
		when(boardmock.getPieceOfSquare(opponentHPos)).thenReturn(opponent2);
		
		assertTrue("AxeBishop should be able to capture opponentPiece vertically", axeBishop.getPossibleMoves(opponentVPos).size()>0);
		assertTrue("AxeBishop should be able to capture opponentPiece horizontally", axeBishop.getPossibleMoves(opponentVPos).size()>0);
		assertFalse("AxeBishop should not be able to move axe-like without capture", axeBishop.getPossibleMoves(new int[] {2,0}).size()>0);
		assertTrue("AxeBishop should be able to move bishop-like without capture", axeBishop.getPossibleMoves(new int[] {1,1}).size()>0);
	}
}
