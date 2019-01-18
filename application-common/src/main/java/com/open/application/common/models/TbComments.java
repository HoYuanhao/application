package com.open.application.common.models;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class TbComments {
    private Long commentId;

    private Long songId;

    private Long userId;

    private String nickName;

    private Integer likedCount;

    private String comment;

    private Long time;

    private String avatarUrl;

    private Integer isHotComments;

    private Integer commentNum;
    private Date getTime;

}