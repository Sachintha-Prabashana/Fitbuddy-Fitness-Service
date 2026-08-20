package lk.ijse.eca.fitnessservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.context.annotation.Primary;

@Configuration
public class RestClientConfig {

    @Bean
    @Primary
    public RestClient.Builder defaultRestClientBuilder(HeaderPropagationInterceptor interceptor) {
        return RestClient.builder().requestInterceptor(interceptor);
    }

    @Bean
    @LoadBalanced
    public RestClient.Builder loadBalancedRestClientBuilder(HeaderPropagationInterceptor interceptor) {
        return RestClient.builder().requestInterceptor(interceptor);
    }
}
