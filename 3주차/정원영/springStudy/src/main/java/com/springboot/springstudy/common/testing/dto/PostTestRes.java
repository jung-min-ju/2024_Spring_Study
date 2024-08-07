package com.springboot.springstudy.common.testing.dto;

import java.util.Date;

public record PostTestRes(String title,
                          String content,
                          Date date,
                          Integer views,
                          Integer like_count) {
}
