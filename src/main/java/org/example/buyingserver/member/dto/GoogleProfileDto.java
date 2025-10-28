package org.example.buyingserver.member.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GoogleProfileDto(
        String email,
        String name,
        String picture,
        String sub
) {
}
