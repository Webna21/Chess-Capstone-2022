package other;

import java.util.ArrayList;

public class PGN {
	public static String toPGN(ArrayList<String> input) {
		String result = "";
		for(int i = 0; i < input.size(); i+=2) {
			if(i<input.size()-1) result+=(input.get(i) + " " + input.get(i+1)+ " ");
			else result+=(input.get(i) + " ");
		}
		
		return result;
	}
	public static void main(String[] args) {
		
	}
}
