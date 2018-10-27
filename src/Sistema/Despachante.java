package Sistema;

import Suporte.Logger;

public class Despachante {

	public static void trocaContexto(BCP executando, BCP atual) {
		// TODO Auto-generated method stub
		retirarContexto(executando);
		inserirContexto(atual);
	}

	public static void retirarContexto(BCP executando) {
		// TODO Auto-generated method stub
		executando.setX(CPU.getX());
		executando.setY(CPU.getY());
		executando.setContadorDePrograma(CPU.getContadorDePrograma());
		executando.setIntrucoes(CPU.getInstrucoes());
		executando.setCreditos(executando.getCreditos()-1);
		executando.setQuantum(executando.getQuantum()*2);
	}

	public static void inserirContexto(BCP executando) {
		// TODO Auto-generated method stub
		CPU.setX(executando.getX());
		CPU.setY(executando.getY());
		CPU.setContadorDePrograma(executando.getContadorDePrograma());
		CPU.setInstrucoes(executando.getIntrucoes());
		CPU.setNomeProcesso(executando.getNomeProcesso());
		Logger.executando(executando.getNomeProcesso());
	}

}
