package pieces;
import enums.*;
import game.Board;
import game.SpecificPieceLegalityCheck;

public class Queen extends Piece {
	private PieceType type;
	public Queen(String file, int rank, Side side, Board board) {
		super(file, rank, side, board);
		type = PieceType.QUEEN;
	}
	public Queen(BoardSquare square, Side side, Board board) {
		super(square, side, board);
		type = PieceType.QUEEN;
	}
	public boolean checkBasicLegality(BoardSquare dest) {
		int rankChange = Math.abs(BoardSquare.getRank(dest)-getRank());
		int fileChange = Math.abs(Integer.parseInt(BoardSquare.valueOf(dest).substring(0,1))-Integer.parseInt(BoardSquare.valueOf(getBoardSquare()).substring(0,1)));
		if(rankChange == fileChange) {
			boolean clearPath = SpecificPieceLegalityCheck.bishopPathCheck(this.getBoard(), this.getBoardSquare(), dest);
			return clearPath;

		} else {
			boolean clearPath = SpecificPieceLegalityCheck.rookPathCheck(this.getBoard(), this.getBoardSquare(), dest);
			return ((rankChange > 0 && fileChange == 0) || (rankChange == 0 && fileChange > 0)) && clearPath;

		}
	}
	public PieceType getPieceType() {
		return type;
	}
	public String toDisplayString() {
		return (side == Side.WHITE) ? "Q" : "q";
	}
}
