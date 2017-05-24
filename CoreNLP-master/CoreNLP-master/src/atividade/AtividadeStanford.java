package atividade;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.Stemmer;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.BasicDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.EnhancedDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.EnhancedPlusPlusDependenciesAnnotation;
import edu.stanford.nlp.util.CoreMap;

public class AtividadeStanford {
	public static void main(String[] args) {
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
		pipeline(text, stoplist);
	}

	//Para evitar demora na execução, basta executar uma função por vez no
	//pipeline OU
	//diminua o texto passado para as tarefas (modifique o arquivo sample.txt)
	//OU procure no começo de cada método pela variável string 'text' (comentada) 
	//e ponha seu novo texto pra sobrescrever o que há no arquivo
	public static void pipeline(String text, String stoplist){
		tokenizeAndSplitter(text);
		lemmas(text);
		ner(text);
		deTree(text);
		stemming(text, stoplist);
	}

	public static void tokenizeAndSplitter(String text){
		//Descomente text caso queira sobrescrever ao que está no arquivo
		//text = "Albert was born in 1996.";

		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit");

		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		Annotation document = new Annotation(text);
		pipeline.annotate(document);

		System.out.println("-------------SPLITTER--------------");
		System.out.println("Original Text: " + text);
		for(CoreMap sentence: document.get(SentencesAnnotation.class)) {
			System.out.println("\nSentence: " + sentence.toString());
			System.out.println("\nWords of Sentence\n");
			for (CoreLabel cl: sentence.get(TokensAnnotation.class)){
				System.out.println(cl.originalText());
			}
		}
	}
	
	public static void lemmas(String text){
		//Descomente text caso queira sobrescrever ao que está no arquivo
		//text = "Albert was born in 1996.";
		
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma");
		
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		
		Annotation documento = new Annotation(text);
		pipeline.annotate(documento);
		
		System.out.println("\n\n-------------LEMMAS--------------");
		System.out.println("Original Text: " + text);
		List<CoreMap> sentences = documento.get(SentencesAnnotation.class);
		for(CoreMap sentence: sentences){
			for(CoreLabel token: sentence.get(TokensAnnotation.class)){
				System.out.println((token.get(LemmaAnnotation.class)));
			}
		}
	}

	public static void ner (String text){
		//Descomente text caso queira sobrescrever ao que está no arquivo
		//text = "Albert was born in 1996.";
		
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma, ner");
		
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		Annotation document = new Annotation(text);
		pipeline.annotate(document);

		System.out.println("\n\n-------------NER--------------");
		System.out.println("Original Text: " + text);
		for(CoreMap sentence: document.get(SentencesAnnotation.class)) {
			System.out.println("\nSentence: " + sentence.toString());
			for (CoreLabel cl: sentence.get(TokensAnnotation.class)){
				System.out.println(cl.originalText() + ":  " + cl.ner());
			}
		}
	}

	public static void deTree(String text){
		//Descomente text caso queira sobrescrever ao que está no arquivo
		//text = "Albert was born in 1996.";
		
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma, parse");
		
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		//dá estouro de memória , nem chega aqui
		Annotation document = new Annotation(text);
		pipeline.annotate(document);

		System.out.println("\n\n-------------DETREE--------------");
		System.out.println("Original Text: " + text);
		for(CoreMap sentence: document.get(SentencesAnnotation.class)) {
			System.out.println("\nSentence: " + sentence.toString());

			//três tipos de árvore de dependência é gerado
			System.out.println("Basic Dependency: ");
			System.out.println(sentence.get(BasicDependenciesAnnotation.class));

			System.out.println("Enhanced Dependency: ");
			System.out.println(sentence.get(EnhancedDependenciesAnnotation.class));

			System.out.println("Enhanced++ Dependency: ");
			System.out.println(sentence.get(EnhancedPlusPlusDependenciesAnnotation.class));
		}
	}

	public static void stemming(String text, String stoplist){
		//Descomente text caso queira sobrescrever ao que está no arquivo
		//text = "Albert was born in 1996.";

		//faz algumas substituições
		//pondo um espaço entre os sinais de pontuação e a letra anterior
		String newText = (text.replace(","," ,").replace("."," .")
				.replace("?", " ?").replace("!"," !").replace(";", " ;")
				.replace("\"", " \"").replace("\'", " \'")
				.replace(")", " )")
				.replace("(", "( "));


		//corrige casos em que há pontos entre as palavras
		//ex.: i.e
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
			if (!stoplist.contains(segment.toLowerCase())){
				if (!(segment.equals(",") || segment.equals(".") || segment.equals("?") 
						|| segment.equals("!") || segment.equals(";"))){
					newText += " " + segment;
				}
				else {
					newText += segment;
				}
			}
		}

		Stemmer s = new Stemmer();
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma");
		
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		Annotation document = new Annotation(newText);
		pipeline.annotate(document);

		System.out.println("\n\n-------------STEM--------------");
		System.out.println("Original Text: " + text);
		for(CoreMap sentence: document.get(SentencesAnnotation.class)) {
			System.out.println("\nSentence: " + sentence.toString());
			for (CoreLabel cl: sentence.get(TokensAnnotation.class)){
				System.out.println(s.stem(cl.originalText()));
			}
		}
	}
}


