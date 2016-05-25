package biz.home.bean;

/**
 * Created by admin on 2015/11/3.
 */
public class ArtificialAnswer {
    /*
        客服回复的结果的类型：text、url
     */
    private String type;
    /*
    客服回复的问题
     */
    private String question;
    /*
    客服回复的答案
     */
    private String answer;
    /*
    客服回复的url连接
     */
    private String url;

    private String messageId;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public ArtificialAnswer() {
    }

    public ArtificialAnswer(String type, String question, String answer, String url) {
        this.type = type;
        this.question = question;
        this.answer = answer;
        this.url = url;
    }

    public ArtificialAnswer(String type, String question, String answer) {
        this.type = type;
        this.question = question;
        this.answer = answer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
