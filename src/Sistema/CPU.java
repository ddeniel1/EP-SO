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

	public static void executarProcesso(BCP executando) {
		// TODO Auto-generated method stub
		boolean flag = false;
		int i;
		if (!Escalonador.getBloqueados().isEmpty()) {
			Escalonador.rodaBloqueio();
		}
		for (i = 0; i < quantum ; i++) {
			
			String instrucaoAtual = instrucoes[contadorDePrograma];
			if (instrucaoAtual.startsWith("X="))
				x = Integer.parseInt(instrucaoAtual.substring(2));
			else if (instrucaoAtual.startsWith("Y="))
				y = Integer.parseInt(instrucaoAtual.substring(2));
			else if (instrucaoAtual.equals("E/S")) {
				Logger.ES((i+1), nomeProcesso);
				contadorDePrograma++;
				Escalonador.ES(executando);
				executando = null;
				flag = true;
				i++;
				break;
			}
			else if (instrucaoAtual.equals("SAIDA")) {
				contadorDePrograma++;
				Escalonador.saida(executando);
				Logger.finalizarProcesso(nomeProcesso, i);
				flag = true;
				i++;
				break;
			}
			contadorDePrograma++;
		}
		//System.out.println(i);
		SO.setIntrucoesTotais(SO.getIntrucoesTotais() + i);
		if(!flag) {
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
}
