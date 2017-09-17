package dhcc.cn.com.fix_phone.remote;

/**
 * 2016/10/27 13
 */
public class ApiException extends Exception {
    private int code;

    public ApiException(int code, String message) {
        super(message);
        this.code = code;
    }
}
