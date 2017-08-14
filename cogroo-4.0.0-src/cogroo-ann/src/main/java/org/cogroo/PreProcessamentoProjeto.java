/**
 * 
 */
package org.cogroo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cogroo.analyzer.Analyzer;
import org.cogroo.analyzer.ComponentFactory;
import org.cogroo.checker.CheckDocument;
import org.cogroo.checker.GrammarChecker;
import org.cogroo.text.Document;
import org.cogroo.text.Sentence;
import org.cogroo.text.Token;
import org.cogroo.text.impl.DocumentImpl;

/**
 * @author JN
 *
 */
public class PreProcessamentoProjeto {

	private static ComponentFactory factory;
	private static Analyzer pipe;
	private static GrammarChecker gc;
	
	private static Hashtable<String, List<String>> mapaLemmas = new Hashtable<>();
	public static void main(String[] args) {
		Scanner sc = null;
		String caminhoPasta = "..\\BaseDecretosLimposPre\\";
		File arqSaida;
		String txt;
		String txtSaida;
		System.out.println("Lista de Lemmas");
		Pattern p = Pattern.compile("(\\d{1})(\\.)(\\d{1}(\\.))");
		Pattern p2 = Pattern.compile("(\\d{1}\\.\\d{1})(\\.)");
		Pattern p3 = Pattern.compile("</>");
		Matcher m;
		FileWriter fw = null;
		BufferedWriter bw = null;
		
		try {
			factory = ComponentFactory.create(new Locale("pt", "BR"));
			pipe = factory.createPipe();
			gc = new GrammarChecker(pipe);
			
		} catch (IllegalArgumentException | IOException e1) {
		}
		
		
		File f = new File("..\\BaseDecretosLimpos");
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
        	
        	
        	m = p.matcher(txt);
        	//while(m.find()){
        		//txt = txt.replaceFirst("(\\d{1})(\\.)(\\d{1}(\\.))", m.group(1) + "," + m.group(3) + ":");
        	//}
        	
        	m = p2.matcher(txt);
        	while(m.find()){
        		txt = txt.replaceFirst("(\\d{1}\\.\\d{1})(\\.)", m.group(1) + ":");
        	}
        	txt = txt.replace(".,", ",");
        	txt = txt.replace(".;", ";");
        	txt = txt.replace(".-", "-");
        	txt = txt.replace(".:", ":");
        	txt = txt.replaceAll("\\.{1,}", "");
        	System.out.println(txt);
        	List<String> lemmas = getLemma(txt);
			mapaLemmas.put(arq.getName(), lemmas);
			
			try {
				arqSaida = new File(caminhoPasta + arq.getName());
				arqSaida.createNewFile();
				fw = new FileWriter(arqSaida);
				bw = new BufferedWriter(fw);
			} catch (IOException e) {
				System.out.println("Pelo jeito...");
			}
			
			txtSaida = "";
			for (String s: lemmas){
				if (s == null){
					continue;
				} else if (s.equals(" ")){
					txtSaida += s; 
				} else {
					txtSaida += " " + s;
				}
			}
			
			System.out.println(txtSaida);
			try {
				bw.write(txtSaida);
				bw.flush();
				bw.close();
			} catch (IOException e) {
				System.out.println("Xiii");
			}
        }

	}
	
	public static ArrayList<String> getLemma(String txt){
		Document doc = new DocumentImpl();
		doc.setText(txt);
		pipe.analyze(doc);

		CheckDocument documento = new CheckDocument(txt);
		gc.analyze(documento);

		ArrayList<String> lemmas = new ArrayList<>();
		for (Sentence s: documento.getSentences()){
			for (Token t: s.getTokens()){
				if (t.getLemmas().length != 0){
					String lemma = t.getLemmas()[0];
					lemmas.add(lemma);
				}			
			}
		}
		return lemmas;
	}

}
