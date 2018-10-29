package Sistema;

public class BCP {
	// Classe do BCP de acordo com o modelo estudado em aula
	private String nomeArquivo;
	// O nome do arquivo eh mais importante que o nome do processo, deixa ai
	private String nomeProcesso;
	private String[] intrucoes;
	private int quantum = 1;
	private int prioridade;
	private int contadorDePrograma = 0;
	private char estado;
	// registradores
	private int x;
	private int y;
	// Base de calculo pro tempo na fila de bloqueados
	private int tempoBloqueado;
	private int creditos;

	// Sistema de encapsulamento por get-sets
	public String getNomeProcesso() {
		return nomeProcesso;
	}

	public void setNomeProcesso(String nome) {
		this.nomeProcesso = nome;
	}

	public String[] getIntrucoes() {
		return intrucoes;
	}

	public void setIntrucoes(String[] intrucoes) {
		this.intrucoes = intrucoes;
	}

	public int getQuantum() {
		return quantum;
	}

	public void setQuantum(int quantum) {
		this.quantum = quantum;
	}

	public int getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(int prioridade) {
		this.prioridade = prioridade;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public int getContadorDePrograma() {
		return contadorDePrograma;
	}

	public void setContadorDePrograma(int contadorDePrograma) {
		this.contadorDePrograma = contadorDePrograma;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(char estado) {
		this.estado = estado;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getTempoBloqueado() {
		return tempoBloqueado;
	}

	public void setTempoBloqueado(int tempoBloqueado) {
		this.tempoBloqueado = tempoBloqueado;
	}

	public int getCreditos() {
		return creditos;
	}

	public void setCreditos(int creditos) {
		this.creditos = creditos;
	}
}
