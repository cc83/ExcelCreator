package main.java.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import main.java.datasdownloading.entities.Country;
import main.java.datasdownloading.entities.Record;
import main.java.datasdownloading.entities.RecordWithTotal;
import main.java.datasdownloading.entities.RecordWithTotalChainedComparator;

public class Sheets {

	public static List<Country> getCountryList(List<Record> records) {
		List<Country> countries = new ArrayList<>();
		Country c = new Country();
		int totImp = 0;
		int totClicks = 0;
		for (int i = 0; i < records.size(); i++) {
			if ((i+1) < records.size()) {
				if (records.get(i+1).getCountryName().equals(records.get(i).getCountryName())) {
					c.setName(records.get(i).getCountryName());
					c.getRecords().add(records.get(i));
					totImp += records.get(i).getImpressions();
					totClicks += records.get(i).getClicks();
				} else {
					c.setName(records.get(i).getCountryName());
					c.setTotal(new Record("total", c.getName(), totImp + records.get(i).getImpressions(), totClicks + records.get(i).getClicks()));
					c.getRecords().add(records.get(i));
					Country zk = new Country(c.getName(), c.getTotal(), c.getRecords());
					countries.add(zk);
					c = new Country();
					totImp = 0;
					totClicks = 0;
				}
				
			} else {
				c.setName(records.get(i).getCountryName());
				c.setTotal(new Record("total", c.getName(), totImp + records.get(i).getImpressions(), totClicks + records.get(i).getClicks()));
				c.getRecords().add(records.get(i));
				Country zk = new Country(c.getName(), c.getTotal(), c.getRecords());
				countries.add(zk);
				c = new Country();
				totImp = 0;
				totClicks = 0;
			}
			
		}
		
		Collections.sort(countries, new Comparator<Country>() {
			@Override
			public int compare(Country o1, Country o2) {
				int totalImp = o2.getTotal().getImpressions() - o1.getTotal().getImpressions();
				if (totalImp == 0) {
					return o1.getName().compareTo(o2.getName());
				}

				return totalImp;
			}
		});
		
		for (Country cr : countries) {
			cr.getRecords().sort(new Comparator<Record>() {

				@Override
				public int compare(Record o1, Record o2) {
					return o2.getImpressions() - o1.getImpressions();
				}
			});
		}
		
		return countries;
	}
	
	
	
	public  static List<RecordWithTotal> getRecordWithDataList(List<Country> countries) {
	    List<RecordWithTotal> list = new ArrayList<>();
	    
	    for (Country c : countries) {
	        int total = c.getTotal().getImpressions();
	        List<Record> records = c.getRecords();
                for (Record r : records) {
                    RecordWithTotal rwt = new RecordWithTotal(r, total);
                    list.add(rwt);
                }
            }
	    
	    return list;
	}
	
}
