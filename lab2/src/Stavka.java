import java.util.List;
import java.util.Set;
import java.util.TreeSet;


/**
 * Stavka LR(1) parsera.
 */
public class Stavka {
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

	
}
