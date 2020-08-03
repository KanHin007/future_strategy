package com.hb.strategy.lawrence.outer;

import com.hb.strategy.lawrence.outer.model.CrawlerResult;
import com.hb.strategy.lawrence.outer.model.future.NiMainCrawler;
import com.hb.strategy.lawrence.outer.model.future.ScMainCrawler;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainRunner {

    public static final String PREFIX = "com.hb.strategy.lawrence.outer.model.future";

    public static void main(String[] args) {

        RestTemplate restTemplate = new RestTemplate();
        List<String> classList = getClassList();
        List<CrawlerResult> results = new ArrayList<>(10);
        classList.forEach(k -> {
            try {
                Class<? extends AbstractSimpleCrawler> c = (Class<? extends AbstractSimpleCrawler>) ClassUtils.getClass(k);
                Constructor<? extends AbstractSimpleCrawler> constructor = c.getDeclaredConstructor(RestTemplate.class);
                AbstractSimpleCrawler crawler = constructor.newInstance(restTemplate);
                CrawlerResult result = crawler.crawler();
                if(result != null) {
                    results.add(result);
                }else{
                    results.add(new CrawlerResult(c.getSimpleName(),null,null));
                }
                System.out.println(result);
            } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
        final StringBuilder stringBuilder = new StringBuilder("品种,之前收盘价,当前价格,差值\n");
        // 处理result
        results.forEach(k -> {

            stringBuilder.append(k.getFutureName())
                    .append(",")
                    .append(k.getPreClosePrice())
                    .append(",")
                    .append(k.getNowPrice())
                    .append(",")
                    .append(k.getSubtractPrice())
                    .append("\n");
        });

        System.out.println(stringBuilder);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            File file = new File("out.csv");
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(stringBuilder.toString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static List<String> getClassList() {
        return Arrays.<String>asList(PREFIX + ClassUtils.PACKAGE_SEPARATOR_CHAR + "ScMainCrawler",
                PREFIX + ClassUtils.PACKAGE_SEPARATOR_CHAR + "NiMainCrawler",
                PREFIX + ClassUtils.PACKAGE_SEPARATOR_CHAR + "CuMainCrawler",
                PREFIX + ClassUtils.PACKAGE_SEPARATOR_CHAR + "AlMainCrawler",
                PREFIX + ClassUtils.PACKAGE_SEPARATOR_CHAR + "AgMainCrawler",
                PREFIX + ClassUtils.PACKAGE_SEPARATOR_CHAR + "AuMainCrawler",
                PREFIX + ClassUtils.PACKAGE_SEPARATOR_CHAR + "BMainCrawler",
                PREFIX + ClassUtils.PACKAGE_SEPARATOR_CHAR + "CfMainCrawler",
                PREFIX + ClassUtils.PACKAGE_SEPARATOR_CHAR + "SnMainCrawler",
                PREFIX + ClassUtils.PACKAGE_SEPARATOR_CHAR + "ZnMainCrawler");
    }


}
