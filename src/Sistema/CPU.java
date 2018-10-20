package Sistema;

import Suporte.Logger;

public class CPU {
	private static String nomeProcesso;
	private static int x;
	private static int y;
	private static int contadorDePrograma;
	private static String[] instrucoes;
	private static int quantum;
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

	public static void executarProcesso() {
		// TODO Auto-generated method stub
		Logger.executando(nomeProcesso);
		
	}
	public static String getNomeProcesso() {
		return nomeProcesso;
	}
	public static void setNomeProcesso(String nomeProcesso) {
		CPU.nomeProcesso = nomeProcesso;
	}
}
