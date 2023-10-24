package com.social.app.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.social.app.service.GroupServices;
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
    private int groupCount;
    @JsonView(Views.DocumentView.class)
    public int getCategoryId(){ return categoryId; }
    @JsonView(Views.DocumentView.class)
    public String getCategoryName(){ return categoryName; }
    @JsonView(Views.DocumentView.class)
    public String getDescription(){ return description; }
    @JsonView(Views.DocumentView.class)
    public int getGroupCount(){
        GroupServices groupServices = new GroupServices();
        return groupServices.calculateGroupCount(this.categoryId);
    }
}
