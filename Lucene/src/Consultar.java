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
	private static String caminhoBase = "..\\BaseDecretos";
	private static String consulta = "Paulista";
	private static String modoConsulta = LuceneConstantes.EMENTA;
    
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
		try {
			indexador.indexar();
			buscador.buscar();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
