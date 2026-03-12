package com.sweetbite.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 微信工具类
 */
@Slf4j
@Component
public class WechatUtil {
    
    @Value("${wechat.appid:}")
    private String appid;
    
    @Value("${wechat.secret:}")
    private String secret;
    
    private static final String JSCODE2SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session";
    
    /**
     * 通过 code 获取 openid 和 session_key
     * 
     * @param code 微信授权 code
     * @return 包含 openid 和 session_key 的 JSON 对象
     */
    public JSONObject getSessionByCode(String code) {
        try {
            String url = String.format("%s?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                    JSCODE2SESSION_URL, appid, secret, code);
            
            log.info("调用微信 API: {}", url);
            
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                log.error("微信 API 请求失败: {}", responseCode);
                return null;
            }
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            
            log.info("微信 API 返回: {}", response.toString());
            
            JSONObject result = JSONObject.parseObject(response.toString());
            
            // 检查是否有错误
            if (result.containsKey("errcode")) {
                log.error("微信 API 返回错误: {}", result.getString("errmsg"));
                return null;
            }
            
            return result;
        } catch (Exception e) {
            log.error("获取微信 session 失败", e);
            return null;
        }
    }
    
    /**
     * 验证 code 是否有效
     */
    public boolean validateCode(String code) {
        JSONObject result = getSessionByCode(code);
        return result != null && result.containsKey("openid");
    }
    
    /**
     * 获取 openid
     */
    public String getOpenid(String code) {
        JSONObject result = getSessionByCode(code);
        if (result != null) {
            return result.getString("openid");
        }
        return null;
    }
}
