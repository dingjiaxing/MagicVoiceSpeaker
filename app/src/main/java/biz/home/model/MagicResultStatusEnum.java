package biz.home.model;

/**
 * Created by admin on 2015/9/9.
 */
public enum MagicResultStatusEnum {
    /*
	 * 资源
	 */
    ERROR("ERROR"),

    /*
     * 对话注册
     */
    SUCCESS("SUCCESS");


    private String code;

    MagicResultStatusEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
