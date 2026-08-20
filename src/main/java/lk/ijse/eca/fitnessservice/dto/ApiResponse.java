package lk.ijse.eca.fitnessservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private Integer status;
    private String path;
    private String timestamp;
    private DataWrapper<T> data;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DataWrapper<T> {
        private String message;
        private T content;
        private String error;
    }
}
