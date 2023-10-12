package com.social.app.service;

import com.social.app.dto.CategoryDTO;
import com.social.app.dto.HintTagDTO;
import com.social.app.model.Category;
import com.social.app.model.HintTag;
import com.social.app.repository.CategoryRepository;
import com.social.app.repository.HintTagRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    HintTagRepository hintTagRepository;
    @Autowired
    ModelMapper modelMapper;

    public ArrayList<CategoryDTO> getAll(){
        ArrayList<Category> categories = (ArrayList<Category>) categoryRepository.findAll(Sort.by(Sort.Order.asc("categoryName")));
        ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
        for (Category c: categories) {
            categoryDTOS.add(modelMapper.map(c, CategoryDTO.class));
        }
        return categoryDTOS;
    }
    public CategoryDTO findByCategoryName(String name){ return  modelMapper.map(categoryRepository.findByCategoryName(name), CategoryDTO.class);}

    public ArrayList<HintTagDTO> getHintTagsByCategoryId(int categoryId){
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found!"));
        ArrayList<HintTag> hintTags = hintTagRepository.findByCategory(category);
        ArrayList<HintTagDTO> hintTagDTOS = new ArrayList<>();
        for (HintTag h: hintTags) {
            hintTagDTOS.add(modelMapper.map(h, HintTagDTO.class));
        }
        return hintTagDTOS;
    }

}
