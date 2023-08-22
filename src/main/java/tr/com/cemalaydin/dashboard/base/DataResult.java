package tr.com.cemalaydin.dashboard.base;


import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DataResult<T> {

    private boolean success;
    private T data;
    private String errorCode;
    private String errorMessage;
    private List<Map<String, Object>> messageList;


    public DataResult() {
        this.success = true;
    }

    public DataResult(boolean success) {
        this.success = success;
    }


    public DataResult(T data) {
        this.data = data;
        this.success = true;
    }

    public DataResult(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.success = false;
    }
}
