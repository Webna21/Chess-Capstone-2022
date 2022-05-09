package engine;

import java.util.ArrayList;

import enums.BoardSquare;
import enums.PieceType;
import enums.Side;
import game.Game;
import game.Move;

public class RandomMoverWithCapture {
	public static Move makeRandomMoveWithCapture(Game game, Side side){
		ArrayList<Move> moves = new ArrayList<Move>();
		ArrayList<Move> captureMoves = new ArrayList<Move>();
		
		for(BoardSquare i: BoardSquare.values()) {
			if(game.getBoard().getTile(i).getPiece().getPossibleMoveList(game.getBoard()).size() != 0 && game.getBoard().getTile(i).getPiece().getSide() == side) {
				ArrayList<Move> attempt = game.getBoard().getTile(i).getPiece().getPossibleMoveList(game.getBoard());
				for(Move j: attempt) {
					moves.add(j);
				}
			}
		}
		
		for(BoardSquare i: BoardSquare.values()) {
			if(game.getBoard().getTile(i).getPiece().getPossibleMoveList(game.getBoard()).size() != 0 && game.getBoard().getTile(i).getPiece().getSide() == side) {
				ArrayList<Move> attempt = game.getBoard().getTile(i).getPiece().getPossibleMoveList(game.getBoard());
				for(Move j: attempt) {
					if(j.isCapture(game.getBoard())) {
						captureMoves.add(j);
					}
				}
			}
		}
		for(Move i: captureMoves) {
			if(game.getBoard().getTile(i.getDest()).getPiece().getPieceType() == PieceType.KING) {
				return i;
			}
			moves.add(i);
			moves.add(i);
			moves.add(i);
			moves.add(i);			
		}
		int index = (int) (Math.random()*moves.size());
		return moves.get(index);
	}
}
