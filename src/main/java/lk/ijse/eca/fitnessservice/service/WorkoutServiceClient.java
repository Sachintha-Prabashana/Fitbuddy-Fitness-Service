package lk.ijse.eca.fitnessservice.service;

import lk.ijse.eca.fitnessservice.dto.ApiResponse;
import lk.ijse.eca.fitnessservice.dto.MemberWorkoutResponseDTO;
import lk.ijse.eca.fitnessservice.exception.ResourceNotFoundException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;

@Service
public class WorkoutServiceClient {

    private final RestClient.Builder loadBalancedRestClientBuilder;

    public WorkoutServiceClient(@LoadBalanced RestClient.Builder loadBalancedRestClientBuilder) {
        this.loadBalancedRestClientBuilder = loadBalancedRestClientBuilder;
    }

    public List<MemberWorkoutResponseDTO> getAllMemberWorkouts(Long memberId) {
        RestClient restClient = loadBalancedRestClientBuilder.clone().baseUrl("http://workout-service").build();
        
        ApiResponse<List<MemberWorkoutResponseDTO>> response = restClient.get()
                .uri("/api/v1/workouts/member/{memberId}/all", memberId)
                .retrieve()
                .body(new ParameterizedTypeReference<ApiResponse<List<MemberWorkoutResponseDTO>>>() {});
                
        if (response != null && response.isSuccess() && response.getData() != null) {
            return response.getData().getContent();
        }
        
        throw new ResourceNotFoundException("Failed to fetch workouts for member ID: " + memberId);
    }
}
