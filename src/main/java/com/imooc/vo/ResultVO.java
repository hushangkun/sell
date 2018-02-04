package com.imooc.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * http请求返回的最外层的对象
 */
@Data
public class ResultVO<T> implements Serializable{

    //直接使用快捷键添加
    private static final long serialVersionUID = -8349181922768084604L;

    //错误码
    private Integer code;

    //提示信息
    private String msg;

    //返回的具体内容
    private T data;

}
