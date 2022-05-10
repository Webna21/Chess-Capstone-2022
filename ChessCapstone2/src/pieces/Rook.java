package pieces;
import enums.*;
import game.Board;
import game.SpecificPieceLegalityCheck;

public class Rook extends Piece {
	private PieceType type;
	public Rook(String file, int rank, Side side, Board board) {
		super(file, rank, side, board);
		type = PieceType.ROOK;
	}
	public Rook(BoardSquare square, Side side, Board board) {
		super(square, side, board);
		type = PieceType.ROOK;
	}
	public boolean checkBasicLegality(Board board,BoardSquare dest) {
		int rankChange = Math.abs(BoardSquare.getRank(dest)-getRank());
		int fileChange = Math.abs(Integer.parseInt(BoardSquare.valueOf(dest).substring(0,1))-Integer.parseInt(BoardSquare.valueOf(getBoardSquare()).substring(0,1)));
		boolean clearPath = SpecificPieceLegalityCheck.rookPathCheck(this.getBoard(), this.getBoardSquare(), dest);
		return ((rankChange > 0 && fileChange == 0) || (rankChange == 0 && fileChange > 0)) && clearPath;
	}
	public int getPieceValue() {
		return 5;
	}
	public PieceType getPieceType() {
		return type;
	}
	public String toDisplayString() {
		return (side == Side.WHITE) ? "R" : "r";
	}
}
