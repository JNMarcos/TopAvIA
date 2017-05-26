package atividade;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;

public class SimilaridadeSentencas {
	public static void main(String[] args) {
		String sentenca1 = "But other sources their";
		String sentenca2 = "But other sources";
		
		double similaridade = similaridadeSentencas(sentenca1, sentenca2);
		System.out.println("\n\n------------SIMILAR----------");
		System.out.println(sentenca1);
		System.out.println(sentenca2);
		System.out.println("Similiradidade entre as sentenças é: " + similaridade);
	}

	public static  double similaridadeSentencas(String sentenca1, String sentenca2){
		sentenca1 = corrigirSentenca(sentenca1);
		sentenca2 = corrigirSentenca(sentenca2);
		//System.out.println(sentenca1);
		//System.out.println(sentenca2);

		String[] s1 = sentenca1.split(" ");
		String[] s2 = sentenca2.split(" ");


		double[][] matrizSimilaridade = new double[s1.length][s2.length];

		//LENDO MODELO TREINADO
		Word2Vec vec = null;
		try {
			vec = WordVectorSerializer.loadFullModel("treinado.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		WordNetParser wordNetParser = null;
		try {
			wordNetParser = new WordNetParser( "WordNet", "/3.0/dict" );
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		double cosSim;
		double similaridadeLIN;
		double somaSim;

		int l = s1.length;
		int c = s2.length;
		
		System.out.println("\n\nEntradas Matriz");
		for (int i = 0; i < l; i++){
			System.out.println("Linha " + (i) + " da Matriz");
			for (int j = 0; j < c; j++){
				cosSim = vec.similarity(s1[i], s2[j]);
				similaridadeLIN = wordNetParser.calcularSimilaridadeLIN(s1[i], s2[j]);
				if (similaridadeLIN == 0){
					somaSim = cosSim;
				} else if (cosSim == 0){
					somaSim = similaridadeLIN;
				}else {
					somaSim = (cosSim + similaridadeLIN)/2;
				}
				matrizSimilaridade[i][j] = somaSim;
				System.out.println(matrizSimilaridade[i][j]);
			}
		}

		//há duas funções de similaridade, implementada de formas distintas
		return calcularSimilaridade(matrizSimilaridade, l, c);
		//return calcularSimilaridade2(matrizSimilaridade, l, c);
	}
	
	public static double calcularSimilaridade2(double[][] matrizSimilaridade, int l, int c){
		ArrayList<Double> maiores = new ArrayList<Double>();
		ArrayList<Point> indices = new ArrayList<Point>();
		int parar = 0;
		if(l > c){
			parar = l;
		}else{
			parar = c;
		}
		
		int ii;
		int jj;
		
		double maior;
		
		while(parar > 0){
		ii = 0;
		jj = 0;
		
		maior = 0;
        for (int i = 0; i < l; i++){
			for (int j = 0; j < c; j++){
				
				if(indices.size() == 0){
					if (matrizSimilaridade[i][j] > maior){
						maior = matrizSimilaridade[i][j];
						ii = i;
						jj = j;
					}
					
				}else{
					for(int k = 0; k < indices.size(); k++){
						if(i == indices.get(k).getX() && j == indices.get(k).getY()){
							if (matrizSimilaridade[i][j] > maior){
								maior = matrizSimilaridade[i][j];
								ii = i;
								jj = j;
							}
						}
					}
					
				}
			}
			
		}
        indices.add(new Point(ii,jj));
        maiores.add(maior);
        
        parar -= parar;
		}
        double total = 0;
        int div = 0;
		for(int n = 0; n <maiores.size(); n++){
			total += maiores.get(n);
			div += 1;
		}
        return total/div;
	}

	
	public static double calcularSimilaridade(double[][] matrizSimilaridade, int l, int c){
		int ii = 0;
		int jj = 0;

		double somaSimilaridade = 0;

		String linhasEliminadas = "";
		String colunasEliminadas = "";

		int menorDimensaoMatriz;
		if (l < c){
			menorDimensaoMatriz = l;
		} else {
			menorDimensaoMatriz = c;
		}

		double maior;
		for  (int dim = menorDimensaoMatriz; dim >= 0; dim--){
			maior = 0;
			for (int i = 0; i < l; i++){
				if(!linhasEliminadas.contains(" " + i + " ")){
					for (int j = 0; j < c; j++){
						if (!colunasEliminadas.contains(" " + j + " ")){
							if (matrizSimilaridade[i][j] > maior){
								maior = matrizSimilaridade[i][j];
								ii = i;
								jj = j;
							}
						}
					}
				}
			}
			linhasEliminadas += " " + ii + " ";
			colunasEliminadas += " " + jj + " ";
			somaSimilaridade += maior;
		}

		System.out.println("\nSoma similaridade: " + somaSimilaridade);
		System.out.println("Menor dimensão da matriz: " + menorDimensaoMatriz);

		return somaSimilaridade/menorDimensaoMatriz;
	}
	
	public static String corrigirSentenca(String sentenca){
		sentenca = sentenca.replaceAll(",", "").replaceAll(":", "").replaceAll("\\.", "").replaceAll(";", "").
				replaceAll("!", "").replaceAll("\\Q?\\E", "");
		sentenca = sentenca.toLowerCase();
		return sentenca;

	}
}
