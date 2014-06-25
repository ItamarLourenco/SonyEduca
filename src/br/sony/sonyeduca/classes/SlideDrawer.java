package br.sony.sonyeduca.classes;

public class SlideDrawer {
	private String title;
	private String count = "0";
	private boolean is_count = false;
	
	public SlideDrawer(){}
	public SlideDrawer(String title){
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}

	public String getCount() {
		return count;
	}
	public boolean isIs_count() {
		return is_count;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public void setCount(String count) {
		this.count = count;
	}
	public void setIs_count(boolean is_count) {
		this.is_count = is_count;
	}
}
