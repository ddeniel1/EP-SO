package Sistema;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import Suporte.Logger;

public class Escalonador {
	// Algumas variaveis que ajudam na hora de rodar o processo, utiliza a Priority
	// Queue com o Comparator ComparaBCP
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
		// Esse metodo carrega os processos e pede ao Logger pra escrever o que precisa
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
		// Define o proximo a ser usado
		BCP atual;
		// Precisa disso, confia.
			//
			//
			//
			//
		///////////
		//		//
		 //	   //
		  // //
			//
		boolean troca = false;

		if (Escalonador.prontos.isEmpty() && !Escalonador.bloqueados.isEmpty()) {
			while (Escalonador.prontos.isEmpty())
				rodaBloqueio();
		}

		atual = Escalonador.prontos.peek();
		if (executando == null || atual.getCreditos() == 0) {
			if (atual.getCreditos() == 0) {
				boolean flag = false;
				for (BCP bcp : bloqueados)
					if (bcp.getCreditos() != 0)
						flag = true;

				if (!flag)
					SO.resturaCreditos();
				else
					while (Escalonador.prontos.isEmpty())
						rodaBloqueio();

			}
			troca = true;
		} else if (executando.getCreditos() == atual.getCreditos() && executando.getEstado() == 'P') {
			Escalonador.prontos.remove(executando);
			troca = false;
		}

		else
			troca = true;

		if (troca)
			executando = Escalonador.prontos.poll();

		executando.setEstado('E');
		Despachante.inserirContexto(executando);
		return executando;
	}

	public static void rodaBloqueio() {
		// Faz o tempo do bloqueio obedecer a ordem
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
		// Realiza o comando de ES e ativa o estado de bloqueado no processo
		executando.setEstado('B');
		executando.setTempoBloqueado(2);
		Despachante.retirarContexto(executando);
		bloqueados.add(executando);
	}

	public static void retirar(BCP executando) {
		// Retira do contexto e atualiza o estado do processo
		executando.setEstado('P');
		Despachante.retirarContexto(executando);
		prontos.add(executando);
	}

	public static void saida(BCP executando) {
		// Pede a remocao do processo da lista de processos ao SO e altera o estado do processo para Fim
		executando.setEstado('F');
		SO.removeProcesso(executando);
	}

}
