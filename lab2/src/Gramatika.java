import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
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
	private Map<String, Integer> indeksiZnakova;
//	private Map<Integer, String> 
	private Map<String, Set<String>> skupoviZapocinje;
	
	private Gramatika() {
		this.nezavrsniZnakovi = new TreeSet<String>();
		this.zavrsniZnakovi = new TreeSet<String>();
		this.sinkronizacijskiZnakovi = new TreeSet<String>();
		this.produkcije = new TreeMap<String, List<ProdukcijaGramatike>>();
		
		this.epsilonProdukcije = new TreeSet<String>();
		this.indeksiZnakova = new HashMap<String, Integer>();
		this.skupoviZapocinje = new TreeMap<String, Set<String>>();
	}
	
	public static Gramatika fromSanDefinition(String sanFile) throws IOException, FileNotFoundException {
		System.setIn(new FileInputStream(sanFile));	
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		Gramatika G = new Gramatika();
		
		String[] nezavrsniZnakovi = reader.readLine().split(" ");
		for(int i = 1; i < nezavrsniZnakovi.length; i++){				// i=1 jer ne zelimo oznaku "%V"
			String znak = nezavrsniZnakovi[i].trim();
			G.nezavrsniZnakovi.add(znak);
			G.indeksiZnakova.put(znak, G.indeksiZnakova.size());
		}
		
		String[] zavrsniZnakovi = reader.readLine().split(" ");
		for(int i = 1; i < zavrsniZnakovi.length; i++){
			String znak = zavrsniZnakovi[i].trim();
			G.zavrsniZnakovi.add(zavrsniZnakovi[i].trim());
			G.indeksiZnakova.put(znak, G.indeksiZnakova.size());
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
		G.izracunajSkupoveZapocinje();
		
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
	
	private int index(String znak){
		return this.indeksiZnakova.get(znak);
	}
	
	private boolean[][] tablicaZapocinjeIzravnoZnakom(){
		int velicinaTablice = nezavrsniZnakovi.size() + zavrsniZnakovi.size();		
		boolean[][] tablica = new boolean[velicinaTablice][velicinaTablice];
		
		for(String lijevaStrana : this.nezavrsniZnakovi){
			int i = this.index(lijevaStrana);
			
			for(ProdukcijaGramatike p : this.produkcije.get(lijevaStrana)){
				List<String> desnaStrana = p.getRightSide();
				
				int iD = 0;
				String znak;
				
				do{
					znak = desnaStrana.get(iD);
					iD++;
					tablica[i][index(znak)] = true;
				}while(jeNezavrsni(znak) && prazniZnakovi.contains(znak));				
			}
		}
		
		return tablica;
	}
	
	private void izracunajSkupoveZapocinje(){
		boolean[][] tablica = tablicaZapocinjeIzravnoZnakom();
				
		for(int i = 0; i < tablica.length; i++){
			tablica[i][i] = true;
		}
		
		boolean imaNovih;
		
		do{
			imaNovih = false;
			
			for(int i = 0; i < tablica.length; i++){
				boolean[] trenutniRedak = tablica[i];
				
				for(int j = 0; j < trenutniRedak.length; i++){
					boolean[] refRedak = tablica[j];
					
					for(int k = 0; k < trenutniRedak.length; k++){
						if(!trenutniRedak[k] &&  refRedak[k]){
							imaNovih = true;
							trenutniRedak[k] = true;
						}
					}
				}
			}
		}while(imaNovih);
		
		for(String znak : indeksiZnakova.keySet()){
			boolean[] redak = tablica[index(znak)];
			Set<String> skupZapocinje = new HashSet<String>();			
			
			for(String znakZapocinje : indeksiZnakova.keySet()){
				if(redak[index(znakZapocinje)]){
					skupZapocinje.add(znakZapocinje);
				}
			}
			
			skupoviZapocinje.put(znak, skupZapocinje);
		}
		
	}
	
	private static boolean jeNezavrsni(String znak){
		return (znak.length() > 2 && znak.charAt(0) == '<' && znak.charAt(znak.length()-1) == '>');
	}
		
}












