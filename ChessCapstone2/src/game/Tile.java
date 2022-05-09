package game;
import enums.*;
import pieces.*;

public class Tile {
	private BoardSquare square;
	private String file;
	private int rank;
	private Piece piece;
	public Tile(String file, int rank, Piece piece) {
		square = BoardSquare.toBoardSquare(file, rank);
		this.file = file;
		this.rank = rank;
		this.piece = piece;
	}
	public Tile(BoardSquare square, Piece piece) {
		this.square = square;
		file = BoardSquare.getFile(square);
		rank = BoardSquare.getRank(square);
		this.piece = piece;
	}
	public BoardSquare getBoardSquare() {
		return square;
	}
	public String getFile() {
		return file;
	}
	public int getRank() {
		return rank;
	}
	public Piece getPiece() {
		return piece;
	}
	public void setPiece(Piece piece) {
		this.piece = piece;
	}
	public boolean hasPiece() {
		return this.piece.getPieceType() != PieceType.EMPTY;
	}
	public String toString() {
		return "T:" + square + piece;
	}
	public String toDisplayString() {
		return piece.toDisplayString();
	}
}
