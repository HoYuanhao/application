package com.open.application.common.util;


import org.apache.http.HttpHost;

public class EsUtil {

  public static HttpHost[] getHttpHost(String address, String scheme) {
    String[] addressArray = address.split(",");
    HttpHost[] httpHosts = new HttpHost[addressArray.length];
    for (int i = 0; i < httpHosts.length; i++) {
      String[] addr = addressArray[i].split(":");
      httpHosts[i] = new HttpHost(addr[0], Integer.parseInt(addr[1]), scheme);
    }
    return httpHosts;
  }

  public static String getTaskIndex(String id) {
    return "task_" + id + "_index";
  }

  public static String getType() {
    return "_doc";
  }

}
