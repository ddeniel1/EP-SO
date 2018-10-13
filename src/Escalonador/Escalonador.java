package Escalonador;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Escalonador {
	public static void main(String[] args) throws IOException {
		FileReader arq = null;
		BufferedReader buff = null;
		String[] processos = lerProcessos(args);
		// Le o quantum e salva na var quantum
		arq = new FileReader(args[args.length - 1]);
		buff = new BufferedReader(arq);
		int quantum = Integer.parseInt(buff.readLine());
		buff.close();
		arq.close();
		PrintWriter writer = new PrintWriter("log" + (quantum > 9 ? quantum : "0" + quantum) + ".txt", "UTF-8");
		// Le e adiciona as prioridades e o quantum
		arq = new FileReader(args[args.length - 2]);
		buff = new BufferedReader(arq);
		for (int i = 0; i < processos.length; i++) {
			processos[i] = (buff.readLine() + " ").concat(processos[i]);
			processos[i] = processos[i].concat("" + quantum);
		}
		buff.close();
		arq.close();
		String[] executando = null;
		// Cria uma fila de prioridades para os processos prontos, utilizando a classe
		// FilaPrioritaria() criada para comparar a ordem de prioridade
		PriorityQueue<String[]> prontos = new PriorityQueue<String[]>(processos.length, new FilaPrioritaria());
		PriorityQueue<String[]> prontos2 = new PriorityQueue<String[]>(processos.length, new FilaPrioritaria());
		for (int i = 0; i < processos.length; i++) {
			prontos.add(processos[i].split(" "));
			prontos2.add(processos[i].split(" "));
		}
		// imprime na ordem de saida dos processos
		while (!prontos2.isEmpty()) {
			String[] aux = prontos2.poll();
			writer.println("Carregando " + aux[1]);
		}
		// HashMap para armazenar os bloqueados e registradores, a Key eh o
		// processo/nome do processo, em
		// prontos[1], respectivamente
		HashMap<String[], Integer> bloqueados = new HashMap<String[], Integer>();
		HashMap<String, Integer> registradores = new HashMap<String, Integer>();
		runEscalonador(prontos, bloqueados, executando, registradores, processos, writer, quantum);
		writer.print("QUANTUM: " + quantum);
		writer.close();
		File file = new File("log" + (quantum > 9 ? quantum : "0" + quantum) + ".txt");
		if (!Desktop.isDesktopSupported()) {
			System.out.println("Desktop is not supported");
			return;
		}
		Desktop desktop = Desktop.getDesktop();
		if (file.exists())
			desktop.open(file);
	}

	private static void runEscalonador(PriorityQueue<String[]> prontos, HashMap<String[], Integer> bloqueados,
			String[] executando, HashMap<String, Integer> registradores, String[] processos, PrintWriter writer,
			int quantum2) {
		HashMap<String, Integer> mediaDeTrocas = new HashMap<String, Integer>();
		int instrucoesTotais = 0;
		int numeroDeQuanta = 0;
		for (int i = 0; i < processos.length; i++) {
			String[] aux = processos[i].split(" ");
			mediaDeTrocas.put(aux[1], 0);
		}
		while (!prontos.isEmpty() || (prontos.isEmpty() && !bloqueados.isEmpty())) {
			while (prontos.isEmpty()) {
				rodaBloqueio(bloqueados, prontos);
			}
			executando = prontos.poll();
			if (executando[0].equals("0")) {
				prontos.add(executando);
				restauraCreditos(prontos, processos);
				executando = prontos.poll();
			}
			if (executando == null)
				break;
			int quantum = Integer.parseInt(executando[executando.length - 1]);
			executando[executando.length - 1] = ("" + quantum * 2);
			boolean flag = false;
			int j, i;
			for (i = 2, j = 0; j < quantum; j++) {
				if (executando[0].equals("0"))
					break;
				if (executando[i].startsWith("X=")) {
					String aux = executando[1] + " X";
					int registro = Integer.parseInt(executando[i].substring(2));
					registradores.put(aux, registro);
				} else if (executando[i].startsWith("Y=")) {
					String aux = executando[1] + " Y";
					int registro = Integer.parseInt(executando[i].substring(2));
					registradores.put(aux, registro);
				} else if (executando[i].equals("E/S")) {
					writer.println("E/S iniciada em " + executando[1]);
					executando[0] = "" + (Integer.parseInt(executando[0]) - 1);
					executando = removeComandos(executando, 1);
					bloqueados.put(executando, 2);
					flag = true;
					if (j == 0)
						writer.println("Processo " + executando[1] + " após 1 instrução (havia apenas a E/S)");
					if (j == 1)
						writer.println(
								"Processo " + executando[1] + " após 2 instruções (havia um comando antes da E/S)");
					break;
				} else if (executando[i].equals("SAIDA")) {
					writer.println(
							executando[1] + " terminado. X=" + registradores.getOrDefault(executando[1] + " X", 0)
									+ " Y=" + registradores.getOrDefault(executando[1] + " Y", 0));
					processos = finalizaProcesso(processos, executando);
					flag = true;
					break;
				}
				executando = removeComandos(executando, 1);
			}
			instrucoesTotais += j;
			numeroDeQuanta++;
			mediaDeTrocas.put(executando[1], mediaDeTrocas.get(executando[1]) + 1);
			if (!bloqueados.isEmpty())
				rodaBloqueio(bloqueados, prontos);
			if (!flag) {
				writer.println("Interrompendo processo " + executando[1] + " após " + j + " instruções.");
				executando[0] = "" + (Integer.parseInt(executando[0]) - 1);
				prontos.add(executando);
			}
		}
		double dividendo = mediaDeTrocas.size();
		int divisor = 0;
		String[] keys = new String[(int) dividendo];
		int i = 0;
		for (String key : mediaDeTrocas.keySet()) {
			keys[i] = key;
			i++;
		}
		for (int k = 0; k < keys.length; k++) {
			int aux = mediaDeTrocas.get(keys[k]);
			divisor += aux;
		}
		writer.println("MEDIA DE TROCAS: " + divisor / dividendo);
		writer.println("MEDIA DE INSTRUÇÕES: " + (double) instrucoesTotais / numeroDeQuanta);
		return;
	}

	private static void rodaBloqueio(HashMap<String[], Integer> bloqueados, PriorityQueue<String[]> prontos) {
		String[][] chaves = new String[bloqueados.size()][];
		int i = 0;
		for (String[] key : bloqueados.keySet()) {
			chaves[i] = key;
			i++;
		}
		for (i = 0; i < chaves.length; i++) {
			if (bloqueados.replace(chaves[i], 2, 1))
				;
			else if (bloqueados.replace(chaves[i], 1, 0))
				;
			else if (bloqueados.replace(chaves[i], 0, -1)) {
				bloqueados.remove(chaves[i]);
				prontos.add(chaves[i]);
			}
		}
	}

	private static void restauraCreditos(PriorityQueue<String[]> prontos, String[] processos) {
		if (processos.length < 1)
			return;
		String[][] aux = new String[prontos.size()][];
		int k = prontos.size();
		for (int i = 0; i < k; i++) {
			aux[i] = prontos.poll();
			for (int j = 0; j < processos.length; j++) {
				String[] pegaCreditos = processos[j].split(" ");
				if (aux[i][1].equals(pegaCreditos[1])) {
					aux[i][0] = pegaCreditos[0];
					prontos.add(aux[i]);
					break;
				}
			}
		}
	}

	private static String[] removeComandos(String[] executando, int quantum) {
		String[] novo = new String[executando.length - quantum];
		novo[0] = ("" + (Integer.parseInt(executando[0])));
		novo[1] = executando[1];
		for (int i = quantum + 2, j = 2; j < novo.length && i < executando.length; j++, i++) {
			novo[j] = executando[i];
		}
		return novo;
	}

	private static String[] finalizaProcesso(String[] processos, String[] executando) {
		if (processos.length == 1)
			return null;
		int indice = -1;
		for (int i = 0; i < processos.length; i++) {
			String[] aux = processos[i].split(" ");
			if (aux[1].equals(executando[1])) {
				indice = i;
				break;
			}
		}
		if (indice == -1)
			return processos;
		String[] novo = new String[processos.length - 1];
		if (indice == processos.length - 1) {
			for (int i = 0, j = 0; i < novo.length && j < processos.length; i++, j++) {
				novo[i] = processos[j];
			}
		}
		for (int i = 0, j = 0; i < novo.length && j < processos.length; i++, j++) {
			if (j == indice)
				j++;
			novo[i] = processos[j];
		}
		return novo;
	}

	static String[] lerProcessos(String[] args) throws IOException {
		FileReader arq = null;
		BufferedReader buff = null;
		String[] processos = new String[args.length - 2];
		for (int i = 0; i < args.length - 2; i++) {
			arq = new FileReader(args[i]);
			buff = new BufferedReader(arq);
			String aux = "";
			while (buff.ready()) {
				aux = aux.concat(buff.readLine() + " ");
			}
			arq.close();
			buff.close();
			processos[i] = aux;
		}
		buff.close();
		arq.close();
		return processos;
	}
}