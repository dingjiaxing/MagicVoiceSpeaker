package biz.home.bean;

import java.io.Serializable;
//import java.util.Date;
//
//import org.springframework.format.annotation.DateTimeFormat;
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//import com.magic.common.base.vo.PageInfo;

public class QuestionAnswerSql implements Serializable{
	private static final long serialVersionUID = -8642562602118695751L;
	private Integer id;
	private String question;
	private String answer;
//	private Date createDate;
//	private String status;
//	/**
//	 * 开始时间
//	 */
//	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
//	@JsonFormat(pattern="yyyy-MM-dd HH:mm", locale = "zh" , timezone="GMT+8")
//	private Date beginDate;
//	/**
//	 * 结束时间
//	 */
//	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
//	@JsonFormat(pattern="yyyy-MM-dd HH:mm", locale = "zh" , timezone="GMT+8")
//	private Date endDate;
//	/**
//	 * 排序
//	 */
//	private String orderBy;
//	/**
//	 * 分页参数
//	 */
//	private PageInfo page;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
//	public Date getCreateDate() {
//		return createDate;
//	}
//	public void setCreateDate(Date createDate) {
//		this.createDate = createDate;
//	}
//	public String getStatus() {
//		return status;
//	}
//	public void setStatus(String status) {
//		this.status = status;
//	}
//	public Date getBeginDate() {
//		return beginDate;
//	}
//	public void setBeginDate(Date beginDate) {
//		this.beginDate = beginDate;
//	}
//	public Date getEndDate() {
//		return endDate;
//	}
//	public void setEndDate(Date endDate) {
//		this.endDate = endDate;
//	}
//	public String getOrderBy() {
//		return orderBy;
//	}
//	public void setOrderBy(String orderBy) {
//		this.orderBy = orderBy;
//	}
//	public PageInfo getPage() {
//		return page;
//	}
//	public void setPage(PageInfo page) {
//		this.page = page;
//	}
	
}
