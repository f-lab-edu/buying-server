package org.example.buyingserver.post.dto;

import java.util.List;

public record PostCreateRequestDto(
        String title,
        String content,
        Integer price,
        Integer quantity,
        List<String> images
) {}