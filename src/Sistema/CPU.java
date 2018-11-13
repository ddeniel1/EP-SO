package Sistema;

import Suporte.Logger;

public class CPU {
	// Classe da CPU eh a que roda os processos em si, tem algumas informacoes uteis
	// como registradores e cp
	private static String nomeProcesso;
	private static int x;
	private static int y;
	private static int contadorDePrograma;
	private static String[] instrucoes;
	private static int quantum;
	private static int quanta = 0;

	// A classe possui alguns gets e sets para facilitar na hora de salvar o
	// contexto.
	public static int getX() {
		return x;
	}

	public static void setX(int x) {
		CPU.x = x;
	}

	public static int getY() {
		return y;
	}

	public static void setY(int y) {
		CPU.y = y;
	}

	public static int getContadorDePrograma() {
		return contadorDePrograma;
	}

	public static void setContadorDePrograma(int contadorDePrograma) {
		CPU.contadorDePrograma = contadorDePrograma;
	}

	public static String[] getInstrucoes() {
		return instrucoes;
	}

	public static void setInstrucoes(String[] instrucoes) {
		CPU.instrucoes = instrucoes;
	}

	public static int getQuantum() {
		return quantum;
	}

	public static void setQuantum(int quantum) {
		CPU.quantum = quantum;
	}

	public static void executarProcesso(BCP executando) {
		// Esse metodo roda o processo e realiza as instrucoes de comando, e/s e
		// atribuicao
		boolean flag = false;
		int i;
		if (!Escalonador.getBloqueados().isEmpty()) {
			Escalonador.rodaBloqueio();
		}
		int quantaAtual = executando.getQuantum();
		for (i = 0; i < (quantum * quantaAtual); i++) {

			String instrucaoAtual = instrucoes[contadorDePrograma];
			if (instrucaoAtual.startsWith("X="))
				x = Integer.parseInt(instrucaoAtual.substring(2));
			else if (instrucaoAtual.startsWith("Y="))
				y = Integer.parseInt(instrucaoAtual.substring(2));
			else if (instrucaoAtual.equals("E/S")) {
				Logger.ES((i + 1), nomeProcesso);
				contadorDePrograma++;
				Escalonador.ES(executando);
				executando = null;
				flag = true;
				i++;
				break;
			} else if (instrucaoAtual.equals("SAIDA")) {
				contadorDePrograma++;
				Escalonador.saida(executando);
				Logger.finalizarProcesso(nomeProcesso, i);
				flag = true;
				i++;
				break;
			}
			contadorDePrograma++;

		}
		SO.setIntrucoesTotais(SO.getIntrucoesTotais() + i);
		quanta += quantaAtual;
		if (!flag) {
			Logger.interrompendo(nomeProcesso, i);
			Escalonador.retirar(executando);
		}
	}

	public static String getNomeProcesso() {
		return nomeProcesso;
	}

	public static void setNomeProcesso(String nomeProcesso) {
		CPU.nomeProcesso = nomeProcesso;
	}

	public static int getQuanta() {
		return quanta;
	}

	public static void setQuanta(int quanta) {
		CPU.quanta = quanta;
	}
}
