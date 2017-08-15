import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;

/**
 * 
 */

/**
 * @author JN
 *
 */
public class Buscador {
	private BrazilianAnalyzer analisador;
	private Directory diretorio;
	
	private String consulta;
	private String modoConsulta;
	
	private FileWriter fw;
	private BufferedWriter bw;
	private File arquivoSaida;
	
	public Buscador(String consulta, String modoConsulta, BrazilianAnalyzer analisador, 
			Directory diretorio){
		this.consulta = consulta;
		this.modoConsulta = modoConsulta;
		this.analisador = analisador;
		this.diretorio = diretorio;	
	}
	public void buscar() throws ParseException, IOException{
		fw = null;
        bw = null;
        
        Query q = new QueryParser(modoConsulta, analisador).parse(consulta);
        
        /*
        IndexReader leitor = DirectoryReader.open(diretorio);
        IndexSearcher buscador = new IndexSearcher(leitor);
        
        TopDocs docs = buscador.search(q, LuceneConstantes.MAXIMO_RESULTADO);
        ScoreDoc[] hits = docs.scoreDocs;
        
        imprimir(buscador, hits, "RI-XML.txt");
        leitor.close();
        */
        
        
        IndexReader leitorT = DirectoryReader.open(diretorio);
        IndexSearcher buscadorT = new IndexSearcher(leitorT);
        
        TopDocs docs = buscadorT.search(q, LuceneConstantes.MAXIMO_RESULTADO);
        ScoreDoc[] hits = docs.scoreDocs;
        
        imprimir(buscadorT, hits, "RI-Tradicional_NOXML_Limpos.txt");
        leitorT.close();
      
	}
	
	public void imprimir(IndexSearcher buscador, ScoreDoc[] hits, 
			String nomeArquivo) throws ParseException, IOException{
		arquivoSaida = new File(nomeArquivo);
		arquivoSaida.createNewFile();
		fw = new FileWriter(arquivoSaida);
        bw = new BufferedWriter(fw);

        System.out.println(hits.length + " documentos encontrados.");
        bw.write(hits.length + " documentos encontrados.");
        bw.newLine();
        for(int i = 0;i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = buscador.doc(docId);
            System.out.println(d.get(LuceneConstantes.NOME_ARQUIVO) + "\n"
            		+ d.get(LuceneConstantes.TIPO) + "\n" + d.get(LuceneConstantes.CONTEUDO));
        bw.write(d.get(LuceneConstantes.NOME_ARQUIVO));
        bw.newLine();
        bw.write(d.get(LuceneConstantes.TIPO));
        bw.newLine();
        bw.newLine();
        
        }
        bw.flush();
        bw.close();
	}
}
