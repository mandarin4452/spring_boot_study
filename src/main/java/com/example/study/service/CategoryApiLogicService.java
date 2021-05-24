package com.example.study.service;

import com.example.study.model.entity.Category;
import com.example.study.model.network.Header;
import com.example.study.model.network.request.CategoryApiRequest;
import com.example.study.model.network.response.CategoryApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryApiLogicService extends BaseService<CategoryApiRequest, CategoryApiResponse, Category> {

    @Override
    public Header<CategoryApiResponse> create(Header<CategoryApiRequest> request) {
        CategoryApiRequest body = request.getData();
        Category category = Category.builder()
                .type(body.getType())
                .title(body.getTitle())
                .build();
        Category newCategory = baseRepository.save(category);
        return response(newCategory);
    }

    @Override
    public Header<CategoryApiResponse> read(Long id) {
        return baseRepository.findById(id)
                .map(category -> response(category))
                .orElseGet(()-> Header.ERROR("No data"));
    }

    @Override
    public Header<CategoryApiResponse> update(Header<CategoryApiRequest> request) {
        CategoryApiRequest body = request.getData();

        return baseRepository.findById(body.getId())
                .map(entityCategory->{
                    entityCategory
                            .setType(body.getType())
                            .setTitle(body.getTitle());
                    return entityCategory;
                })
                .map(newEntityCategory -> baseRepository.save(newEntityCategory))
                .map(category -> response(category))
                .orElseGet(()->Header.ERROR("No data"));
    }

    @Override
    public Header delete(Long id) {
        return baseRepository.findById(id)
                .map(category-> {
                    baseRepository.delete(category);
                    return Header.OK();
                })
                .orElseGet(()-> Header.ERROR("No data"));
    }

    private Header<CategoryApiResponse> response(Category category) {
        CategoryApiResponse body = CategoryApiResponse.builder()
                .id(category.getId())
                .type(category.getType())
                .title(category.getTitle())
                .build();
        return Header.OK(body);
    }
}
