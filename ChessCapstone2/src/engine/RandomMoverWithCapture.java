package engine;

import java.util.ArrayList;

import enums.BoardSquare;
import enums.Move;
import enums.PieceType;
import enums.Side;
import game.Game;

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
				System.out.println("Forced");
				return i;
			}
		}
		if(side == Side.WHITE && game.getWCheckStatus() == true) {
			for(Move i: moves) {
				if(game.getBoard().getTile(i.getPrev()).getPiece().getPieceType() == PieceType.KING){
					return i;
				}
			}
		}
		if(side == Side.BLACK && game.getWCheckStatus() == true) {
			for(Move i: moves) {
				if(game.getBoard().getTile(i.getPrev()).getPiece().getPieceType() == PieceType.KING){
					return i;
				}
			}
		}
		ArrayList<Move> higherOrEqualMoves = new ArrayList<Move>();
		for(Move i: captureMoves) {
			if(game.getBoard().getTile(i.getDest()).getPiece().getPieceValue() >= game.getBoard().getTile(i.getPrev()).getPiece().getPieceValue()) {
				higherOrEqualMoves.add(i);
			}
		}
		if(higherOrEqualMoves.size()>0) {
			Move mostValue = higherOrEqualMoves.get(0);
			int highest = 0;
			for(Move i: higherOrEqualMoves) {
				if(game.getBoard().getTile(i.getDest()).getPiece().getPieceValue() > highest) {
					highest = game.getBoard().getTile(i.getDest()).getPiece().getPieceValue();
					mostValue = i;
				}
			}
			System.out.println("higher or equal highest");
			return mostValue;
		}
		for(Move i: captureMoves) {
			moves.add(i);
			moves.add(i);
			moves.add(i);
			moves.add(i);			
		}
		int index = (int) (Math.random()*moves.size());
		System.out.println("chosen");
		return moves.get(index);
	}
}
