import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;



public class SemantickiAnalizator {

	public static void main(String[] args) throws IOException {
		BufferedReader bf = new BufferedReader(new FileReader("primjeri/16_cast2/test.in"));
		Cvor glavni = Cvor.stvori_stablo_iz_filea(bf);
		System.out.println(glavni);
	}

}
