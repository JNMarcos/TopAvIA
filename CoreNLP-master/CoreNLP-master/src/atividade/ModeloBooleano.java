/**
 * 
 */
package atividade;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author JN
 *
 */
public class ModeloBooleano {

	/**
	 * @param args
	 */
	static List<String> documentos = new ArrayList<>();
	static List<String> vocabulario = new ArrayList<>();
	static Hashtable<Integer, List<String>> vocabularioPorDocumento = new Hashtable<>();

	public static void main(String[] args) {
		//separe cada 'documento' por nova linha
		String nomeArquivo = "sample.txt";

		// A stoplist usada como padrão advém da lista do site
		// http://xpo6.com/list-of-english-stop-words/
		// caso queira alterar as stopwords, basta modificá-las
		// no arquivo stoplist.txt
		String nomeArquivoStopList = "stoplist.txt";

		//consulta a ser realizada
		String consulta = "Happy Albert";

		//a cada espaço, a consulta é separada, pois é um novo termo
		String[] partesConsulta = consulta.split(" ");
		//usada para converter a consulta para a forma binária
		List<Integer> consBinario = new ArrayList<>();

		String stoplist = "";

		File f = new File(nomeArquivo);
		Scanner sc = null;
		try {
			sc = new Scanner(f);
		} catch (FileNotFoundException e) {
			System.out.println("Arquivo não encontrado");
		}

		while (sc.hasNextLine()) {
			documentos.add(sc.nextLine());
		}

		//comente a próxima linha, caso não queira que o que haja
		//no arquivo não seja descartado
		//documentos.clear();

		//comente as próximas 3 linhas caso não queira que nada a mais 
		//ao que há no arquivo seja adicionado
		documentos.add("Albert was born in 1996."); 
		documentos.add("Alan Turing is great birthday.");
		documentos.add("Happy birthday to Alan.");

		f = new File(nomeArquivoStopList);
		try {
			sc = new Scanner(f);
		} catch (FileNotFoundException e) {
			System.out.println("Arquivo não encontrado");
		}

		while (sc.hasNextLine()) {
			stoplist += sc.nextLine();
		}

		sc.close();

		System.out.println("Documentos: ");
		pipeline(stoplist);
		System.out.print("\n");

		System.out.println("Consulta: " + consulta + "\n\nVocabulário");

		//cria-se o vocabulário a partir das palavras existentes em 
		for (int i = 0; i < vocabularioPorDocumento.size(); i++) {
			for (int j = 0; j < vocabularioPorDocumento.get(i + 1).size(); j++) {
				if (!vocabulario.contains(vocabularioPorDocumento.get(i + 1).get(j))) {
					vocabulario.add(vocabularioPorDocumento.get(i + 1).get(j));
					System.out.println(vocabularioPorDocumento.get(i + 1).get(j));
				}
			}
		}

		for (int i = 0; i < vocabulario.size(); i++) {
			consBinario.add(0);
		}

		int kk = 0;
		for (int i = 0; i < partesConsulta.length; i++){
			//busca-se o índice da palavra na consulta
			kk = vocabulario.indexOf(partesConsulta[i]);
			//se = -1, é pq na consulta há vocábulos q não existem
			//no vocabulário
			if (kk != -1) consBinario.set(kk, 1);	
		}

		//usado para representar cada documento como um vetor booleano
		List<Integer> docBinario;
		//armazena cada vetor booleano de todos os documentos
		Hashtable<Integer, List<Integer>> docIndex = new Hashtable<>();

		for (int j = 0; j < vocabularioPorDocumento.size(); j++) {
			docBinario = new ArrayList<>();
			for (int i = 0; i < vocabulario.size(); i++) {
				docBinario.add(0);
			}
			for (int k = 0; k < vocabularioPorDocumento.get(j + 1).size(); k++) {
				for (int i = 0; i < vocabulario.size(); i++) {
					if (vocabulario.get(i).contains((CharSequence) vocabularioPorDocumento.get(j + 1).get(k))) {
						docBinario.set(i, 1);
						break;
					}
				}
			}
			docIndex.put(j + 1, (ArrayList<Integer>) docBinario);
		}

		System.out.println("\nRepresentação dos Documentos em Vetores Booleanos");
		for (int i = 0; i < docIndex.size(); i++) {
			System.out.println("- documento " + (i + 1) + ": " +  docIndex.get(i + 1).toString());
		}

		System.out.println("\nRepresentação Binária da Consulta");
		System.out.println(consBinario.toString());

		System.out.println("\nRetorno da Consulta");
		String retConsulta = consulta(consBinario, docIndex);
		if (retConsulta != ""){
			System.out.println(retConsulta);
		} else {
			System.out.println("Nenhum documento encontrado.");
		}
	}

	// Para evitar demora na execução, basta executar uma função por vez no
	// pipeline OU
	// diminua o texto passado para as tarefas (modifique o arquivo sample.txt)
	// OU procure no começo de cada método pela variável string 'texto'
	// (comentada)
	// e ponha seu novo texto pra sobrescrever o que há no arquivo
	public static void pipeline(String stoplist) {
		for (int i = 0; i < documentos.size(); i++)
			obterVocabularioPorDocumento(documentos.get(i), stoplist, i + 1);
	}

	public static String consulta (List<Integer> consulta, 
			Hashtable<Integer, List<Integer>> documentosIdx){
		String retornoConsulta = "";
		boolean retornarDoc;
		for (int i = 0; i < documentosIdx.size(); i++){
			//de início, todo documento é tratado como retornado
			retornarDoc = true;
			if (documentosIdx.get(i + 1).contains(1)){
				for (int j = 0; j < consulta.size(); j++){
					//caso haja na consulta uma palvra e no documento não
					//ele não deve ser retornado
					if (consulta.get(j) == 1 && documentosIdx.get(i + 1).get(j) != 1){
						retornarDoc = false;
						break;
					} 
				}
				
				if (retornarDoc == true){
					retornoConsulta += "documento " + (i + 1) + ", "; 
					retornoConsulta += "texto: \"" + documentos.get(i)+ "\"\n";
				}
			}
		}
			return retornoConsulta;
	}

	public static void obterVocabularioPorDocumento(String texto, String stoplist, int num) {

		// Descomente texto caso queira sobrescrever ao que está no arquivo
		// texto = "Albert was born in 1996.";

		// faz algumas substituições
		// pondo um espaço entre os sinais de pontuação e a letra anterior
		String textoManipulacao = (texto.replace(",", " ,").replace(".", " .").replace("?", " ?").replace("!", " !")
				.replace(";", " ;").replace("\"", "\" ").replace("/", " / ")
				.replaceAll("\' ", " \'").replace(")", " )")
				.replace("(", "( "));

		// corrige casos em que há pontos entre as palavras
		// ex.: i.e
		Pattern p = Pattern.compile("([a-z]) \\.([a-z])");
		Matcher m = p.matcher(textoManipulacao);
		while (m.find()) {
			char a = m.group(1).charAt(0);
			char b = m.group(2).charAt(0);
			textoManipulacao = textoManipulacao.replaceFirst("([a-z]) \\.([a-z])", a + "." + b);
		}

		System.out.println("- documento " + (num) + ": "+ texto);

		// particiona o texto pelo espaço
		String[] parts = textoManipulacao.split("\\s+");

		textoManipulacao = "";
		List<String> vocabDocumento = new ArrayList<>();
		//só adiciona ao vocabulário de um texto, palavras que não estejam no 
		//stoplist, não seja nenhum dos caracteres de pontuação nem seja número
		for (String segment : parts) {
			if (!(stoplist.contains(segment.toLowerCase()) || 
					segment.equals(",") || segment.equals(".") || 
					segment.equals("?") || segment.equals("!")
					|| segment.equals(";") || segment.equals("/")
					|| segment.equals(")") || segment.equals("(")
					|| segment.equals("\"") || segment.equals("\'") || segment.matches("\\d{1,9}"))){
				vocabDocumento.add(segment);
			}
		}
		vocabularioPorDocumento.put(num, vocabDocumento);
	}
}
