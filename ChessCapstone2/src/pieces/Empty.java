package pieces;
import enums.*;
import game.Board;

public class Empty extends Piece {
	private PieceType type;
	public Empty(String file, int rank, Board board) {
		super(file, rank, Side.NEUTRAL, board);
		type = PieceType.EMPTY;
	}
	public Empty(BoardSquare square, Board board) {
		super(square, Side.NEUTRAL, board);
		type = PieceType.EMPTY;
	}
	public boolean checkBasicLegality(BoardSquare dest) {
		return false;
	}
	public PieceType getPieceType() {
		return type;
	}
	public String toDisplayString() {
		return "-";
	}
}
