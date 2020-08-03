package com.hb.strategy.lawrence.outer.model.future;

import com.hb.strategy.lawrence.outer.AbstractSimpleCrawler;
import org.springframework.web.client.RestTemplate;

/**
 * 原油主力爬取
 */
public class NiMainCrawler extends AbstractSimpleCrawler {

    private static final String url = "https://gu.sina.cn/ft/api/jsonp.php/var%20_NID_5_" + System.currentTimeMillis() / 1000 + "=/GlobalService.getMink?symbol=NID&type=5";

    public NiMainCrawler(RestTemplate restTemplate) {
        super(url, restTemplate, "伦镍", -2, 0,"01:00:00");
    }

}
