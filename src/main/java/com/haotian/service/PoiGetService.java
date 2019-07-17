package com.haotian.service;

import com.alibaba.fastjson.JSON;
import com.haotian.entity.PoiConfigRoot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.*;
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
    private AtomicInteger sumCount = new AtomicInteger(0);
    private AtomicInteger fileCount = new AtomicInteger(0);

    public PoiGetService(){
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

            log.info(stringBuilder.toString());

            PoiConfigRoot poiConfigRoot = JSON.parseObject(stringBuilder.toString(), PoiConfigRoot.class);

            // 创建输出文件目录
            File filePath = new File(poiConfigRoot.getOutFilePath());

            if(!filePath.exists()){
                filePath.mkdirs();
            }

            // 创建输出文件
            final File targetFile = new File(poiConfigRoot.getOutFilePath() + File.separator + poiConfigRoot.getFileName()+"-"+fileCount.get()+".csv");

            // 输出流
            FileOutputStream fos  =new FileOutputStream(targetFile);


            // 此处实现循环请求api
                     // 并循环获取每一条数据
            // 判断数据行数是否等于上限 如果等于 则修改请求参数查询下一页

            // 如果小于或者0 就查询下一个条件


            // 如果达到文件行数上限
            if(sumCount.get()%poiConfigRoot.getFileMaxRows()==0){
                fos.close();
                // 新增一个文件
                fos =new FileOutputStream(new File(poiConfigRoot.getOutFilePath() + File.separator + poiConfigRoot.getFileName()+"-"+  fileCount.incrementAndGet()+".csv"));
            }





        }

    }
}
