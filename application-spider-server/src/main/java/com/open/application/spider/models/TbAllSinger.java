package com.open.application.spider.models;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TbAllSinger {
    private Long singerid;

    private String singername;

    private String singerhref;

    private String singerdesc;

}