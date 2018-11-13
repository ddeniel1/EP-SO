package Sistema;

import java.util.Comparator;

// Priority Queue do java querer um classe comparator com o metodo compare,
//os metodos de comparacao sao os especificados no enunciado do trabalho
public class comparaBCP implements Comparator<BCP> {
	@Override
	public int compare(BCP a, BCP b) {
		if (a.getCreditos() < b.getCreditos())
			return 1;
		else if (b.getCreditos() < a.getCreditos())
			return -1;
		else {
			if (a.getPrioridade() < b.getPrioridade())
				return 1;
			else if (b.getPrioridade() < a.getPrioridade())
				return -1;
			else {
				if (a.getNomeArquivo().compareTo(b.getNomeArquivo()) > 0)
					return 1;
				else if (a.getNomeArquivo().compareTo(b.getNomeArquivo()) < 0)
					;
				else
					return 0;
			}

		}
		return 0;
	}
}
