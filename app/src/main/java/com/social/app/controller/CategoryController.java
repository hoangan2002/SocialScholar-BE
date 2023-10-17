package com.social.app.controller;

import com.social.app.dto.GroupDTO;
import com.social.app.model.Category;
import com.social.app.model.Groups;
import com.social.app.service.CategoryService;
import com.social.app.service.GroupServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("api/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    GroupServices groupServices;

    @GetMapping("/getAll")
    public ArrayList<Category> getAll(){
        return categoryService.getAll();
    }

    @GetMapping ArrayList<GroupDTO> searchByCategory(@RequestParam("category")String cateName){
        Category category = categoryService.findByCategoryName(cateName);
        if(category==null)
            return null;
        ArrayList<Groups> groups = groupServices.findByCategory(category);
        ArrayList<GroupDTO> groupDTOS = groupServices.groupsResponses(groups);
        return groupDTOS;
    }

}
