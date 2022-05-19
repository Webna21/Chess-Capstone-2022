package gui;

import java.awt.*;

import javax.swing.SwingUtilities;

import engine.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import enums.*;
import game.*;
import other.FEN;
import other.PGN;
import pieces.*;

public class Table {
	//JFrame GUI is from https://www.youtube.com/watch?v=h8fSdSUKttk&list=PLOJzCFLZdG4zk5d-1_ah2B4kqZSeIlWtt, everything else by me, Andrew Lis
	private final JFrame gameFrame;
	private final BoardPanel boardPanel;
    private Game ChessGame;
    
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece movingPiece;
    
    private Side mySide;
    private Side computerSide;
    private boolean withEngine;
    private boolean onlyEngine;
	
	private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(700,700);
	private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400,350);
	private final static Dimension TILE_PANEL_DIMENSION = new Dimension(20,20);
	
    private Color lightTileColor = Color.decode("#eeeed2");
    private Color darkTileColor = Color.decode("#769656");
    
    private int c = 0;
	
	public Table() {
		
		Scanner sc = new Scanner(System.in);
		String whichSide;
		do {
			System.out.println("Select Player1 Side. (white/black/none)");
			whichSide = sc.nextLine();
		} while(!whichSide.equalsIgnoreCase("black") && !whichSide.equalsIgnoreCase("white") && !whichSide.equalsIgnoreCase("none"));
		
		if(whichSide.equalsIgnoreCase("black")) {
			mySide = Side.BLACK; 
			computerSide = Side.WHITE;
		}
		else if(whichSide.equalsIgnoreCase("white")) {
			mySide = Side.WHITE; 
			computerSide = Side.BLACK;
		} else if(whichSide.equalsIgnoreCase("none")) {
			onlyEngine = true;
		}
		if(!onlyEngine) {
			String engineAnswer;
			do {
				System.out.println("Play against engine? (yes/no)");
				engineAnswer = sc.nextLine();
			} while(!engineAnswer.equalsIgnoreCase("yes") && !engineAnswer.equalsIgnoreCase("no"));
			
			if(engineAnswer.equalsIgnoreCase("yes")) {
				withEngine = true;
			} else if(engineAnswer.equalsIgnoreCase("no")) {
				withEngine = false;
			}
		}
		sc.close();
		ChessGame = new Game(mySide);
		
		this.gameFrame = new JFrame("ChessGUI");
		this.gameFrame.setLayout(new BorderLayout());
		
		this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
		
		this.boardPanel = new BoardPanel();
		this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
		
		this.gameFrame.setVisible(true);
	}
	
	@SuppressWarnings("serial")
	private class BoardPanel extends JPanel {
		final List<TilePanel> boardTiles;
		
		BoardPanel() {
			super(new GridLayout(8,8));
			this.boardTiles = new ArrayList<>();
			for(int i = 0; i < 64; i++) {
				final TilePanel tilePanel = new TilePanel(this,i);
				this.boardTiles.add(tilePanel);
				add(tilePanel);
			}
			setPreferredSize(BOARD_PANEL_DIMENSION);
			validate();
		}
		
		public void drawBoard(Board board) {
			removeAll();
			for(final TilePanel tilePanel : boardTiles) {
				tilePanel.drawTile(board);
				add(tilePanel);
			}
			validate();
			repaint();

		}
	}

	@SuppressWarnings("serial")
	private class TilePanel extends JPanel {
		
		private final BoardSquare square;
		
		TilePanel(final BoardPanel boardPanel, final int tileId) {
			super(new GridBagLayout());
			if(ChessGame.getBoard().getBotSide() == Side.WHITE) {
				this.square = BoardSquare.whiteSide()[c];
				c++;
			} else {
				this.square = BoardSquare.blackSide()[c];
				c++;
			}
			setPreferredSize(TILE_PANEL_DIMENSION);
			assignTileColor();
			assignTilePieceIcon(ChessGame.getBoard());
			
			
			addMouseListener(new MouseListener() {
				public void mouseClicked(final MouseEvent e) {
					if(SwingUtilities.isRightMouseButton(e)) {
						sourceTile = null;
						destinationTile = null;
						movingPiece = null;
					} else if(SwingUtilities.isLeftMouseButton(e)) {
						if(sourceTile == null) {
							sourceTile = ChessGame.getBoard().getTile(square);
							movingPiece = sourceTile.getPiece();
							if(movingPiece.getPieceType() == PieceType.EMPTY) {
								sourceTile = null;
								movingPiece = null;
							}
						} else {
							destinationTile = ChessGame.getBoard().getTile(square);
							if(destinationTile.getPiece().getSide() == ChessGame.getCurrentTurn()) {
								sourceTile = ChessGame.getBoard().getTile(square);
								destinationTile = null;
								movingPiece = sourceTile.getPiece();
							} else if(movingPiece.getPieceType() == PieceType.KING && Math.abs(BoardSquare.getFileValue(destinationTile.getBoardSquare()) - 
									BoardSquare.getFileValue(sourceTile.getBoardSquare())) == 2) {
								ChessGame.castleKing(sourceTile.getBoardSquare(), destinationTile.getBoardSquare());
							} else if(movingPiece.getPieceType() == PieceType.PAWN && Math.abs(destinationTile.getRank()-sourceTile.getRank()) == 1 &&
									Math.abs(BoardSquare.getFileValue(destinationTile.getBoardSquare()) - BoardSquare.getFileValue(sourceTile.getBoardSquare())) == 1) {
								ChessGame.pawnCapture(sourceTile.getBoardSquare(), destinationTile.getBoardSquare());
							} else if(sourceTile.getPiece().getPieceType() == PieceType.PAWN && (destinationTile.getRank() == 1 || destinationTile.getRank() == 8)){
								ChessGame.promotePawn(sourceTile.getBoardSquare(),destinationTile.getBoardSquare());
							} else {
								ChessGame.movePiece(ChessGame.getBoard(),sourceTile.getBoardSquare(), destinationTile.getBoardSquare());
								sourceTile = null;
								destinationTile = null;
								movingPiece = null;
							}
						}
						SwingUtilities.invokeLater(new Runnable() {

							public void run() {
								boardPanel.drawBoard(ChessGame.getBoard());
								printDisplayTable();
								if(withEngine) {
									engine1MoverWhite();
									engine1MoverBlack();
								} else if(onlyEngine) {
									engine1MoverBothSides();
								}
							}
						});
					}			
				}

				public void mousePressed(final MouseEvent e) {}

				public void mouseReleased(final MouseEvent e) {}

				public void mouseEntered(final MouseEvent e) {}

				public void mouseExited(final MouseEvent e) {}
			});
			
			
			validate();
		}
		public void drawTile(final Board board) {
			assignTileColor();
			assignTilePieceIcon(board);
			validate();
			repaint();
		}
		public void assignTilePieceIcon(Board board) {
			this.removeAll();
			if(board.getTile(square).hasPiece()) {
				try {
					final BufferedImage image = ImageIO.read(new File("ChessPieceImages/" + String.valueOf(board.getTile(square).getPiece().getSide()).substring(0,1) + String.valueOf(board.getTile(square).getPiece().getPieceType()).substring(0,1) + ".png"));
					add(new JLabel(new ImageIcon(image)));
				} catch(IOException e) {
					e.printStackTrace();
				}
		
			}
		}
		private void assignTileColor() {
			if(BoardSquare.getRank(square) % 2 == 0) setBackground(Integer.parseInt(BoardSquare.valueOf(square).substring(0,1)) % 2 != 0 ? lightTileColor : darkTileColor);
			if(BoardSquare.getRank(square) % 2 != 0) setBackground(Integer.parseInt(BoardSquare.valueOf(square).substring(0,1)) % 2 == 0 ? lightTileColor : darkTileColor);
		}
	}
	public void engine1MoverBothSides() {
		if(ChessGame.getCurrentTurn() == Side.BLACK) {
			Move toMove = engine1.moveEngine1(ChessGame, Side.BLACK);
			ChessGame.movePiece(ChessGame.getBoard(),toMove.getPrev(), toMove.getDest());
		} else if(ChessGame.getCurrentTurn() == Side.WHITE) {
			Move toMove = engine1.moveEngine1(ChessGame, Side.WHITE);
			ChessGame.movePiece(ChessGame.getBoard(),toMove.getPrev(), toMove.getDest());
		}
	}
	public void engine1MoverWhite() {
		if(ChessGame.getCurrentTurn() == Side.WHITE && computerSide == Side.WHITE) {
			Move toMove = engine1.moveEngine1(ChessGame, Side.WHITE);
			ChessGame.movePiece(ChessGame.getBoard(),toMove.getPrev(), toMove.getDest());
		}
	}
	public void engine1MoverBlack() {
		if(ChessGame.getCurrentTurn() == Side.BLACK && computerSide == Side.BLACK) {
			Move toMove = engine1.moveEngine1(ChessGame, Side.BLACK);
			ChessGame.movePiece(ChessGame.getBoard(),toMove.getPrev(), toMove.getDest());
		}
	}
	public void printTable() {
		ChessGame.printGame();
		System.out.println("source: " + sourceTile + " destination: " + destinationTile + " movingPiece: " + movingPiece);
		System.out.println("-----------------------------------------------------------");
	}
	public void printDisplayTable() {
		ChessGame.printDisplayGame();
		String currentMover = String.valueOf(ChessGame.getCurrentTurn()).substring(0,1).toLowerCase();
		if(ChessGame.getBoard().getBotSide() == Side.WHITE) {
			System.out.println("FEN: " + FEN.WhiteToFEN(ChessGame.getBoard().displayBoardtoArrayList()) + " " + currentMover + " " + ChessGame.castlingForFEN());
//			System.out.println("url: " + "https://lichess.org/analysis/" + FEN.WhiteToFEN(ChessGame.getBoard().displayBoardtoArrayList()) + "_" + currentMover + "_" + ChessGame.castlingForFEN());
		} else {
			System.out.println("FEN: " + FEN.BlackToFEN(ChessGame.getBoard().displayBoardtoArrayList()) + " " + currentMover + " " + ChessGame.castlingForFEN());
//			System.out.println("url: " + "https://lichess.org/analysis/" + FEN.BlackToFEN(ChessGame.getBoard().displayBoardtoArrayList()) + "_" + currentMover + "_" + ChessGame.castlingForFEN());
		}
		System.out.println("PGN: " + PGN.toPGN(ChessGame.getScoreSheet()));
		System.out.println("-----------------------------------------------------------");
	}
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Table table = new Table();
	}
}


