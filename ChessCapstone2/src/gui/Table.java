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
	
	private final JFrame gameFrame;
	private final BoardPanel boardPanel;
    private Game ChessGame;
    
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece movingPiece;
	
	private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(700,700);
	private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400,350);
	private final static Dimension TILE_PANEL_DIMENSION = new Dimension(20,20);
	
    private Color lightTileColor = Color.decode("#eeeed2");
    private Color darkTileColor = Color.decode("#769656");
    
    private int c = 0;
	
	public Table() {
		/*
		Scanner sc = new Scanner(System.in);
		System.out.println("Select Player1 Side. (white/black)");
		String whichSide = sc.nextLine();
		if(whichSide.equals("black")) ChessGame = new Game(Side.BLACK);
		else if (whichSide.equals("white")) ChessGame = new Game(Side.WHITE);
		else {
			System.out.println("invalid input");
			System.exit(-1);
		}
		*/
		ChessGame = new Game(Side.WHITE);
		
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
							} else {
								ChessGame.movePiece(sourceTile.getBoardSquare(), destinationTile.getBoardSquare());
								sourceTile = null;
								destinationTile = null;
								movingPiece = null;
							}
						}
						SwingUtilities.invokeLater(new Runnable() {

							public void run() {
								boardPanel.drawBoard(ChessGame.getBoard());
								printDisplayTable();
								
								if(ChessGame.getCurrentTurn() == Side.BLACK) {
									Move toMove = RandomMoverWithCapture.makeRandomMoveWithCapture(ChessGame, Side.BLACK);
									ChessGame.movePiece(toMove.getPrev(), toMove.getDest());
								} else {
									Move toMove = RandomMoverWithCapture.makeRandomMoveWithCapture(ChessGame, Side.WHITE);
									ChessGame.movePiece(toMove.getPrev(), toMove.getDest());
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


