package com.rabbiter.music.controller;

import com.alibaba.fastjson.JSONObject;
import com.rabbiter.music.pojo.Consumer;
import com.rabbiter.music.service.ConsumerService;
import com.rabbiter.music.utils.Consts;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@Api(tags = "user")
public class UserController {

    @Autowired
    private ConsumerService consumerService;

    @ApiOperation(value = "user login status")
    @RequestMapping(value = "/user/login/status", method = RequestMethod.POST)
    public Object loginStatus(HttpServletRequest request, HttpSession session) {
        JSONObject jsonObject = new JSONObject();

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Consumer consumer = consumerService.getByUsername(username);
        if (consumer == null) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "user not found");
            return jsonObject;
        }

        if (password == null || !password.equals(consumer.getPassword())) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "password error");
            return jsonObject;
        }

        session.setAttribute(Consts.USERNAME, consumer.getUsername());
        session.setAttribute(Consts.USER_ID, consumer.getId());

        jsonObject.put(Consts.CODE, 1);
        jsonObject.put(Consts.MSG, "login success");
        jsonObject.put("userMsg", consumer);
        return jsonObject;
    }

    @ApiOperation(value = "user logout")
    @RequestMapping(value = "/user/logout", method = RequestMethod.POST)
    public Object logout(HttpSession session) {
        JSONObject jsonObject = new JSONObject();
        try {
            session.invalidate();
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "logout success");
        } catch (Exception e) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "logout failed");
        }
        return jsonObject;
    }

    @ApiOperation(value = "user login info")
    @RequestMapping(value = "/user/login/info", method = RequestMethod.GET)
    public Object loginInfo(HttpSession session) {
        JSONObject jsonObject = new JSONObject();

        Object userId = session.getAttribute(Consts.USER_ID);
        Object username = session.getAttribute(Consts.USERNAME);

        if (userId != null) {
            jsonObject.put(Consts.CODE, 1);
            JSONObject data = new JSONObject();
            data.put("userId", userId);
            data.put("username", username);
            jsonObject.put("data", data);
            jsonObject.put(Consts.MSG, "logged in");
        } else {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "not logged in");
        }
        return jsonObject;
    }

    @ApiOperation(value = "reset password")
    @RequestMapping(value = "/user/resetPassword", method = RequestMethod.POST)
    public Object resetPassword(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String username = request.getParameter("username");
        if (username == null || username.trim().isEmpty()) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "username required");
            return jsonObject;
        }
        Consumer consumer = consumerService.getByUsername(username.trim());
        if (consumer == null) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "user not found");
            return jsonObject;
        }
        Consumer upd = new Consumer();
        upd.setId(consumer.getId());
        upd.setPassword("123");
        boolean flag = consumerService.update(upd);
        if (flag) {
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "password reset to 123");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE, 0);
        jsonObject.put(Consts.MSG, "reset failed");
        return jsonObject;
    }
}


