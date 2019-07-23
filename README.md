# AmapPoiGet 高德POI搜索实现 关键字搜索 城市代码搜索 分类搜索
程序下载地址：
链接：https://share.weiyun.com/5hgigg2

spring boot jar 项目 用于获取 高德 poi 数据

```
根据自己需求查询相关编码表格 配置poiConfig.json后启动程序即可

poiConfig.json 属性说明
    apiUrl 为接口地址
    key 高德开发者创建应用时 提供的key
    outFilePath 输出文件存储目录
    fileName 文件存储名称不包含后缀 ，后缀固定格式为.csv 使用excel 导入后 转码 文本为utf-8 直接使用excel打开会出现乱码
    fileMaxRows 每一个文本存储的最大行数 达到指定行数 自动增加文件
    offset 每一次查询返回多少条 可能返回多页 不要超过50 因为封顶既是50 不会更多
configs JSON集合
    JSON集合中的JSON对象 属性说明：
        types 支持汉字和编码两种  参照编码表 amap_poicode.xlsx
        city 为城市编码 参照编码表 AMap_adcode_citycode.xlsx
        keywords 选填 可以配合types 精确查询

使用到城市编码：

    门头沟区	110109
    房山区	110111
    通州区	110112
    顺义区	110113
    昌平区	110114
    大兴区	110115

类别编码；
    文化科教类 140000
    医疗保健类 090000
    因为社会福利类别在api中无法体现 故筛选关键字 和大类别 来精确筛选社会福利收养疗养等社会福利设施
        关键字：敬老|养老|收容|收养|疗养
        类别关键字：育休闲服务|度假疗养场所|疗养院

```
