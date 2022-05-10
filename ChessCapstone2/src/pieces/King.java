package pieces;
import java.util.ArrayList;

import enums.*;
import game.Board;

public class King extends Piece {
	private PieceType type;
	public King(String file, int rank, Side side, Board board) {
		super(file, rank, side, board);
		type = PieceType.KING;
	}
	public King(BoardSquare square, Side side, Board board) {
		super(square, side, board);
		type = PieceType.KING;
	}
	public boolean checkBasicLegality(Board board,BoardSquare dest) {
		int rankChange = Math.abs(BoardSquare.getRank(dest)-getRank());
		int fileChange = Math.abs(Integer.parseInt(BoardSquare.valueOf(dest).substring(0,1))-Integer.parseInt(BoardSquare.valueOf(getBoardSquare()).substring(0,1)));
		return (rankChange == 1 && fileChange == 0) || (fileChange == 1 && rankChange == 0) || (rankChange == 1 && fileChange == 1);
	}
	public static boolean checkAdvancedLegality(Board board,BoardSquare prev,BoardSquare dest, Side side) {
		boolean destIsNotCheck = true;
		Side otherSide = side == Side.WHITE ? Side.BLACK : Side.WHITE;
		ArrayList<Move> moves = board.getPossibleMovesList(otherSide);
		for(Move i: moves) {
			if(i.getDest() == dest) {
				destIsNotCheck = false;
			}
		}
		return destIsNotCheck;
	}
	public int getPieceValue() {
		return 99;
	}
	public PieceType getPieceType() {
		return type;
	}
	public String toDisplayString() {
		return (side == Side.WHITE) ? "K" : "k";
	}
}
