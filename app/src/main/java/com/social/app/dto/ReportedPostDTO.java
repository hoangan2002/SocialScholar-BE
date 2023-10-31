package com.social.app.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.social.app.model.Comment;
import com.social.app.model.Groups;
import com.social.app.model.PostReport;
import com.social.app.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportedPostDTO {
    private long postId;
    private String titles;
    private List<PostReport> reports;

    @JsonView(Views.ReportedPostView.class)
    public long getPostId() {
        return this.postId;
    }
    @JsonView(Views.ReportedPostView.class)
    public String getTitles(){
        return this.titles;
    }
    @JsonView(Views.ReportedPostView.class)
    public List<ReportForPostDTO> getReports() {
        ModelMapper modelMapper = new ModelMapper();
        ArrayList<ReportForPostDTO> res = new ArrayList<>();
        for (PostReport postReport: reports) {
            res.add(modelMapper.map(postReport, ReportForPostDTO.class));
        }
        return res;
    }
}
