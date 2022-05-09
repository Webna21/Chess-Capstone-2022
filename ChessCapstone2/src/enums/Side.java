package enums;

public enum Side {
	WHITE,
	BLACK,
	NEUTRAL;
	
	public static String toString(Side side) {
		switch(side) {
		case WHITE:
			return "WHITE";
		case BLACK: 
			return "BLACK";
		default:
			return "NEUTRAL";
		}
	}
}
