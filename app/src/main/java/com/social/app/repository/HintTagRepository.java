package com.social.app.repository;

import com.social.app.model.Category;
import com.social.app.model.HintTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface HintTagRepository extends JpaRepository<HintTag, Integer> {
    public ArrayList<HintTag> findByCategory(Category category);
}
