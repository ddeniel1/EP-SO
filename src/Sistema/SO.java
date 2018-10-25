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
		
		setTabelaDeProcessos(Leitor.leiaPastaDeArquivos("processos"));
		setQntProcessos(tabelaDeProcessos.size());
		int quantum = tabelaDeProcessos.get(0).getQuantum();
		Logger.inicializaLog(quantum);
		Escalonador.inicializaProntos(tabelaDeProcessos.size(), new comparaBCP());
		Escalonador.inicializaBloqueados();
		Escalonador.carregarProcessos();
		BCP executando = null;
		while (!SO.tabelaDeProcessos.isEmpty()) {
			
			executando = Escalonador.escolheProximo(executando);
			if (executando == null)
				break;
			CPU.executarProcesso(executando);
			
			trocasTotais++;
		}
		double mediaTrocas = (double)trocasTotais / qntProcessos;
		double mediasInstrucoes = (double) instrucoesTotais / trocasTotais;
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
