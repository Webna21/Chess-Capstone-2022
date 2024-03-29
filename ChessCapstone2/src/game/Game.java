package game;

import java.util.*;
import enums.*;
import pieces.*;

public class Game {
	private Board gameBoard;
	private ArrayList<String> scoreSheet;
	private int numMoves;
	private Side currentTurn;
	private boolean wCheckStatus;
	private boolean bCheckStatus;
	private Side lastSideMover;
	
	public Game(Side side) {
		gameBoard = new Board(side);
		scoreSheet = new ArrayList<String>();
		numMoves = 0;
		lastSideMover = Side.BLACK;
		currentTurn = Side.WHITE;
		/*
		 * TODO:
		 * IMPORTANT
		 * PIECE PATH CANNOT HAVE PIECES
		 * FIX PAWN BUGS
		 * checkCheck
		 * arrayPossibleMoves
		 * Turn
		 * more in notebook
		 */
		wCheckStatus = false; 
		bCheckStatus = false;
	}
	public Side getCurrentTurn() {
		return currentTurn;
	}
	public Board getBoard() {
		return gameBoard;
	}
	public boolean getWCheckStatus() {
		return wCheckStatus;
	}
	public boolean getBCheckStatus() {
		return bCheckStatus;
	}
	public void setWCheckStatus(boolean check) {
		wCheckStatus = check;
	}
	public void setBCheckStatus(boolean check) {
		bCheckStatus = check;
	}
	public Game getGame() {
		return this;
	}
	public void setBoard(Board board) {
		gameBoard = board;
	}
	public void setScoreSheet(ArrayList<String> scoreSheet) {
		this.scoreSheet = scoreSheet;
	}
	public void setNumMoves(int n) {
		numMoves = n;
	}
	public void setCurrentTurn(Side s) {
		currentTurn = s;
	}
	public void setLastSideMover(Side s) {
		lastSideMover = s;
	}
	public void movePiece(Board board,BoardSquare x, BoardSquare y) {
		if(moveLegality(board,x,y) && currentTurn != Side.NEUTRAL) {
			if(currentTurn == Side.WHITE) numMoves++;
			if(currentTurn != Side.NEUTRAL) lastSideMover = currentTurn;
			moveInspection(x,y);
			writeScoreSheet(x,y);
			gameBoard.movePiece(x,y);
			if(currentTurn != Side.NEUTRAL) currentTurn = currentTurn == Side.WHITE ? Side.BLACK : Side.WHITE;	
			if(currentTurn == Side.NEUTRAL) {
				System.out.println("GAME OVER, CHECKMATE.");
				System.out.println(Side.toString(lastSideMover) + " WINS!");
			}
			
		} else System.out.println("illegal move");
	}
	public void testMovePiece(Board board, BoardSquare x, BoardSquare y) {
		board.movePiece(x, y);
	}
	public void movePiece(Board board,Move move) {
		movePiece(board,move.getPrev(),move.getDest());
	}
	public void castleKing(BoardSquare x, BoardSquare y) {
		if(SpecificPieceLegalityCheck.castleLegality(this, x, y) && gameBoard.getTile(x).getPiece().getSide() == currentTurn) {
			if(currentTurn == Side.WHITE) numMoves++;
			if(gameBoard.getTile(x).getPiece().getSide() == Side.WHITE) {
				if(BoardSquare.getFileValue(y) > BoardSquare.getFileValue(x)) {
					gameBoard.getTile(x).getPiece().incrementTimesMoved();
					gameBoard.getTile("h",1).getPiece().incrementTimesMoved();
					gameBoard.movePiece(BoardSquare.e1,BoardSquare.g1);
					gameBoard.movePiece(BoardSquare.h1,BoardSquare.f1);
					basicWriteScoreSheet("O-O");
				} else {
					gameBoard.getTile(x).getPiece().incrementTimesMoved();
					gameBoard.getTile("a",1).getPiece().incrementTimesMoved();
					gameBoard.movePiece(BoardSquare.e1,BoardSquare.c1);
					gameBoard.movePiece(BoardSquare.a1,BoardSquare.d1);
					basicWriteScoreSheet("O-O-O");
				}
			} else {
				if(BoardSquare.getFileValue(y) > BoardSquare.getFileValue(x)) {
					gameBoard.getTile(x).getPiece().incrementTimesMoved();
					gameBoard.getTile("h",8).getPiece().incrementTimesMoved();
					gameBoard.movePiece(BoardSquare.e8,BoardSquare.g8);
					gameBoard.movePiece(BoardSquare.h8,BoardSquare.f8);
					basicWriteScoreSheet("O-O");
				} else {
					gameBoard.getTile(x).getPiece().incrementTimesMoved();
					gameBoard.getTile("a",8).getPiece().incrementTimesMoved();
					gameBoard.movePiece(BoardSquare.e8,BoardSquare.c8);
					gameBoard.movePiece(BoardSquare.a8,BoardSquare.d8);
					basicWriteScoreSheet("O-O-O");
				}
			}
			currentTurn = currentTurn == Side.WHITE ? Side.BLACK : Side.WHITE;
		}
	}
	public void pawnCapture(BoardSquare x, BoardSquare y) {
		if(SpecificPieceLegalityCheck.pawnCaptureLegality(this, x, y) && gameBoard.getTile(x).getPiece().getSide() == currentTurn) {
			if(BoardSquare.getRank(y) == 1 || BoardSquare.getRank(y) == 8) {
				promotePawn(x,y);
			} else {
				if(currentTurn == Side.WHITE) numMoves++;
				gameBoard.getTile(x).getPiece().incrementTimesMoved();
				gameBoard.movePiece(x, y);
				pawnCaptureWriteScoreSheet(x,y);
				currentTurn = currentTurn == Side.WHITE ? Side.BLACK : Side.WHITE;
			}
		}
	}
	public void promotePawn(BoardSquare x, BoardSquare y) {
		if(!gameBoard.getTile(y).hasPiece() && gameBoard.getTile(x).getPiece().getPieceType() == PieceType.PAWN && Math.abs(BoardSquare.getRank(y)-BoardSquare.getRank(x))==1) {
			if(currentTurn == Side.WHITE) numMoves++;
			gameBoard.getTile(x).getPiece().incrementTimesMoved();

			String toPieceNotation;
			if(gameBoard.getTile(x).getPiece().getSide() == Side.WHITE) {
				gameBoard.getTile(y).setPiece(new Queen(BoardSquare.getFile(y),BoardSquare.getRank(y),Side.WHITE,gameBoard));
				gameBoard.getTile(x).setPiece(new Empty(BoardSquare.getFile(x),BoardSquare.getRank(x),gameBoard));
				toPieceNotation = "Q";
			} else {
				gameBoard.getTile(y).setPiece(new Queen(BoardSquare.getFile(y),BoardSquare.getRank(y),Side.BLACK,gameBoard));
				gameBoard.getTile(x).setPiece(new Empty(BoardSquare.getFile(x),BoardSquare.getRank(x),gameBoard));
				toPieceNotation = "q";
			}
			basicWriteScoreSheet(BoardSquare.getFile(x)+ "x" + BoardSquare.getFile(y) + "=" + toPieceNotation);
			currentTurn = currentTurn == Side.WHITE ? Side.BLACK : Side.WHITE;
		}
	}
	public void moveInspection(BoardSquare x, BoardSquare y) {
		gameBoard.getTile(x).getPiece().incrementTimesMoved();
		if(gameBoard.getTile(y).getPiece().getPieceType() == PieceType.KING) {
			currentTurn = Side.NEUTRAL;
		}
		inspectCheckStatus();
	}
	public boolean moveLegality(Board board,BoardSquare x, BoardSquare y) {
		//only move whoevers turns it is pieces
		if(gameBoard.getTile(x).getPiece().getSide() != currentTurn) return false;
		//cannot capture own pieces
		if(gameBoard.getTile(y).getPiece().getSide() == currentTurn) return false;
		//if pawn, cannot capture moving forward
		if(gameBoard.getTile(x).getPiece().getPieceType() == PieceType.PAWN && gameBoard.getTile(y).getPiece().getSide() == (currentTurn == Side.WHITE ? Side.BLACK : Side.WHITE));
		//check basic legality
		if(gameBoard.getTile(x).getPiece().checkBasicLegality(board,y) == false) return false;
		if(gameBoard.getTile(x).getPiece().getPieceType() == PieceType.KING) {
			if(gameBoard.getTile(x).getPiece().getSide() == Side.WHITE) {
				if(!King.checkAdvancedLegality(board, x, y, Side.WHITE)) return false;
			} else {
				if(!King.checkAdvancedLegality(board, x, y, Side.BLACK)) return false;
			}
		}
		return true;
	}
	public void inspectCheckStatus() {
		ArrayList<Move> whiteMoves = new ArrayList<Move>();
		ArrayList<Move> blackMoves = new ArrayList<Move>();
		
		for(BoardSquare i: BoardSquare.values()) {
			if(this.getBoard().getTile(i).getPiece().getPossibleMoveList(this.getBoard()).size() != 0 && this.getBoard().getTile(i).getPiece().getSide() == Side.WHITE) {
				ArrayList<Move> attempt = this.getBoard().getTile(i).getPiece().getPossibleMoveList(this.getBoard());
				for(Move j: attempt) {
					whiteMoves.add(j);
				}
			}
		}
		for(BoardSquare i: BoardSquare.values()) {
			if(this.getBoard().getTile(i).getPiece().getPossibleMoveList(this.getBoard()).size() != 0 && this.getBoard().getTile(i).getPiece().getSide() == Side.BLACK) {
				ArrayList<Move> attempt = this.getBoard().getTile(i).getPiece().getPossibleMoveList(this.getBoard());
				for(Move j: attempt) {
					blackMoves.add(j);
				}
			}
		}
		
		for(Move i: whiteMoves) {
			if(gameBoard.getTile(i.getDest()).getPiece().getPieceType() == PieceType.KING) {
				bCheckStatus = true;
			}
		}
		for(Move i: blackMoves) {
			if(gameBoard.getTile(i.getDest()).getPiece().getPieceType() == PieceType.KING) {
				wCheckStatus = true;
			}
		}
	}
	public static BoardSquare[] inputMove(String input1, String input2) {
		BoardSquare x = BoardSquare.toBoardSquare(input1.substring(0,1), Integer.parseInt(input1.substring(1,2)));
		BoardSquare y = BoardSquare.toBoardSquare(input2.substring(0,1), Integer.parseInt(input2.substring(1,2)));
		return new BoardSquare[] {x,y};
	}
	public ArrayList<String> getScoreSheet() {
		return scoreSheet;
	}
	public void basicWriteScoreSheet(String str) {
		StringBuilder move = new StringBuilder();
		move.append(str);
		if(currentTurn == Side.WHITE) move.insert(0,numMoves + ".");
		scoreSheet.add(move.toString());
	}
	public void pawnCaptureWriteScoreSheet(BoardSquare x, BoardSquare y) {
		StringBuilder move = new StringBuilder();
		move.append(BoardSquare.getFile(x));
		move.append("x");
		move.append(BoardSquare.getFile(y)+BoardSquare.getRank(y));
		if(currentTurn == Side.WHITE) move.insert(0,numMoves + ".");
		scoreSheet.add(move.toString());
		
	}
	public void writeScoreSheet(BoardSquare x, BoardSquare y) {
		StringBuilder move = new StringBuilder();
		move.append(gameBoard.getTile(x).getPiece().toDisplayString().toUpperCase());
		move.append(y);
		if(gameBoard.getTile(y).hasPiece()) move.insert(1,"x");
		if(gameBoard.getTile(x).getPiece().getPieceType() != PieceType.PAWN) move.insert(1, gameBoard.getTile(x).getFile());
		if(gameBoard.getTile(x).getPiece().getPieceType() == PieceType.PAWN) move.deleteCharAt(0);
		if(currentTurn == Side.WHITE) move.insert(0,numMoves + ".");
		scoreSheet.add(move.toString());
	}
	public String castlingForFEN() {
		String a = "";
		boolean WKingMoved = gameBoard.getTile("e",1).getPiece().getPieceType() != PieceType.KING || gameBoard.getTile("e",1).getPiece().hasMoved();
		boolean BKingMoved = gameBoard.getTile("e",8).getPiece().getPieceType() != PieceType.KING || gameBoard.getTile("e",8).getPiece().hasMoved();
		boolean WhRookMoved = gameBoard.getTile("h",1).getPiece().getPieceType() != PieceType.ROOK || gameBoard.getTile("h",1).getPiece().hasMoved();
		boolean WaRookMoved = gameBoard.getTile("a",1).getPiece().getPieceType() != PieceType.ROOK || gameBoard.getTile("a",1).getPiece().hasMoved();
		boolean BhRookMoved = gameBoard.getTile("h",8).getPiece().getPieceType() != PieceType.ROOK || gameBoard.getTile("h",8).getPiece().hasMoved();
		boolean BaRookMoved = gameBoard.getTile("a",8).getPiece().getPieceType() != PieceType.ROOK || gameBoard.getTile("a",8).getPiece().hasMoved();

		if(WKingMoved==false) {
			if(WhRookMoved==false) a += "K";
			if(WaRookMoved==false) a += "Q";
		}
		if(BKingMoved==false) {
			if(BhRookMoved==false) a += "k";
			if(BaRookMoved==false) a += "q";
		}
		return a;
	}
	public int getNumMoves() {
		return numMoves;
	}
	public Side getLastSideMover() {
		return lastSideMover;
	}
	public void printDisplayGame() {
		gameBoard.printDisplayBoard();
		System.out.println("----------------");
		System.out.println("scoreSheet: " + scoreSheet);
		System.out.println("currentTurn: " + currentTurn);
		System.out.println("wCheck: " + wCheckStatus);
		System.out.println("bCheck: " + bCheckStatus);
		System.out.println();
	}
	public void printGame() {
		gameBoard.printBoard();
		System.out.println("----------------");
		System.out.println("scoreSheet: " + scoreSheet);
		System.out.println("currentTurn: " + currentTurn);
		System.out.println();
	}
	public static void main(String[] args) {
		Game a = new Game(Side.WHITE);
		a.printGame();
	}
}
