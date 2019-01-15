package com.open.application.spider.models;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TbPlaylist {
    private Long playlistid;

    private String listtitle;

    private String listhref;

    private String photohref;

    private Integer ishot;
}