package game;

import enums.*;

public class Move {
	private BoardSquare prev;
	private BoardSquare dest;
	public Move(BoardSquare prev, BoardSquare dest) {
		this.prev = prev;
		this.dest = dest;
	}
	public BoardSquare getPrev() {
		return prev;
	}
	public BoardSquare getDest() {
		return dest;
	}
	public String toString() {
		return BoardSquare.getFile(prev) + BoardSquare.getRank(prev) + "->" + BoardSquare.getFile(dest) + BoardSquare.getRank(dest);
	}
}
