package com.social.app.controller;

import com.social.app.model.Category;
import com.social.app.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("api/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @GetMapping("getAll")
    public ArrayList<Category> getAll(){
        return categoryService.getAll();
    }
}
