package Sistema;

import java.io.IOException;
import java.util.ArrayList;
import Suporte.Leitor;
import Suporte.Logger;

public class SO {
	private static ArrayList<BCP> tabelaDeProcessos;
	private static int qntProcessos;
	private static int instrucoesTotais;
	private static int trocasTotais;

	public static void main(String[] args) throws IOException {
		// Procura a pasta de processos e pede pro Leitor ler tudo o que precisa
		setTabelaDeProcessos(Leitor.leiaPastaDeArquivos("processos"));
		setQntProcessos(tabelaDeProcessos.size());
		// Salva o quantum do processador e coloca em todos os organismos do sistema
		int quantum = Leitor.lerQuantum("processos");
		CPU.setQuantum(quantum);
		Logger.inicializaLog(quantum);
		// Inicializa a fila de prontos de acordo com a prioridade definida em
		// ComparaBCP, inicializa a fila de bloqueados e carrega os processos
		Escalonador.inicializaProntos(tabelaDeProcessos.size(), new comparaBCP());
		Escalonador.inicializaBloqueados();
		Escalonador.carregarProcessos();

		BCP executando = null;
		// Executa os processos do SO utilizando o Escalonador
		while (!SO.tabelaDeProcessos.isEmpty()) {

			executando = Escalonador.escolheProximo(executando);
			if (executando == null)
				break;
			System.out.println(executando.getNomeProcesso());
			CPU.executarProcesso(executando);

			trocasTotais++;
		}
		// Defini a media de trocas e a media de instrucoes
		double mediaTrocas = (double) trocasTotais / qntProcessos;
		double mediasInstrucoes = (double) instrucoesTotais / CPU.getQuanta();
		// O logger termina o arquivo de log após essa chamada
		Logger.mediasQuantum(mediaTrocas, mediasInstrucoes, quantum);
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
			bcp.setQuantum(1);
		}
	}

	public static int getIntrucoesTotais() {
		return instrucoesTotais;
	}

	public static void setIntrucoesTotais(int intrucoesTotais) {
		SO.instrucoesTotais = intrucoesTotais;
	}

	public static int getTrocasTotais() {
		return trocasTotais;
	}

	public static void setTrocasTotais(int trocasTotais) {
		SO.trocasTotais = trocasTotais;
	}

	public static void removeProcesso(BCP executando) {
		// TODO Auto-generated method stub

		Despachante.retirarContexto(executando);
		tabelaDeProcessos.remove(executando);
	}

}
