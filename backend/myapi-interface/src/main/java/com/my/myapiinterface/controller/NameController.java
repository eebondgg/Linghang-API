package com.my.myapiinterface.controller;


import com.my.myapiclientsdk.model.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/name")
public class NameController {
    @PostMapping("/user")
    public String getName(@RequestBody User user){
        return "你的名字是"+user.getUsername();
    }

}
