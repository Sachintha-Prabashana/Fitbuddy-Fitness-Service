package lk.ijse.eca.fitnessservice.service;

import lk.ijse.eca.fitnessservice.dto.ApiResponse;
import lk.ijse.eca.fitnessservice.dto.FullProfileResponseDTO;
import lk.ijse.eca.fitnessservice.exception.ResourceNotFoundException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;

@Service
public class MemberServiceClient {

    private final RestClient.Builder loadBalancedRestClientBuilder;

    public MemberServiceClient(@LoadBalanced RestClient.Builder loadBalancedRestClientBuilder) {
        this.loadBalancedRestClientBuilder = loadBalancedRestClientBuilder;
    }

    public FullProfileResponseDTO getMemberProfile(Long userId) {
        RestClient restClient = loadBalancedRestClientBuilder.clone().baseUrl("http://member-service").build();
        
        ApiResponse<FullProfileResponseDTO> response = restClient.get()
                .uri("/api/v1/members/{userId}/full-profile", userId)
                .retrieve()
                .body(new ParameterizedTypeReference<ApiResponse<FullProfileResponseDTO>>() {});
                
        if (response != null && response.isSuccess() && response.getData() != null) {
            return response.getData().getContent();
        }
        
        throw new ResourceNotFoundException("Failed to fetch member profile for ID: " + userId);
    }
}
