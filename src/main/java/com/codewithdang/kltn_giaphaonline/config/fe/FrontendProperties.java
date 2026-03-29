package com.codewithdang.kltn_giaphaonline.config.fe;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "app.frontend")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FrontendProperties {
    String baseUrl;
    String verifyEndpoint;
}
