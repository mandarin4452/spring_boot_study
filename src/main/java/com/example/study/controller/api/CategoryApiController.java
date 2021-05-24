package com.example.study.controller.api;

import com.example.study.controller.CrudController;
import com.example.study.model.entity.Category;
import com.example.study.model.network.request.CategoryApiRequest;
import com.example.study.model.network.response.CategoryApiResponse;
import com.example.study.service.CategoryApiLogicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/api/category")
public class CategoryApiController extends CrudController<CategoryApiRequest, CategoryApiResponse, Category> {
    @Autowired
    private CategoryApiLogicService categoryLogicService;

    @PostConstruct
    public void init() { this.baseService = categoryLogicService; }
}
