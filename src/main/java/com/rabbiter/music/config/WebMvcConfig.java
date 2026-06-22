package com.rabbiter.music.config;

import com.rabbiter.music.utils.Consts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.*;

import javax.servlet.http.*;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${web.upload-path:C:/w002/music_upload/}")
    private String uploadPath;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:9172",
                        "http://127.0.0.1:9172",
                        "http://localhost:9173",
                        "http://127.0.0.1:9173"
                )
                .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 閺勭姴鐨犳稉濠佺炊閻╊喖缍嶉崚?/img/banner/**
        registry.addResourceHandler("/img/banner/**")
                .addResourceLocations("file:" + uploadPath);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 閸撳秶顏棃娆愨偓渚€銆夐棃銏犲弳閸?        registry.addViewController("/admin-ui").setViewName("forward:/admin-ui/index.html");
        registry.addViewController("/admin-ui/").setViewName("forward:/admin-ui/index.html");
        registry.addViewController("/user-ui").setViewName("forward:/user-ui/index.html");
        registry.addViewController("/user-ui/").setViewName("forward:/user-ui/index.html");
        registry.addViewController("/").setViewName("forward:/user-ui/index.html");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminLoginInterceptor())
                .addPathPatterns("/admin/**")
                .excludePathPatterns(
                        "/admin/login/status",
                        "/admin/logout",
                        "/admin/login/info",
                        "/admin/update",
                        "/admin/updateAvatar"
                );
    }

    static class AdminLoginInterceptor implements HandlerInterceptor {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                return true;
            }

            HttpSession session = request.getSession(false);
            Object adminName = (session == null) ? null : session.getAttribute(Consts.NAME);
            if (adminName != null) return true;

            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(401);
            response.getWriter().write("{\"code\":401,\"msg\":\"not logged in\"}");
            return false;
        }
    }
}

