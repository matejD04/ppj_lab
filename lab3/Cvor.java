import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Cvor {
	public String ime;
	public ArrayList<Cvor> djeca;
	public Cvor roditelj;
	public String tip;
	public String l_izraz;
	public int razina;  //dubina u stablu
	public int index;   //index u listi objekata, koristi se pri stvaranju stabla
	
	private static ArrayList<HashMap<String,String>> lista;
	
	public Cvor(){
		
	}
	
	public Cvor(String ime1, Cvor roditelj1, int razina1, int index1){
		ime=ime1;
		roditelj=roditelj1;
		razina=razina1;
		index=index1;
		tip=null;
		l_izraz=null;
	}
	
	@Override
	public String toString(){
		String a="";
		for(int i=0;i<razina;i++)
			a+=" ";
		a+=ime+"\n";
		for(Cvor d : djeca)
			a+=d;
		return a;
	}
	
	public String samo_ovaj_cvor(){
		return ime+" "+tip+" "+l_izraz+" "+razina;
	}
	
	public String trenutacna_produkcija(){
		String a=ime+" ::=";
		for(int i=0;i<djeca.size();i++)
			a+=" "+djeca.get(i).ime;
		return a;
	}
	public static Cvor stvori_stablo_iz_filea(BufferedReader bf) throws IOException{
		String line;
		line = bf.readLine();
		HashMap<String, String> redak = new HashMap<String,String>();
		lista = new ArrayList<HashMap<String,String>>();
		while(line!=null){
			redak.put("objekt", line.trim());
			redak.put("stupanj", ""+prebroji_razmake(line));
			lista.add(redak);
			redak = new HashMap<String,String>();
			line = bf.readLine();
		}
		Cvor glavni = new Cvor(lista.get(0).get("objekt"),null,0,0);
		stvori_cvorove(glavni);
		return glavni;
	}
	private static int prebroji_razmake(String tekst){
		int brojac=0;
		for(int i=0;i<tekst.length();i++){
			if(tekst.charAt(i)==' ')
				brojac++;
			else
				return brojac;
		}
		return 0;
	}
	private static void stvori_cvorove(Cvor roditelj){
		ArrayList<Cvor> djeca = new ArrayList<Cvor>();
		for(int i=roditelj.index+1;i<=raspon_djece(roditelj.index);i++){
			if(Integer.parseInt(lista.get(i).get("stupanj"))==roditelj.razina+1){
				Cvor sljedeci = new Cvor(lista.get(i).get("objekt"),roditelj,roditelj.razina+1,i);
				djeca.add(sljedeci);
				stvori_cvorove(sljedeci);
			}
		}
		roditelj.djeca=djeca;
	}
	
	/** vraca index zadnjeg djeteta
	 * 
	 * @param roditelj - index roditelja u listi
	 * @return
	 */
	private static int raspon_djece(int roditelj){
		int razina = Integer.parseInt(lista.get(roditelj).get("stupanj"));
		int index=roditelj;
		for(int i=roditelj+1;i<lista.size();i++){
			HashMap<String,String> redak=lista.get(i);
			if(Integer.parseInt(redak.get("stupanj"))==razina+1)
				index=i;
			else if(Integer.parseInt(redak.get("stupanj"))==razina)
				break;
		}
		return index;
	}
}
