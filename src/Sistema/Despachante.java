package Sistema;

import Suporte.Logger;

public class Despachante {
	// Esse aqui soh serve pra salvar o contexto e atualizar o contexto no BCP do processo.

	public static void trocaContexto(BCP executando, BCP atual) {
		retirarContexto(executando);
		inserirContexto(atual);
	}

	public static void retirarContexto(BCP executando) {
		executando.setX(CPU.getX());
		executando.setY(CPU.getY());
		executando.setContadorDePrograma(CPU.getContadorDePrograma());
		executando.setIntrucoes(CPU.getInstrucoes());
		executando.setCreditos(executando.getCreditos()-1);
		executando.setQuantum(executando.getQuantum()*2);
	}

	public static void inserirContexto(BCP executando) {
		CPU.setX(executando.getX());
		CPU.setY(executando.getY());
		CPU.setContadorDePrograma(executando.getContadorDePrograma());
		CPU.setInstrucoes(executando.getIntrucoes());
		CPU.setNomeProcesso(executando.getNomeProcesso());
		Logger.executando(executando.getNomeProcesso());
	}

}
