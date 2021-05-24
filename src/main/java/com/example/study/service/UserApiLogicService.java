package com.example.study.service;

import com.example.study.ifs.CrudInterface;
import com.example.study.model.entity.Item;
import com.example.study.model.entity.OrderGroup;
import com.example.study.model.entity.User;
import com.example.study.model.enumclass.UserStatus;
import com.example.study.model.network.Header;
import com.example.study.model.network.Pagination;
import com.example.study.model.network.request.UserApiRequest;
import com.example.study.model.network.response.ItemApiResponse;
import com.example.study.model.network.response.OrderGroupApiResponse;
import com.example.study.model.network.response.UserApiResponse;
import com.example.study.model.network.response.UserOrderInfoApiResponse;
import com.example.study.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserApiLogicService implements CrudInterface<UserApiRequest, UserApiResponse> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderGroupApiLogicService orderGroupApiLogicService;

    @Autowired
    private ItemApiLogicService itemApiLogicService;

    public Header<List<UserApiResponse>> search(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        List<UserApiResponse> userApiResponseList = users.stream()
                .map(user -> response(user))
                .collect(Collectors.toList());

        Pagination pagination = Pagination.builder()
                .totalPages(users.getTotalPages())
                .totalElements(users.getTotalElements())
                .currentPage(users.getNumber())
                .currentElements(users.getNumberOfElements())
                .build();
        return Header.OK(userApiResponseList, pagination);
    }

    // Pagination in others :
    // 1. add to crud controller
    // 2. add to service
    // 3. add to baseService

    // 1. Get request data
    // 2. Create user
    // 3. Created Data --> UserApiResponse return
    @Override
    public Header<UserApiResponse> create(Header<UserApiRequest> request) {

        // 1. Get request data
        UserApiRequest userApiRequest = request.getData();

        // 2. Create user
        User user = User.builder()
                .account(userApiRequest.getAccount())
                .password(userApiRequest.getPassword())
                .status(UserStatus.REGISTERED)
                .phoneNumber(userApiRequest.getPhoneNumber())
                .email(userApiRequest.getEmail())
                .registeredAt(LocalDateTime.now())
                .build();

        // 3.return
        User newUser = userRepository.save(user);
        return Header.OK(response(newUser));
    }

    @Override
    public Header<UserApiResponse> read(Long id) {
        // id -> repository getOne, getById
        // user -> userApiResponse return


        return userRepository.findById(id)
                .map(user->response(user))
                //.map(userApiResponse -> Header.OK(userApiResponse))
                .map(Header::OK)
                .orElseGet(
                        ()->Header.ERROR("No data")
                );
        // 아래 구문들을 한 줄로!
        /*
        Optional<User> optional = userRepository.findById(id);

        optional
                .map(user-> response(user))
                .orElseGet(
                        ()->Header.ERROR("No data")
                );
        */

    }

    @Override
    public Header<UserApiResponse> update(Header<UserApiRequest> request) {

        // 1. data
        UserApiRequest userApiRequest = request.getData();
        // 2. id -> user find
        Optional<User> optional = userRepository.findById(userApiRequest.getId());

        return optional.map(user->{
            // 3. update --> if user
            user.setAccount(userApiRequest.getAccount())
                    .setPassword(userApiRequest.getPassword())
                    .setStatus(userApiRequest.getStatus())
                    .setPhoneNumber(userApiRequest.getPhoneNumber())
                    .setEmail(userApiRequest.getEmail())
                    .setRegisteredAt(userApiRequest.getRegisteredAt())
                    .setUnregisteredAt(userApiRequest.getUnregisteredAt());

            return user;
            // 4.userApiResponse
        }).map(user-> userRepository.save(user)) // update --> newUser
                .map(user->response(user)) // create userApiResponse
                .map(Header::OK)
                .orElseGet(()->Header.ERROR("No data"));

    }

    @Override
    public Header delete(Long id) {
        // 1. id -> repository -> user
        Optional<User> optional = userRepository.findById(id);
        // 2. repository -> delete
        return optional.map(user->{
            userRepository.delete(user);
            return Header.OK();
        }).orElseGet(()->Header.ERROR("No data"));
        // 3. return response
    }

    private UserApiResponse response(User user) {
        // user --> userApiResponse
        UserApiResponse userApiResponse = UserApiResponse.builder()
                .id(user.getId())
                .account(user.getAccount())
                .password(user.getPassword()) // need to encrypt & limit length
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .status(user.getStatus())
                .registeredAt(user.getRegisteredAt())
                .unRegisteredAt(user.getUnregisteredAt())
                .build();

        // Header + data
        return userApiResponse;
    }

    public Header<UserOrderInfoApiResponse> orderInfo(Long id) {
        // 1. find user
        User user = userRepository.getOne(id);
        UserApiResponse userApiResponse = response(user);


        // 2. find order group
        List<OrderGroup> orderGroupList = user.getOrderGroupList();
        List<OrderGroupApiResponse> orderGroupApiResponseList = orderGroupList.stream()
                .map(orderGroup -> {
                    OrderGroupApiResponse orderGroupApiResponse = orderGroupApiLogicService.response(orderGroup).getData();
                    // 3. get Item response
                    List<ItemApiResponse> itemApiResponseList = orderGroup.getOrderDetailList().stream()
                            .map(detail-> detail.getItem())
                            .map(item->itemApiLogicService.response(item).getData())
                            .collect(Collectors.toList());
                    orderGroupApiResponse.setItemApiResponseList(itemApiResponseList);
                    return orderGroupApiResponse;
                })
                .collect(Collectors.toList());
        userApiResponse.setOrderGroupApiResponseList(orderGroupApiResponseList);
        UserOrderInfoApiResponse userOrderInfoApiResponse = UserOrderInfoApiResponse.builder()
                .userApiResponse(userApiResponse)
                .build();

        return Header.OK(userOrderInfoApiResponse);
    }
}
