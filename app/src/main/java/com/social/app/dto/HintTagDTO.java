package com.social.app.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HintTagDTO {
    private int tagId;
    private String tagName;
}
