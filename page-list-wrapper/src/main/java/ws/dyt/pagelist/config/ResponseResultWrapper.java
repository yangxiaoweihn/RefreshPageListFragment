package ws.dyt.pagelist.config;

import java.util.List;

/**
 * 数据包装器
 *
 * @param <T_RESPONSE>
 */
public class ResponseResultWrapper<T_RESPONSE> {
    public int StatusCode;
    public String StatusMessage;
    public List<T_RESPONSE> Data;

    public ResponseResultWrapper(int statusCode, String statusMessage, List<T_RESPONSE> data) {
        this.StatusCode = statusCode;
        this.StatusMessage = statusMessage;
        this.Data = data;
    }
}