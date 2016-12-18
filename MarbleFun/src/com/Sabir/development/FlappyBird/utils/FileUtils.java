package com.Sabir.development.FlappyBird.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileUtils {
	private FileUtils(){
		
	}
	
	public static String loadAsString(String path){
		StringBuilder result = new StringBuilder();
		
		try{
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String buffer="";
			
			while((buffer = reader.readLine())!=null){
				result.append(buffer);
			}
			
			reader.close();
		}
		catch(IOException e){
			
		}
		
		return result.toString();
	}
}
