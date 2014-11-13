import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public class test {
public static ArrayList<HashMap<String, Object>> sviPrijelazieNka = new ArrayList<HashMap<String, Object>>();
	public static void test(String... args) throws IOException {
		
		Gramatika G = Gramatika.fromSanDefinition("lab2_primjeri/knjiga/test.san");
		
		List<String> niz = new ArrayList<String>();
		
		for(String znak : G.getNezavrsniZnakovi()){
			niz.clear();
			niz.add(znak);
			System.out.println(znak + " : " + G.skupZapocinje(niz));
		}
		
		for(String znak : G.getZavrsniZnakovi()){
			niz.clear();
			niz.add(znak);
			System.out.println(znak + " : " + G.skupZapocinje(niz));
		}
		
		// stvaranje stavki
		ArrayList<Stavka> stavke = new ArrayList<Stavka>();
		for(String a : G.getNezavrsniZnakovi()){
			List<ProdukcijaGramatike> lista = G.produkcije.get(a);
			for(ProdukcijaGramatike b : lista){
				stavke.addAll(Stavka.fromProduction(a, b.getRightSide()));
			}
		}
		for(ProdukcijaGramatike a : G.epsilonProdukcije){
			stavke.addAll(Stavka.fromProduction(a.getLeftSide(), a.getRightSide()));
		}
		//gotovo stvaranje stavki
		System.out.println(stavke);
		Stavka pocetno_stanje = null;
		ArrayList<Stavka> stanja = new ArrayList<Stavka>(); //odvojene liste zbog lakse uporabe kasnije
		ArrayList<Set<String>> stanja_skupovi = new ArrayList<Set<String>>();  //isti indexi
		ArrayList<HashMap<String, Object>> prijelazi = new ArrayList<HashMap<String, Object>>();   //prijelazi automata
		HashMap<String, Object> red = new HashMap<String, Object>();
		for(Stavka a : stavke){
			if(a.lijevaStrana.equals(G.pocetniNezavrsniZnak) && a.pozicijaTocke==0){  //ako smo nasli pocetni znak
				stanja.add(a);
				Set skup = new TreeSet<String>();
				skup.add("%");   //oznaka kraja niza
				stanja_skupovi.add(skup);
				pocetno_stanje=a;
				break;
			}
		}
		int i=0;
		while(i<stanja.size()){   //dok ima stanja
			Stavka obradjuje = stanja.get(i);  //stanje koje obradjujemo
			int pozicija_tocke = obradjuje.pozicijaTocke;
			if(pozicija_tocke<obradjuje.desnaStrana.size()){   //da li smo na kraju stavke
				String sljedeci_znak = obradjuje.desnaStrana.get(pozicija_tocke);  //znak desno od tocke
				List<String> nakon_sljedeceg = obradjuje.desnaStrana.subList(pozicija_tocke+1, obradjuje.desnaStrana.size());   //niz beta nakon znaka
				if(G.getNezavrsniZnakovi().contains(sljedeci_znak)){   //ako je znak nezavrsni
					for(Stavka a : stavke){
						if(a.lijevaStrana.equals(sljedeci_znak) && a.pozicijaTocke==0){   //nadji sve stavke s njime na lijevoj strani
							Set<String> skup = new TreeSet<String>();
							if(nakon_sljedeceg.isEmpty() || G.jePrazni(nakon_sljedeceg.get(0))){   //ako je znak prazan, dodaj skup T od trenutacne razine
								if(!nakon_sljedeceg.isEmpty())
									skup = G.skupZapocinje(nakon_sljedeceg); 
								skup.addAll(stanja_skupovi.get(i));
							}
							else{
								skup = G.skupZapocinje(nakon_sljedeceg);   //skup T od bete
								if(skup.isEmpty())
									skup.add("%");
							}
							if(!stanja.contains(a) || !provjeri_pripadnost(stanja,a,stanja_skupovi,skup)){
								stanja.add(a);      //ako stanje ne postoji u listi stanja, dodaj ga
								stanja_skupovi.add(skup);
							}
							red.put("poc",obradjuje);
							red.put("znak", "$");
							red.put("zavr", a);
							red.put("skupk", skup);
							red.put("skupp", stanja_skupovi.get(i));
							prijelazi.add(red);
							red = new HashMap<String, Object>();
						}
					}
				}
				for(Stavka a : stavke){    //za zavrsni i nezavrsni znak, obavi obican prijelaz, tj pomicanje tocke
					if(a.lijevaStrana.equals(obradjuje.lijevaStrana) && a.desnaStrana.equals(obradjuje.desnaStrana) && a.pozicijaTocke==(pozicija_tocke+1)){
						if(!stanja.contains(a) || !provjeri_pripadnost(stanja,a,stanja_skupovi,stanja_skupovi.get(i))){
							stanja.add(a);
							stanja_skupovi.add(stanja_skupovi.get(i));
						}
						red.put("poc",obradjuje);
						red.put("znak", sljedeci_znak);
						red.put("zavr", a);
						red.put("skupk", stanja_skupovi.get(i));
						red.put("skupp", stanja_skupovi.get(i));
						prijelazi.add(red);
						red = new HashMap<String, Object>();
					}
				}
			}
			i++;
		}
		System.out.println(i+" "+stanja);
		System.out.println(stanja_skupovi);
		System.out.println(prijelazi);
		System.out.println("OK");
		sviPrijelazieNka.addAll(prijelazi);
	}
	//provjerava da li za odredjeno stanje vec postoji isti par (stanje, skup) u listama
	static boolean provjeri_pripadnost(ArrayList<Stavka> stanja, Stavka stanje, ArrayList<Set<String>> lista, Set<String> skup){
		for(int i=0;i<stanja.size();i++){
			if(stanja.get(i).equals(stanje))
				if(lista.get(i).equals(skup))
					return true;
		}
		return false;
	}
}
