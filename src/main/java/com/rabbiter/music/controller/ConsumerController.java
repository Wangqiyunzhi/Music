package com.rabbiter.music.controller;

import com.alibaba.fastjson.JSONObject;
import com.rabbiter.music.pojo.Consumer;
import com.rabbiter.music.service.ConsumerService;
import com.rabbiter.music.utils.Consts;
import com.rabbiter.music.utils.PathUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/consumer")
@Api(tags = "consumer")
public class ConsumerController {

    @Autowired
    private ConsumerService consumerService;

    @ApiOperation(value = "add consumer")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object addConsumer(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String sex = request.getParameter("sex");
        String phoneNum = request.getParameter("phoneNum");
        String email = request.getParameter("email");
        String birth = request.getParameter("birth");
        String introduction = request.getParameter("introduction");
        String location = request.getParameter("location");
        String avator = request.getParameter("avator");

        if (username == null || username.trim().isEmpty()) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "username required");
            return jsonObject;
        }
        if (password == null || password.trim().isEmpty()) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "password required");
            return jsonObject;
        }
        Consumer existed = consumerService.getByUsername(username.trim());
        if (existed != null) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "username exists");
            return jsonObject;
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = new Date();
        if (birth != null) {
            try {
                birthDate = dateFormat.parse(birth);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Consumer consumer = new Consumer();
        consumer.setUsername(username.trim());
        consumer.setPassword(password.trim());
        if (sex != null) consumer.setSex(new Byte(sex));
        consumer.setPhoneNum(phoneNum);
        consumer.setEmail(email);
        consumer.setBirth(birthDate);
        consumer.setIntroduction(introduction);
        consumer.setLocation(location);
        consumer.setAvator(avator);

        boolean flag = consumerService.insert(consumer);
        if (flag) {
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "add success");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE, 0);
        jsonObject.put(Consts.MSG, "add failed");
        return jsonObject;
    }

    @ApiOperation(value = "update consumer")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Object updateConsumer(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String id = request.getParameter("id");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String sex = request.getParameter("sex");
        String phoneNum = request.getParameter("phoneNum");
        String email = request.getParameter("email");
        String birth = request.getParameter("birth");
        String introduction = request.getParameter("introduction");
        String location = request.getParameter("location");

        if (id == null || id.trim().isEmpty()) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "id required");
            return jsonObject;
        }
        if (username == null || username.trim().isEmpty()) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "username required");
            return jsonObject;
        }
        if (password == null || password.trim().isEmpty()) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "password required");
            return jsonObject;
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = new Date();
        if (birth != null) {
            try {
                birthDate = dateFormat.parse(birth);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Consumer consumer = new Consumer();
        consumer.setId(Integer.parseInt(id));
        consumer.setUsername(username.trim());
        consumer.setPassword(password.trim());
        if (sex != null) consumer.setSex(new Byte(sex));
        consumer.setPhoneNum(phoneNum);
        consumer.setEmail(email);
        consumer.setBirth(birthDate);
        consumer.setIntroduction(introduction);
        consumer.setLocation(location);

        boolean flag = consumerService.update(consumer);
        if (flag) {
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "update success");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE, 0);
        jsonObject.put(Consts.MSG, "update failed");
        return jsonObject;
    }

    @ApiOperation(value = "delete consumer")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Object deleteConsumer(HttpServletRequest request) {
        String id = request.getParameter("id");
        boolean flag = consumerService.delete(Integer.parseInt(id));
        return flag;
    }

    @ApiOperation(value = "select by id")
    @RequestMapping(value = "/selectByPrimaryKey", method = RequestMethod.GET)
    public Object selectByPrimaryKey(HttpServletRequest request) {
        String id = request.getParameter("id");
        return consumerService.selectByPrimaryKey(Integer.parseInt(id));
    }

    @ApiOperation(value = "all consumer")
    @RequestMapping(value = "/allConsumer", method = RequestMethod.GET)
    public Object allConsumer(HttpServletRequest request) {
        return consumerService.allConsumer();
    }

    @ApiOperation(value = "update consumer pic")
    @RequestMapping(value = "/updateConsumerPic", method = RequestMethod.POST)
    public Object updateConsumerPic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id") int id) {
        JSONObject jsonObject = new JSONObject();
        if (avatorFile.isEmpty()) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "upload failed");
            return jsonObject;
        }

        String fileName = System.currentTimeMillis() + avatorFile.getOriginalFilename();
        String filePath = PathUtils.getClassLoadRootPath() + "/userImages/";
        File file1 = new File(filePath);
        if (!file1.exists()) {
            file1.mkdir();
        }
        File dest = new File(filePath + fileName);
        String storeAvatorPath = "/userImages/" + fileName;
        try {
            avatorFile.transferTo(dest);
            Consumer consumer = new Consumer();
            consumer.setId(id);
            consumer.setAvator(storeAvatorPath);
            boolean flag = consumerService.update(consumer);
            if (flag) {
                jsonObject.put(Consts.CODE, 1);
                jsonObject.put(Consts.MSG, "upload success");
                jsonObject.put("avator", storeAvatorPath);
                return jsonObject;
            }
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "upload failed");
            return jsonObject;
        } catch (IOException e) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "upload failed: " + e.getMessage());
        } finally {
            return jsonObject;
        }
    }

    @ApiOperation(value = "reset password")
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
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

    @ApiOperation(value = "login")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Object login(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (username == null || username.trim().isEmpty()) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "username required");
            return jsonObject;
        }
        if (password == null || password.trim().isEmpty()) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "password required");
            return jsonObject;
        }

        boolean flag = consumerService.verifyPassword(username.trim(), password.trim());
        if (flag) {
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "login success");
            jsonObject.put("userMsg", consumerService.getByUsername(username.trim()));
            return jsonObject;
        }
        jsonObject.put(Consts.CODE, 0);
        jsonObject.put(Consts.MSG, "username or password error");
        return jsonObject;
    }
}


