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
	
	public Buscador(String consulta, String modoConsulta, BrazilianAnalyzer analisador, 
			Directory diretorio){
		this.consulta = consulta;
		this.modoConsulta = modoConsulta;
		this.analisador = analisador;
		this.diretorio = diretorio;
		
	}
	public void buscar() throws ParseException, IOException{
        Query q = new QueryParser(modoConsulta, analisador).parse(consulta);
        IndexReader leitor = DirectoryReader.open(diretorio);
        IndexSearcher buscador = new IndexSearcher(leitor);
        TopDocs docs = buscador.search(q, LuceneConstantes.MAXIMO_RESULTADO);
        
        ScoreDoc[] hits = docs.scoreDocs;

        System.out.println(hits.length + " documentos encontrados.");
        for(int i = 0;i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = buscador.doc(docId);
            System.out.println(d.get(LuceneConstantes.NOME_ARQUIVO) + "\n"
            		+ d.get(LuceneConstantes.EMENTA));
        }
        leitor.close();
	}
}
