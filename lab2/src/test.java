import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class test {

	public static void main(String[] args) throws IOException {
		
		Gramatika G = Gramatika.fromSanDefinition("lab2_primjeri\\pr_knjiga\\test.san");
		
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
		
		System.out.println("OK");
	}
}
