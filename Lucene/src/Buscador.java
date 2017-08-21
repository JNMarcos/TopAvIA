import java.io.IOException;

import org.apache.lucene.analysis.br.BrazilianAnalyzer;
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
	public void buscar(int iteracao) throws ParseException, IOException{
        
        Query q = new QueryParser(modoConsulta, analisador).parse(consulta);
        
        //PARA RI NÃO TRADICIONAL
        
        IndexReader leitor = DirectoryReader.open(diretorio);
        IndexSearcher buscador = new IndexSearcher(leitor);
        
        TopDocs docs = buscador.search(q, LuceneConstantes.MAXIMO_RESULTADO);
        ScoreDoc[] hits = docs.scoreDocs;
        
        if (iteracao == 0)
        Imprimir.imprimirResultado(buscador, hits, LuceneConstantes.NOME_ARQUIVO_SAIDA);
        leitor.close();
        
        
        
        //PARA RI TRADICIONAL
        /*
        IndexReader leitorT = DirectoryReader.open(diretorio);
        IndexSearcher buscadorT = new IndexSearcher(leitorT);
        
        TopDocs docs = buscadorT.search(q, LuceneConstantes.MAXIMO_RESULTADO);
        ScoreDoc[] hits = docs.scoreDocs;
        
        if (iteracao == 0)
        Imprimir.imprimirResultado(buscadorT, hits, LuceneConstantes.NOME_ARQUIVO_SAIDA);
        leitorT.close();
      */
	}
	
	
}
