package com.powernode.model;

import com.powernode.constant.BusinessEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> implements Serializable {

    /**
     * 响应码
     */
    private Integer code = 200;

    /**
     * 响应信息
     */
    private String msg = "ok";
    /**
     * 响应数据
     */
    private T data;

    /**
     * 操作成功
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(T data)
    {
        Result<T> result = new Result<>();
        result.setData(data);
        return result;
    }

    /**
     * 操作失败
     * @param businessEnum
     * @param <T>
     * @return
     */
    public static <T> Result<T> fail(BusinessEnum businessEnum)
    {
        Result<T> result = new Result<>();
        result.setCode(businessEnum.getCode());
        result.setMsg(businessEnum.getDesc());
        return result;
    }

    /**
     * 操作失败
     * @param code
     * @param msg
     * @return
     * @param <T>
     */
    public static <T> Result<T> fail(Integer code,String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
