package com.withccm.oauthapi.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.withccm.oauthapi.security.AppProperties;

@EnableConfigurationProperties({
	AppProperties.class
})
@Configuration
public class PropertiesConfig {
}
