package com.example.study.controller;

import com.example.study.model.SearchParam;
import com.example.study.model.network.Header;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api") // localhost:8080/api
public class GetController {
    // localhost:8080/api/getMethod
    @RequestMapping(method = RequestMethod.GET, path = "/getMethod")
    public String getRequest(){
        return "Get Method";
    }
    // localhost:8080/api/getParameter?id=___&password=___
    @GetMapping("/getParameter")
    public String getParameter(@RequestParam String id, @RequestParam (name = "password") String password) {
        System.out.println("id : " + id + "\t password : " + password);
        return id + "\n" + password;
    }

    // localhost:8080/api/getMultiParameter?account=admin&email=test@gmail.com&page=10
    @GetMapping("/getMultiParameter")
    public SearchParam getMultiParameter(SearchParam searchParam){
        System.out.println(searchParam.getAccount());
        System.out.println(searchParam.getEmail());
        System.out.println(searchParam.getPage());

        return searchParam;
    }
    @GetMapping("/header")
    public Header getHeader() {
        // {"resultCode" : "OK", "description" : "test desc"}
        return Header.builder().resultCode("OK").description("test desc").build();
    }
}
