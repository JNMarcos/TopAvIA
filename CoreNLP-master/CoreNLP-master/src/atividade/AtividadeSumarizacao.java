/**
 * 
 */
package atividade;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class AtividadeSumarizacao {

	/**
	 * @param args
	 */

	private static int numSentencasResumo = 3;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String filename = "sample.txt";

		//A stoplist usada como padrão advém da lista do site
		//http://xpo6.com/list-of-english-stop-words/
		//caso queira alterar as stopwords, basta modificá-las 
		//no arquivo stoplist.txt
		String filenameStopList = "stoplist.txt";

		String text = "";
		String stoplist = "";

		File f = new File(filename);
		Scanner sc = null;
		try {
			sc = new Scanner(f);
		} catch (FileNotFoundException e) {
			System.out.println("Arquivo não encontrado");
		}

		while (sc.hasNextLine()){
			text += sc.nextLine();
		}

		f = new File(filenameStopList);
		try {
			sc = new Scanner(f);
		} catch (FileNotFoundException e) {
			System.out.println("Arquivo não encontrado");
		}

		while (sc.hasNextLine()){
			stoplist += sc.nextLine();
		}

		sc.close();
		sumarizar(text, stoplist);

	}

	public static void sumarizar(String text, String stoplist){ 
		Hashtable<Integer, String> sentencas = new Hashtable<>();
		Hashtable<String, Double> palavrasSentenca;
		Hashtable<Integer, Hashtable<String, Double>> conjuntoSentPal = new Hashtable<>();

		//Descomente text caso queira sobrescrever ao que está no arquivo
		//text = "Albert was born in 1996. He is a boy very good! Congratulations!";

		//faz algumas substituições
		//pondo um espaço entre os sinais de pontuação e a letra anterior
		String newText = (text.replace(","," ,").replace("."," .")
				.replace("?", " ?").replace("!"," !").replace(";", " ;")
				.replace("\"", " \"").replace("\'", " \'")
				.replace(")", " )")
				.replace("(", "( ")
				);


		//corrige casos em que há pontos entre as palavras //ex.: i.e
		Pattern p = Pattern.compile("([a-z]) \\.([a-z])");
		Matcher m = p.matcher(newText);
		while(m.find()){
			char a = m.group(1).charAt(0);
			char b = m.group(2).charAt(0);
			newText = newText.replaceFirst("([a-z]) \\.([a-z])", a + "." + b);
		}

		//particiona o texto pelo espaço
		String[] parts = newText.split("\\s+");

		newText = "";
		for (String segment: parts){
			//a stoplist está comentada pois como é feito antes
			//if (!stoplist.contains(segment.toLowerCase())){
			if (!(segment.equals(",") || segment.equals(".") || segment.equals("?")
					|| segment.equals("!") || segment.equals(";")
					|| segment.equals(")")
					|| segment.equals("(")
					|| segment.equals("\"")
					|| segment.equals("\'")
					|| segment.equals("/"))){
				newText += " " + segment;
			}
			else {
				newText += segment;
			}
			//}
		}

		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma");

		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		Annotation document = new Annotation(newText);
		pipeline.annotate(document);

		System.out.println("\n-------------STEM--------------");

		ArrayList<String> palavras = new ArrayList<>();
		String chave = ""; // chave da hash
		List<CoreMap> sent = document.get(SentencesAnnotation.class);
		int quantidadeDeSentencas = sent.size();
		int tamanhoSent = 0;
		int indice = 0;	

		System.out.println("Há " + quantidadeDeSentencas + " sentenças no texto.\n");
		System.out.println("Original Text: " + text);

		quantidadeDeSentencas = 0;
		for(CoreMap sentence: sent) {
			//instacia uma sentença
			sentencas.put(quantidadeDeSentencas, sentence.toString());
			quantidadeDeSentencas++;

			palavrasSentenca = new Hashtable<>();

			System.out.println("\nSentence: " + sentence.toString());

			//para cada palavra que encontra na sentença
			//adiciona na hashtable de palavras que existem na sentença
			//a cada repetição de palavra adiona um a sua frequência
			for (CoreLabel cl: sentence.get(TokensAnnotation.class)){
				chave = cl.originalText().toLowerCase();
				//caso não queira que outras strings não sejam reconhecidas
				//adicione abaixo, seguindo o padrão
				if (chave.equals(".") || chave.equals(",")|| chave.equals("?") || 
						chave.equals("!") || chave.equals(";") || chave.equals("\"")
						|| chave.equals("\'") || chave.equals("-") ||
						chave.equals("(") || chave.equals(")")){
					continue;
				} else if (palavrasSentenca.containsKey(chave)){
					tamanhoSent++;
					palavrasSentenca.put(chave, palavrasSentenca.get(chave) + 1);
				} else {
					tamanhoSent++;
					palavrasSentenca.put(chave, 1.0);
					if(!palavras.contains(chave)){
						palavras.add(chave);
					}
				}
			}
			conjuntoSentPal.put(indice, (Hashtable<String, Double>) palavrasSentenca.clone());
			indice++;

			System.out.println("TF da sentença");
			for (Object a : palavrasSentenca.keySet().toArray()){
				palavrasSentenca.put((String) a, palavrasSentenca.get(a)/ tamanhoSent);
				System.out.println(palavras.get(palavras.indexOf(a)) + ":  " + palavrasSentenca.get(a));
			}

			tamanhoSent = 0;
		}

		//conta o número de sentenças q uma palavra aparece
		Hashtable<String, Integer> quantidadePorSetenca = new Hashtable<>();
		for(int i = 0; i < palavras.size(); i++){
			for(int j = 0; j < conjuntoSentPal.size(); j++ ){
				if(conjuntoSentPal.get(j).containsKey(palavras.get(i))){ //; verifica se a sentença possui a palavra
					if(quantidadePorSetenca.containsKey(palavras.get(i))){
						quantidadePorSetenca.put(palavras.get(i), quantidadePorSetenca.get(palavras.get(i)) + 1);
					}else{
						quantidadePorSetenca.put(palavras.get(i), 1);
					}
				}
			}
		}

		System.out.println("\n\nQuantidade de vezes que as palavras aparecem nas frases: " + quantidadePorSetenca.toString());

		Hashtable<Integer, Double> tfXidf = calcularIDF(conjuntoSentPal, quantidadePorSetenca, quantidadeDeSentencas, palavras);

		System.out.println("\nIDF das sentenças (por posição da sentença): " + tfXidf.toString());

		//pega os elementos (valores) da hash tfXidf, ou seja, o próprio valor
		//do índice		
		Enumeration e = tfXidf.elements();
		double[] tfIDFArray = new double[tfXidf.size()];
		double[] tfIDFArrayOrganizado;

		int it = 0;
		while (e.hasMoreElements()){
			tfIDFArray[it] =  (double) e.nextElement();
			it++;
		}

		//faz clone do array, para manter posição do original
		tfIDFArrayOrganizado = tfIDFArray.clone();

		//se o número de sentenças definido for menor que 
		//o texto original, faz a sumarização
		if (numSentencasResumo < quantidadeDeSentencas && numSentencasResumo >=1){
			System.out.println("\nAs " + numSentencasResumo + " sentenças de maior pontuação (RESUMO): ");
			Arrays.sort(tfIDFArrayOrganizado);
			for (int i = tfIDFArrayOrganizado.length - 1; 
					i >= (tfIDFArrayOrganizado.length - 1) - (numSentencasResumo - 1) ; 
					i--){
				for (int j = 0; j < tfIDFArray.length; j++){
					if (tfIDFArrayOrganizado[i] == tfIDFArray[j]){
						System.out.println(tfIDFArrayOrganizado[i] + ": " + sentencas.get(j));	
						break;
					}
				}	
			}
		} else {
			//caso contrário, não se faz sumarização, 
			//mas retorna o texto pela sua "ordem de importância" da frase
			System.out.println("\nAs " + quantidadeDeSentencas + " sentenças ordenas  pela pontuação: ");
			Arrays.sort(tfIDFArrayOrganizado);
			for (int i = tfIDFArrayOrganizado.length - 1; 
					i >= 0; 
					i--){
				for (int j = 0; j < tfIDFArray.length; j++){
					if (tfIDFArrayOrganizado[i] == tfIDFArray[j]){
						System.out.println(tfIDFArrayOrganizado[i] + ": " + sentencas.get(j));	
						break;
					}
				}	
			}
			
		}
	}

	private static Hashtable<Integer, Double> calcularIDF(Hashtable<Integer, Hashtable<String, Double>> conjuntoSentPal,
			Hashtable<String, Integer> quantidadePorSetenca, int quantidadeDeSentencas, ArrayList<String> palavras) {
		//conjuntoSentPal possui cada palavra e seu TF
		// quantidadeDeSentencas M
		// quantidadePorSetenca em quantas sentenças cada palavra está presente
		Hashtable<String, Double> idf = new Hashtable<>();
		for(int i = 0; i < palavras.size(); i++){
			idf.put(palavras.get(i), Math.log((double) (
					(quantidadeDeSentencas + 1)/
					quantidadePorSetenca.get(palavras.get(i)).doubleValue())));
		}
		Hashtable<Integer, Double> tfXidf = new Hashtable<>();
		for(int i = 0; i < conjuntoSentPal.size(); i++){
			for(int j = 0; j < palavras.size(); j++){
				if(conjuntoSentPal.get(i).containsKey(palavras.get(j))){
					if(tfXidf.get(i) == null){
						tfXidf.put(i, conjuntoSentPal.get(i).get(palavras.get(j)).doubleValue() * quantidadePorSetenca.get(palavras.get(j)).doubleValue());
					}else{
						tfXidf.put(i, tfXidf.get(i).doubleValue() + conjuntoSentPal.get(i).get(palavras.get(j)).doubleValue() *
								quantidadePorSetenca.get(palavras.get(j)).doubleValue() );
					}
				}
			}
		}
		
		return tfXidf;
	}
}
