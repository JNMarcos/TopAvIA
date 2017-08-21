import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;

public class Imprimir {
	private static FileWriter fw = null;
	private static BufferedWriter bw = null;
	private static File arquivoSaida;
    
    public static void imprimirResultado(IndexSearcher buscador, ScoreDoc[] hits, 
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
            //System.out.println(d.get(LuceneConstantes.NOME_ARQUIVO) + "\n"
            	//	+ d.get(LuceneConstantes.TIPO) + "\n" + d.get(LuceneConstantes.CONTEUDO));
        bw.write(d.get(LuceneConstantes.NOME_ARQUIVO));
        bw.newLine();
        bw.write(d.get(LuceneConstantes.TIPO));
        bw.newLine();
        bw.newLine();
        
        }
        bw.flush();
        bw.close();
	}
    
    public static void imprimirTempo(double tempo, String nomeArquivo,
    		boolean modo) throws IOException{
    	arquivoSaida = new File(nomeArquivo);
		fw = new FileWriter(arquivoSaida, true);
        bw = new BufferedWriter(fw);
        bw.newLine();
        bw.newLine();
        
        if (modo)
        bw.write("Tempo em ms: " + tempo);
        else
        bw.write("Tempo médio em ms: " + tempo);
        
    	
        bw.flush();
        bw.close();
    }
}
