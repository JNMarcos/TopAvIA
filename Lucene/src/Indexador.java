import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.Directory;


public class Indexador {
	private File[] arquivos = null;
	private BrazilianAnalyzer analisador;
	private Directory diretorio;
	
	public Indexador(File[] arquivos, BrazilianAnalyzer analisador, 
			Directory diretorio){
		this.arquivos = arquivos;
		this.analisador = analisador;
		this.diretorio = diretorio;
	}
	
    public void indexar() throws IOException, ParseException {
    	IndexWriterConfig configuracao = new IndexWriterConfig(analisador);
    	IndexWriter w = new IndexWriter(diretorio, configuracao);
        
    	Scanner sc;

        
    	String texto;
        
        for (int i = 0; i < arquivos.length; i++){
        	texto = "";
        	sc = new Scanner(arquivos[i]);
        	while (sc.hasNextLine()){
        		texto += sc.nextLine();
        	}
        	
        	if (texto.contains(">")){
        	String corpoDecreto = "";
        	if (texto.contains("CORPO>")) corpoDecreto = texto.split("CORPO>")[1];
        	
        	String nomeDecreto = arquivos[i].getName().replaceFirst("\\.(txt|html|xml)", "");
        	
        	String ementaDecreto = "";
        	String consideracoesDecreto = "";
        	String ordenamentoDecreto = "";
        	String atribuicaoDecreto = "";
        	String assinaturasDecreto = "";
        	
        	if (texto.contains("EM>")) ementaDecreto = texto.split("EM>")[1];
        	if (texto.contains("CONSS>")) consideracoesDecreto = corpoDecreto.split("CONSS>")[1];
        	if (texto.contains("ORD>")) ordenamentoDecreto = corpoDecreto.split("ORD")[1];
        	if (texto.contains("ATRIB>")) atribuicaoDecreto = corpoDecreto.split("ATRIB")[1];
        	if (texto.contains("ASS>")) assinaturasDecreto = texto.split("ASS>")[1];
        	
        	addDoc(w, i + "", nomeDecreto, ementaDecreto, consideracoesDecreto,
        			ordenamentoDecreto, atribuicaoDecreto, assinaturasDecreto);
        	} else {
        		
        	}
        }
       
        w.close();
    }

    
    private static void addDoc(IndexWriter w, String titulo, String nome, 
    		String ementa, String consideracoes, String ordenamento, 
    		String atribuicao, String assinatura) throws IOException {
        Document doc = new Document();
        doc.add(new TextField(LuceneConstantes.NOME_CAMINHO, titulo, Field.Store.YES));
        doc.add(new TextField(LuceneConstantes.NOME_ARQUIVO, nome, Field.Store.YES));
        doc.add(new TextField(LuceneConstantes.EMENTA, ementa, Field.Store.YES));
        doc.add(new TextField(LuceneConstantes.CONSS, consideracoes, Field.Store.YES));
        doc.add(new TextField(LuceneConstantes.ORD, ordenamento, Field.Store.YES));
        doc.add(new TextField(LuceneConstantes.ATRIB, atribuicao, Field.Store.YES));
        doc.add(new TextField(LuceneConstantes.ASS, assinatura, Field.Store.YES));
        
        w.addDocument(doc);
    }
    
}