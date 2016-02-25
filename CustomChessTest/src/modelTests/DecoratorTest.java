package modelTests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import model.Board;
import model.PieceFactory;
import model.pieces.decorator.DecoratedPieceFactory;
import model.pieces.decorator.Decorator;
import model.pieces.decorator.Mighty;
import player.PlayerColor;

public class DecoratorTest {

	@Test
	public void test(){
		PlayerColor playerColor = PlayerColor.BLACK;
		PlayerColor opponentColor = playerColor.getOppositColor();
		int x = 2, y = 2;
		Board boardmock = mock(Board.class);
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
				"King should not be able to move to attacked square even though not adjacent",
				"King should not be able to move to not adjacent square even though not attacked"
		};

		when(boardmock.isAttacked(opponentColor,testcases[0])).thenReturn(false);
		when(boardmock.isAttacked(opponentColor,testcases[1])).thenReturn(true);
		when(boardmock.isAttacked(opponentColor,testcases[2])).thenReturn(false);
		
		Decorator king = DecoratedPieceFactory.newDecoratedPiece("Mighty",
				PieceFactory.newPiece(boardmock, "King",playerColor,new int[] {x,y}));

		for(int i=0; i<testcases.length; i++){
			assertEquals(description[i], expectedValue[i], king.moveCorrect(testcases[i]));
		}
	}
}
