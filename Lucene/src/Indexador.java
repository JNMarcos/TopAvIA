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
        	addDoc(w, i + "", arquivos[i].getName().replaceFirst("\\.(txt|html|xml)", ""), 
        			texto, texto.split("EM>")[1], texto.split("CORPO>")[1]);
        }
       
        w.close();
    }

    
    private static void addDoc(IndexWriter w, String titulo, String nome, 
    		String conteudo, String ementa, String corpo) throws IOException {
        Document doc = new Document();
        doc.add(new TextField(LuceneConstantes.NOME_CAMINHO, conteudo, Field.Store.YES));
        doc.add(new TextField(LuceneConstantes.CONTEUDO, conteudo, Field.Store.YES));
        doc.add(new TextField(LuceneConstantes.NOME_ARQUIVO, nome, Field.Store.YES));
        doc.add(new TextField(LuceneConstantes.CORPO, corpo, Field.Store.YES));
        doc.add(new TextField(LuceneConstantes.EMENTA, ementa, Field.Store.YES));
        
        w.addDocument(doc);
        //
    }
    
}