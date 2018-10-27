package Suporte;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import Sistema.CPU;

public class Logger {
	private static PrintWriter writer;

	public PrintWriter getWriter() {
		return writer;
	}

	public void setWriter(PrintWriter writer) {
		Logger.writer = writer;
	}

	public static void carregaProcesso(String nomeProcesso) {
		Logger.writer.println("Carregando "+ nomeProcesso);
	}

	public static void inicializaLog(int quantum) throws FileNotFoundException, UnsupportedEncodingException {
		Logger.writer = new PrintWriter("log/log" + (quantum > 9 ? quantum : "0" + quantum) + ".txt", "UTF-8");
		
	}

	public static void executando(String nomeProcesso) {
		// TODO Auto-generated method stub
		writer.println("Executando "+nomeProcesso);
	}

	public static void ES(int i, String nomeProcesso) {
		// TODO Auto-generated method stub
		writer.println("E/S iniciada em " + nomeProcesso);
		interrompendo(nomeProcesso, i);
	}



	public static void finalizarProcesso(String nomeProcesso, int i) {
		// TODO Auto-generated method stub
		writer.println(nomeProcesso + " terminado. X="+CPU.getX()+". Y="+CPU.getY());
	}

	public static void interrompendo(String nomeProcesso, int i) {
		// TODO Auto-generated method stub
		if (i == 0)
			writer.println("Interrompendo " + nomeProcesso + " após 1 instrução");
		else
			writer.println("Interrompendo " + nomeProcesso + " após " + (i) + " instruções");
	}

	public static void mediasQuantum(double mediaTrocas, double mediasInstrucoes, int quantum) {
		// TODO Sua mãe
		writer.println("MEDIA DE TROCAS: " + mediaTrocas);
		writer.println("MEDIA DE INSTRUCOES: " +  mediasInstrucoes);
		writer.println("QUANTUM: "+ quantum);
		writer.close();
	}
}
