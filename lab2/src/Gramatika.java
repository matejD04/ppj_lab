import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
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
	String pocetniNezavrsniZnak;
	private Set<String> zavrsniZnakovi;
	private Set<String> sinkronizacijskiZnakovi;
	Map<String, List<ProdukcijaGramatike>> produkcije; // kljuc - lijeva strana;
														// vrijednost - sve
														// produkcije sa istom
														// lijevom stranom
	Set<ProdukcijaGramatike> epsilonProdukcije; // svi nezavrsni znakovi koji
												// imaju eps-produkcije
	private Set<String> prazniZnakovi;
	private Map<String, Integer> indeksiZnakova;
	private Map<String, Set<String>> skupoviZapocinje;

	private Gramatika() {
		this.nezavrsniZnakovi = new TreeSet<String>();
		this.pocetniNezavrsniZnak = null;
		this.zavrsniZnakovi = new TreeSet<String>();
		this.sinkronizacijskiZnakovi = new TreeSet<String>();
		this.produkcije = new TreeMap<String, List<ProdukcijaGramatike>>();
		this.epsilonProdukcije = new HashSet<ProdukcijaGramatike>();
		this.indeksiZnakova = new HashMap<String, Integer>();
		this.skupoviZapocinje = new TreeMap<String, Set<String>>();
	}

	public Set<String> getNezavrsniZnakovi() {
		return nezavrsniZnakovi;
	}

	public Set<String> getZavrsniZnakovi() {
		return zavrsniZnakovi;
	}

	public static Gramatika fromSanDefinition(String sanFile)
			throws IOException, FileNotFoundException {
		System.setIn(new FileInputStream(sanFile));
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		Gramatika G = new Gramatika();
		String[] nezavrsniZnakovi = reader.readLine().split(" ");
		G.pocetniNezavrsniZnak = nezavrsniZnakovi[1].trim(); // postavljanje
																// originalno
																// zadanog
																// pocetnog
																// znaka
																// gramatike
		for (int i = 1; i < nezavrsniZnakovi.length; i++) { // i=1 jer ne zelimo
															// oznaku "%V"
			String znak = nezavrsniZnakovi[i].trim();
			G.nezavrsniZnakovi.add(znak);
			G.indeksiZnakova.put(znak, G.indeksiZnakova.size());
		}
		G.promijeniPocetniNezavrsniZnak(); // dodavanje novog nezavrsnog znaka i
											// odgovarajuce produkcije
		String[] zavrsniZnakovi = reader.readLine().split(" ");
		for (int i = 1; i < zavrsniZnakovi.length; i++) {
			String znak = zavrsniZnakovi[i].trim();
			G.zavrsniZnakovi.add(zavrsniZnakovi[i].trim());
			G.indeksiZnakova.put(znak, G.indeksiZnakova.size());
		}
		String[] synZnakovi = reader.readLine().split(" ");
		for (int i = 1; i < synZnakovi.length; i++) {
			G.sinkronizacijskiZnakovi.add(synZnakovi[i].trim());
		}
		String line = null;
		String trenutnaLijevaStrana = null;
		int prioritet = 1; // prioritet 0 (najveci prioritet) rezerviran je za
							// dodanu pocetnu produkciju
		while ((line = reader.readLine()) != null) {
			if (!line.startsWith(" ")) {
				trenutnaLijevaStrana = line.trim();
			} else {
				if (line.startsWith(" $")) {
					G.epsilonProdukcije.add(ProdukcijaGramatike
							.fromDefinitionString(trenutnaLijevaStrana, "",
									prioritet));
					prioritet++;
				} else {
					if (!G.produkcije.containsKey(trenutnaLijevaStrana)) {
						G.produkcije.put(trenutnaLijevaStrana,
								new LinkedList<ProdukcijaGramatike>());
					}
					G.produkcije.get(trenutnaLijevaStrana).add(
							ProdukcijaGramatike.fromDefinitionString(
									trenutnaLijevaStrana, line, prioritet));
					prioritet++;
				}
			}
		}
		G.pronadiPrazneZnakove();
		G.izracunajSkupoveZapocinje();
		return G;
	}

	private void promijeniPocetniNezavrsniZnak() {
		String noviPocetniZnak = "<#S0#>";
		this.nezavrsniZnakovi.add(noviPocetniZnak); // dodavanje novog pocetnog
													// nezavrsnog znaka
													// gramatike.
		this.indeksiZnakova.put(noviPocetniZnak, this.indeksiZnakova.size());
		ProdukcijaGramatike pocetnaProdukcija = ProdukcijaGramatike
				.fromDefinitionString(noviPocetniZnak,
						this.pocetniNezavrsniZnak, 0);
		List<ProdukcijaGramatike> tmp = new LinkedList<ProdukcijaGramatike>();
		tmp.add(pocetnaProdukcija);
		this.produkcije.put(noviPocetniZnak, tmp);
		this.pocetniNezavrsniZnak = noviPocetniZnak;
	}

	private void pronadiPrazneZnakove() {
		Set<String> listaPraznih = new HashSet<String>(); // prvi korak trazenja
															// praznih znakova -
															// u listu
		for (ProdukcijaGramatike p : this.epsilonProdukcije) { // praznih dodaju
																// se sve lijeve
																// strane
																// eps-produkcija
			listaPraznih.add(p.getLeftSide());
		}
		Set<String> noviPrazni = new HashSet<String>(listaPraznih); // dodaje se
																	// listaPraznih
																	// samo da
																	// bi se
																	// moglo uci
																	// u petlju
		while (!noviPrazni.isEmpty()) {
			noviPrazni.clear();
			for (Map.Entry<String, List<ProdukcijaGramatike>> produkcija : this.produkcije
					.entrySet()) {
				if(listaPraznih.contains(produkcija.getKey())){
					break;
				}
				
				for (ProdukcijaGramatike p : produkcija.getValue()) {
					List<String> desnaStrana = p.getRightSide();
					boolean jePrazna = true;
					for (String znak : desnaStrana) {
						if (!listaPraznih.contains(znak)) {
							jePrazna = false;
							break;
						}
					}
					if (jePrazna) {
						noviPrazni.add(produkcija.getKey());
						break;
					}
				}
			}
			listaPraznih.addAll(noviPrazni);
		}
		this.prazniZnakovi = listaPraznih;
	}

	private int index(String znak) {
		return this.indeksiZnakova.get(znak);
	}

	private boolean[][] tablicaZapocinjeIzravnoZnakom() {
		int velicinaTablice = nezavrsniZnakovi.size() + zavrsniZnakovi.size();
		boolean[][] tablica = new boolean[velicinaTablice][velicinaTablice];
		for (String lijevaStrana : this.nezavrsniZnakovi) {
			int i = this.index(lijevaStrana);
			for (ProdukcijaGramatike p : this.produkcije.get(lijevaStrana)) {
				List<String> desnaStrana = p.getRightSide();
				int iD = 0;
				String znak;
				do {
					znak = desnaStrana.get(iD);
					iD++;
					tablica[i][index(znak)] = true;
				} while (jeNezavrsni(znak) && prazniZnakovi.contains(znak) && iD < desnaStrana.size());
			}
		}
		return tablica;
	}

	private void izracunajSkupoveZapocinje() {
		boolean[][] tablica = tablicaZapocinjeIzravnoZnakom();
		for (int i = 0; i < tablica.length; i++) {
			tablica[i][i] = true;
		}
		boolean imaNovih;
		do {
			imaNovih = false;
			for (int i = 0; i < tablica.length; i++) {
				boolean[] trenutniRedak = tablica[i];
				for (int j = 0; j < trenutniRedak.length; j++) {
					if (!trenutniRedak[j]) {
						continue;
					}
					boolean[] refRedak = tablica[j];
					for (int k = 0; k < trenutniRedak.length; k++) {
						if (!trenutniRedak[k] && refRedak[k]) {
							imaNovih = true;
							trenutniRedak[k] = true;
						}
					}
				}
				tablica[i] = trenutniRedak;
			}
		} while (imaNovih);
		for (String znak : indeksiZnakova.keySet()) {
			boolean[] redak = tablica[index(znak)];
			Set<String> skupZapocinje = new HashSet<String>();
			for (String znakZapocinje : indeksiZnakova.keySet()) {
				if (redak[index(znakZapocinje)]) {
					if (!jeNezavrsni(znakZapocinje)) {
						skupZapocinje.add(znakZapocinje);
					}
				}
			}
			skupoviZapocinje.put(znak, skupZapocinje);
		}
	}

	private static boolean jeNezavrsni(String znak) {
		return (znak.length() > 2 && znak.charAt(0) == '<' && znak.charAt(znak
				.length() - 1) == '>');
	}

	public boolean jePrazni(String znak) { // dodana provjera dal je znak prazan
		return prazniZnakovi.contains(znak);
	}

	public Set<String> skupZapocinje(List<String> nizZnakova) {
		Set<String> skup = new HashSet<String>();
		for (String znak : nizZnakova) {
			skup.addAll(skupoviZapocinje.get(znak));
			if (!this.jePrazni(znak)) // vraca se skup zapocinje samo prvog
										// znaka ako nije prazan
				break;
		}
		return skup;
	}
}