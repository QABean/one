package cn.migu.newportal.selection.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.migu.newportal.cache.util.PropertiesConfig;
import cn.migu.newportal.util.string.StringTools;

/**
 * http协议调用接口
 * 
 * @author wulinfeng
 * @version C10 2017年5月19日
 * @since SDP V300R003C10
 */
public class HttpClientUtil
{

    private static Logger logger = LogManager.getLogger(HttpClientUtil.class);
    
    public static String getDataFromIB(String url)
    {
        
        String responseContent = null;
        CloseableHttpResponse response = null;
        
        // 设置超时
        RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(15000)
            .setConnectTimeout(15000)
            .setConnectionRequestTimeout(15000)
            .build();
        
        // 调用IB接口
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try
        {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setConfig(requestConfig);
            response = httpClient.execute(httpGet);
            
            // 请求响应成功则获取响应
            if (null != response && response.getStatusLine().getStatusCode() == 200)
            {
                HttpEntity entity = response.getEntity();
                responseContent = EntityUtils.toString(entity, "UTF-8");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                // 关闭连接,释放资源
                if (response != null)
                {
                    response.close();
                }
                if (httpClient != null)
                {
                    httpClient.close();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        
        return responseContent;
    }
    
    /**
     * 调用bi接口，获取咪咕猜相关json
     * 
     *
     * @author gongdaxin
     * @param prtype
     * @param msisdn
     * @param pageSize
     * @param sheetCount
     * @param clientVersion
     * @return
     * @throws URISyntaxException
     */
    public static String getDataFromBI(String prtype, String msisdn, String pageSize, String sheetCount,
        String clientVersion)
        throws URISyntaxException
    {
        URI uri = null;
        if (StringTools.isEq("63", prtype))
        {
            uri = new URIBuilder().setScheme(PropertiesConfig.getProperty("bi_getData_scheme"))
                .setHost(PropertiesConfig.getProperty("bi_getData_host"))
                .setPort(PropertiesConfig.getInt("bi_getData_port", 8088))
                .setPath(PropertiesConfig.getProperty("bi_getData_path"))
                .setParameter("prtype", prtype)
                .setParameter("token", PropertiesConfig.getProperty("param_value_token"))
                .setParameter("msisdn", msisdn)
                .setParameter("random", PropertiesConfig.getProperty("param_value_random"))
                .setParameter("page_id", PropertiesConfig.getProperty("page_id"))
                .setParameter("page_booknum", pageSize)
                .setParameter("edition_id", PropertiesConfig.getProperty("edition_id_63"))
                .setParameter("count", sheetCount)
                .setParameter("jsonpcallback", PropertiesConfig.getProperty("jsonpcallback"))
                .build();
        }
        else if (StringTools.isEq("65", prtype))
        {
            uri = new URIBuilder().setScheme(PropertiesConfig.getProperty("bi_getData_scheme"))
                .setHost(PropertiesConfig.getProperty("bi_getData_host"))
                .setPort(PropertiesConfig.getInt("bi_getData_port", 8088))
                .setPath(PropertiesConfig.getProperty("bi_getData_path"))
                .setParameter("prtype", prtype)
                .setParameter("token", PropertiesConfig.getProperty("param_value_token"))
                .setParameter("msisdn", msisdn)
                .setParameter("random", PropertiesConfig.getProperty("param_value_random"))
                .setParameter("page_id", PropertiesConfig.getProperty("page_id"))
                .setParameter("page_booknum", pageSize)
                .setParameter("edition_id", PropertiesConfig.getProperty("edition_id_65"))
                .setParameter("count", sheetCount)
                .setParameter("page_sheetnum", PropertiesConfig.getProperty("page_sheetnum"))
                .setParameter("ctype", clientVersion)
                .setParameter("tertype", PropertiesConfig.getProperty("tertype"))
                .setParameter("jsonpcallback", "jsonp4")
                .build();
        }
        else
        {
            uri = new URIBuilder().setScheme(PropertiesConfig.getProperty("bi_getData_scheme"))
                .setHost(PropertiesConfig.getProperty("bi_getData_host"))
                .setPort(PropertiesConfig.getInt("bi_getData_port", 8088))
                .setPath(PropertiesConfig.getProperty("bi_getData_path"))
                .setParameter("prtype", prtype)
                .setParameter("token", PropertiesConfig.getProperty("param_value_token"))
                .setParameter("msisdn", msisdn)
                .setParameter("random", PropertiesConfig.getProperty("param_value_random"))
                .setParameter("page_id", PropertiesConfig.getProperty("page_id"))
                .setParameter("page_booknum", pageSize)
                .setParameter("count", sheetCount)
                .setParameter("jsonpcallback", PropertiesConfig.getProperty("jsonpcallback"))
                .build();
        }
        
        if (logger.isDebugEnabled())
        {
            logger.debug("HttpClientUtil.getDataFromBI();  url=" + uri.toString());
        }
        
        String result = HttpClientUtil.getDataFromIB(uri.toString());
        
        if (logger.isDebugEnabled())
        {
            logger.debug("HttpClientUtil.getDataFromBI();result=" + result);
        }
        
        String resultJosn = "";
        if (!StringTools.isEmpty(result))
        {
            // 取出最外层小括号
            resultJosn = result.substring(result.indexOf("(") + 1, result.lastIndexOf(")"));
        }
        return resultJosn;
    }
}
