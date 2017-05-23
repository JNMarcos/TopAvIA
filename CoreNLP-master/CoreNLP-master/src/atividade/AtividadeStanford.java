package atividade;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.SentenceUtils;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.DependencyParseAnnotator;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.BasicDependenciesAnnotation;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.util.CoreMap;

public class AtividadeStanford {


	public static void main(String[] args) {
		String filename = "sample.txt";
		File f = new File(filename);
		Scanner sc = null;
		try {
			sc = new Scanner(f);
		} catch (FileNotFoundException e) {
			System.out.println("Arquivo não encontrado");
		}
		String text = "";
		while (sc.hasNextLine()){
			text += sc.nextLine();
		}

		TokenizeAndSplitter(text);
		NER(text);
		DeTree(text);

	}

	public static void TokenizeAndSplitter(String text){
		//Descomente text caso queira sobrescrever ao que está no arquivo
		//text = "";
		
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit");

		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		Annotation document = new Annotation(text);
		pipeline.annotate(document);

		/*Usado para capturar as sentenças sem estarem particionadas*/
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

	public static void NER (String text){
		//Descomente text caso queira sobrescrever ao que está no arquivo
		//text = "";
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma, ner");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		Annotation document = new Annotation(text);
		pipeline.annotate(document);

		System.out.println("-------------NER--------------");
		System.out.println("Original Text: " + text);
		for(CoreMap sentence: document.get(SentencesAnnotation.class)) {
			System.out.println("\nSentence: " + sentence.toString());
			for (CoreLabel cl: sentence.get(TokensAnnotation.class)){
				System.out.println(cl.originalText() + ":  " + cl.ner());
			}
		}
	}

	public static void DeTree(String text){
		//Descomente text caso queira sobrescrever ao que está no arquivo
		text = "Albert was born in 1996.";
		Properties props = new Properties();

		props.put("annotators", "tokenize, ssplit, pos, depparse");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		//dá estouro de memória , nem chega aqui
		Annotation document = new Annotation(text);
		pipeline.annotate(document);
		
		System.out.println(document.get(BasicDependenciesAnnotation.class));

		System.out.println("-------------DETREE--------------");
		System.out.println("Original Text: " + text);
		//System.out.println(pipeline.getDependencyTreePrinter());
		/*for(CoreMap sentence: document.get(SentencesAnnotation.class)) {
			System.out.println("\nSentence: " + sentence.toString());
			System.out.println(sentence.get(BasicDependenciesAnnotation.class));
			for (CoreLabel cl: sentence.get(TokensAnnotation.class)){
				System.out.println(cl.originalText() + ":  " + cl.after());
			}
		}*/
	}

	public static void stemming(String text){
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		Annotation document = new Annotation(text);
		pipeline.annotate(document);

		MaxentTagger tagger = new MaxentTagger();
		List<List<HasWord>> sentences = null;
		try {
			sentences = MaxentTagger.tokenizeText(new BufferedReader(new FileReader("sample.txt")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (List<HasWord> sentence : sentences) {
			List<TaggedWord> tSentence = tagger.tagSentence(sentence);
			System.out.println(SentenceUtils.listToString(tSentence, false));
		}
	}
	/*
		System.out.println("Stop Words");
		System.out.println("Original Text: " + text);
		for(CoreMap sentence: document.get(SentencesAnnotation.class)) {
			System.out.println("\nSentence: " + sentence.toString());
			System.out.println("\nWords of Sentence\n");
			for (CoreLabel cl: sentence.get(TokensAnnotation.class)){
				System.out.println(cl.get(LemmaAnnotation.class));
			//	System.out.println(cl.lemma());
			}
		}*/

}


