package com.springboot.rest.api.server.utils;


import com.springboot.rest.api.server.config.ApplicationContextConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

@Slf4j
public class PropertyUtil {

    /**
     * Gets the value of a property stored in the application.properties file.
     * If a property name is not found, just return the property name.
     * @param propertyName
     * @return property value
     */
    public static String getProperty(String propertyName) {

        ApplicationContext applicationContext = ApplicationContextConfig.getApplicationContext();

        if (applicationContext != null && applicationContext.getEnvironment().getProperty(propertyName) != null) {
            return applicationContext.getEnvironment().getProperty(propertyName);
        } else if (applicationContext == null) {
            log.debug("Application context was not loaded");
        } else if (applicationContext.getEnvironment().getProperty(propertyName) == null) {
            log.debug(propertyName + " property was not found");
        }
        return propertyName;
    }
}
