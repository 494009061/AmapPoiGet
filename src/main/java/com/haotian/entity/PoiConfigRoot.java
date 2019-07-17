package com.haotian.entity;

import java.util.List;

/**
 * 项目根配置
 *
 * @author: Mr.Zhang
 * @email zhangpeng@hiynn.com
 * @date: 2019/7/17 17:17
 */
public class PoiConfigRoot {


    @Override
    public String toString() {
        return "PoiConfigRoot{" +
                "apiUrl='" + apiUrl + '\'' +
                ", key='" + key + '\'' +
                ", outFilePath='" + outFilePath + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileMaxRows='" + fileMaxRows + '\'' +
                ", offset='" + offset + '\'' +
                ", configs=" + configs +
                '}';
    }

    private String apiUrl;

    /**
     * 请求服务权限标识
     */
    private String key;
    /**
     * 文件输出目录
     */
    private String outFilePath;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件最大输出行数超过 以.1递增开始 重新生成
     */
    private int fileMaxRows;

    /**
     * 每页记录数据
     * <p>
     * 强烈建议不超过25，若超过25可能造成访问报错(与网络因素有关)
     */
    private int offset;

    /**
     * poi搜索参数
     */
    private List<PoiConfig> configs;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOutFilePath() {
        return outFilePath;
    }

    public void setOutFilePath(String outFilePath) {
        this.outFilePath = outFilePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileMaxRows() {
        return fileMaxRows;
    }

    public void setFileMaxRows(int fileMaxRows) {
        this.fileMaxRows = fileMaxRows;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public List<PoiConfig> getConfigs() {
        return configs;
    }

    public void setConfigs(List<PoiConfig> configs) {
        this.configs = configs;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }
};
