package Sistema;

import java.util.LinkedList;
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
		Escalonador.bloqueados = new LinkedList<BCP>();
	}
	public static void carregarProcessos() {
		// TODO Auto-generated method stub
		PriorityQueue<BCP> prontos2 = new PriorityQueue<BCP>(SO.getTabelaDeProcessos().size(), new comparaBCP());
		
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
		BCP atual;
		System.out.println("Prontos");
		for (BCP bcp : prontos) {
			System.out.println(bcp.getNomeProcesso()+ " "+ bcp.getCreditos() + " " + bcp.getPrioridade());
			
		}
		System.out.println("__________________");
		System.out.println("Bloqueados");
		for (BCP bcp : bloqueados) {
			System.out.println(bcp.getNomeProcesso()+ " "+ bcp.getCreditos() + " " + bcp.getPrioridade());
			
		}
		System.out.println("__________________");
		if (Escalonador.prontos.isEmpty() && !Escalonador.bloqueados.isEmpty()) {
			while (Escalonador.prontos.isEmpty())
				rodaBloqueio();
		}
		if (executando == null) {
			atual = Escalonador.prontos.peek();
			if (atual.getCreditos() == 0) {
				boolean flag = false;
				for (BCP bcp : bloqueados) 
					if (bcp.getCreditos() != 0) flag = true;
				
				if(flag)SO.resturaCreditos();
			}
			executando = Escalonador.prontos.poll();
			executando.setEstado('E');
			Despachante.inserirContexto(executando);
			
		}
		else {
			boolean mantem = true;
			atual = Escalonador.prontos.peek();
			if(executando.getCreditos() ==  atual.getCreditos()) {
				
				if (atual.getCreditos() == 0) {
					boolean flag = false;
					for (BCP bcp : bloqueados) 
						if (bcp.getCreditos() != 0) flag = true;
					
					if(flag)SO.resturaCreditos();
					mantem = false;
				}
				else {
					if (executando.getPrioridade() < atual.getPrioridade())
						mantem = false;
					else 
						mantem = true;
				}
				
			}
			else if(executando.getCreditos() <  atual.getCreditos())
				mantem = false;
			else
				mantem = true;
			if (!mantem) {
				atual = Escalonador.prontos.poll();
				Escalonador.prontos.add(executando);
				Despachante.trocaContexto(executando, atual);
				executando.setEstado('P');
				atual.setEstado('E');
				executando = atual;
			}
		}
		
		return executando;
	}
	public static void rodaBloqueio() {
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
	public static void ES(BCP executando) {
		// TODO Auto-generated method stub
		executando.setEstado('B');
		executando.setTempoBloqueado(2);
		bloqueados.add(executando);
		Despachante.retirarContexto(executando);
	}
	public static void troca(BCP executando) {
		// TODO Auto-generated method stub
		Despachante.retirarContexto(executando);
	}
	
}
