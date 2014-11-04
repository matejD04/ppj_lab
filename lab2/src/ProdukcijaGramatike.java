import java.util.LinkedList;
import java.util.List;


public class ProdukcijaGramatike {

	private String leftSide;
	private List<String> rightSide;
	
	private ProdukcijaGramatike() {
		leftSide = null;
		rightSide = null;
	}
	
	public String getLeftSide() {
		return leftSide;
	}
	
	public List<String> getRightSide() {
		return rightSide;
	}	
	
	public static ProdukcijaGramatike fromDefinitionString(String leftSide, String rightSide){
		ProdukcijaGramatike p = new ProdukcijaGramatike();
		
		p.leftSide = leftSide;
		p.rightSide = new LinkedList<String>();
		
		String[] symbolSequence = rightSide.trim().split(" ");
		for(String symbol : symbolSequence){
			p.rightSide.add(symbol);
		}		
		
		return p;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((leftSide == null) ? 0 : leftSide.hashCode());
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
		
		return sb.toString();
	}
}
