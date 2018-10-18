package Principal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class Escalonador {
	private static PriorityQueue<BCP> prontos;
	private static ArrayList<BCP> tabelaDeProcessos;
	private static Queue<BCP> bloqueados;
	private static int trocasTotais = 0;
	private static int instrucoesTotais = 0;
	private static int numeroDeQuanta = 0;
	private static int qntProcessos;
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		File pasta = new File("processos");
		File[] arquivos = pasta.listFiles();
		Arrays.sort(arquivos);
		tabelaDeProcessos = lerProcessos(arquivos);
		qntProcessos = tabelaDeProcessos.size();
		
		prontos = new PriorityQueue<BCP>(tabelaDeProcessos.size(), new comparaBCP());
		PriorityQueue<BCP> prontos2 = new PriorityQueue<BCP>(tabelaDeProcessos.size(), new comparaBCP());
		int quantum = tabelaDeProcessos.get(0).getQuantum();
		PrintWriter writer = new PrintWriter("log/log" + (quantum > 9 ? quantum : "0" + quantum) + ".txt", "UTF-8");
		
		for (int i = 0; i < tabelaDeProcessos.size(); i++) {
			prontos.add(tabelaDeProcessos.get(i));
			prontos2.add(tabelaDeProcessos.get(i));
		}
		while (!prontos2.isEmpty()) {
			BCP aux = prontos2.poll();
			writer.println("Carregado "+ aux.getNomeProcesso());
		}
		bloqueados =  new LinkedList<BCP>();
		runEscalonador(writer);
		writer.print("QUANTUM: "+quantum);
		writer.close();
		
	}

	private static void runEscalonador(PrintWriter writer) {
		// TODO Auto-generated method stub
		BCP executando;
		while (!prontos.isEmpty() || (prontos.isEmpty() && !bloqueados.isEmpty())) {
			while (prontos.isEmpty()) 
				rodaBloqueio();
			executando = prontos.poll();
			executando.setEstado('E');
			if (executando.getCreditos() == 0) {
				prontos.add(executando);
				restauraCreditos();
				executando = prontos.poll();
			}
			if (executando == null)
				break;
			executarProcesso(executando, writer);
		}
		double mediaTrocas = (double)trocasTotais/qntProcessos;
		double mediaInstrucoes = (double)instrucoesTotais/numeroDeQuanta;
		writer.println("MEDIA DE TROCAS: "+ mediaTrocas);
		writer.println("MEDIA DE INTRUCOES: "+ mediaInstrucoes);
		System.out.println(numeroDeQuanta);
	}

	private static void executarProcesso(BCP executando, PrintWriter writer) {
		// TODO Auto-generated method stub
		writer.println("Executando "+ executando.getNomeProcesso());
		int quantum = executando.getQuantum();
		executando.setQuantum(executando.getQuantum()*2);
		String[] instrucoes = executando.getIntrucoes();
		boolean flag = false;
		int j;
		for (j = 0; j < quantum; j++) {
			if (executando.getCreditos() == 0)
				break;
			String instrucaoAtual = instrucoes[executando.getContadorDePrograma()];
			if (instrucaoAtual.startsWith("X=")) {
				int valor = Integer.parseInt(instrucaoAtual.substring(2));
				executando.setX(valor);
			}
			else if (instrucaoAtual.startsWith("Y=")) {
				int valor = Integer.parseInt(instrucaoAtual.substring(2));
				executando.setY(valor);
			}
			else if (instrucaoAtual.equals("E/S")) {
				writer.println("E/S iniciada em " + executando.getNomeProcesso());
				executando.setEstado('B');
				executando.setTempoBloqueado(2);
				bloqueados.add(executando);
				executando.setCreditos(executando.getCreditos()-1);
				executando.setContadorDePrograma(executando.getContadorDePrograma()+1);
				flag = true;
				if (j == 0)
					writer.println("Interrompendo " + executando.getNomeProcesso() + " após 1 instrução");
				if (j > 0)
					writer.println(
							"Interrompendo " + executando.getNomeProcesso() + " após " + (j+1) +" instruções");
				break;
			}
			else if (instrucaoAtual.equals("SAIDA")) {
				writer.println(executando.getNomeProcesso()+" terminado. X="+ executando.getX() + ". Y="+executando.getY());
				tabelaDeProcessos.remove(executando);
				flag = true;
				break;
			}
			executando.setContadorDePrograma(executando.getContadorDePrograma()+1);
		}
		System.out.println("-------------");
		for (BCP bcp : prontos) {
			System.out.println(bcp.getNomeProcesso() + " " + bcp.getCreditos());
		}
		instrucoesTotais += j;
		numeroDeQuanta++;
		trocasTotais++;
		if (!bloqueados.isEmpty()) {
			rodaBloqueio();
			
		}
		if (!flag) {
			if (j == 1)
				writer.println("Processo " + executando.getNomeProcesso() + " após 1 instrução");
			if (j > 1)
				writer.println(
						"Processo " + executando.getNomeProcesso() + " após " + j +" instruções");
			executando.setCreditos(executando.getCreditos()-1);
			prontos.add(executando);
			executando.setEstado('P');
			
		}
	}

	private static void restauraCreditos() {
		// TODO Auto-generated method stub
		if(tabelaDeProcessos.size() < 1)
			return;
		for (BCP bcp : prontos) {
			bcp.setCreditos(bcp.getPrioridade());
		}
	}

	private static void rodaBloqueio() {
		// TODO Auto-generated method stub
		for (BCP bcp : bloqueados) {
			bcp.setTempoBloqueado(bcp.getTempoBloqueado()-1);
			if(bcp.getTempoBloqueado() == 0) {
				bcp.setEstado('P');
				prontos.add(bcp);
				
			}		
		}
		for (BCP bcp : tabelaDeProcessos) {
			if(bcp.getEstado() == 'P' && bloqueados.contains(bcp))
				bloqueados.remove(bcp);
		}
	}

	private static ArrayList<BCP> lerProcessos(File[] arquivos) throws IOException {
		// TODO Auto-generated method stub
		FileReader arq = new FileReader(arquivos[arquivos.length-2]);
		BufferedReader buff = new BufferedReader(arq);
		String[] prioridades = new String[arquivos.length-2];
		for (int i = 0; i < prioridades.length; i++) {
			prioridades[i] = buff.readLine();
		}
		arq = new FileReader(arquivos[arquivos.length - 1]);
		buff = new BufferedReader(arq);
		int quantum = Integer.parseInt(buff.readLine());
		
		ArrayList<BCP> tabelaDeProcessos = criarProcessos(arquivos, prioridades, quantum);
		return tabelaDeProcessos;
	}

	private static ArrayList<BCP> criarProcessos(File[] arquivos, String[] prioridades, int quantum) throws IOException {
		// TODO Auto-generated method stub
		
		FileReader arq = null;
		BufferedReader buffer = null;
		ArrayList<BCP> tabelaDeProcessos = new ArrayList<BCP>();
		int n = arquivos.length-2;
		for (int i = 0; i < n; i++) {
			arq = new FileReader(arquivos[i]);
			buffer = new BufferedReader(arq);
			String aux = "";
			while (buffer.ready()) {
				aux = aux.concat(buffer.readLine() + " ");
			}
			String[] aux2 = aux.split(" ");
			arq.close();
			buffer.close();
			BCP atual = new BCP();
			atual.setNomeArquivo(arquivos[i].getName());
			atual.setNomeProcesso(aux2[0]);
			atual.setPrioridade(Integer.parseInt(prioridades[i]));
			atual.setCreditos(atual.getPrioridade());
			atual.setQuantum(quantum);
			atual.setEstado('P');
			String[] help = new String[aux2.length-1];
			for (int j = 0; j < help.length; j++) {
				help[j] = aux2[j+1];
			}
			atual.setIntrucoes(help);
			tabelaDeProcessos.add(atual);
		}
		return tabelaDeProcessos;
	}

	

}
