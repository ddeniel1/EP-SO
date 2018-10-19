package Sistema;

import java.util.PriorityQueue;
import java.util.Queue;

import Principal.BCP;
import Principal.comparaBCP;
import Suporte.Logger;

public class Escalonador {
	private static PriorityQueue<BCP> prontos;
	private static Queue<BCP> bloqueados;
	public static PriorityQueue<BCP> getProntos() {
		return prontos;
	}
	public static void setProntos(PriorityQueue<BCP> prontos) {
		Escalonador.prontos = prontos;
	}
	public static Queue<BCP> getBloqueados() {
		return bloqueados;
	}
	public static void setBloqueados(Queue<BCP> bloqueados) {
		Escalonador.bloqueados = bloqueados;
	}
	public static void inicializaProntos(int size, comparaBCP comparaBCP) {
		// TODO Auto-generated method stub
		Escalonador.prontos = new PriorityQueue<BCP>(size, comparaBCP);
	}
	public static void inicializaBloqueados() {
		// TODO Auto-generated method stub
		
	}
	public static void carregarProcessos() {
		// TODO Auto-generated method stub
		PriorityQueue<BCP> prontos2 = new PriorityQueue<BCP>(Escalonador.prontos.size(), new comparaBCP());
		
		for (int i = 0; i < SO.getTabelaDeProcessos().size(); i++) {
			prontos.add(SO.getTabelaDeProcessos().get(i));
			prontos2.add(SO.getTabelaDeProcessos().get(i));
		}
		while (!prontos2.isEmpty()) {
			BCP aux = prontos2.poll();
			Logger.carregaProcesso(aux.getNomeProcesso());
		}
	}
}