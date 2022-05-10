package game;

import enums.*;

public class Testing {
	public static void main(String[] args) {
		Game a = new Game(Side.WHITE);
		Game b = new Game(Side.WHITE);
		b.setBoard(a.getBoard());
		b.setScoreSheet(a.getScoreSheet());
		b.setBCheckStatus(a.getBCheckStatus());
		b.setWCheckStatus(a.getWCheckStatus());
		b.setCurrentTurn(a.getCurrentTurn());
		b.setLastSideMover(a.getLastSideMover());
		b.setNumMoves(a.getNumMoves());
		
		
		
		
		a.movePiece(a.getBoard(), BoardSquare.e2, BoardSquare.e4);
		b.movePiece(b.getBoard(),BoardSquare.d2, BoardSquare.d4);
		System.out.println("a:");
		a.printDisplayGame();
		System.out.println("b:");
		b.printDisplayGame();
		
	}
}
