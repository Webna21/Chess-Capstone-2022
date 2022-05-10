package game;
import java.util.ArrayList;

import enums.*;
import pieces.*;

public class Board {
	private Tile[][] tileBoard;
	Side atBot;
	Side atTop;
	public Board(Side side) {
		tileBoard = new Tile[8][8];
		atBot = side;
		atTop = side == Side.WHITE ? Side.BLACK : Side.WHITE;
		int c = 0;
		if(side == Side.WHITE) {
			for(int i = 0; i < 8; i++) {
				for(int j = 7; j >= 0; j--) {
					tileBoard[j][i] = new Tile(BoardSquare.values()[c], new Empty(BoardSquare.values()[c],this));
					c++;}}} else {
			for(int i = 7; i >= 0; i--) {
				for(int j = 0; j < 8; j++) {
					tileBoard[j][i] = new Tile(BoardSquare.values()[c], new Empty(BoardSquare.values()[c],this));
					c++;}}}
		
		defaultBoard();
	}
	//empty board for piece path testing
	public Board() {
		tileBoard = new Tile[8][8];
		atBot = Side.WHITE;
		atTop = Side.BLACK;
		int c = 0;
		for(int i = 0; i < 8; i++) {
			for(int j = 7; j >= 0; j--) {
				tileBoard[j][i] = new Tile(BoardSquare.values()[c], new Empty(BoardSquare.values()[c],this));
				c++;}}
		
	}
	public void defaultBoard() {
		setPawns();
		setKnights();
		setBishops();
		setRooks();
		setQueens();
		setKings();
	}
	public ArrayList<Move> getPossibleMovesList(Side side){
		ArrayList<Move> moves = new ArrayList<Move>();
		for(BoardSquare i: BoardSquare.values()) {
			if(this.getTile(i).getPiece().getPossibleMoveList(this).size() != 0 && this.getTile(i).getPiece().getSide() == side) {
				ArrayList<Move> pieceMoves = this.getTile(i).getPiece().getPossibleMoveList(this);
				for(Move j: pieceMoves) {
					moves.add(j);
				}
			}
		}
		return moves;
	}
	public Tile[][] getTileBoard(){
		return tileBoard;
	}
	public Tile getTile(BoardSquare square) {
		for(Tile[] i: tileBoard) {
			for(Tile j: i) {
				if(j.getBoardSquare() == square) {
					return j;
				}
			}
		}
		return new Tile(BoardSquare.z0, new Empty(BoardSquare.z0,this));
	}
	public Tile getTile(String file, int rank) {
		return getTile(BoardSquare.toBoardSquare(file.toLowerCase(),rank));
	}
	public void movePiece(BoardSquare x, BoardSquare y) {
		getTile(y).setPiece(getTile(x).getPiece());
		getTile(y).getPiece().setBoardSquare(y);
		getTile(x).setPiece(new Empty(getTile(x).getBoardSquare(),this));

	}
	public void setPawns() {
		for(char i = 'a'; i <= 'h'; i++) {
			getTile(String.valueOf(i),2).setPiece(new Pawn(String.valueOf(i),2,Side.WHITE,this));
			getTile(String.valueOf(i),7).setPiece(new Pawn(String.valueOf(i),7,Side.BLACK,this));
		}
	}
	public void setKnights() {
		getTile("b",1).setPiece(new Knight("b",1,Side.WHITE,this));
		getTile("g",1).setPiece(new Knight("g",1,Side.WHITE,this));
		getTile("b",8).setPiece(new Knight("b",8,Side.BLACK,this));
		getTile("g",8).setPiece(new Knight("g",8,Side.BLACK,this));
	}
	public void setBishops() {
		getTile("c",1).setPiece(new Bishop("c",1,Side.WHITE,this));
		getTile("f",1).setPiece(new Bishop("f",1,Side.WHITE,this));
		getTile("c",8).setPiece(new Bishop("c",8,Side.BLACK,this));
		getTile("f",8).setPiece(new Bishop("f",8,Side.BLACK,this));
	}
	public void setRooks() {
		getTile("a",1).setPiece(new Rook("a",1,Side.WHITE,this));
		getTile("h",1).setPiece(new Rook("h",1,Side.WHITE,this));
		getTile("a",8).setPiece(new Rook("a",8,Side.BLACK,this));
		getTile("h",8).setPiece(new Rook("h",8,Side.BLACK,this));
	}
	public void setQueens() {
		getTile("d",1).setPiece(new Queen("d",1,Side.WHITE,this));
		getTile("d",8).setPiece(new Queen("d",8,Side.BLACK,this));
	}
	public void setKings() {
		getTile("e",1).setPiece(new King("e",1,Side.WHITE,this));
		getTile("e",8).setPiece(new King("e",8,Side.BLACK,this));
	}
	public ArrayList<String> displayBoardtoArrayList() {
		ArrayList<String> arr = new ArrayList<String>();
		for(Tile[] x: tileBoard) {
			for(Tile y: x) {
				arr.add(y.toDisplayString());
			}
		}
		return arr;
	}
	public Side getBotSide() {
		return atBot;
	}
	public void printBoard() {
		for(Tile[] x: tileBoard) {
			for(Tile y: x) {
				System.out.print(y.toString() + "\t");
			}
			System.out.println();
		}
	}

	public void printDisplayBoard() {
		for(Tile[] x: tileBoard) {
			for(Tile y: x) {
				System.out.print(y.toDisplayString() + " ");
			}
			System.out.println();
		}
	}
	public static void main(String[] args) {
		Board a = new Board();
		a.printBoard();
	}
}
