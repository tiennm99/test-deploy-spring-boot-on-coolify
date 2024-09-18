package com.miti99.testdeployspringbootoncoolify.entity;

import java.io.Serializable;
import lombok.Data;

@Data
public class UserCache implements Serializable {
    private String id;
    private String name;
    private String email;
}
