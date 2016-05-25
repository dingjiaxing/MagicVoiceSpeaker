package biz.home.model;

public enum MagicResultQuestionEnum {
	
	RESOURCE("RESOURCE");

	private String code;

	MagicResultQuestionEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
