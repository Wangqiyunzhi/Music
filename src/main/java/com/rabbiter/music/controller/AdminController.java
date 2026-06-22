package com.rabbiter.music.controller;

import com.alibaba.fastjson.JSONObject;
import com.rabbiter.music.pojo.Admin;
import com.rabbiter.music.service.AdminService;
import com.rabbiter.music.utils.Consts;
import com.rabbiter.music.utils.PathUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

@RestController
@Api(tags = "admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @ApiOperation(value = "login")
    @RequestMapping(value = "/admin/login/status", method = RequestMethod.POST)
    public Object loginStatus(HttpServletRequest request, HttpSession session) {
        JSONObject jsonObject = new JSONObject();
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        boolean flag = adminService.verifyPassword(name, password);
        if (flag) {
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "login success");
            session.setAttribute(Consts.NAME, name);
            return jsonObject;
        }
        jsonObject.put(Consts.CODE, 0);
        jsonObject.put(Consts.MSG, "username or password error");
        return jsonObject;
    }

    @ApiOperation(value = "backend index")
    @GetMapping(value = "/manage")
    public ModelAndView index() {
        return new ModelAndView("/src/main/resources/static/screenIndex/index.html");
    }

    @ApiOperation(value = "logout")
    @RequestMapping(value = "/admin/logout", method = RequestMethod.POST)
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

    @ApiOperation(value = "login info")
    @RequestMapping(value = "/admin/login/info", method = RequestMethod.GET)
    public Object loginInfo(HttpSession session) {
        JSONObject jsonObject = new JSONObject();
        Object name = session.getAttribute(Consts.NAME);
        if (name != null) {
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put("data", name);
            jsonObject.put(Consts.MSG, "logged in");
        } else {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "not logged in");
        }
        return jsonObject;
    }

    @ApiOperation(value = "admin info")
    @GetMapping(value = "/admin/info")
    public Object adminInfo(@RequestParam(required = false) String name, HttpSession session) {
        String n = StringUtils.hasText(name) ? name : (String) session.getAttribute(Consts.NAME);
        if (!StringUtils.hasText(n)) return null;
        return adminService.getByName(n);
    }

    @ApiOperation(value = "update admin")
    @PostMapping(value = "/admin/update")
    public Object updateAdmin(HttpServletRequest request, HttpSession session) {
        JSONObject jsonObject = new JSONObject();
        String currentName = (String) session.getAttribute(Consts.NAME);
        String currentNameParam = request.getParameter("currentName");
        if (!StringUtils.hasText(currentName) && StringUtils.hasText(currentNameParam)) {
            currentName = currentNameParam;
        }
        if (!StringUtils.hasText(currentName)) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "not logged in");
            return jsonObject;
        }
        Admin admin = adminService.getByName(currentName);
        if (admin == null) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "admin not found");
            return jsonObject;
        }

        String name = request.getParameter("name");
        String password = request.getParameter("password");

        if (StringUtils.hasText(name) && !name.equals(currentName)) {
            Admin existed = adminService.getByName(name);
            if (existed != null) {
                jsonObject.put(Consts.CODE, 0);
                jsonObject.put(Consts.MSG, "name already exists");
                return jsonObject;
            }
            admin.setName(name);
        }
        if (StringUtils.hasText(password)) {
            admin.setPassword(password);
        }

        boolean ok = adminService.update(admin);
        if (ok) {
            if (StringUtils.hasText(name)) {
                session.setAttribute(Consts.NAME, name);
            }
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "update success");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE, 0);
        jsonObject.put(Consts.MSG, "update failed");
        return jsonObject;
    }

    @ApiOperation(value = "update admin avatar")
    @PostMapping(value = "/admin/updateAvatar")
    public Object updateAvatar(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String currentName,
            HttpSession session
    ) {
        JSONObject jsonObject = new JSONObject();
        if (file == null || file.isEmpty()) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "upload failed");
            return jsonObject;
        }
        Integer adminId = null;
        if (StringUtils.hasText(id) && !"null".equalsIgnoreCase(id)) {
            try {
                adminId = Integer.valueOf(id);
            } catch (NumberFormatException e) {
                adminId = null;
            }
        }
        if (adminId == null) {
            String name = StringUtils.hasText(currentName)
                    ? currentName
                    : (String) session.getAttribute(Consts.NAME);
            if (StringUtils.hasText(name)) {
                Admin adminByName = adminService.getByName(name);
                if (adminByName != null) {
                    adminId = adminByName.getId();
                }
            }
        }
        if (adminId == null) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "admin not found");
            return jsonObject;
        }
        String fileName = System.currentTimeMillis() + file.getOriginalFilename();
        String filePath = PathUtils.getClassLoadRootPath() + "/userImages/";
        File dir = new File(filePath);
        if (!dir.exists()) dir.mkdir();
        File dest = new File(filePath + fileName);
        String storePath = "/userImages/" + fileName;
        try {
            file.transferTo(dest);
            Admin admin = new Admin();
            admin.setId(adminId);
            admin.setAvator(storePath);
            boolean ok = adminService.update(admin);
            if (ok) {
                jsonObject.put(Consts.CODE, 1);
                jsonObject.put(Consts.MSG, "upload success");
                jsonObject.put("avator", storePath);
                return jsonObject;
            }
        } catch (IOException e) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "upload failed: " + e.getMessage());
            return jsonObject;
        }
        jsonObject.put(Consts.CODE, 0);
        jsonObject.put(Consts.MSG, "upload failed");
        return jsonObject;
    }
}

