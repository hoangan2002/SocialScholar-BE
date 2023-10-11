package com.social.app.service;

import com.social.app.model.Category;
import com.social.app.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public ArrayList<Category> getAll(){
        return (ArrayList<Category>) categoryRepository.findAll(Sort.by(Sort.Order.asc("categoryName")));
    }
    public Category findByCategoryName(String name){ return categoryRepository.findByCategoryName(name);}
}
