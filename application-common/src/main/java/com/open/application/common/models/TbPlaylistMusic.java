package com.open.application.common.models;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class TbPlaylistMusic {

  private Long playlistid;

  private Long songid;

  private String songname;

  private String songsinger;

  private String songurl;

  private String outerurl;

  private Date getTime;

}