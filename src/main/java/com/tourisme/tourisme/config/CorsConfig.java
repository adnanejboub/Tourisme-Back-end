// package com.tourisme.tourisme.config;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration
// public class CorsConfig implements WebMvcConfigurer {

//     @Override
//     public void addCorsMappings(CorsRegistry registry) {
//         registry.addMapping("/**")
//                 .allowedOriginPatterns(
//                     "http://localhost:*",
//                     "http://127.0.0.1:*", 
//                     "http://10.0.2.2:*",
//                     "http://192.168.*:*",
//                     "https://localhost:*",
//                     "https://127.0.0.1:*"
//                 )
//                 .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
//                 .allowedHeaders("Origin", "Content-Type", "Accept", "Authorization", "X-Requested-With", "Access-Control-Request-Method", "Access-Control-Request-Headers")
//                 .exposedHeaders("Authorization", "Content-Type")
//                 .allowCredentials(true)
//                 .maxAge(3600);
//     }
// } 