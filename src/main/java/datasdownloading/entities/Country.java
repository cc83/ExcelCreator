package main.java.datasdownloading.entities;

import java.util.ArrayList;
import java.util.List;

public class Country {
	private String name;
	
	private Record total;
	
	private List<Record> records;

	public Country() {
		total = new Record();
		records = new ArrayList<>();
		name = "";
	}

	public Country(String name, Record total, List<Record> records) {
		this.name = name;
		this.total = total;
		this.records = records;
	}

	public Record getTotal() {
		return total;
	}

	public void setTotal(Record total) {
		this.total = total;
	}

	public List<Record> getRecords() {
		return records;
	}

	public void setRecords(List<Record> records) {
		this.records = records;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		String res = "";
		
		res += "\nName : " + name + "\n";
		res += "Records list : " + records + "\n";
		res += "Total : " + total + "\n";
		
		return res;
	}

}
