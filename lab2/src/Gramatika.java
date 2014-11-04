import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Gramatika {

	private Set<String> nezavrsniZnakovi;
	private Set<String> zavrsniZnakovi;
	private Set<String> sinkronizacijskiZnakovi;
	private Map<String, List<ProdukcijaGramatike>> produkcije;		// kljuc - lijeva strana; vrijednost - sve produkcije sa istom lijevom stranom
	private Set<String> epsilonProdukcije;							// svi nezavrsni znakovi koji imaju eps-produkcije
		
	private Set<String> prazniZnakovi;
	
	private Gramatika() {
		this.nezavrsniZnakovi = new TreeSet<String>();
		this.zavrsniZnakovi = new TreeSet<String>();
		this.sinkronizacijskiZnakovi = new TreeSet<String>();
		this.produkcije = new TreeMap<String, List<ProdukcijaGramatike>>();
		this.epsilonProdukcije = new TreeSet<String>();
	}
	
	public static Gramatika fromSanDefinition(String sanFile) throws IOException, FileNotFoundException {
		System.setIn(new FileInputStream(sanFile));	
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		Gramatika G = new Gramatika();
		
		String[] nezavrsniZnakovi = reader.readLine().split(" ");
		for(int i = 1; i < nezavrsniZnakovi.length; i++){				// i=1 jer ne zelimo oznaku "%V"
			G.nezavrsniZnakovi.add(nezavrsniZnakovi[i].trim());
		}
		
		String[] zavrsniZnakovi = reader.readLine().split(" ");
		for(int i = 1; i < zavrsniZnakovi.length; i++){
			G.zavrsniZnakovi.add(zavrsniZnakovi[i].trim());
		}
		
		String[] synZnakovi = reader.readLine().split(" ");
		for(int i = 1; i < synZnakovi.length; i++){
			G.sinkronizacijskiZnakovi.add(synZnakovi[i].trim());
		}
		
		String line = null;
		String trenutnaLijevaStrana = null;
		while((line = reader.readLine()) != null){
			if(!line.startsWith(" ")){
				trenutnaLijevaStrana = line.trim();
			}else if(line.startsWith(" $")){
				G.epsilonProdukcije.add(trenutnaLijevaStrana);
			}else{
				if(!G.produkcije.containsKey(trenutnaLijevaStrana)){
					G.produkcije.put(trenutnaLijevaStrana, new LinkedList<ProdukcijaGramatike>());
				}
				
				G.produkcije.get(trenutnaLijevaStrana).add(ProdukcijaGramatike.fromDefinitionString(trenutnaLijevaStrana, line));				
			}
		}
		
		G.pronadiPrazneZnakove();
		
		return G;
	}
	
	
	private void pronadiPrazneZnakove(){
		Set<String> listaPraznih = new HashSet<String>(this.epsilonProdukcije);		// prvi korak trazenja praznih znakova - u listu  
																					// praznih dodaju se sve lijeve strane eps-produkcija
		
		Set<String> noviPrazni = new HashSet<String>(listaPraznih);					// dodaje se listaPraznih samo da bi se moglo uci u petlju
		while(!noviPrazni.isEmpty()){
			noviPrazni.clear();
			
			for(Map.Entry<String, List<ProdukcijaGramatike>> produkcija : this.produkcije.entrySet()){
				for(ProdukcijaGramatike p : produkcija.getValue()){
					List<String> desnaStrana = p.getRightSide();
					boolean jePrazna = true;
					for(String znak : desnaStrana){
						if(!listaPraznih.contains(znak)){
							jePrazna = false;
							break;
						}
					}
					
					if(jePrazna){
						noviPrazni.add(produkcija.getKey());
						break;
					}
				}			
			}
			
			listaPraznih.addAll(noviPrazni);
		}
	
		this.prazniZnakovi = listaPraznih;
	}
		
}