package com.open.application.common.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TbAllSinger implements Serializable {
    private Long singerId;

    private String singerName;

    private String singerHref;

    private String singerDesc;

    private Date getTime;
}