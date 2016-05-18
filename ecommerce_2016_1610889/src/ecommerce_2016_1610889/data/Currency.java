package ecommerce_2016_1610889.data;

public class Currency {

	private String name;
	private double toDollar;
	private double toEur;
	
	
	public Currency(){
		
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getToDollar() {
		return toDollar;
	}

	public void setToDollar(double toDollar) {
		this.toDollar = toDollar;
	}

	public double getToEur() {
		return toEur;
	}

	public void setToEur(double toEur) {
		this.toEur = toEur;
	}

	
}
