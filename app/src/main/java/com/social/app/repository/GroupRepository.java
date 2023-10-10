package com.social.app.repository;

import com.social.app.model.Category;
import com.social.app.model.Groups;
import com.social.app.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface GroupRepository  extends JpaRepository<Groups, Long> {
    Groups findByGroupId(long groupId);
    Groups findByGroupName(String groupName);
    Groups save(Groups groups);

    ArrayList<Groups> findAll();

    @Override
    void deleteById(Long aLong);
    ArrayList<Groups> findByCategory(Category category);
}
