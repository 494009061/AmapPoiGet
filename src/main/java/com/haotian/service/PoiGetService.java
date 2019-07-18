package com.haotian.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haotian.entity.HttpResponse;
import com.haotian.entity.PoiConfig;
import com.haotian.entity.PoiConfigRoot;
import com.haotian.util.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 开始获取的工具类
 *
 * @author: Mr.Zhang
 * @email zhangpeng@hiynn.com
 * @date: 2019/7/17 17:02
 */
@Component
@Order(1)
public class PoiGetService implements CommandLineRunner {

    private Logger log = LoggerFactory.getLogger(PoiGetService.class);
    /**
     * 总行数记录
     */
    private AtomicInteger sumCount = new AtomicInteger(0);
    /**
     * 每一次查询返回的数据的总量 查询结束归零
     */
    private int pageCount = 0;
    /**
     * 文件计数
     */
    private AtomicInteger fileCount = new AtomicInteger(0);
    /**
     * 错误计数
     */
    private AtomicInteger errorCount = new AtomicInteger(0);
    private BufferedWriter bw = null;


    public PoiGetService() {
        log.info("this is PoiGetService implements CommandLineRunner ");
    }


    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     * @throws Exception on error
     */
    @Override
    public void run(String... args) throws Exception {
        try (
                InputStream resourceAsStream = PoiGetService.class.getClassLoader().getResourceAsStream("poiConfig.json");
                InputStreamReader isr = new InputStreamReader(resourceAsStream, "utf-8");
                BufferedReader br = new BufferedReader(isr);

        ) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
            String configStr = stringBuilder.toString();
            log.info(configStr);
            PoiConfigRoot poiConfigRoot = JSON.parseObject(configStr, PoiConfigRoot.class);
            // 创建输出文件目录
            File filePath = new File(poiConfigRoot.getOutFilePath());
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
            // 第一个输出文件
            final File targetFile = new File(poiConfigRoot.getOutFilePath() + File.separator + poiConfigRoot.getFileName() + "-0.csv");

            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile), "utf-8"));

            // 请求api
            Map<String, String> param = new HashMap<>(8);
            AtomicInteger page = new AtomicInteger(0);
            param.put("key", poiConfigRoot.getKey());
            param.put("offset", String.valueOf(poiConfigRoot.getOffset()));
            param.put("page", String.valueOf(page.incrementAndGet()));
            param.put("extensions", "base");
            param.put("output", "JSON");
            param.put("citylimit", "true");
            param.put("children", "0");
            List<PoiConfig> configs = poiConfigRoot.getConfigs();
            for (int i = 0; i < configs.size(); i++) {

                try {
                    PoiConfig config = configs.get(i);

                    if (StringUtils.isNotBlank(config.getKeyWords())) {
                        param.put("keywords", config.getKeyWords());
                    } else {
                        param.remove("keywords");
                    }

                    if (StringUtils.isNotBlank(config.getTypes())) {
                        param.put("types", config.getTypes());
                    } else {
                        param.remove("types");
                    }

                    if (StringUtils.isNotBlank(config.getCity())) {
                        param.put("city", config.getCity());
                    } else {
                        param.remove("city");
                    }

                    log.info(param.toString());

                    Thread.sleep(100);

                    HttpResponse httpResponse = HttpUtils.get(poiConfigRoot.getApiUrl(), param, HttpUtils.UTF8);

                    String stringMessage = httpResponse.getStringMessage();

                    JSONObject jsonObject = JSON.parseObject(stringMessage);

                    String status = jsonObject.getString("status");
                    // error check
                    if (status.equals("0")) {
                        if (errorCount.incrementAndGet() == 5) {
                            log.info("error Count 达到5次 退出 请检查错误日志");
                            System.exit(0);
                        }
                        log.info("查询失败错误码{}，错误信息{}。重新本次查询.", status, jsonObject.getString("info"));
                        i--;
                        continue;
                    }

                    Integer count = jsonObject.getInteger("count");
                    JSONArray pois = jsonObject.getJSONArray("pois");

                    for (int j = 0; pois != null && j < pois.size(); j++) {
                        JSONObject poi = pois.getJSONObject(j);
                        print(poiConfigRoot, poi);
                    }

                    log.info("count:{},pois:{}", count, pois.size());

                    // 每次最多显示50 累计达到count的值 就证明查完了
                    pageCount += pois.size();
                    if (count != 0 && pageCount < count) {
                        param.put("page", String.valueOf(page.incrementAndGet()));
                        i--;
                    } else {
                        page.set(1);
                        param.put("page",page.toString());
                        pageCount = 0;
                    }

                } catch (IOException | URISyntaxException e) {
                    log.info("", e);
                    // error check
                    if (errorCount.incrementAndGet() == 5) {
                        log.info("error Count 达到5次 退出 请检查错误日志");
                        System.exit(0);
                    }
                    i--;
                    continue;
                }
            }
        } finally {
            if (bw != null) {
                bw.close();
            }
        }

    }


    private void print(PoiConfigRoot configRoot, JSONObject poi) throws IOException {
        bw.append(poi.getString("name")).append(',').
                append(poi.getString("address")).append(',').
                append(poi.getString("pname")).append(poi.getString("cityname")).append(poi.getString("adname")).append(',').
                append(poi.getString("location")).append(',').
                append(poi.getString("type").replaceAll(";", ",")).append(System.lineSeparator());
        bw.flush();

        // 这个if 要放到 最后 来判断 计数是否达到上限
        if ((this.sumCount.incrementAndGet() % configRoot.getFileMaxRows()) == 0) {
            if (this.bw != null) {
                this.bw.close();
            }
            // 关闭上一个输出流 重新创建一个新的输出流和文件
            this.bw = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(
                                    configRoot.getOutFilePath() + File.separator + configRoot.getFileName() + "-" + this.fileCount.incrementAndGet() + ".csv"
                            ),
                            "utf-8"
                    )
            );
        }

    }

}
