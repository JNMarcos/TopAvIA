package atividade;

import java.net.MalformedURLException;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.sussex.nlp.jws.AdaptedLesk;
import edu.sussex.nlp.jws.AdaptedLeskTanimoto;
import edu.sussex.nlp.jws.HirstAndStOnge;
import edu.sussex.nlp.jws.JWS;
import edu.sussex.nlp.jws.JiangAndConrath;
import edu.sussex.nlp.jws.LeacockAndChodorow;
import edu.sussex.nlp.jws.Lin;
import edu.sussex.nlp.jws.Path;
import edu.sussex.nlp.jws.Resnik;
import edu.sussex.nlp.jws.WuAndPalmer;

public class WordNetParser {

  // Atributos
	
  private String wordNetHome;
  private IDictionary iDictionary;
  private JWS ws;
  private Lin lin;
  private HirstAndStOnge hirstAndStOnge;
  
  
  // Construtores
	
  public WordNetParser(String wordNetHome, String complemento) throws MalformedURLException {
	
    this.wordNetHome = wordNetHome;

    URL url = new URL ( "file:" + this.wordNetHome + complemento );
    
    this.iDictionary = new Dictionary ( url );
	
    this.iDictionary.open();

	this.ws = new JWS( this.wordNetHome, "3.0" );

	this.lin = this.ws.getLin();

//	this.hirstAndStOnge = this.ws.getHirstAndStOnge();
		
  }
  
  public List<String> recuperarDefinicoes(String termo) {

	if( termo == null || termo.length() == 0 ) {
		
	  return null;
	  
	}
	
    IIndexWord idxWord = this.iDictionary.getIndexWord( termo, POS.NOUN );
	
    if( idxWord == null ) {
    	
      return null;
      
    }
    
    List<String> definicoes = new ArrayList<String>();
    
    for(IWordID wordID : idxWord.getWordIDs()){
	
      IWord word = this.iDictionary.getWord( wordID );
		
      ISynset synset = word.getSynset();
	  
      if( synset == null ) {
    	  
        continue;
        
      }
      
      definicoes.add( synset.getGloss() );
      
    }
    
    if( definicoes.isEmpty() ) {
    	
      return null;
      
    }

    return definicoes;
    
  }
  
  public List<String> recuperarSinonimos(String termo) {
	  
    if( termo == null || termo.length() == 0 ) {
				
	  return null;
				  
	}
			
    termo = termo.toLowerCase();
    
    if( termo.startsWith( "the" ) ) {
    	
      termo = termo.substring( 3, termo.length() ).trim();
      
    }
    
    IIndexWord idxWord = this.iDictionary.getIndexWord( termo, POS.NOUN );
				
    if( idxWord == null ) {
	    	
      return null;
	      
    }
	    	
    Set<String> sinonimos = new HashSet<String>();
		    
    if( idxWord.getWordIDs() == null ) {
	    	
      return null;
	      
    }
	        
    for(IWordID wordID : idxWord.getWordIDs()){
			
      IWord word = this.iDictionary.getWord( wordID );
			
      ISynset synset = word.getSynset();
		      
	  List<IWord> sinonimosAUX = synset.getWords();
		  
	  for(IWord iWord: sinonimosAUX) {
		
		if( iWord.getLemma() == null )  {
				
		  continue;
			  
		}
			
		if( iWord.getLemma().equalsIgnoreCase( termo ) ) {
				
		  continue;
			  
		}
			
	    sinonimos.add( iWord.getLemma().replaceAll( "_", " " ) );
		    
	  }
			  
	  System.out.println( "\n" );
			  
    }
			
    System.out.println( "\n" );
	
    if( sinonimos.isEmpty() ) {
	    	
      return null;
      
    }
	    
    return new ArrayList<String>( sinonimos );
	    
  }

  public double calcularSimilaridadeLIN(String termo1, String termo2) {
	  
	//Lin lin2 = this.ws.getLin();
	  
    double similaridade = this.lin.max( termo1, termo2, "n" );
    
    return similaridade;
    
  }

  public double calcularSimilaridadePATH(String termo1, String termo2) {
	      
	Path path = this.ws.getPath();
		
    double similaridade = path.max( termo1, termo2, "n" );
	    
    return similaridade;
	    
  }

  public double calcularSimilaridadeAdaptedLesk(String termo1, String termo2) {
	      
	AdaptedLesk adaptedLesk = this.ws.getAdaptedLesk();
		
    double similaridade = adaptedLesk.max( termo1, termo2, "n" );
	    
    return similaridade;
	    
  }
  
  public double calcularSimilaridadeAdaptedLeskTanimoto(String termo1, String termo2) {
	      
	AdaptedLeskTanimoto adaptedLeskTanimoto = this.ws.getAdaptedLeskTanimoto();
		
    double similaridade = adaptedLeskTanimoto.max( termo1, termo2, "n" );
	    
    return similaridade;
	    
  }
  
  public double calcularSimilaridadeHirstAndStOnge(String termo1, String termo2) {
	      
    double similaridade = this.hirstAndStOnge.max( termo1, termo2, "n" );
	    
    return similaridade;
	    
  }
  
  public double calcularSimilaridadeJiangAndConrath(String termo1, String termo2) {
	      
    JiangAndConrath jiangAndConrath = this.ws.getJiangAndConrath();
		
    double similaridade = jiangAndConrath.max( termo1, termo2, "n" );
	    
    return similaridade;
	    
  }
  
  public double calcularSimilaridadeLeacockAndChodorow(String termo1, String termo2) {
	      
    LeacockAndChodorow leacockAndChodorow = this.ws.getLeacockAndChodorow();
		
    double similaridade = leacockAndChodorow.max( termo1, termo2, "n" );
	    
    return similaridade;
	    
  }
  
  public double calcularSimilaridadeResnik(String termo1, String termo2) {
	      
    Resnik resnik = this.ws.getResnik();
		
    double similaridade = resnik.max( termo1, termo2, "n" );
	    
    return similaridade;
	    
  }

  public double calcularSimilaridadeWuAndPalmer(String termo1, String termo2) {
	  	    
    WuAndPalmer wuAndPalmer = this.ws.getWuAndPalmer();
			
    double similaridade = wuAndPalmer.max( termo1, termo2, "n" );
		    
    return similaridade;
		    
  }

  public IDictionary getiDictionary() {
	
    return iDictionary;

  }

  public void setiDictionary(IDictionary iDictionary) {
	
    this.iDictionary = iDictionary;

  }
   
}
