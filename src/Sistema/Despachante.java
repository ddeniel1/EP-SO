package Sistema;

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
		SO.setIntrucoesTotais(SO.getIntrucoesTotais()+(CPU.getContadorDePrograma()-executando.getContadorDePrograma()));
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
		CPU.setQuantum(executando.getQuantum());
		CPU.setNomeProcesso(executando.getNomeProcesso());
	}
	
}
