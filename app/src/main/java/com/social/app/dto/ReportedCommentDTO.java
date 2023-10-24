package com.social.app.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.social.app.model.*;
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
public class ReportedCommentDTO {
    private long commentId;
    private List<CommentReport> reports;
    @JsonView(Views.ReportedCommentView.class)
    public long getCommentId() {
        return this.commentId;
    }
    @JsonView(Views.ReportedCommentView.class)
    public List<ReportForCommentDTO> getReports() {
        ModelMapper modelMapper = new ModelMapper();
        ArrayList<ReportForCommentDTO> res = new ArrayList<>();
        for (CommentReport report: reports) {
            res.add(modelMapper.map(report, ReportForCommentDTO.class));
        }
        return res;
    }
}
