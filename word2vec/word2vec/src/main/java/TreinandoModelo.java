

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collection;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.LineSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentencePreProcessor;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

public class TreinandoModelo {

	public static void main(String[] args) throws FileNotFoundException {		
        
        SentenceIterator iter = new LineSentenceIterator(new File("STS.input.MSRpar.txt"));
        iter.setPreProcessor(new SentencePreProcessor() {
            public String preProcess(String sentence) {
                return sentence.toLowerCase();
            }
        });
        
        
        TokenizerFactory t = new DefaultTokenizerFactory();
        t.setTokenPreProcessor(new CommonPreprocessor());
        
        
        Word2Vec vec = new Word2Vec.Builder()
                .minWordFrequency(1)
                .iterations(1)
                .layerSize(100)
                .seed(42)
                .windowSize(10)
                .iterate(iter)
                .tokenizerFactory(t)
                .build();

        vec.fit();
        
        //SALVANDO MODELO TREINADO
        WordVectorSerializer.writeFullModel(vec, "treinado.txt");
        
	}

}
