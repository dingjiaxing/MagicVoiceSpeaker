package biz.home.bean;

import java.io.Serializable;

public class MagicResultFunctionalSchedule implements Serializable{
        
	/**
	 * 
	 */
	private static final long serialVersionUID = -7636315154933415502L;

	/*
	 * 闹钟、日程:取值为reminder或clock
	 */
	private String name;
	
	/*
	 * 名称
	 */
	private String content;
	
	/*
	 * 安排时间
	 */

	private String repeat;

	public String getRepeat() {
		return repeat;
	}

	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}

	private MagicResultFunctionalScheduleArrangeDateTime datetime;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public MagicResultFunctionalScheduleArrangeDateTime getDatetime() {
		return datetime;
	}

	public void setDatetime(MagicResultFunctionalScheduleArrangeDateTime datetime) {
		this.datetime = datetime;
	}
	
	
}
