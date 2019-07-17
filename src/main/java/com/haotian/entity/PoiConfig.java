package com.haotian.entity;

/**
 * 搜索条件配置 在根配置中 可能出现多个
 *
 * @author: Mr.Zhang
 * @email zhangpeng@hiynn.com
 * @date: 2019/7/17 17:18
 */
public class PoiConfig {
    /**
     * 规则： 多个关键字用“|”分割
     * <p>
     * 若不指定city，并且搜索的为泛词（例如“美食”）的情况下，返回的内容为城市列表以及此城市内有多少结果符合要求。
     * <p>
     * 必填
     * <p>
     * (keywords和types两者至少必选其一)
     */
    private String keyWords;
    /**
     * 可选值：分类代码 或 汉字（若用汉字，请严格按照附件之中的汉字填写）
     * <p>
     * 分类代码由六位数字组成，一共分为三个部分，前两个数字代表大类；中间两个数字代表中类；最后两个数字代表小类。
     * <p>
     * 若指定了某个大类，则所属的中类、小类都会被显示。
     * <p>
     * 例如：010000为汽车服务（大类）
     * <p>
     * 010100为加油站（中类）
     * <p>
     * 010101为中国石化（小类）
     * <p>
     * 010900为汽车租赁（中类）
     * <p>
     * 010901为汽车租赁还车（小类）
     * <p>
     * 当指定010000，则010100等中类、010101等小类都会被包含，当指定010900，则010901等小类都会被包含。 若不指定city，返回的内容为城市列表以及此城市内有多少结果符合要求。
     * <p>
     * 当您的keywords和types都是空时，默认指定types为120000（商务住宅）&150000（交通设施服务）
     * <p>
     * 必填
     * <p>
     * (keywords和types两者至少必选其一)
     */
    private String types;
    /**
     * 查询城市  可选值：城市中文、中文全拼、citycode、adcode
     * <p>
     * 如：北京/beijing/010/110000
     * <p>
     * 填入此参数后，会尽量优先返回此城市数据，但是不一定仅局限此城市结果，若仅需要某个城市数据请调用citylimit参数。
     * <p>
     * 如：在深圳市搜天安门，返回北京天安门结果。
     * <p>
     * 可选
     * <p>
     * 无（全国范围内搜索）
     */
    private String city;
    /**
     * 仅返回指定城市数据
     * <p>
     * 可选值：true/false
     */
    private String cityLimit;

    /**
     * 城市-区
     */
    private String adname;

    public String getAdname() {
        return adname;
    }

    public void setAdname(String adname) {
        this.adname = adname;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityLimit() {
        return cityLimit;
    }

    public void setCityLimit(String cityLimit) {
        this.cityLimit = cityLimit;
    }
}
