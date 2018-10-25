package Suporte;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import Escalonador.Escalonador;
import Escalonador.FilaPrioritaria;

public class Teste {
	static public double mediaTrocas;
	static public double mediaInstrucoes;
	public static void main(String[] args) throws IOException {
		
		ArrayList<Integer> quantuns = new ArrayList<Integer>();
		ArrayList<Double> mediasT = new ArrayList<Double>();
		ArrayList<Double> mediasI = new ArrayList<Double>();
		FileReader arq = null; 
		BufferedReader buff = null; 
		String[] processos = Escalonador.lerProcessos(args); 
		String[] copiaP = processos.clone();
		arq = new FileReader(args[args.length - 2]);
		buff = new BufferedReader(arq);
		String[] prioridade = new String[processos.length];
		for (int i = 0; i < processos.length; i++) {
			prioridade[i] = buff.readLine();
		}
		
		for (int quantum = 1; quantum <= 22; quantum++) {
			PrintWriter writer = new PrintWriter("logTeste/log" + (quantum > 9 ? quantum : "0" + quantum) + ".txt", "UTF-8");
			// Le e adiciona as prioridades e o quantum
			for (int i = 0; i < processos.length; i++) {
				processos[i] = (prioridade[i]+ " ").concat(processos[i]);
				processos[i] = processos[i].concat("" + quantum);
			}
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
			Escalonador.runEscalonador(prontos, bloqueados, executando, registradores, processos, writer, quantum);
			writer.print("QUANTUM: " + quantum);
			writer.close();
			//File file = new File("logTeste/log" + (quantum > 9 ? quantum : "0" + quantum) + ".txt");
			if (!Desktop.isDesktopSupported()) {
				System.out.println("Desktop is not supported");
				return;
			}
			quantuns.add(quantum);
			mediasT.add(mediaTrocas);
			mediasI.add(mediaInstrucoes);
			processos = copiaP.clone();
			
		}
		generateCsvFile("Relatorio/resumo.csv", quantuns, mediasT, mediasI);
		
	}
	private static void generateCsvFile(String nomeArquivo, ArrayList<Integer> quantuns, ArrayList<Double> mediasT, ArrayList<Double> mediasI) throws IOException {
		// TODO Auto-generated method stub
		FileWriter writer = new FileWriter(nomeArquivo);
		
		writer.append("Identificação");
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
			writer.append("Quantum"+ quantuns.get(i));
			writer.append(",");
			writer.append(""+quantuns.get(i));
			writer.append(",");
			writer.append(""+mediasT.get(i));
			writer.append(",");
			writer.append(""+mediasI.get(i));
			writer.append(",");
			writer.append("Quantum"+ quantuns.get(i));
			writer.append("\n");
		}
        writer.flush();
        writer.close();
	}

}
