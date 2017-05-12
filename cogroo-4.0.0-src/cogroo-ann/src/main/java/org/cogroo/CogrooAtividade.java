package org.cogroo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import org.cogroo.analyzer.Analyzer;
import org.cogroo.analyzer.ComponentFactory;
import org.cogroo.checker.CheckDocument;
import org.cogroo.checker.GrammarChecker;
import org.cogroo.text.Document;
import org.cogroo.text.Sentence;
import org.cogroo.text.Token;
import org.cogroo.text.impl.DocumentImpl;
import org.cogroo.text.tree.Node;
import org.cogroo.text.tree.TreeUtil;

public class CogrooAtividade {
	/** 
	     * @param args 
	     *          the language to be used, "pt_BR" by default 
	     * @throws IOException  
	     * @throws IllegalArgumentException  
	     */
	public static void main(String[] args) throws IllegalArgumentException, IOException {
		long start = System.nanoTime();

		String fraseLemmatization = "Ontem foi quarta, hoje é quinta e amanhã sexta";
		String frasePOSTagging = "Santa Cruz ficou no empate com o Atlético Paranaense. O clube paranaense perdeu um pênalti.";
		String fraseParser = "Essa árvore é muito útil.";

		System.out.println("Frase: " + fraseLemmatization);
		System.out.println("Lista de Lemmas");
		ArrayList<String> listaLemmas = getLemma(fraseLemmatization);
		for (String a: listaLemmas){
			System.out.println(a);
		}

		
		System.out.println("\nFrase: " + frasePOSTagging);
		System.out.println("POS Tagging");
		ArrayList<String> listaPOSES = getPOS(frasePOSTagging);
		for (String a: listaPOSES){
			System.out.println(a);
		}

		System.out.println("\nFrase: " + fraseParser);
		System.out.println("Árvore Sintática");
		ArrayList<Node> arvore = parser(fraseParser);
	}
	public static ArrayList<String> getLemma(String txt) throws IllegalArgumentException, IOException{
		ComponentFactory factory = ComponentFactory.create(new Locale("pt", "BR"));
		Analyzer pipe = factory.createPipe();
		GrammarChecker gc = new GrammarChecker(pipe);

		Document doc = new DocumentImpl();
		doc.setText(txt);
		pipe.analyze(doc);

		CheckDocument documento = new CheckDocument(txt);
		gc.analyze(documento);

		ArrayList<String> lemmas = new ArrayList<>();
		for (Sentence s: documento.getSentences()){
			for (Token t: s.getTokens()){
				String lemma = t.getLexeme() + ":    " + Arrays.toString(t.getLemmas());
				lemmas.add(lemma);
			}
		}
		return lemmas;
	}

	public static ArrayList<String> getPOS(String txt) throws IllegalArgumentException, IOException{
		ComponentFactory factory = ComponentFactory.create(new Locale("pt", "BR"));
		Analyzer pipe = factory.createPipe();
		GrammarChecker gc = new GrammarChecker(pipe);

		Document doc = new DocumentImpl();
		doc.setText(txt);
		pipe.analyze(doc);

		CheckDocument documento = new CheckDocument(txt);
		gc.analyze(documento);

		ArrayList<String> poses = new ArrayList<>();
		for (Sentence s: documento.getSentences()){
			for (Token t: s.getTokens()){
				String pos = t.getLexeme() + ":    " + t.getPOSTag();
				poses.add(pos);
			}
		}
		return poses;
	}
	public static ArrayList<Node> parser(String txt) throws IllegalArgumentException, IOException{
		ArrayList<Node> nodes = new ArrayList<>();
		ComponentFactory factory = ComponentFactory.create(new Locale("pt", "BR"));
		Analyzer pipe = factory.createPipe();
		GrammarChecker gc = new GrammarChecker(pipe);
		Document doc = new DocumentImpl();
		doc.setText(txt);
		pipe.analyze(doc);
		CheckDocument documento = new CheckDocument(txt);
		gc.analyze(documento);
		for (Sentence s: documento.getSentences()){
			Node no = TreeUtil.createTree(s);
			nodes.add(no);
			System.out.println(s.asTree());
		}
		return nodes;
	}

}
