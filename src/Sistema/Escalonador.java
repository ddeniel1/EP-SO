package Sistema;

import java.util.PriorityQueue;
import java.util.Queue;

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
		Escalonador.prontos = new PriorityQueue<BCP>(size, comparaBCP);
	}
	public static void inicializaBloqueados() {
		
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
	public static BCP escolheProximo(BCP executando) {
		// TODO Auto-generated method stub
		if (executando == null) {
			executando = Escalonador.prontos.poll();
			executando.setEstado('E');
		}
		else {
			while (Escalonador.prontos.isEmpty())
				rodaBloqueio();
			Escalonador.prontos.peek();
			
		}
		return executando;
	}
	private static void rodaBloqueio() {
		// TODO Auto-generated method stub
		for (BCP bcp : bloqueados) {
			bcp.setTempoBloqueado(bcp.getTempoBloqueado() - 1);
			if (bcp.getTempoBloqueado() == 0) {
				bcp.setEstado('P');
				prontos.add(bcp);

			}
		}
		for (BCP bcp : SO.getTabelaDeProcessos()) {
			if (bcp.getEstado() == 'P' && bloqueados.contains(bcp))
				bloqueados.remove(bcp);
		}
	}
	
}
