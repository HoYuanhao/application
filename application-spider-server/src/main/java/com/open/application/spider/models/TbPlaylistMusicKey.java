package com.open.application.spider.models;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TbPlaylistMusicKey {
    private Long playlistid;

    private Long songid;

}