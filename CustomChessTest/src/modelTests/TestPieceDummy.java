package modelTests;

import model.Board;
import model.pieces.Piece;
import moveLogic.MoveLogic;
import player.PlayerColor;

public class TestPieceDummy extends Piece {
	public TestPieceDummy(PlayerColor color, Board board, int[] pos) {
		super(color, board, pos);
	}

	public void initializeMoveLogic() {
		setMoveLogic(new MoveLogic(getBoard(),this,"0,1|1,0"));
	}

	public void initializeView() {
		setView(()->{});
	}
}
