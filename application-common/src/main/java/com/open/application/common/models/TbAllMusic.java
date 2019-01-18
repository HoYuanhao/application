package com.open.application.common.models;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class TbAllMusic {
    private Long songId;

    private String songName;

    private String songSinger;

    private String songUrl;

    private String outerUrl;

    private String lyric;

    private Date getTime;

}