package com.social.app.repository;

import com.social.app.model.Category;
import com.social.app.model.Document;
import com.social.app.model.Groups;
import com.social.app.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

public interface GroupRepository  extends JpaRepository<Groups, Long> {
    Groups findByGroupId(long groupId);
    Groups findByGroupName(String groupName);
    Groups save(Groups groups);

    int countByCategory(Category category);
    ArrayList<Groups> findAll();

    @Override
    void deleteById(Long aLong);

    ArrayList<Groups> findAllByCategoryCategoryNameIgnoreCase(String categoryName);
    ArrayList<Groups> findAllByTagsContainsIgnoreCase(String tag);


    ArrayList<Groups> findByCategory(Category category);
    @Query(value = "SELECT * FROM groups_ WHERE MATCH (Groups_Name,tags) AGAINST (?1 WITH QUERY EXPANSION)", nativeQuery = true)
    ArrayList<Groups> fullTextSearch(String keyword);
}
