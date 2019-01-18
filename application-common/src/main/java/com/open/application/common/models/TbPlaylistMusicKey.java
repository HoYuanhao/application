package com.open.application.common.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TbPlaylistMusicKey {
    private Long playlistid;

    private Long songid;
    private Date getTime;

}