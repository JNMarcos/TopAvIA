package org.cogroo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class RemoverStop {
	public static void main(String[] args) {
		Scanner sc = null;
		String caminhoPasta = "..\\BaseDecretosStopWord\\";
		//String caminhoPasta = "..\\BaseDecretosLimposPreStopWord\\";
		File arqSaida = null;
		String txt = null;
		FileWriter fw = null;
		BufferedWriter bw = null;
		
		File arqStopwords = new File("..\\stopwords.txt");
		try {
			txt = "";
			sc = new Scanner(arqStopwords);
			
			while(sc.hasNextLine()){
        		txt += sc.nextLine(); //adicione a txt
        	}
		} catch (FileNotFoundException e1) {
		
		}
		
		String[] stopwords = txt.split(",");
		
		File f = new File("..\\BaseDecretos");
		//File f = new File("..\\BaseDecretosLimposPre");
		//lista todos os arquivos presentes na pasta
        File[] arquivos = f.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isFile();
			}
		});
        
        
        for (File arq : arquivos)
        {
        	txt = "";
        	
        	try {
				sc = new Scanner(arq);
			} catch (FileNotFoundException e) {
				System.out.println("ERROR");
			}
        	
        	System.out.println(arq.getName());
        	
        	//enquanto houver linhas no arquivo
        	while(sc.hasNextLine()){
        		txt += sc.nextLine(); //adicione a txt
        	}
        	
        	txt = txt.toUpperCase();
        	
        	
        	for (String stop: stopwords){
        		System.out.println(stop);
        		txt = txt.replaceAll(" " + stop.trim() + " ", " ");
        	}
        	
        	try {
				arqSaida = new File(caminhoPasta + arq.getName());
				arqSaida.createNewFile();
				fw = new FileWriter(arqSaida);
				bw = new BufferedWriter(fw);
			} catch (IOException e) {
				System.out.println("Pelo jeito...");
			}
        	
        	System.out.println(txt);
			try {
				bw.write(txt);
				bw.flush();
				bw.close();
			} catch (IOException e) {
				System.out.println("Xiii");
			}
			
        }
        
        
	}
}
