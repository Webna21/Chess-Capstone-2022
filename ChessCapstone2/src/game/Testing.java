package game;

import java.util.ArrayList;

import enums.*;
import gui.*;
import other.*;
import pieces.*;

public class Testing {
	public static void main(String[] args) {
		Game game1 = new Game(Side.WHITE);
		ArrayList<Move> moves = new ArrayList<Move>();
		for(BoardSquare i: BoardSquare.values()) {
			if(game1.getBoard().getTile(i).getPiece().getPossibleMoveList(game1.getBoard()).size() != 0) {
				ArrayList<Move> a = game1.getBoard().getTile(i).getPiece().getPossibleMoveList(game1.getBoard());
				for(Move j: a) {
					moves.add(j);
				}
			}
		}
		System.out.println(moves);
		System.out.println(moves.size());
		game1.printDisplayGame();
	}
}
