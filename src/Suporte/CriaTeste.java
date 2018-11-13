package Suporte;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Random;

import Sistema.BCP;
import Sistema.CPU;
import Sistema.Escalonador;
import Sistema.SO;
import Sistema.comparaBCP;

public class CriaTeste {
	private static int trocaTotais;

	// Classe auxiliar para criacao de casos testes para fazer os graficos
	public static void main(String[] args) throws IOException {
		lerPastaProcessos("processos");
		lerPastaProcessos("NovosProcessos");

		//criarTestes();
		//lerTestes("entradasTeste");

	}

	private static void lerPastaProcessos(String pasta) throws IOException {
		ArrayList<Integer> quantuns = new ArrayList<Integer>();
		ArrayList<Double> mediasT = new ArrayList<Double>();
		ArrayList<Double> mediasI = new ArrayList<Double>();
		ArrayList<BCP> tabelaDeProcessosCopia = Leitor.leiaPastaDeArquivos(pasta);
		SO.setQntProcessos(tabelaDeProcessosCopia.size());
		for (int quantum = 1; quantum < 22; quantum++) {
			CPU.setQuantum(quantum);
			double mediaTrocas = 0;
			trocaTotais = 0;
			double mediaInstrucoes = 0;
			CPU.setQuanta(0);
			SO.setIntrucoesTotais(0);
			SO.setTabelaDeProcessos(copiarTabelaDeProcessos(tabelaDeProcessosCopia, quantum));
			Logger.inicializaLog(quantum);
			Escalonador.inicializaProntos(tabelaDeProcessosCopia.size(), new comparaBCP());
			Escalonador.inicializaBloqueados();
			Escalonador.carregarProcessos();
			BCP executando = null;
			executarProcessos(executando);
			mediaTrocas = (double) (trocaTotais -1)/ SO.getQntProcessos();
			mediaInstrucoes = (double) SO.getIntrucoesTotais() / CPU.getQuanta();
			Logger.mediasQuantum(mediaTrocas, mediaInstrucoes, quantum);
			quantuns.add(quantum);
			mediasT.add(mediaTrocas);
			mediasI.add(mediaInstrucoes);
		}
		if (pasta.equals("processos")) {
			gerarCSV("Relatorio/resumoProcessos.csv", quantuns, mediasI, mediasT);
		} else if (pasta.equals("NovosProcessos")) {
			gerarCSV("Relatorio/resumoNovosProcessos.csv", quantuns, mediasI, mediasT);
		} else {
			String nomeArquivo = "Relatorio/" + pasta + ".csv";
			gerarCSV(nomeArquivo, quantuns, mediasI, mediasT);
		}

	}

	private static void gerarCSV(String nomeArquivo, ArrayList<Integer> quantuns, ArrayList<Double> mediasI,
			ArrayList<Double> mediasT) throws IOException {
		FileWriter writer = new FileWriter(nomeArquivo);

		writer.append("Identifica��o");
		writer.append(',');
		writer.append("QNT_Quantum");
		writer.append(',');
		writer.append("MediaTrocas");
		writer.append(",");
		writer.append("MediaIntrucoes");
		writer.append(",");
		writer.append("Tipo");
		writer.append('\n');

		for (int i = 0; i < quantuns.size(); i++) {
			writer.append("Quantum" + quantuns.get(i));
			writer.append(",");
			writer.append("" + quantuns.get(i));
			writer.append(",");
			writer.append("" + mediasT.get(i));
			writer.append(",");
			writer.append("" + mediasI.get(i));
			writer.append(",");
			writer.append("Quantum" + quantuns.get(i));
			writer.append("\n");
		}
		writer.flush();
		writer.close();
	}

	private static ArrayList<BCP> copiarTabelaDeProcessos(ArrayList<BCP> tabelaDeProcessosCopia, int quantum) {
		ArrayList<BCP> copia = new ArrayList<BCP>();
		for (BCP bcp : tabelaDeProcessosCopia) {
			bcp.setContadorDePrograma(0);
			bcp.setCreditos(bcp.getPrioridade());
			bcp.setEstado('P');
			bcp.setQuantum(1);
			bcp.setX(0);
			bcp.setY(0);
			bcp.setTempoBloqueado(0);
			copia.add(bcp);

		}
		return copia;
	}

	private static void executarProcessos(BCP executando) {
		while (!SO.getTabelaDeProcessos().isEmpty()) {
			executando = Escalonador.escolheProximo(executando);
			if (executando == null)
				break;
			CPU.executarProcesso(executando);
			trocaTotais++;
		}
	}

	private static void lerTestes(String pasta) throws IOException {
		File pastas[];
		File diretorio = new File(pasta);
		pastas = diretorio.listFiles();
		for (int i = 0; i < pastas.length; i++) {

			String nomePasta = "entradasTeste/" + pastas[i].getName();
			lerPastaProcessos(nomePasta);
		}
	}

	private static void criarTestes() throws FileNotFoundException, UnsupportedEncodingException {
		Random gerador = new Random();
		for (int i = 0; i < 10; i++) {
			new File("entradasTeste/" + i + "/").mkdirs();
			int processos = gerador.nextInt(15) + 1;
			for (int j = 0; j < processos; j++) {

				PrintWriter writer = new PrintWriter("entradasTeste/" + i + "/" + (j < 10 ? "0" + j : j) + ".txt",
						"UTF-8");
				String s = "TESTE-" + j;
				writer.println(s);
				int instrucoes = gerador.nextInt(21) + 1;
				for (int k = 0; k < instrucoes; k++) {
					int instruc = gerador.nextInt(20);
					if (instruc < 5)
						writer.println("X=" + gerador.nextInt(30));
					else if (instruc < 10)
						writer.println("Y=" + gerador.nextInt(30));
					else if (instruc < 15)
						writer.println("COM");
					else
						writer.println("E/S");
				}
				writer.print("SAIDA");

				writer.close();
				writer = new PrintWriter("entradasTeste/" + i + "/prioridades.txt", "UTF-8");
				for (int k = 0; k < processos; k++) {
					writer.println((int) Math.ceil(Math.random() * instrucoes));
				}
				writer.close();
				writer = new PrintWriter("entradasTeste/" + i + "/quantum.txt", "UTF-8");
				writer.println(10);
				writer.close();
			}
		}
	}
}
