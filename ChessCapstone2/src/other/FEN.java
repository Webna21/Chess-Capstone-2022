package other;

import java.util.*;

public class FEN {
	public static String WhiteToFEN(ArrayList<String> input) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < input.size(); i++) {
			sb.append(input.get(i));
		}
		for(int i = sb.length()-1; i >= 0; i--) {
			if(i%8==0) {
				sb.insert(i,"/");
			}
		}
		sb.deleteCharAt(0);

		return dashesToNumber(sb.toString());
	}
	public static String BlackToFEN(ArrayList<String> input) {
		StringBuilder sb = new StringBuilder();
		sb.append(WhiteToFEN(input));
		return sb.reverse().toString();
	}
	public static String dashesToNumber(String input) {
		StringBuilder sb = new StringBuilder();
		sb.append(input);
		while(sb.toString().contains("-")) {
			int c = 1;
			int a = sb.indexOf("-");
			int b = sb.indexOf("-");
			while(sb.indexOf("-",a+1) == a + 1) {
				c++;
				a = sb.indexOf("-",a+1);
			}
			sb.replace(b,b+c,String.valueOf(c));
		}
		
		return sb.toString();
	}
	public static void main(String[] args) {
	}
}
