package pieces;
import enums.*;
import game.Board;

public class Knight extends Piece {
	private PieceType type;
	public Knight(String file, int rank, Side side, Board board) {
		super(file, rank, side, board);
		type = PieceType.NIGHT;
	}
	public Knight(BoardSquare square, Side side, Board board) {
		super(square, side, board);
		type = PieceType.NIGHT;
	}
	public boolean checkBasicLegality(Board board, BoardSquare dest) {
		int rankChange = Math.abs(BoardSquare.getRank(dest)-getRank());
		int fileChange = Math.abs(Integer.parseInt(BoardSquare.valueOf(dest).substring(0,1))-Integer.parseInt(BoardSquare.valueOf(getBoardSquare()).substring(0,1)));
		return (rankChange == 1 && fileChange == 2) || (rankChange == 2 && fileChange == 1);
	}
	public int getPieceValue() {
		return 3;
	}
	public PieceType getPieceType() {
		return type;
	}
	public String toDisplayString() {
		return (side == Side.WHITE) ? "N" : "n";
	}
}
