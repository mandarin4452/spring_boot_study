package com.example.study.service;

import com.example.study.ifs.CrudInterface;
import com.example.study.model.entity.Item;
import com.example.study.model.network.Header;
import com.example.study.model.network.request.ItemApiRequest;
import com.example.study.model.network.response.ItemApiResponse;
import com.example.study.repository.ItemRepository;
import com.example.study.repository.PartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

// CRUD 작성법
// 1. Request, Response 모델 생성
// 2. (Option) CRUD interface 생성
// 3. CRUD에 대해 Controller에 Request 매핑
// 4. Service Logic 생성
// 5. Service Logic 내에서도 CRUD에 대해서 실질적으로 들어가는 Logic 작성
// 6.Controller와 알맞은 메서드 매핑

@Service
public class ItemApiLogicService extends BaseService<ItemApiRequest, ItemApiResponse, Item>{
    @Autowired
    private PartnerRepository partnerRepository;
    // BaseService 상속으로 하단 ItemRepository는 이제 사용 X
    /*
    @Autowired
    private ItemRepository itemRepository;
     */
    @Override
    public Header<ItemApiResponse> create(Header<ItemApiRequest> request) {
        ItemApiRequest body = request.getData();
        Item item = Item.builder()
                .status(body.getStatus())
                .name(body.getName())
                .title(body.getTitle())
                .content(body.getContent())
                .price(body.getPrice())
                .brandName(body.getBrandName())
                .registeredAt(LocalDateTime.now())
                .partner(partnerRepository.getOne(body.getPartnerId())).build();

        Item newItem = baseRepository.save(item);
        return response(newItem);
    }

    @Override
    public Header<ItemApiResponse> read(Long id) {
        return baseRepository.findById(id)
                .map(item->response(item))
                .orElseGet(()-> Header.ERROR("No data"));
    }

    @Override
    public Header<ItemApiResponse> update(Header<ItemApiRequest> request) {
         ItemApiRequest body = request.getData();

         return baseRepository.findById(body.getId())
                 .map(entityItem-> {
                    entityItem
                            .setStatus(body.getStatus())
                            .setName(body.getName())
                            .setTitle(body.getTitle())
                            .setContent(body.getContent())
                            .setPrice(body.getPrice())
                            .setBrandName(body.getBrandName())
                            .setRegisteredAt(body.getRegisteredAt())
                            .setUnregisteredAt(body.getUnregisteredAt())
                            ;
                    return entityItem;
                 })
                 .map(newEntityItem-> baseRepository.save(newEntityItem))
                 .map(item-> response(item))
                 .orElseGet(()->Header.ERROR("No data"));
    }

    @Override
    public Header delete(Long id) {
        return baseRepository.findById(id)
                // Delete의 경우 null return이므로 map(item->itemRepository.delete()) 불가능
                .map(item->{
                    baseRepository.delete(item);
                    return Header.OK();
                })
                .orElseGet(()->Header.ERROR("No data"));
    }

    public Header<ItemApiResponse> response(Item item) {
        ItemApiResponse body = ItemApiResponse.builder()
                .id(item.getId())
                .status(item.getStatus())
                .name(item.getName())
                .title(item.getTitle())
                .content(item.getContent())
                .brandName(item.getBrandName())
                .registeredAt(item.getRegisteredAt())
                .unregisteredAt(item.getUnregisteredAt())
                .partnerId(item.getPartner().getId())
                .build();
        return Header.OK(body);
    }
}
