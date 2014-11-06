import java.util.List;
import java.util.Set;
import java.util.TreeSet;


/**
 * Stavka LR(1) parsera.
 */
public class Stavka implements Comparable<Stavka> {
	private String lijevaStrana;
	private List<String> desnaStrana;
	int duljinaDesneStrane;			// broj znakova (zav. ili nezav.) u desnoj strani produkcije
	private int pozicijaTocke;		// pozicijaTocke oznacava mjesto na kojem se nalazi tocka: 
									//			0 - sami pocetak
									//			1 - iza prvog znaka desne strane
									// 			2 - iza drugog znaka desne strane ...
	
	private Stavka() {
		lijevaStrana = null;
		desnaStrana = null;
		pozicijaTocke = 0;
		duljinaDesneStrane = 0;
	}
	
	public static Set<Stavka> fromProduction(String leftSide, List<String> rightSide){
		Set<Stavka> noveStavke = new TreeSet<Stavka>();
		
		for(int i = 0; i <= rightSide.size(); i++){
			Stavka novaStavka = new Stavka();
			novaStavka.lijevaStrana = leftSide;
			novaStavka.desnaStrana = rightSide;
			novaStavka.pozicijaTocke = i;
			novaStavka.duljinaDesneStrane = rightSide.size();
			
			noveStavke.add(novaStavka);
		}
		
		return noveStavke;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(lijevaStrana).append(" → ");
		
		for(int i = 0; i < desnaStrana.size(); i++){
			if(i == pozicijaTocke){
				sb.append("•");
			}
			
			sb.append(desnaStrana.get(i));
		}
		
		if(pozicijaTocke == desnaStrana.size()){
			sb.append("•");
		}
		
		return sb.toString();		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((desnaStrana == null) ? 0 : desnaStrana.hashCode());
		result = prime * result + duljinaDesneStrane;
		result = prime * result
				+ ((lijevaStrana == null) ? 0 : lijevaStrana.hashCode());
		result = prime * result + pozicijaTocke;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Stavka other = (Stavka) obj;
		if (desnaStrana == null) {
			if (other.desnaStrana != null)
				return false;
		} else if (!desnaStrana.equals(other.desnaStrana))
			return false;
		if (duljinaDesneStrane != other.duljinaDesneStrane)
			return false;
		if (lijevaStrana == null) {
			if (other.lijevaStrana != null)
				return false;
		} else if (!lijevaStrana.equals(other.lijevaStrana))
			return false;
		if (pozicijaTocke != other.pozicijaTocke)
			return false;
		return true;
	}
	
	@Override
	public int compareTo(Stavka o) {
		return this.lijevaStrana.compareTo(o.lijevaStrana);		
	}
}
