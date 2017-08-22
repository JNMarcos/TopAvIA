import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

/**
 * 
 */

/**
 * @author JN
 *
 */
public class Consultar {
	private static String caminhoBase = "..\\BaseDecretosStop";
	private static String consulta = "PAULISTA";
	private static String modoConsulta = LuceneConstantes.CONTEUDO;
    
	public static void main(String[] args) {
		BrazilianAnalyzer analisador = new BrazilianAnalyzer();
        Directory diretorio = new RAMDirectory();
        
	    File f = new File(caminhoBase);
        File[] arquivos = f.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isFile();
			}
		});
        
		Indexador indexador = new Indexador(arquivos, analisador, diretorio);

	
		
		Buscador buscador = new Buscador(consulta, modoConsulta, analisador, diretorio);
		long inicio;
		long diferenca;
		long media = 0;
		try {
			indexador.indexar();
			for (int i = 0; i < LuceneConstantes.N_ITERACOES; i++){
				inicio = System.currentTimeMillis();
				buscador.buscar(i);
				diferenca = System.currentTimeMillis() - inicio;
				media += diferenca;
				System.out.println("Tempo da " + i + "ª iteração em ms: " + diferenca);
				Imprimir.imprimirTempo(diferenca, LuceneConstantes.NOME_ARQUIVO_SAIDA,true);
		}
		System.out.println("Tempo Médio em ms: " + media/LuceneConstantes.N_ITERACOES);

		Imprimir.imprimirTempo(media/LuceneConstantes.N_ITERACOES, LuceneConstantes.NOME_ARQUIVO_SAIDA, false);

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
