package com.social.app.controller;

import com.social.app.dto.CategoryDTO;
import com.social.app.dto.GroupDTO;
import com.social.app.dto.HintTagDTO;
import com.social.app.model.Category;
import com.social.app.model.Groups;
import com.social.app.service.CategoryService;
import com.social.app.service.GroupServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    GroupServices groupServices;
    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/getAll")
    public ArrayList<CategoryDTO> getAll(){
        return categoryService.getAll();
    }

    @GetMapping
    public ArrayList<GroupDTO> searchByCategory(@RequestParam("category")String cateName){
        Category category = modelMapper.map(categoryService.findByCategoryName(cateName), Category.class);
        if(category==null)
            return null;
        ArrayList<Groups> groups = groupServices.findByCategory(category);
        ArrayList<GroupDTO> groupDTOS = groupServices.groupsResponses(groups);
        return groupDTOS;
    }

    @GetMapping("/getHintTag/{categoryId}")
    public ArrayList<HintTagDTO> getHintTagsByCategoryId(@PathVariable("categoryId") int categoryId){
        return categoryService.getHintTagsByCategoryId(categoryId);
    }


}
