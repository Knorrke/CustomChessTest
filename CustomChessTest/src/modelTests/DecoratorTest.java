package modelTests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import model.Board;
import model.PieceFactory;
import model.pieces.Piece;
import model.pieces.decorator.AdditionalMoveAllowanceDecorator;
import model.pieces.decorator.DecoratedPieceFactory;
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
	public void mightyTest(){
		int x = 2, y = 2;
		
		int[][] testcases = {
				new int[] {x+1,y+1},
				new int[] {x-1,y-1},
				new int[] {x+2,y+2}
		};

		boolean[] expectedValue = {
				true,
				false,
				false
		};
		
		String[] description = {
				"King should be able to move to adjacent square thats not attacked",
				"King should not be able to move to attacked square even though adjacent",
				"King should not be able to move to not adjacent square even though not attacked"
		};

		when(boardmock.isAttacked(opponentColor,testcases[0])).thenReturn(false);
		when(boardmock.isAttacked(opponentColor,testcases[1])).thenReturn(true);
		when(boardmock.isAttacked(opponentColor,testcases[2])).thenReturn(false);
		
		Piece king = DecoratedPieceFactory.newDecoratedPiece("Mighty",
				PieceFactory.newPiece(boardmock, "King",playerColor,new int[] {x,y}));

		for(int i=0; i<testcases.length; i++){
			assertEquals(description[i], expectedValue[i], king.moveCorrect(testcases[i]));
		}
	}
	
	@Test
	public void knightRidingNoKnightTest() {
		int x = 2, y = 2;

		Piece king = DecoratedPieceFactory.newDecoratedPiece("KnightRiding",
				PieceFactory.newPiece(boardmock, "King",playerColor,new int[] {x,y}));

		for(int i=-1; i<=1; i++) {
			for(int j=-1; j<=1; j++) {				
				if(i==0 && j==0) continue;
				when(boardmock.getPieceOfSquare(new int[]{x+i,y+j})).thenReturn(null);
			}
		}
		assertTrue("King should be able to move to adjacent square without horse next to him",
				king.moveCorrect(new int[] {x+1,y+1}));
		assertFalse("King should not be able to move like knight, if not adjacent to knight",
				king.moveCorrect(new int[] {x+1,y+2}));
	}
	
	@Test
	public void knightRidingWithKnightTest() {
		int x = 2, y = 2;

		Piece knight = PieceFactory.newPiece(boardmock, "Knight", playerColor, new int[] {x+1,y+1});
		Piece king = DecoratedPieceFactory.newDecoratedPiece("KnightRiding",
				PieceFactory.newPiece(boardmock, "King",playerColor,new int[] {x,y}));

		for(int i=-1; i<=1; i++) {
			for(int j=-1; j<=1; j++) {				
				if(i==0 && j==0) continue;
				if(i==-1 && j==-1) {					
					when(boardmock.getPieceOfSquare(new int[]{x+i,y+j})).thenReturn(knight);
				} else {
					when(boardmock.getPieceOfSquare(new int[]{x+i,y+j})).thenReturn(null);
				}
			}
		}
		assertTrue("King should be able to move to adjacent square with horse next to him",
				king.moveCorrect(new int[] {x+1,y+1}));
		assertTrue("King should be able to move like knight, if adjacent to knight",
				king.moveCorrect(new int[] {x+1,y+2}));
		assertFalse("King should not be able to move weird, even if adjacent to knight",
				king.moveCorrect(new int[] {x+1,y+3}));
	}
	
	@Test
	public void knightRidingWithDecoratedKnightTest() {
		int x = 2, y = 2;

		Piece knight = new AdditionalMoveAllowanceDecorator(
				PieceFactory.newPiece(boardmock, "Knight", playerColor, new int[] {x+1,y+1})) {};
				
		Piece king = DecoratedPieceFactory.newDecoratedPiece("KnightRiding",
				PieceFactory.newPiece(boardmock, "King",playerColor,new int[] {x,y}));

		for(int i=-1; i<=1; i++) {
			for(int j=-1; j<=1; j++) {				
				if(i==0 && j==0) continue;
				if(i==-1 && j==-1) {					
					when(boardmock.getPieceOfSquare(new int[]{x+i,y+j})).thenReturn(knight);
				} else {
					when(boardmock.getPieceOfSquare(new int[]{x+i,y+j})).thenReturn(null);
				}
			}
		}
		assertTrue("King should be able to move to adjacent square with horse next to him",
				king.moveCorrect(new int[] {x+1,y+1}));
		assertTrue("King should be able to move like knight, if adjacent to knight",
				king.moveCorrect(new int[] {x+1,y+2}));
		assertFalse("King should not be able to move weird, even if adjacent to knight",
				king.moveCorrect(new int[] {x+1,y+3}));
	}
	
}
