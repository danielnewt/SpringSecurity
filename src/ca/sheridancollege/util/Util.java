package ca.sheridancollege.util;

public class Util {
	
	public static boolean isEmpty(String string){
		if(string == null || string.trim().isEmpty()){
			return true;
		}
		return false;
	}
}
