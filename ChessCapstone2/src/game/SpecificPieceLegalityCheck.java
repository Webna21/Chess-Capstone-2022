package game;

import enums.*;

public class SpecificPieceLegalityCheck {
	public static boolean castleLegality(Game game, BoardSquare prev, BoardSquare dest) {
		if(game.getBoard().getTile(prev).getPiece().getSide() == Side.WHITE) {
			if(game.getBoard().getTile(prev).getPiece().hasMoved()) return false;
			else if(BoardSquare.getFileValue(dest) > BoardSquare.getFileValue(prev)){
				if(game.getBoard().getTile(BoardSquare.h1).getPiece().getPieceType() == PieceType.ROOK && 
						game.getBoard().getTile(BoardSquare.h1).getPiece().getSide() == Side.WHITE) {
					if(game.getBoard().getTile(BoardSquare.f1).getPiece().getPieceType() == PieceType.EMPTY && 
							game.getBoard().getTile(BoardSquare.g1).getPiece().getPieceType() == PieceType.EMPTY && !game.getBoard().getTile(BoardSquare.h1).getPiece().hasMoved()) {
						return true;
					}
				} 
			} else if(BoardSquare.getFileValue(dest) < BoardSquare.getFileValue(prev)){
				if(game.getBoard().getTile(BoardSquare.a1).getPiece().getPieceType() == PieceType.ROOK && 
						game.getBoard().getTile(BoardSquare.a1).getPiece().getSide() == Side.WHITE) {
					if(game.getBoard().getTile(BoardSquare.b1).getPiece().getPieceType() == PieceType.EMPTY && 
							game.getBoard().getTile(BoardSquare.c1).getPiece().getPieceType() == PieceType.EMPTY && 
							game.getBoard().getTile(BoardSquare.d1).getPiece().getPieceType() == PieceType.EMPTY && !game.getBoard().getTile(BoardSquare.a1).getPiece().hasMoved()) {
						return true;
					}
				} 
			} 

		} else if(game.getBoard().getTile(prev).getPiece().getSide() == Side.BLACK) {
			if(game.getBoard().getTile(prev).getPiece().hasMoved()) return false;
			else if(BoardSquare.getFileValue(dest) > BoardSquare.getFileValue(prev)){
				if(game.getBoard().getTile(BoardSquare.h8).getPiece().getPieceType() == PieceType.ROOK && 
						game.getBoard().getTile(BoardSquare.h8).getPiece().getSide() == Side.BLACK) {
					if(game.getBoard().getTile(BoardSquare.f8).getPiece().getPieceType() == PieceType.EMPTY && 
							game.getBoard().getTile(BoardSquare.g8).getPiece().getPieceType() == PieceType.EMPTY && !game.getBoard().getTile(BoardSquare.h8).getPiece().hasMoved()) {
						return true;
					}
				}
			} else if(BoardSquare.getFileValue(dest) < BoardSquare.getFileValue(prev)){
				if(game.getBoard().getTile(BoardSquare.a8).getPiece().getPieceType() == PieceType.ROOK && 
						game.getBoard().getTile(BoardSquare.a8).getPiece().getSide() == Side.BLACK) {
					if(game.getBoard().getTile(BoardSquare.b8).getPiece().getPieceType() == PieceType.EMPTY && 
							game.getBoard().getTile(BoardSquare.c8).getPiece().getPieceType() == PieceType.EMPTY && 
							game.getBoard().getTile(BoardSquare.d8).getPiece().getPieceType() == PieceType.EMPTY && !game.getBoard().getTile(BoardSquare.a8).getPiece().hasMoved()) {
						return true;
					}
				}
			} 

		}
		return false;
	}
	public static boolean pawnCaptureLegality(Game game, BoardSquare prev, BoardSquare dest) {
		int rankChange = BoardSquare.getRank(dest)-BoardSquare.getRank(prev);
		int fileChange = Math.abs(Integer.parseInt(BoardSquare.valueOf(dest).substring(0,1))-Integer.parseInt(BoardSquare.valueOf(prev).substring(0,1)));
		Side thisSide = game.getBoard().getTile(prev).getPiece().getSide();
		Side enemySide = thisSide == Side.WHITE ? Side.BLACK : Side.WHITE;
		boolean correctDirection = false;
		if(thisSide == Side.WHITE) {
			if(BoardSquare.getRank(dest) > BoardSquare.getRank(prev)) {
				correctDirection = true;
			}
		} else {
			if(BoardSquare.getRank(dest) < BoardSquare.getRank(prev)) {
				correctDirection = true;
			}
		}
		return correctDirection && Math.abs(rankChange) == fileChange && fileChange == 1 && game.getBoard().getTile(dest).hasPiece() && game.getBoard().getTile(dest).getPiece().getSide() == enemySide;
	}
	public static boolean rookPathCheck(Board board, BoardSquare prev, BoardSquare dest) {
		boolean clearPath = true;
		if(BoardSquare.getRank(prev) == BoardSquare.getRank(dest)) {
			if(BoardSquare.getFileValue(prev) < BoardSquare.getFileValue(dest)) {
				for(char i = (char) (BoardSquare.getFile(prev).charAt(0)+1); i < BoardSquare.getFile(dest).charAt(0); i++) {
					if(board.getTile(BoardSquare.toBoardSquare(String.valueOf(i), BoardSquare.getRank(prev))).hasPiece()) {
						clearPath = false;
					}
				}
			} else {
				for(char i = (char) (BoardSquare.getFile(dest).charAt(0)+1); i < BoardSquare.getFile(prev).charAt(0); i++) {
					if(board.getTile(BoardSquare.toBoardSquare(String.valueOf(i), BoardSquare.getRank(prev))).hasPiece()) {
						clearPath = false;
					}
				}
			}
		} else if(BoardSquare.getFile(prev).equals(BoardSquare.getFile(dest))) {
			if(BoardSquare.getRank(prev) < BoardSquare.getRank(dest)) {
				for(int i = BoardSquare.getRank(prev)+1; i < BoardSquare.getRank(dest); i++) {
					if(board.getTile(BoardSquare.toBoardSquare(BoardSquare.getFile(prev), i)).hasPiece()) {
						clearPath = false;
					}
				}
			} else {
				for(int i = BoardSquare.getRank(dest)+1; i < BoardSquare.getRank(prev); i++) {
					if(board.getTile(BoardSquare.toBoardSquare(BoardSquare.getFile(prev), i)).hasPiece()) {
						clearPath = false;
					}
				}
			}
		} else {
			clearPath = false;
		}
		return clearPath;
	}
	public static boolean bishopPathCheck(Board board, BoardSquare prev, BoardSquare dest) {
		boolean clearPath = true;
		if(BoardSquare.getFileValue(prev) < BoardSquare.getFileValue(dest)) {
			if(BoardSquare.getRank(prev) < BoardSquare.getRank(dest)) {
				// file(prev) < file(dest) && rank(prev) < rank(dest)
				for(int i = 1; i <= BoardSquare.getRank(dest) - BoardSquare.getRank(prev) - 1; i++) {
					if(board.getTile(BoardSquare.toBoardSquare(String.valueOf((char) (BoardSquare.getFile(prev).charAt(0)+i)),(Integer.valueOf(BoardSquare.getRank(prev))+i))).hasPiece()) {
//						System.out.println(String.valueOf((char) (BoardSquare.getFile(prev).charAt(0)+i))+" "+(Integer.valueOf(BoardSquare.getRank(prev))+i));
						clearPath = false;
					}
				}
			} else { //filevalue(prev) < fileValue(dest) && rank(prev) > rank(dest)
				for(int i = 1; i <= BoardSquare.getRank(prev) - BoardSquare.getRank(dest) - 1; i++) {
					if(board.getTile(BoardSquare.toBoardSquare(String.valueOf((char) (BoardSquare.getFile(prev).charAt(0)+i)),(Integer.valueOf(BoardSquare.getRank(prev))-i))).hasPiece()) {
//						System.out.println(String.valueOf((char) (BoardSquare.getFile(prev).charAt(0)+i))+" "+(Integer.valueOf(BoardSquare.getRank(prev))-i));
						clearPath = false;
					}
				}
			}
			
		} else if(BoardSquare.getFileValue(prev) > BoardSquare.getFileValue(dest)) {
			if(BoardSquare.getRank(prev) > BoardSquare.getRank(dest)) {
				//file(prev) > file(dest) && rank(prev) > rank(dest)
				for(int i = 1; i <= BoardSquare.getRank(prev) - BoardSquare.getRank(dest) - 1; i++) {
					if(board.getTile(BoardSquare.toBoardSquare(String.valueOf((char) (BoardSquare.getFile(prev).charAt(0)-i)),(Integer.valueOf(BoardSquare.getRank(prev))-i))).hasPiece()) {
//						System.out.println(String.valueOf((char) (BoardSquare.getFile(prev).charAt(0)-i))+" "+(Integer.valueOf(BoardSquare.getRank(prev))-i));
						clearPath = false;
					}
				}
			} else { //file(prev) > file(dest) && rank(prev) < rank(dest)
				for(int i = 1; i <= BoardSquare.getRank(dest) - BoardSquare.getRank(prev) - 1; i++) {
					if(board.getTile(BoardSquare.toBoardSquare(String.valueOf((char) (BoardSquare.getFile(prev).charAt(0)-i)),(Integer.valueOf(BoardSquare.getRank(prev))+i))).hasPiece()) {
//						System.out.println(String.valueOf((char) (BoardSquare.getFile(prev).charAt(0)-i))+" "+(Integer.valueOf(BoardSquare.getRank(prev))+i));
						clearPath = false;
					}
				}
			}
		} else {
			clearPath = false;
		}
		return clearPath;
	}
	public static boolean pawnPathCheck(Board board, BoardSquare prev, BoardSquare dest) {
		boolean clearPath = true;
		if(BoardSquare.getRank(prev) < BoardSquare.getRank(dest)) {
			if(board.getTile(BoardSquare.toBoardSquare(BoardSquare.getFile(prev), BoardSquare.getRank(prev)+1)).hasPiece()) {
				clearPath = false;
			}
		} else if(BoardSquare.getRank(prev) > BoardSquare.getRank(dest)) {
			if(board.getTile(BoardSquare.toBoardSquare(BoardSquare.getFile(prev), BoardSquare.getRank(prev)-1)).hasPiece()) {
				clearPath = false;
			}
		} else {
			clearPath = false;
		}
		return clearPath;
	}
}
