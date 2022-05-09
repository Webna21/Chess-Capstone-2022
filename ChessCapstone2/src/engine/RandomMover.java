package engine;

import java.util.ArrayList;

import enums.BoardSquare;
import enums.Side;
import game.*;

public class RandomMover {
	public static Move makeRandomMove(Game game, Side side){
		ArrayList<Move> moves = new ArrayList<Move>();
		
		for(BoardSquare i: BoardSquare.values()) {
			if(game.getBoard().getTile(i).getPiece().getPossibleMoveList(game.getBoard()).size() != 0 && game.getBoard().getTile(i).getPiece().getSide() == side) {
				ArrayList<Move> attempt = game.getBoard().getTile(i).getPiece().getPossibleMoveList(game.getBoard());
				for(Move j: attempt) {
					moves.add(j);
				}
			}
		}
		int index = (int) (Math.random()*moves.size());
		return moves.get(index);
	}
}
