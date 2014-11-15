import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class eNKADKA {
	static Map<String, TreeSet<String>> sviPrijelazi = new HashMap<String, TreeSet<String>>();
	static TreeSet<String> svaStanja = new TreeSet<String>();
	static TreeSet<String> sviUlazniZnakovi = new TreeSet<String>();
	static String nkaPocetno = new String();
	static Map<String, TreeSet<String>> nkaPrijelazi = new HashMap<String, TreeSet<String>>();
	
	static ArrayList<TreeSet<String>> dkaStanja = new ArrayList<TreeSet<String>>();
	static Map<String, TreeSet<String>> dkaPrijelazi = new HashMap<String, TreeSet<String>>();
	
	public static ArrayList<HashMap<String, Object>> goraneNka = new ArrayList<HashMap<String, Object>>();
	
	static Map<TreeSet<String>, Integer> brojeviStanja = new HashMap<TreeSet<String>, Integer>();
	public static ArrayList<HashMap<String, String>> tablica = new ArrayList<HashMap<String, String>>();
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		/*System.setIn(new FileInputStream("primjeri/00aab_1/test.san"));	
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));*/
		//defeNKA();
		defGoranNka();
		
		eNKA2NKA();
		System.out.println("DEF NKA"+svaStanja.toString()+sviUlazniZnakovi.toString());
		for(String i:svaStanja){
			for(String j:sviUlazniZnakovi){
				if(nkaPrijelazi.containsKey(i+','+j)){
					System.out.println(i+','+j+"->"+nkaPrijelazi.get(i+','+j).toString());
				}
			}
		}
		
		NKA2DKA();
		int p=0;
		for(TreeSet<String> s:dkaStanja){
			brojeviStanja.put(s, p);
			System.out.println(s.toString()+":"+p);
			p++;
		}
		System.out.println("DEF DKA");
		for(TreeSet<String> i:dkaStanja){
			for(String j:sviUlazniZnakovi){
				if(dkaPrijelazi.containsKey(i.toString()+','+j) && brojeviStanja.containsKey(dkaPrijelazi.get(i.toString()+','+j))){
					System.out.println(brojeviStanja.get(i).toString()+','+j+"->"+brojeviStanja.get(dkaPrijelazi.get(i.toString()+','+j)));
				}
			}
		}
		/*
		System.out.println("PRIMITIVNI ISPIS DEF DKA");
		for(TreeSet<String> i:dkaStanja){
			for(String j:sviUlazniZnakovi){
				if(dkaPrijelazi.containsKey(i.toString()+','+j)){
					System.out.println(i.toString()+','+j+"->"+dkaPrijelazi.get(i.toString()+','+j).toString());
				}
			}
		}*/
		TreeSet<String> prihZnakovi = new TreeSet<String>();
		TreeSet<String> neprihZnakovi = new TreeSet<String>();
		for(String j:sviUlazniZnakovi){
			if(!j.startsWith("<"))
				prihZnakovi.add(j);
			else
				neprihZnakovi.add(j);
		}
		prihZnakovi.add("%");
		
		System.out.println("PRIHZNAKOVI"+neprihZnakovi.toString());
		
		for(int i=0;i<dkaStanja.size();i++){
			TreeSet<String> stanje = dkaStanja.get(i);
			
			
			
			
			for(String j:prihZnakovi){//pomakni
				System.out.println(stanje+" "+dkaPrijelazi.get(stanje.toString()+','+j));
				if(dkaPrijelazi.containsKey(stanje.toString()+','+j) && !dkaPrijelazi.get(stanje.toString()+','+j).contains("(X)")){
					//System.out.println(stanje.toString()+','+j+"->"+dkaPrijelazi.get(stanje.toString()+','+j).toString());
					HashMap<String,String> red = new HashMap<String,String>();
					red.put(i+","+j, "p"+dkaStanja.indexOf(dkaPrijelazi.get(stanje.toString()+','+j)));
					tablica.add(red);
					
					
					
				}
			}
			for(String j:neprihZnakovi){//STAVI
				System.out.println(stanje+" "+dkaPrijelazi.get(stanje.toString()+','+j));
				if(dkaPrijelazi.containsKey(stanje.toString()+','+j) && !dkaPrijelazi.get(stanje.toString()+','+j).contains("(X)")){
					//System.out.println(stanje.toString()+','+j+"->"+dkaPrijelazi.get(stanje.toString()+','+j).toString());
					HashMap<String,String> red = new HashMap<String,String>();
					red.put(i+","+j, "s"+dkaStanja.indexOf(dkaPrijelazi.get(stanje.toString()+','+j)));
					tablica.add(red);
					
					
					
				}
			}
			for(String s:stanje){//reduciraj
				System.out.println(s);
				String b = s.split("\\{\\[", 2)[0];
				String znakovi = s.split("\\{\\[", 2)[1];
				znakovi = znakovi.substring(0, znakovi.length()-2);
				
				TreeSet<String> skup = new TreeSet<String>();
				for(String z:znakovi.split(", ")){
					skup.add(z);
				}
				
				if(b.endsWith("*")){
					HashMap<String,String> red = new HashMap<String,String>();
					if(!b.startsWith("<#S0#>")){
						for(String z:skup){
							if(red.containsKey(i+","+z)&& !red.get(i+","+z).equals("r("+b+")") || !red.containsKey(i+","+z)){
								String r=b.substring(0, b.length()-1);
								if(r.endsWith(" "))
									r+="$";
								red.put(i+","+z, "r("+r+")");
							}
						}
					}
					else{//PRIHVATI
						
					}
					tablica.add(red);
				}
			}
			
		}
		System.out.println(tablica);
	}
	
	private static void defGoranNka() throws IOException {
		test.test();
		goraneNka = test.sviPrijelazieNka;
		System.out.print(goraneNka.toString());
		
		for(HashMap<String, Object> h:goraneNka){
			TreeSet<String> skuppS = new TreeSet<String>();
			skuppS.addAll((Collection<? extends String>) h.get("skupp"));
			TreeSet<String> skupkS = new TreeSet<String>();
			skupkS.addAll((Collection<? extends String>) h.get("skupk"));
			System.out.println(skuppS.toString()+"\n"+skupkS.toString());
			String skupp = skuppS.toString();
			String skupk = skupkS.toString();
			
			System.out.println(h.get("poc").toString()+"{"+skupp+"}"+","+h.get("znak").toString()+" ==> "+h.get("zavr").toString()+"{"+skupk+"}");
			svaStanja.add(h.get("poc").toString()+"{"+skupp+"}");
			svaStanja.add(h.get("poc").toString()+"{"+skupk+"}");
			if(!h.get("znak").toString().equals("$"))
				sviUlazniZnakovi.add(h.get("znak").toString());
			
			if(h.get("poc").toString().startsWith("<#S0#>") && h.get("znak").toString().equals("$")){//DEFINICIJA POCETNOG STANJA, bice samo jedno stanje koje pocinje <#S0#>.... i ima $ prijelaz
				nkaPocetno = h.get("poc").toString()+"{"+skupp+"}";
			}
			
			if(sviPrijelazi.containsKey(h.get("poc").toString()+"{"+skupp+"}"+","+h.get("znak").toString())){
				if(true){
					sviPrijelazi.get(h.get("poc").toString()+"{"+skupp+"}"+","+h.get("znak").toString()).add(h.get("zavr").toString()+"{"+skupk+"}");
				}
			}
			else{
				TreeSet<String> prelaziU = new TreeSet<String>();
				prelaziU.add(h.get("zavr").toString()+"{"+skupk+"}");
				sviPrijelazi.put(h.get("poc").toString()+"{"+skupp+"}"+","+h.get("znak").toString(), prelaziU);
			}
		}
		
		System.out.println("DEF GORAN ENKA");
		for(String i:svaStanja){
			for(String j:sviUlazniZnakovi){
				if(sviPrijelazi.containsKey(i+","+j)){
					System.out.println(i+","+j+"==>>"+sviPrijelazi.get(i+","+j).toString());
				}
			}
			if(sviPrijelazi.containsKey(i+",$")){
				System.out.println(i+",$"+"==>>"+sviPrijelazi.get(i+",$").toString());
			}
		}
		System.out.println("\\DEF GORAN ENKA");
		
		
			
	}

	private static Set<String> epsOkruzenje(String stanje){
		TreeSet<String> novaStanja = new TreeSet<String>();
		TreeSet<String> staraStanja = new TreeSet<String>();
		
		novaStanja.add(stanje);
		staraStanja.add(stanje);
		
		if(sviPrijelazi.containsKey(stanje+",$")){
			for(String s:sviPrijelazi.get(stanje+",$")){
				novaStanja.add(s);
			}
		}
		
		if(novaStanja.equals(staraStanja)){
			return novaStanja;
		}
		else{
			staraStanja.addAll(novaStanja);
		}
		while(true){
			for(String u:staraStanja){
				if(sviPrijelazi.containsKey(u+",$")){
					for(String s:sviPrijelazi.get(u+",$")){
						novaStanja.add(s);
					}
				}
			}
		
			if(novaStanja.equals(staraStanja)){
				return novaStanja;
			}
			else{
				staraStanja.addAll(novaStanja);
				continue;
			}			
		}
	}
	
	private static  void eNKA2NKA(){
		for(String stanje:svaStanja){
			for(String znak:sviUlazniZnakovi){
				TreeSet<String> epsOkrPrelaziU = new TreeSet<String>();
				TreeSet<String> nkaPrelaziU = new TreeSet<String>();
				
				for(String stanjeEpsOkr:epsOkruzenje(stanje)){//Prijelazi za sve iz epsOkruzenja stanja
					if(sviPrijelazi.containsKey(stanjeEpsOkr+','+znak)){
						for(String s:sviPrijelazi.get(stanjeEpsOkr+','+znak)){
							epsOkrPrelaziU.add(s);
						}
					}
				}
				
				for(String stanje2:epsOkrPrelaziU){//Epsilon okruzenja za sve u koje prelaze iz epsOkruzenja gore stanja 
					for(String stanjeEpsOkr:epsOkruzenje(stanje2)){
						nkaPrelaziU.add(stanjeEpsOkr);
					}
				}
				
				if(!nkaPrelaziU.isEmpty()){//ne ubacuj nepostojeæe prijelaze
					nkaPrijelazi.put(stanje+","+znak, nkaPrelaziU);
				}
			}
		}
	}
	
	private static void NKA2DKA(){
		ArrayList<TreeSet<String>> novaStanja = new ArrayList<TreeSet<String>>();
		
		//GLUPO
		TreeSet<String> pocetno = new TreeSet<String>();
		pocetno.addAll(epsOkruzenje(nkaPocetno));
		dkaStanja.add(pocetno);
		
		for(String z:sviUlazniZnakovi){
			TreeSet<String> novoStanje = new TreeSet<String>(); 
			
			if(nkaPrijelazi.containsKey(nkaPocetno+','+z))//tu mi je bio error pa sam dodao
			for(String s:nkaPrijelazi.get(nkaPocetno+','+z)){
				novoStanje.add(s);
			}
			
			if(!novaStanja.contains(novoStanje)){
				novaStanja.add(novoStanje);
			}
			
			dkaPrijelazi.put(pocetno.toString()+","+z, novoStanje);
		}
		
		while(true){
			ArrayList<TreeSet<String>> novijaStanja = new ArrayList<TreeSet<String>>();
			novijaStanja.addAll(novaStanja);
			
			for(TreeSet<String> velikoStanje:novaStanja){
				for(String z:sviUlazniZnakovi){
					TreeSet<String> velStanjePrijelaza = new TreeSet<String>();
					
					for(String stanje:velikoStanje){
						if(nkaPrijelazi.containsKey(stanje+','+z)){
							for(String stanjePrijelaza:nkaPrijelazi.get(stanje+','+z)){
								velStanjePrijelaza.add(stanjePrijelaza);
							}
						}
					}
					if(!velStanjePrijelaza.isEmpty()){
						dkaPrijelazi.put(velikoStanje.toString()+","+z, velStanjePrijelaza);
						if(!novijaStanja.contains(velStanjePrijelaza))
							novijaStanja.add(velStanjePrijelaza);
					}
					else{
						TreeSet<String> arg1 = new TreeSet<String>();
						arg1.add("(X)");
						dkaPrijelazi.put(velikoStanje.toString()+","+z, arg1);
					}
				}
			}
			
			if(novaStanja.equals(novijaStanja)){
				dkaStanja.addAll(novaStanja);
				return;
			}
			
			for(TreeSet<String> stanje:novijaStanja){
				if(!novaStanja.contains(stanje)){
					novaStanja.add(stanje);
				}
			}
		}
	}
	
}
