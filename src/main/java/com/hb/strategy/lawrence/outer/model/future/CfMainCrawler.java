package com.hb.strategy.lawrence.outer.model.future;

import com.hb.strategy.lawrence.outer.AbstractSimpleCrawler;
import org.springframework.web.client.RestTemplate;

/**
 * 原油主力爬取
 */
public class CfMainCrawler extends AbstractSimpleCrawler {

    private static final String url = "https://gu.sina.cn/ft/api/jsonp.php/var%20_CT_5_" + System.currentTimeMillis() / 1000 + "=/GlobalService.getMink?symbol=CT&type=5";

    public CfMainCrawler(RestTemplate restTemplate) {
        super(url, restTemplate, "美棉", -3, -1,"23:00:00");
    }

}
