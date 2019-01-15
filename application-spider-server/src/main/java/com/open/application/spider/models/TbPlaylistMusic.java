package com.open.application.spider.models;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TbPlaylistMusic extends TbPlaylistMusicKey {
    private String songname;

    private String songsinger;

    private String songurl;

    private String outerurl;

}