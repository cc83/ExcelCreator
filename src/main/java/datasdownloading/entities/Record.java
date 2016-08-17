package main.java.datasdownloading.entities;

public class Record {
	private String publisherName = "";
	
	private String countryName = "";
	
	private int impressions;
	
	private int clicks;

	public Record() {
	}

	public Record(String publisherName, String countryName, int impressions, int clicks) {
		this.publisherName = publisherName;
		this.countryName = countryName;
		this.impressions = impressions;
		this.clicks = clicks;
	}

	public String getPublisherName() {
		return publisherName;
	}

	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public int getImpressions() {
		return impressions;
	}

	public void setImpressions(int impressions) {
		this.impressions = impressions;
	}

	public int getClicks() {
		return clicks;
	}

	public void setClicks(int clicks) {
		this.clicks = clicks;
	}
}
