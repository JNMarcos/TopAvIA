/**
 * 
 */

/**
 * @author JN
 *
 */
public class Algoritmo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String texto = "Such an analysis can reveal features that are "
				+ "not easily visible from the variations in the "
				+ "individual genes and can lead to a picture of "
				+ "expression that is more biologically transparent and"
				+ " accessible to interpretation";
		
		String[] particoesTexto = substringsTexto(correcaoTexto(texto));
		particoesTexto = pipeline(particoesTexto);

		//teste somente regras1A
		/*String[] particoesTexto = substringsTexto("CARESSES PONIES CARESS CATS");
		particoesTexto = regras1A(particoesTexto);
		*/
		
		//teste somente regras1B
		/*String[] particoesTexto = substringsTexto("FEED AGREED PLASTERED BLED MOTORING SING");
		particoesTexto = regras1B(particoesTexto);
		*/
		
		//teste somente correcao1B (recebe uma string, diferente dos outros)
		/*System.out.println(correcaoRegras1B("CONFLAT"));
		System.out.println(correcaoRegras1B("TROUBL"));
		System.out.println(correcaoRegras1B("SIZ"));
		System.out.println(correcaoRegras1B("HOPP"));
		System.out.println(correcaoRegras1B("TANN"));
		System.out.println(correcaoRegras1B("FALL"));
		System.out.println(correcaoRegras1B("HISS"));
		System.out.println(correcaoRegras1B("FIZZ"));
		System.out.println(correcaoRegras1B("FAIL"));
		System.out.println(correcaoRegras1B("FIL"));
		*/
		
		//teste somente regras1C
		/*String[] particoesTexto = substringsTexto("HAPPY SKY");
		particoesTexto = regras1C(particoesTexto);
		*/
		
		//teste somente regras2
		/*String[] particoesTexto = substringsTexto("RELATIONAL CONDITIONAL VALENCI HESITANCI DIGITIZER CONFORMABLI RADICALLI DIFFERENTLI VILELI ANALOGOUSLI VIETNAMIZATION PREDICATION OPERATOR FEUDALISM DECISIVENESS HOPEFULNESS CALLOUSNESS FORMALITI SENSITIVITI SENSIBILITI");
		particoesTexto = regras2(particoesTexto);
		 */

		//teste somente regras3
		/*String[] particoesTexto = substringsTexto("TRIPLICATE FORMATIVE FORMALIZE ELECTRICITI ELECTRICAL HOPEFUL GOODNESS");
		particoesTexto = regras3(particoesTexto);
		 */

		//teste somente regras4
		/*String[] particoesTexto = substringsTexto("REVIVAL ALLOWANCE INFERENCE AIRLINER GYROSCOPIC ADJUSTABLE DEFENSIBLE IRRITANT REPLACEMENT ADJUSTMENT DEPENDENT ADOPTION HOMOLOGOU COMMUNISM ACTIVATE ANGULARITI HOMOLOGOUS EFFECTIVE BOWDLERIZE");
		particoesTexto = regras4(particoesTexto);
		 */
		
		//teste somente regras5A
		/*String[] particoesTexto = substringsTexto("PROBATE RATE CEASE");
		particoesTexto = regras5A(particoesTexto);
		*/
		
		//teste somente regras5B
		/*String[] particoesTexto = substringsTexto("CONTROLL ROLL");
		particoesTexto = regras5B(particoesTexto);
		*/

		System.out.println("Frase: " + texto);
		System.out.println("Lista de Lemmas");
		for (String a: particoesTexto){
			System.out.println(a);
		}
	}
	
	public static String[] pipeline(String[] particoesTexto){
		/*A modifição da ordem de execução das regras altera o resultado final*/
		particoesTexto = regras1A(particoesTexto);
		particoesTexto = regras1B(particoesTexto);
		particoesTexto = regras1C(particoesTexto);
		particoesTexto = regras2(particoesTexto);
		particoesTexto = regras3(particoesTexto);
		particoesTexto = regras4(particoesTexto);
		particoesTexto = regras5A(particoesTexto);
		particoesTexto = regras5B(particoesTexto);
		
		return particoesTexto;
	}

	/*Verifica se um caractere é vogal*/
	public static boolean isVogal(char c){
		switch(c){
		case 'A':
		case 'E':
		case 'I':
		case 'O':
		case 'U':
			return true;
		default: 
			return false;
		}
	}
	
	/*Verifica se num determinado trecho há ao menos uma vogal. 
	 *Leva em consideração a regra do "Y Vogal".
	 *Se aparece um Y na palavra, por si só é suficiente para dizer que tem uma 
	 *vogal na palavra, pois se há um caractere * antes dele ou é vogal ou é consoante.
	 *Se * é vogal, há uma vogal na palavra. Se * é consoante, Y é a vogal.
	 *Se não há caractere antes de Y, então Y é vogal.
	 */
	public static boolean temVogal(String txt){
		boolean ehVogal = false;
		for (int i = txt.length() - 1; i >= 0  && ehVogal == false; i--){
			ehVogal = isVogal(txt.charAt(i)); 
			if (txt.charAt(i) == 'Y') ehVogal = true;
		}
		return ehVogal;
	}

	/*Verifica se no stem há a combinação consoante-vogal-consoante.
	 *Sendo a última consoante diferente de X, Y e W*/
	public static boolean temCVC(String txt){
		boolean temCVC = false;
		if (txt.length() >= 3 && isVogal(txt.charAt(txt.length() - 3)) == false && 
				isVogal(txt.charAt(txt.length() - 2)) == true &&
				isVogal(txt.charAt(txt.length() - 1)) == false &&
				(txt.charAt(txt.length() - 1) != 'Y' && txt.charAt(txt.length() - 1) != 'W'
				&& txt.charAt(txt.length() - 1) != 'X')){
			temCVC = true;
		}
		return temCVC;
	}

	/*Verifica se o stem tem consoante dupla 
	 *(rr: duplo erre, ss: duplo esse, zz: duplo zê) no FIM 
	*/
	public static boolean temConsoanteDuplaNoFim(String txt){
		boolean temConsoanteDupla = false;

		//quando o tamanho da string for ao menos 2
		//e não for uma vogal
		//e forem os caracteres iguais
		if (txt.length() >= 2 && isVogal(txt.charAt(txt.length() - 1)) == false && 
				(txt.charAt(txt.length() - 1) == txt.charAt(txt.length() - 2))){
			temConsoanteDupla = true;
		}
		return temConsoanteDupla;
	}

	/*Verifica se há [C](VC)^m[C]
	 * ehMMaiorOuIgual se true, indica que nVezes para ser true 
	 * deve ser MAIOR que m (m > 1 ou m > 0, p. ex.)
	 * caso false (= igual), nVezes para ser true deve ser IGUAL a m (m=1)*/
	public static boolean temVC(String txt, int m, boolean ehMMaiorOuIgual){
		boolean temVC = false;
		int nVezes = 0;
		if (txt.length() >= 2){//se tamanho for menor que 2 já é falso
			if (ehMMaiorOuIgual){//nVezes deve ser maior que m
				for (int i = txt.length() - 1; i > 0 && nVezes <= m; i--){//vai até o penúltimo índice
					if (isVogal(txt.charAt(i-1)) == true && 
							isVogal(txt.charAt(i)) == false){
						nVezes++;
					}
				}

				if (nVezes > m) temVC = true;
			} else{//nVezes deve ser IGUAL m
				for (int i = txt.length() - 1; i > 0; i--){//vai até o penúltimo índice
					if (isVogal(txt.charAt(i-1)) == true && 
							isVogal(txt.charAt(i)) == false){
						nVezes++;
					} 
				}
				if (nVezes == m) temVC = true;
			}
		}
		return temVC;
	}

	/*Normaliza o texto*/
	public static String correcaoTexto(String texto){
		//remove os sinais de pontuação comuns
		texto = texto.replace(",","").replace(".","").replace("?", "").replace("!","").replace(";", "")
				.replace("-", "");
		//remove os espaços em branco em excesso
		texto = texto.replace("  ", " ").trim();
		//passa todas as letras para maiúscula
		texto = texto.toUpperCase();
		return texto;
	}

	/*Particiona o texto*/
	public static String[] substringsTexto(String texto){
		//retorna o texto partido nos espaços
		return texto.split(" ");
	}

	public static String[] regras1A(String[] txt){
		for (int i = 0; i < txt.length; i++){
			//sses -> es
			if (txt[i].endsWith("SSES")) txt[i] = txt[i].substring(0,txt[i].length() - 2);
			//ies -> i
			else if (txt[i].endsWith("IES")) txt[i] = txt[i].substring(0,txt[i].length() - 2);
			else if (txt[i].endsWith("SS"));	//faz nada
			//s -> 
			else if (txt[i].endsWith("S")) txt[i] = txt[i].substring(0,txt[i].length() - 1);
		}	
		return txt;
	}

	public static String[] regras1B(String[] txt){
		for (int i = 0; i < txt.length; i++){
			if (txt[i].endsWith("EED")){
				if (temVC(txt[i].substring(0, txt[i].length() - 2), 0, true)){
					txt[i] = txt[i].substring(0,txt[i].length() - 1);
					txt[i] = correcaoRegras1B(txt[i]);
				}
			} else if (txt[i].endsWith("ED")){
				if (temVogal(txt[i].substring(0, txt[i].length() - 2))){
					txt[i] = txt[i].substring(0,txt[i].length() - 2);
					txt[i] = correcaoRegras1B(txt[i]);
				}
			} else if (txt[i].endsWith("ING")){
				if (temVogal(txt[i].substring(0, txt[i].length() - 3))){
					txt[i] = txt[i].substring(0,txt[i].length() - 3);
					txt[i] = correcaoRegras1B(txt[i]);
				}
			}
		}
		return txt;
	}

	public static String correcaoRegras1B(String txt){
		if (txt.endsWith("AT") || txt.endsWith("BL") || txt.endsWith("IZ")){
			txt = txt + "E";
		} else if (temConsoanteDuplaNoFim(txt) && 
				!(txt.endsWith("L") || txt.endsWith("Z") || txt.endsWith("S"))){
			txt = txt.substring(0,txt.length() - 1);
		} else if (temVC(txt, 1, false) && temCVC(txt)){
			txt = txt + "E";
		}
		return txt;
	}

	public static String[] regras1C(String[] txt){
		for (int i = 0; i < txt.length; i++){
			if (txt[i].endsWith("Y") && temVogal(txt[i].substring(0, txt[i].length() - 1))){
				txt[i] = txt[i].substring(0,txt[i].length() - 1) + "I";
			}
		}
		return txt;
	}

	public static String[] regras2(String[] txt){
		for (int i = 0; i < txt.length; i++){
			if (txt[i].endsWith("ATIONAL") && temVC(txt[i].substring(0, txt[i].length() - 7), 0, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 7) + "ATE";
			} else if (txt[i].endsWith("TIONAL") && temVC(txt[i].substring(0, txt[i].length() - 6), 0, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 2);
			} else if (txt[i].endsWith("ENCI") && temVC(txt[i].substring(0, txt[i].length() - 4), 0, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 1) + "E";
			} else if (txt[i].endsWith("ANCI") && temVC(txt[i].substring(0, txt[i].length() - 4), 0, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 1) + "E";
			} else if (txt[i].endsWith("IZER") && temVC(txt[i].substring(0, txt[i].length() - 4), 0, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 1);
			} else if (txt[i].endsWith("ABLI") && temVC(txt[i].substring(0, txt[i].length() - 4), 0, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 1) + "E";
			} else if (txt[i].endsWith("ALLI") && temVC(txt[i].substring(0, txt[i].length() - 4), 0, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 2);
			} else if (txt[i].endsWith("ENTLI") && temVC(txt[i].substring(0, txt[i].length() - 5), 0, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 2);
			} else if (txt[i].endsWith("ELI") && temVC(txt[i].substring(0, txt[i].length() - 3), 0, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 2);
			} else if (txt[i].endsWith("OUSLI") && temVC(txt[i].substring(0, txt[i].length() - 5), 0, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 2);
			} else if (txt[i].endsWith("IZATION") && temVC(txt[i].substring(0, txt[i].length() - 7), 0, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 5) + "E";
			} else if (txt[i].endsWith("ATION") && temVC(txt[i].substring(0, txt[i].length() - 5), 0, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 3) + "E";
			} else if (txt[i].endsWith("ATOR") && temVC(txt[i].substring(0, txt[i].length() - 4), 0, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 2) + "E";
			} else if (txt[i].endsWith("ALISM") && temVC(txt[i].substring(0, txt[i].length() - 5), 0, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 3);
			} else if (txt[i].endsWith("IVENESS") && temVC(txt[i].substring(0, txt[i].length() - 7), 0, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 4);
			} else if (txt[i].endsWith("FULNESS") && temVC(txt[i].substring(0, txt[i].length() - 7), 0, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 4);
			} else if (txt[i].endsWith("OUSNESS") && temVC(txt[i].substring(0, txt[i].length() - 7), 0, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 4);
			} else if (txt[i].endsWith("ALITI") && temVC(txt[i].substring(0, txt[i].length() - 5), 0, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 3);
			} else if (txt[i].endsWith("IVITI") && temVC(txt[i].substring(0, txt[i].length() - 5), 0, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 3) + "E";
			} else if (txt[i].endsWith("BILITI") && temVC(txt[i].substring(0, txt[i].length() - 6), 0, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 6) + "BLE";
			}
		}
		return txt;
	}

	public static String[] regras3(String[] txt){
		for (int i = 0; i < txt.length; i++){
			if (txt[i].endsWith("ICATE") && temVC(txt[i].substring(0, txt[i].length() - 5), 0, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 3);
			} else if (txt[i].endsWith("ATIVE") && temVC(txt[i].substring(0, txt[i].length() - 5), 0, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 5);
			} else if (txt[i].endsWith("ALIZE") && temVC(txt[i].substring(0, txt[i].length() - 5), 0, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 3);
			} else if (txt[i].endsWith("ICITI") && temVC(txt[i].substring(0, txt[i].length() - 5), 0, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 3);
			} else if (txt[i].endsWith("ICAL") && temVC(txt[i].substring(0, txt[i].length() - 4), 0, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 2);
			} else if (txt[i].endsWith("FUL") && temVC(txt[i].substring(0, txt[i].length() - 3), 0, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 3);
			} else if (txt[i].endsWith("NESS") && temVC(txt[i].substring(0, txt[i].length() - 4), 0, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 4);
			}
		}

		return txt;
	}

	public static String[] regras4(String[] txt){
		for (int i = 0; i < txt.length; i++){
			if (txt[i].endsWith("AL") && temVC(txt[i].substring(0, txt[i].length() - 2), 1, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 2);
			} else if (txt[i].endsWith("ANCE") && temVC(txt[i].substring(0, txt[i].length() - 4), 1, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 4);
			} else if (txt[i].endsWith("ENCE") && temVC(txt[i].substring(0, txt[i].length() - 4), 1, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 4);
			} else if (txt[i].endsWith("ER") && temVC(txt[i].substring(0, txt[i].length() - 2), 1, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 2);
			} else if (txt[i].endsWith("IC") && temVC(txt[i].substring(0, txt[i].length() - 2), 1, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 2);
			} else if (txt[i].endsWith("ABLE") && temVC(txt[i].substring(0, txt[i].length() - 4), 1, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 4);
			} else if (txt[i].endsWith("IBLE") && temVC(txt[i].substring(0, txt[i].length() - 4), 1, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 4);
			} else if (txt[i].endsWith("ANT") && temVC(txt[i].substring(0, txt[i].length() - 3), 1, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 3);
			} else if (txt[i].endsWith("EMENT") && temVC(txt[i].substring(0, txt[i].length() - 5), 1, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 5);
			} else if (txt[i].endsWith("MENT") && temVC(txt[i].substring(0, txt[i].length() - 4), 1, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 4);
			} else if (txt[i].endsWith("ENT") && temVC(txt[i].substring(0, txt[i].length() - 3), 1, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 3);
			} else if (txt[i].endsWith("ION")){
				if (txt[i].length() >= 4 && 
						(txt[i].charAt(txt[i].length() - 4) == 'S' || txt[i].charAt(txt[i].length() - 4) == 'T')
						&& temVC(txt[i].substring(0, txt[i].length() - 3), 1, true)){
					txt[i] = txt[i].substring(0, txt[i].length() - 3);
				}
			} else if (txt[i].endsWith("OU") && temVC(txt[i].substring(0, txt[i].length() - 2), 1, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 2);
			} else if (txt[i].endsWith("ISM") && temVC(txt[i].substring(0, txt[i].length() - 3), 1, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 3);
			} else if (txt[i].endsWith("ATE") && temVC(txt[i].substring(0, txt[i].length() - 3), 1, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 3);
			} else if (txt[i].endsWith("ITI") && temVC(txt[i].substring(0, txt[i].length() - 3), 1, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 3);
			} else if (txt[i].endsWith("OUS") && temVC(txt[i].substring(0, txt[i].length() - 3), 1, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 3);
			} else if (txt[i].endsWith("IVE") && temVC(txt[i].substring(0, txt[i].length() - 3), 1, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 3);
			} else if (txt[i].endsWith("IZE") && temVC(txt[i].substring(0, txt[i].length() - 3), 1, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 3);
			}
		}
		return txt;
	}
	
	public static String[] regras5A(String[] txt){
		for (int i = 0; i < txt.length; i++){
			if (txt[i].endsWith("E") && temVC(txt[i].substring(0, txt[i].length() - 1), 1, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 1);
			} else if (txt[i].endsWith("E") && 
					temVC(txt[i].substring(0, txt[i].length() - 1), 1, false) && 
					temCVC(txt[i].substring(0, txt[i].length() - 1)) == false){
				txt[i] = txt[i].substring(0, txt[i].length() - 1);
			}
		}
		return txt;
	}
	
	public static String[] regras5B(String[] txt){
		for (int i = 0; i < txt.length; i++){
			if (txt[i].endsWith("L") && temConsoanteDuplaNoFim(txt[i]) && temVC(txt[i], 1, true)){
				txt[i] = txt[i].substring(0, txt[i].length() - 1);
			} 
		}
		return txt;
	}
}
