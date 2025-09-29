package dev.dead.spring6restmvc.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class CustomerDTO {
    private UUID id;
    private String customerName;
    private Integer version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
