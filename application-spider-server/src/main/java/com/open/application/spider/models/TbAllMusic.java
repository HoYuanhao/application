package com.open.application.spider.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TbAllMusic {
    private Long songid;

    private String songname;

    private String songsinger;

    private String songurl;

    private String outerurl;

    private String lyric;

}