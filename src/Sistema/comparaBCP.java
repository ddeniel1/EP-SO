package Sistema;

import java.util.Comparator;

public class comparaBCP implements Comparator<BCP> {
	@Override
	public int compare(BCP a, BCP b) {
		if (a.getCreditos() < b.getCreditos())
			return 1;
		else if (b.getCreditos() < a.getCreditos())
			return -1;
		else if (b.getCreditos() == a.getCreditos())
			if(a.getNomeArquivo().compareTo(b.getNomeArquivo())>0) return 1;
			else return -1;
		return 0;
	}
}
