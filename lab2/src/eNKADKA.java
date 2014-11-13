import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
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
		System.out.println("DEF DKA");
		for(TreeSet<String> i:dkaStanja){
			for(String j:sviUlazniZnakovi){
				if(dkaPrijelazi.containsKey(i.toString()+','+j)){
					System.out.println(i.toString()+','+j+"->"+dkaPrijelazi.get(i.toString()+','+j).toString());
				}
			}
		}
	}
	
	private static void defGoranNka() throws IOException {
		test.test();
		goraneNka = test.sviPrijelazieNka;
		System.out.print(goraneNka.toString());
		
		for(HashMap<String, Object> h:goraneNka){
			System.out.println(h.get("poc").toString()+"{"+h.get("skupp").toString()+"}"+","+h.get("znak").toString()+" ==> "+h.get("zavr").toString()+"{"+h.get("skupk").toString()+"}");
			svaStanja.add(h.get("poc").toString()+"{"+h.get("skupp").toString()+"}");
			svaStanja.add(h.get("poc").toString()+"{"+h.get("skupk").toString()+"}");
			if(!h.get("znak").toString().equals("$"))
				sviUlazniZnakovi.add(h.get("znak").toString());
			
			if(h.get("poc").toString().startsWith("<#S0#>") && h.get("znak").toString().equals("$")){//DEFINICIJA POCETNOG STANJA, bice samo jedno stanje koje pocinje <#S0#>.... i ima $ prijelaz
				nkaPocetno = h.get("poc").toString()+"{"+h.get("skupp").toString()+"}";
			}
			
			if(sviPrijelazi.containsKey(h.get("poc").toString()+"{"+h.get("skupp").toString()+"}"+","+h.get("znak").toString())){
				if(true){
					sviPrijelazi.get(h.get("poc").toString()+"{"+h.get("skupp").toString()+"}"+","+h.get("znak").toString()).add(h.get("zavr").toString()+"{"+h.get("skupk").toString()+"}");
				}
			}
			else{
				TreeSet<String> prelaziU = new TreeSet<String>();
				prelaziU.add(h.get("zavr").toString()+"{"+h.get("skupk").toString()+"}");
				sviPrijelazi.put(h.get("poc").toString()+"{"+h.get("skupp").toString()+"}"+","+h.get("znak").toString(), prelaziU);
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
