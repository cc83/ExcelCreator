package main.java.datasdownloading.entities;

public class RecordWithTotal extends Record {

	private int totalImp;

	public int getTotalImp() {
		return totalImp;
	}

	public void setTotalImp(int totalImp) {
		this.totalImp = totalImp;
	}

	public RecordWithTotal(int totalImp) {
		super();
		this.totalImp = totalImp;
	}
	
	
	public RecordWithTotal(Record r,int totalImp) {
	    super(r.getPublisherName(),r.getCountryName(),r.getImpressions(),r.getClicks());
	    this.totalImp = totalImp;

	}
	
    @Override
    public String toString() {
        return "RecordWithTotal [Publisher : "+publisherName+", totalImp : " + totalImp + "]";
    }
	
	
}
