package pieces;
import enums.*;
import game.Board;
import game.SpecificPieceLegalityCheck;

public class Pawn extends Piece {
	private PieceType type;
	public Pawn(String file, int rank, Side side, Board board) {
		super(file, rank, side, board);
		type = PieceType.PAWN;
	}
	public Pawn(BoardSquare square, Side side, Board board) {
		super(square, side, board);
		type = PieceType.PAWN;
	}
	public boolean checkBasicLegality(BoardSquare dest) {
		int rankChange = Math.abs(BoardSquare.getRank(dest)-this.getRank());
		int fileChange = Math.abs(Integer.parseInt(BoardSquare.valueOf(dest).substring(0,1))-Integer.parseInt(BoardSquare.valueOf(getBoardSquare()).substring(0,1)));
		
		boolean correctDirection = false;
		if(this.getSide() == Side.WHITE) {
			if(BoardSquare.getRank(dest) > BoardSquare.getRank(this.getBoardSquare())) {
				correctDirection = true;
			}
		} else {
			if(BoardSquare.getRank(dest) < BoardSquare.getRank(this.getBoardSquare())) {
				correctDirection = true;
			}
		}
		boolean clearPath = SpecificPieceLegalityCheck.pawnPathCheck(this.getBoard(),this.getBoardSquare(),dest);
		if(rankChange == 2 && this.getNumTimesMoved()!=0) {
			return false;
		}
		return rankChange <= 2 && fileChange == 0 && correctDirection && clearPath;
	}
	public PieceType getPieceType() {
		return type;
	}
	public String toDisplayString() {
		return (side == Side.WHITE) ? "P" : "p";
	}
}
