package Principal;

import java.util.Comparator;

public class comparaBCP implements Comparator<BCP> {
	@Override
	public int compare(BCP a, BCP b) {
		// TODO Auto-generated method stub
		if(a.getCreditos() < b.getCreditos())
			return 1;
		else if (b.getCreditos() < a.getCreditos())
			return -1;
		else if (b.getCreditos() == a.getCreditos()) 
			return a.getNomeArquivo().compareTo(b.getNomeArquivo());
		
			
		return 0;
	}
}
