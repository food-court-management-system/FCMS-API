package xiaolin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import xiaolin.config.interceptor.CashierInterceptor;
import xiaolin.config.interceptor.CustomerInterceptor;
import xiaolin.config.interceptor.FoodStallInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {

    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new CashierInterceptor())
                .addPathPatterns("/cashier");

        registry.addInterceptor(new CustomerInterceptor())
                .addPathPatterns("/customer");

        registry.addInterceptor(new FoodStallInterceptor())
                .addPathPatterns("/stall");
    }
}
