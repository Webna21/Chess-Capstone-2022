package pieces;
import enums.*;
import game.Board;

public abstract class Piece {
	private BoardSquare square;
	private String file;
	private int rank;
	private int numTimesMoved;
	private Board board;
	Side side;
	public Piece(String file, int rank, Side side, Board board) {
		square = BoardSquare.toBoardSquare(file, rank);
		this.file = file;
		this.rank = rank;
		this.side = side;
		this.numTimesMoved = 0;
		this.board = board;
	}
	public Piece(BoardSquare square, Side side, Board board) {
		this.square = square;
		file = BoardSquare.getFile(square);
		rank = BoardSquare.getRank(square);
		this.side = side;
		this.numTimesMoved = 0;
		this.board = board;
	}
	public BoardSquare getBoardSquare() {
		return square;
	}
	public Board getBoard() {
		return board;
	}
	public String getFile() {
		return file;
	}
	public int getFileValue() {
		return BoardSquare.getFileValue(square);
	}
	public int getRank() {
		return rank;
	}
	public Side getSide() {
		return side;
	}
	public int getNumTimesMoved() {
		return numTimesMoved;
	}
	public boolean hasMoved() {
		return numTimesMoved == 0 ? false : true;
	}
	public void incrementTimesMoved() {
		numTimesMoved++;
	}
	public void setBoardSquare(BoardSquare square) {
		this.square = square;
		file = BoardSquare.getFile(this.square);
		rank = BoardSquare.getRank(this.square);
	}
	public void setFile(String file) {
		this.file = file;
		square = BoardSquare.toBoardSquare(this.file,rank);
	}
	public void setRank(int rank) {
		this.rank = rank;
		square = BoardSquare.toBoardSquare(file,this.rank);
	}
	public String toString() {
		return " P:" + square.toString() + " " + String.valueOf(getPieceType()).substring(0,4) + " " + side.toString().substring(0,5);
	}
	public int getRankChange(BoardSquare dest) {
		return Math.abs(BoardSquare.getRank(dest)-getRank());
	}
	public int getFileChange(BoardSquare dest) {
		return Math.abs(Integer.parseInt(BoardSquare.valueOf(dest).substring(0,1))-Integer.parseInt(BoardSquare.valueOf(getBoardSquare()).substring(0,1)));
	}
	public abstract boolean checkBasicLegality(BoardSquare dest);

	public abstract PieceType getPieceType();
	public abstract String toDisplayString();
}
