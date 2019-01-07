package com.open.application.console.server.util;

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

  public static String getIndex(String id,String pre,String suf){
    return pre+"_"+id+"_"+suf;
  }

  public static String getType(){
    return "_doc";
  }

}
