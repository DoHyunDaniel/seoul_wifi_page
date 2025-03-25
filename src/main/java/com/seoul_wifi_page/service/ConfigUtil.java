package com.seoul_wifi_page.service;

import java.io.InputStream;
import java.util.Properties;

/**
 * 설정 파일(config.properties)에서 API Key를 불러오는 유틸리티 클래스입니다.
 */
public class ConfigUtil {

    /**
     * config.properties 파일에서 api.key 값을 읽어 반환합니다.
     *
     * @return API 키 문자열, 없거나 오류 발생 시 null 반환
     */
    public static String getApiKey() {
        Properties prop = new Properties();

        try (InputStream input = ConfigUtil.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("Sorry, unable to find config.properties");
            }

            prop.load(input);
            return prop.getProperty("api.key");

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
