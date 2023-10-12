package com.social.app.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private int categoryId;
    private String categoryName;
    private String description;
}
