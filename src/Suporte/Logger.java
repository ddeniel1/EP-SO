package Suporte;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

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
}
