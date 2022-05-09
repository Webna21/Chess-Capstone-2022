package enums;

import java.util.Arrays;

public enum BoardSquare {
	a1,a2,a3,a4,a5,a6,a7,a8,
	b1,b2,b3,b4,b5,b6,b7,b8,
	c1,c2,c3,c4,c5,c6,c7,c8,
	d1,d2,d3,d4,d5,d6,d7,d8,
	e1,e2,e3,e4,e5,e6,e7,e8,
	f1,f2,f3,f4,f5,f6,f7,f8,
	g1,g2,g3,g4,g5,g6,g7,g8,
	h1,h2,h3,h4,h5,h6,h7,h8,
	z0;
	
	public static BoardSquare toBoardSquare(String file, int rank) {
		String coordinate = file.toLowerCase() + rank;
		for(BoardSquare x : BoardSquare.values()) {
			if(coordinate.equals(String.valueOf(x))) {
				return x;
			}
		}
		return z0;
	}
	public static String getFile(BoardSquare square) {
		return String.valueOf(square).substring(0,1);
	}
	public static int getRank(BoardSquare square) {
		return Integer.parseInt(String.valueOf(square).substring(1,2));
	}
	public static int getFileValue(BoardSquare square) {
		return Integer.parseInt(valueOf(square).substring(0,1));
	}
	public static String valueOf(BoardSquare square) {
		return String.valueOf(square).charAt(0) - 96 + "" + getRank(square);
	}
	public static BoardSquare[] whiteSide(){
		BoardSquare[] result = new BoardSquare[64];
		int c = 0;
		for(int i = 8; i >= 1; i--) {
			for(BoardSquare x : BoardSquare.values()) {
				if(BoardSquare.getRank(x) == i) {
					result[c] = x;
					c++;
				}
			}
		}
		return result;
	}
	public static BoardSquare[] blackSide() {
		BoardSquare[] result = new BoardSquare[64];
		BoardSquare[] reverse = new BoardSquare[64];
		int c1 = 0;
		for(int i = 63; i >= 0; i--) {
			reverse[c1] = BoardSquare.values()[i];
			c1++;
		}
		int c = 0;
		for(int i = 1; i <= 8; i++) {
			for(BoardSquare x : reverse) {
				if(BoardSquare.getRank(x) == i) {
					result[c] = x;
					c++;
				}
			}
		}
		return result;
	}
	public static void main(String[] args) {
		System.out.println(Arrays.toString(blackSide()));
	}
}
