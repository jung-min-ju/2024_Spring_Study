package spring.sw.week4.common.config.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${cors.allowed.ngrok.origin}")
    private String ngrokOrigin;

    @Value("${cors.allowed.client.origin}")
    private String clientOrigin;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/auth/**")
                .allowedOrigins("*")
                .allowedMethods("POST");

        registry
                .addMapping("/jwt/**")
                .allowedOrigins("*")
                .allowedMethods("POST");
        registry
            .addMapping("/ws/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST");
        registry
                .addMapping("/swagger-ui.html")
                .allowedOrigins("*")
                .allowedMethods("POST");
        registry
                .addMapping("/**")
                .allowedOrigins(ngrokOrigin)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);

        registry
                .addMapping("/**")
                .allowedOrigins(clientOrigin)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

}
