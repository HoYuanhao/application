package com.open.application.common.models;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class TbPlaylist {
    private Long playListId;

    private String listTitle;

    private String listHref;

    private String photoHref;

    private Integer isHot;
    private Date getTime;
}