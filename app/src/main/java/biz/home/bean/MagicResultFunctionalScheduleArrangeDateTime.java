package biz.home.bean;

import java.io.Serializable;

public class MagicResultFunctionalScheduleArrangeDateTime implements Serializable {

	
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1726329762006511922L;

	private String date;
    
    private String dateOrig;
    
    private String type;
    
	private String time;
    
    private String timeOrig;
    
    
    public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDateOrig() {
		return dateOrig;
	}

	public void setDateOrig(String dateOrig) {
		this.dateOrig = dateOrig;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTimeOrig() {
		return timeOrig;
	}

	public void setTimeOrig(String timeOrig) {
		this.timeOrig = timeOrig;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}





}
