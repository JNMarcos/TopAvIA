import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collection;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;

public class LendoModelo {

	public static void main(String[] args) throws FileNotFoundException {
		
		//LENDO MODELO TREINADO
		Word2Vec vec = WordVectorSerializer.loadFullModel("pathToSaveModelPolitica.txt");
        
        Collection<String> lst = vec.wordsNearest("temer", 10);
        System.out.println(lst);       
        
        //Collection<String> kingList = vec.wordsNearest(Arrays.asList("temer", "mulher"), Arrays.asList("homem"), 10);
        //System.out.println(kingList);
        
        double cosSim = vec.similarity("temer", "presidente");
        System.out.println(cosSim);		        

	}

}
