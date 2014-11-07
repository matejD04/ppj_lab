import java.util.ArrayList;
import java.util.List;


public class ProdukcijaGramatike {

	private String leftSide;
	private List<String> rightSide;		// ako je rightSide slucajno prazna lista - onda je ovo epsilon produkcija
	private int prioritet;
	
	private ProdukcijaGramatike() {
		leftSide = null;
		rightSide = null;
		prioritet = 0;				// prioritet za razrjesavanje Reduciraj/Reduciraj proturjecja - manja vrijednost je veci prioritet
	}
	
	public String getLeftSide() {
		return leftSide;
	}
	
	public List<String> getRightSide() {
		return rightSide;
	}	
	
	public int getPrioritet() {
		return prioritet;
	}
	
	public static ProdukcijaGramatike fromDefinitionString(String leftSide, String rightSide, int prioritet){
		ProdukcijaGramatike p = new ProdukcijaGramatike();
		
		p.leftSide = leftSide;
		p.rightSide = new ArrayList<String>();
		p.prioritet = prioritet;
		
		String[] symbolSequence = rightSide.trim().split(" ");
		for(String symbol : symbolSequence){
			if(!symbol.isEmpty()) p.rightSide.add(symbol);
		}		
		
		return p;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((leftSide == null) ? 0 : leftSide.hashCode());
		result = prime * result + prioritet;
		result = prime * result
				+ ((rightSide == null) ? 0 : rightSide.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProdukcijaGramatike other = (ProdukcijaGramatike) obj;
		if (leftSide == null) {
			if (other.leftSide != null)
				return false;
		} else if (!leftSide.equals(other.leftSide))
			return false;
		if (prioritet != other.prioritet)
			return false;
		if (rightSide == null) {
			if (other.rightSide != null)
				return false;
		} else if (!rightSide.equals(other.rightSide))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(leftSide).append("->");
		
		for(String symbol : rightSide){
			sb.append(symbol);
		}
		
		sb.append(" [").append(prioritet).append("]");
		
		return sb.toString();
	}
}
