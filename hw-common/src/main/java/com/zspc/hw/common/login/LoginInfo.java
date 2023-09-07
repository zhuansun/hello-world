package com.zspc.hw.common.login;

import lombok.Data;

import java.io.Serializable;


@Data
public class LoginInfo implements Serializable {
    private static final long serialVersionUID = -1L;

    private String userCode;

    private String userName;
}
