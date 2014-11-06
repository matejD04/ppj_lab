import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class test {

	public static void main(String[] args) throws IOException {
		
		Gramatika G = Gramatika.fromSanDefinition("lab2_primjeri\\pr_knjiga\\test.san");
		
		
		List<String> niz = new ArrayList<String>();
		niz.add("<A>");
		System.out.println(G.skupZapocinje(niz));
		
		niz.clear();
		niz.add("<B>");
		System.out.println(G.skupZapocinje(niz));
		
		niz.clear();
		niz.add("<C>");
		System.out.println(G.skupZapocinje(niz));
		
		niz.clear();
		niz.add("<D>");
		System.out.println(G.skupZapocinje(niz));
		
		niz.clear();
		niz.add("<E>");
		System.out.println(G.skupZapocinje(niz));
		
		
		System.out.println("OK");
	}
}
