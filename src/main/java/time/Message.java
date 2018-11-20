package time;

/**
 * @author fandong
 * @create 2018/11/8
 */
public class Message {

    private String msg;

    private String type;

    private String auth;

    private String fromUsername;

    private String toUsername;

    private String code;

    private String errMsg;

    public Message() {
    }

    public Message(String code, String errMsg) {
        this.code = code;
        this.errMsg = errMsg;
    }



    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }

    public String getFromUsername() {
        return fromUsername;
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    public Message(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Message{" +
                "msg='" + msg + '\'' +
                ", type='" + type + '\'' +
                ", auth='" + auth + '\'' +
                ", fromUsername='" + fromUsername + '\'' +
                ", toUsername='" + toUsername + '\'' +
                ", code='" + code + '\'' +
                ", errMsg='" + errMsg + '\'' +
                '}';
    }
}
