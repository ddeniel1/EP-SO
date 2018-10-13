package Escalonador;

import java.util.Comparator;

public class FilaPrioritaria implements Comparator<String[]> {

	public int compare(String[] a, String[] b) {
		int iB = Integer.parseInt(b[0]);
		int iA = Integer.parseInt(a[0]);
		if (iA < iB)
			return 1;
		else if (iB < iA)
			return -1;
		else if(iB==iA){
			if (a[1].compareToIgnoreCase(b[1])>0)
				return 1;
			else
				return -1;
		}
		return 0;
	}
}
