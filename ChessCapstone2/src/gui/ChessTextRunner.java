package gui;

import java.util.Scanner;

import enums.BoardSquare;
import enums.PieceType;
import enums.Side;
import game.*;
import other.FEN;
import other.PGN;

public class ChessTextRunner {
	public static void main(String[] args) {
		Game a;
		Scanner sc = new Scanner(System.in);
//		System.out.println("Select Player1 Side. (white/black)");
//		String whichSide = sc.nextLine();
//		if(whichSide.equals("black")) a = new Game(Side.BLACK);
//		else a = new Game(Side.WHITE);
		a = new Game(Side.WHITE);
		
		boolean playOn = true;
		while(playOn) {
			a.printDisplayGame();
			String currentMover = String.valueOf(a.getCurrentTurn()).substring(0,1).toLowerCase();
			if(a.getBoard().getBotSide() == Side.WHITE) {
				System.out.println("FEN: " + FEN.WhiteToFEN(a.getBoard().displayBoardtoArrayList()) + " " + currentMover + " " + a.castlingForFEN());
				System.out.println("url: " + "https://lichess.org/analysis/" + FEN.WhiteToFEN(a.getBoard().displayBoardtoArrayList()) + "_" + currentMover + "_" + a.castlingForFEN());			
			} else {
				System.out.println("FEN: " + FEN.BlackToFEN(a.getBoard().displayBoardtoArrayList()) + " " + currentMover + " " + a.castlingForFEN());
				System.out.println("url: " + "https://lichess.org/analysis/" + FEN.BlackToFEN(a.getBoard().displayBoardtoArrayList()) + "_" + currentMover + "_" + a.castlingForFEN());
			}
			System.out.println("PGN: " + PGN.toPGN(a.getScoreSheet()));
			System.out.println("-----------------------------------------------------------");
			System.out.println("input square from:");
			String move1 = sc.nextLine();
			if(move1.equals("end")) {
				sc.close(); 
				System.exit(0);
			}
			System.out.println("input square to:");
			String move2 = sc.nextLine();
			if(move2.equals("end")) {
				sc.close(); 
				System.exit(0);
			}
			BoardSquare b1 = BoardSquare.toBoardSquare(move1.substring(0,1), Integer.parseInt(move1.substring(1,2)));
			BoardSquare b2 = BoardSquare.toBoardSquare(move2.substring(0,1), Integer.parseInt(move2.substring(1,2)));
			System.out.println("----------------------");
			if(a.getBoard().getTile(b1).getPiece().getPieceType() == PieceType.KING && 
					Math.abs(BoardSquare.getFileValue(b2) - BoardSquare.getFileValue(b1)) == 2){
				a.castleKing(b1, b2);
			} else if(a.getBoard().getTile(b1).getPiece().getPieceType() == PieceType.PAWN && Math.abs(BoardSquare.getRank(b2)-BoardSquare.getRank(b1)) == 1 &&
					Math.abs(BoardSquare.getFileValue(b2) - BoardSquare.getFileValue(b1)) == 1) {
				a.pawnCapture(b1, b2);
			} else {
				a.movePiece(a.getBoard(),b1, b2);
			}
		}
	}
}
