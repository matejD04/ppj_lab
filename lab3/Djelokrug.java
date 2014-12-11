import java.util.ArrayList;
import java.util.HashMap;


public class Djelokrug {
	public ArrayList<Djelokrug> djecaDjelokrug;//nepotrebno?
	public Djelokrug roditeljDjelokrug;//ugnjezdujuci blok
	public ArrayList<Cvor> cvorovi;
	
	public HashMap<String, Identifikator> tablica_lokalnih_imena;
	
	public Djelokrug(Djelokrug roditeljDjelokrug){
		this.tablica_lokalnih_imena = new HashMap<String, Identifikator>();
		this.roditeljDjelokrug = roditeljDjelokrug;
	}
	
	public void dodajIdentifikatorUTablicu(String tip, String naziv, String vrijednost){
		tablica_lokalnih_imena.put(naziv, new Identifikator(tip, vrijednost));
	}
	
	public boolean sadrzi(String ime){
		if(tablica_lokalnih_imena.containsKey(ime)){
			return true;
		}
		else{
			if(roditeljDjelokrug!=null){
				return roditeljDjelokrug.sadrzi(ime);
			}
			else{
				return false;
			}
		}
	}
}
