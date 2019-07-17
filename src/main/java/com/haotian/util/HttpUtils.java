package com.haotian.util;

import com.alibaba.fastjson.JSONObject;
import com.haotian.entity.HttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpMessage;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.Map.Entry;

/**
 * @version 1.0.0.4
 * @ClassName: HttpUtils
 * @author: 张朋
 * @date: 2016年11月2日 下午3:11:49
 */
public final class HttpUtils {

    private static final char QP_SEP_S = ';';
    public final static String UTF8 = "utf-8";
    public final static String GBK = "GBK";
    public final static String UTF16BE = "UTF-16BE";
    public final static String UTF16LE = "UTF-16LE";
    public final static String ASCII = "ASCII";
    public final static String TEXT_XML = "text/xml";
    public final static String TEXT_PLAIN = "text/plain";
    public final static String TEXT_HTML = "text/html";
    public final static String APPLICATION_XML = "application/xml";
    public final static String APPLICATION_JSON = "application/json";
    public final static String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
    public final static String APPLICATION_BINARY = "binary/octet-stream";
    private static CloseableHttpClient httpClient = null;

    public final static Map<String, String> DefaultHeaders = new HashMap<String, String>() {

        private static final long serialVersionUID = -4221907242678810638L;

        {
            put("accept", "*/*");
            put("Accept-Language", "zh-CN,zh;q=0.8");
            put("Accept-Charset", "utf-8;q=0.8");
            put("connection", "Keep-Alive");
            put("Keep-Alive", "timeout=30");//设置较小的长连接超时时间、 释放更多的可用连接
            //put("connection", "close");
            put("User-Agent", "For SpringBootWeb HttpUtils Java 1.8(@Author Email : zp.haotian@foxmail.com");
        }
    };

    private HttpUtils() {

    }

    /*
     * 通用连接配置
     */
    private static RequestConfig defaultRequestConfig;

    private static final int socketTimeout = 200000;
    private static final int connectionRequestTimeout = 200000;
    private static final int connectTimeout = 200000;

    /**
     * 初始化
     *
     * @throws Exception
     */
    static {
        defaultRequestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout).build();
        httpClient = HttpClientBuilder.create().setDefaultRequestConfig(defaultRequestConfig).build();
    }

    /**
     * 一个简单的GET请求
     *
     * @param uri
     * @param params
     * @param encoding
     * @return
     * @throws Exception
     */
    public final static HttpResponse get(String uri, Map<String, String> params, String encoding) throws IOException, URISyntaxException {
        return get(uri, params, DefaultHeaders, encoding);
    }

    /**
     * 可设置请求头参数的GET请求
     *
     * @param uri
     * @param params
     * @param headers
     * @param encoding
     * @return
     * @throws Exception
     */
    public final static HttpResponse get(String uri, Map<String, String> params, Map<String, String> headers,
                                         String encoding) throws IOException, URISyntaxException {

        HttpGet get = null;

        int code = 0;

        if (null == encoding) {
            encoding = UTF8;
        }

        HttpResponse resp = new HttpResponse();

        try {

            URIBuilder uriBuilder = new URIBuilder(uri);

            if (null != params && params.size() > 0) {
                // 拼接参数
                for (Iterator<Entry<String, String>> ite = params.entrySet().iterator(); ite.hasNext(); ) {
                    Entry<String, String> entry = ite.next();
                    uriBuilder.addParameter(entry.getKey(), entry.getValue());
                }
            }

            get = new HttpGet(uriBuilder.build());

            setHeaders(get, headers);

            try (CloseableHttpResponse httpResponse = httpClient.execute(get);) {

                if ((code = httpResponse.getStatusLine().getStatusCode()) == 200) {
                    HttpEntity httpEntity = httpResponse.getEntity();
                    resp.setMessage(EntityUtils.toString(httpEntity, encoding));
                }
                resp.setCode(code);
            }
        } finally {
            if (null != get) {
                get.abort();
            }
        }
        return resp;
    }

    /**
     * 一个简单的POST请求
     *
     * @param uri
     * @param params
     * @param encoding
     * @return
     * @throws Exception
     */
    public final static HttpResponse post(String uri, Map<String, String> params, String encoding) throws IOException {
        return post(uri, params, DefaultHeaders, encoding);
    }

    /**
     * 可设置请求头参数的 POST请求
     *
     * @param uri
     * @param params
     * @param headers
     * @param encoding
     * @return
     * @throws Exception
     */
    public final static HttpResponse post(String uri, Map<String, String> params, Map<String, String> headers,
                                          String encoding) throws IOException {

        HttpPost post = null;
        int code = 0;

        if (null == encoding) {
            encoding = UTF8;
        }

        HttpResponse resp = new HttpResponse();

        try {

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();

            if (null != params && params.size() > 0) {
                // 拼接参数
                for (Iterator<Entry<String, String>> ite = params.entrySet().iterator(); ite.hasNext(); ) {
                    Entry<String, String> entry = ite.next();
                    String name = entry.getKey();
                    String value = entry.getValue();
                    nvps.add(new BasicNameValuePair(name, value));
                }
            }

            StringEntity entity = new UrlEncodedFormEntity(nvps, encoding);
            entity.setContentEncoding(encoding);// 解决中文乱码问题
            entity.setContentType(APPLICATION_X_WWW_FORM_URLENCODED);

            post = new HttpPost(uri);
            post.setEntity(entity);

            setHeaders(post, headers);

            try (CloseableHttpResponse httpResponse = httpClient.execute(post);) {
                if ((code = httpResponse.getStatusLine().getStatusCode()) == 200) {
                    HttpEntity httpEntity = httpResponse.getEntity();
                    resp.setMessage(EntityUtils.toString(httpEntity, encoding));
                }
                resp.setCode(code);
            }

        } finally {
            if (null != post) {
                post.abort();
            }
        }
        return resp;
    }

    /**
     * 请求类型为 multipart/form-data 的 文件上传请求
     * <p>
     * POST Content-Type=multipart/form-data
     *
     * @param uri      请求地址
     * @param params   参数集合
     * @param headers  请求头参数集合
     * @param files    文件集合
     * @param encoding 编码
     * @return HttpResponse
     * @throws Exception
     */
    public final static HttpResponse postMultipartFormData(String uri, Map<String, String> params,
                                                           Map<String, String> headers, List<File> files, String encoding) throws IOException {

        HttpPost post = null;
        int code = 0;
        if (null == encoding) {
            encoding = UTF8;
        }

        HttpResponse resp = new HttpResponse();

        try {

            // 初始化MultipartEntity构造器
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();

            builder.setContentType(ContentType.MULTIPART_FORM_DATA.withCharset(encoding));

            // 构建文本类型参数
            if (null != params && params.size() > 0) {
                for (Iterator<Entry<String, String>> ite = params.entrySet().iterator(); ite.hasNext(); ) {
                    Entry<String, String> param = ite.next();
                    builder.addTextBody(param.getKey(), param.getValue(), ContentType.TEXT_PLAIN.withCharset(encoding));
                }
            }
            // 构建文件参数
            if (null != files && files.size() > 0) {
                for (File file : files) {
                    FileBody fileBody = new FileBody(file);
                    builder.addPart(file.getName(), fileBody);
                }
            }

            post = new HttpPost(uri);

            //设置头信息
            setHeaders(post, headers);

            //构建为entity实体
            HttpEntity entity = builder.build();
            post.setEntity(entity);

            try (CloseableHttpResponse httpResponse = httpClient.execute(post);) {
                if ((code = httpResponse.getStatusLine().getStatusCode()) == 200) {
                    HttpEntity httpEntity = httpResponse.getEntity();
                    resp.setMessage(EntityUtils.toString(httpEntity, encoding));
                }
                resp.setCode(code);
            }

        } finally {
            if (null != post) {
                post.abort();
            }
        }
        return resp;
    }

    /**
     * 一个简单的JSON请求
     *
     * @param uri
     * @param json
     * @param encoding
     * @return
     * @throws Exception
     */
    public final static HttpResponse json(String uri, JSONObject json, String encoding) throws IOException {
        return postStringEntity(uri, json.toJSONString(), encoding, HttpUtils.APPLICATION_JSON);
    }

    /**
     * 可设置请求头参数的JSON请求
     *
     * @param uri
     * @param headers
     * @param encoding
     * @return
     * @throws Exception
     */
    public final static HttpResponse json(String uri, JSONObject json, Map<String, String> headers, String encoding)
            throws Exception {
        return postStringEntity(uri, json.toJSONString(), headers, encoding, HttpUtils.APPLICATION_JSON);
    }

    /**
     * 一个简单的POST自定义请求 ,可定义内容和ContentType
     *
     * @param uri
     * @param data
     * @param encoding
     * @param contentType
     * @return HttpResponse
     * @throws Exception
     */
    public final static HttpResponse postStringEntity(String uri, String data, String encoding, String contentType)
            throws IOException {
        return postStringEntity(uri, data, DefaultHeaders, encoding, contentType);

    }

    /**
     * 可设置请求头参数的POST自定义请求,可定义body内容和ContentType
     *
     * @param uri
     * @param data
     * @param headers
     * @param encoding
     * @param contentType
     * @return
     * @throws Exception
     */
    public final static HttpResponse postStringEntity(String uri, String data, Map<String, String> headers,
                                                      String encoding, String contentType) throws IOException {
        HttpPost post = null;
        int code = 0;

        if (null == encoding) {
            encoding = UTF8;
        }
        HttpResponse resp = new HttpResponse();
        try {

            StringEntity entity = new StringEntity(data, encoding);// 解决中文乱码问题
            entity.setContentType(contentType);

            post = new HttpPost(uri);
            post.setEntity(entity);
            setHeaders(post, headers);

            try (CloseableHttpResponse httpResponse = httpClient.execute(post);) {
                if ((code = httpResponse.getStatusLine().getStatusCode()) == 200) {
                    HttpEntity httpEntity = httpResponse.getEntity();
                    resp.setMessage(EntityUtils.toString(httpEntity, encoding));
                }
                resp.setCode(code);
            }

        } finally {
            if (null != post) {
                post.abort();
            }
        }
        return resp;
    }


    /**
     * 可设置请求头参数的POST文件流请求
     *
     * @param uri
     * @param file
     * @param headers
     * @param encoding
     * @return
     * @throws Exception
     */
    public final static HttpResponse postFileEntity(String uri, File file, Map<String, String> headers,
                                                    String encoding) throws IOException {

        HttpPost post = null;
        int code = 0;

        if (null == encoding) {
            encoding = UTF8;
        }
        HttpResponse resp = new HttpResponse();
        try {

            InputStreamEntity entity = new InputStreamEntity(new FileInputStream(file));
            entity.setContentType(APPLICATION_BINARY);

            post = new HttpPost(uri);
            post.setEntity(entity);
            setHeaders(post, headers);

            try (CloseableHttpResponse httpResponse = httpClient.execute(post);) {
                if ((code = httpResponse.getStatusLine().getStatusCode()) == 200) {
                    HttpEntity httpEntity = httpResponse.getEntity();
                    resp.setMessage(EntityUtils.toString(httpEntity, encoding));
                }
                resp.setCode(code);
            }

        } finally {
            if (null != post) {
                post.abort();
            }
        }
        return resp;
    }

    /**
     * 跟具URL获取Byte[] ,可用于小文件下载
     *
     * @param uri
     * @return @throws Exception @throws
     */
    public final static HttpResponse getBytes(String uri) throws IOException {
        HttpGet get = null;

        int code = 0;

        HttpResponse resp = new HttpResponse();

        try {

            get = new HttpGet(uri);

            try (CloseableHttpResponse httpResponse = httpClient.execute(get);) {

                if ((code = httpResponse.getStatusLine().getStatusCode()) == 200) {
                    HttpEntity httpEntity = httpResponse.getEntity();
                    resp.setMessage(EntityUtils.toByteArray(httpEntity));
                }
                resp.setCode(code);

            }

        } finally {
            if (null != get) {
                get.abort();
            }
        }
        return resp;
    }

    /**
     * 设置请求头
     *
     * @param message HttpPost OR HttpGet 都属于HttpMessage 实现类 子类
     * @param headers
     */
    private static void setHeaders(HttpMessage message, Map<String, String> headers) {

        if (headers != null && headers.size() > 0) {
            for (Iterator<Entry<String, String>> iter = headers.entrySet().iterator(); iter.hasNext(); ) {
                Entry<String, String> next = iter.next();
                message.setHeader(next.getKey(), next.getValue());
            }
        } else {
            headers = DefaultHeaders;
            setHeaders(message, headers);
        }
    }

}
