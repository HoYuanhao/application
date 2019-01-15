package com.open.application.spider.models;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TbComments {
    private Long commentid;

    private Long songid;

    private Long userid;

    private String nickname;

    private Integer likedcount;

    private String comment;

    private Long time;

    private String avatarurl;

    private Integer ishotcomments;

    private Integer commentnum;

}