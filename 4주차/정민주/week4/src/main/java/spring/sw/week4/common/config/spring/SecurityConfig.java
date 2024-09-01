package spring.sw.week4.common.config.spring;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import spring.sw.week4.common.springSecurity.JwtAuthEntryPoint;
import spring.sw.week4.common.springSecurity.filter.JwtAuthenticationFilter;
import spring.sw.week4.common.springSecurity.filter.JwtExceptionFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtExceptionFilter jwtExceptionFilter;


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests->
                        requests
                                .requestMatchers("/static/**").permitAll()
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers("/jwt/**").permitAll()
                                .requestMatchers("/ws/**").permitAll()
                                .requestMatchers (
                                        "/swagger-resources/**",
                                        "/swagger-ui/**",
                                        "/v3/api-docs",
                                        "/api-docs/**",
                                        "/v3/api-docs/swagger-config",
                                        "/favicon.ico",
                                        "/public/**"
                                ).permitAll()
                                .anyRequest().authenticated()
                )
                .exceptionHandling(config->
                        config.authenticationEntryPoint(this.jwtAuthEntryPoint) //인증되지 않은 사용자 접근시 에러 발생
                )
                .sessionManagement(it->
                        it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션 사용 안함
                )
                .addFilterBefore(this.jwtExceptionFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(this.jwtAuthenticationFilter, this.jwtExceptionFilter.getClass())
                .build();
    }
}

//SWAGGER 주소 : http://localhost:8080/swagger-ui/index.html