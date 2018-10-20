package Sistema;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import Suporte.Logger;

public class SO {
	private static ArrayList<BCP> tabelaDeProcessos;
	private static int qntProcessos;
	private static int intrucoesTotais;
	private static int trocasTotais;
	public static void main(String[] args) throws IOException {
		
		File pasta = new File("processos");
		File[] arquivos = pasta.listFiles();
		Arrays.sort(arquivos);
		setTabelaDeProcessos(lerProcessos(arquivos));
		setQntProcessos(tabelaDeProcessos.size());
		Logger.inicializaLog(tabelaDeProcessos.get(0).getQuantum());
		Escalonador.inicializaProntos(tabelaDeProcessos.size(), new comparaBCP());
		Escalonador.inicializaBloqueados();
		Escalonador.carregarProcessos();
		BCP executando = null;
		while (!SO.tabelaDeProcessos.isEmpty()) {
			executando = Escalonador.escolheProximo(executando);
			if (executando == null)
				break;
			CPU.executarProcesso();
		}
	}
	private static ArrayList<BCP> lerProcessos(File[] arquivos) throws IOException {
		// FileReader com as prioridades dos processos
		FileReader arq = new FileReader(arquivos[arquivos.length - 2]);
		BufferedReader buff = new BufferedReader(arq);				String[] prioridades = new String[arquivos.length - 2];
		// Laco que coloca as prioridades dos processos
		for (int i = 0; i < prioridades.length; i++) {
			prioridades[i] = buff.readLine();
		}
		// Quantum inicial do BCP
		arq = new FileReader(arquivos[arquivos.length - 1]);
		buff = new BufferedReader(arq);
		int quantum = Integer.parseInt(buff.readLine());
		// Cria os processos na arrayList
		ArrayList<BCP> tabelaDeProcessos = criarProcessos(arquivos, prioridades, quantum);
		buff.close();
		return tabelaDeProcessos;
	}
	private static ArrayList<BCP> criarProcessos(File[] arquivos, String[] prioridades, int quantum) throws IOException {
		FileReader arq = null;
		BufferedReader buffer = null;
		ArrayList<BCP> tabelaDeProcessos = new ArrayList<BCP>();
		int n = arquivos.length - 2;
		for (int i = 0; i < n; i++) {
			// Faz a leitura dos arquivos em si
			arq = new FileReader(arquivos[i]);
			buffer = new BufferedReader(arq);
			String aux = "";
			while (buffer.ready()) {
				aux = aux.concat(buffer.readLine() + " ");
			}
			String[] aux2 = aux.split(" ");
			arq.close();
			buffer.close();
			// Apos a leitura, faz a interpretacao e coloca no BCP
			BCP atual = new BCP();
			atual.setNomeArquivo(arquivos[i].getName());
			atual.setNomeProcesso(aux2[0]);
			atual.setPrioridade(Integer.parseInt(prioridades[i]));
			atual.setCreditos(atual.getPrioridade());
			atual.setQuantum(quantum);
			atual.setEstado('P');
			String[] help = new String[aux2.length - 1];
			for (int j = 0; j < help.length; j++) {
				help[j] = aux2[j + 1];
			}
			atual.setIntrucoes(help);
			tabelaDeProcessos.add(atual);
		}
		return tabelaDeProcessos;
	}
	public static ArrayList<BCP> getTabelaDeProcessos() {
		return tabelaDeProcessos;
	}
	public static void setTabelaDeProcessos(ArrayList<BCP> tabelaDeProcessos) {
		SO.tabelaDeProcessos = tabelaDeProcessos;
	}
	public static int getQntProcessos() {
		return qntProcessos;
	}
	public static void setQntProcessos(int qntProcessos) {
		SO.qntProcessos = qntProcessos;
	}
	public static void resturaCreditos() {
		// TODO Auto-generated method stub
		for (BCP bcp : tabelaDeProcessos) {
			bcp.setCreditos(bcp.getPrioridade());
		}
	}
	public static int getIntrucoesTotais() {
		return intrucoesTotais;
	}
	public static void setIntrucoesTotais(int intrucoesTotais) {
		SO.intrucoesTotais = intrucoesTotais;
	}
	public static int getTrocasTotais() {
		return trocasTotais;
	}
	public static void setTrocasTotais(int trocasTotais) {
		SO.trocasTotais = trocasTotais;
	}

}
