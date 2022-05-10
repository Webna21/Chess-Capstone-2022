package pieces;
import enums.*;
import game.Board;
import game.SpecificPieceLegalityCheck;

public class Bishop extends Piece {
	private PieceType type;
	public Bishop(String file, int rank, Side side, Board board) {
		super(file, rank, side, board);
		type = PieceType.BISHOP;
	}
	public Bishop(BoardSquare square, Side side, Board board) {
		super(square, side, board);
		type = PieceType.BISHOP;
	}
	public boolean checkBasicLegality(Board board,BoardSquare dest) {
		int rankChange = Math.abs(BoardSquare.getRank(dest)-getRank());
		int fileChange = Math.abs(Integer.parseInt(BoardSquare.valueOf(dest).substring(0,1))-Integer.parseInt(BoardSquare.valueOf(getBoardSquare()).substring(0,1)));
		boolean clearPath = SpecificPieceLegalityCheck.bishopPathCheck(this.getBoard(), this.getBoardSquare(), dest);
		return rankChange == fileChange && clearPath;
	}
	public PieceType getPieceType() {
		return type;
	}
	public int getPieceValue() {
		return 3;
	}
	public String toDisplayString() {
		return (side == Side.WHITE) ? "B" : "b";
	}
}
