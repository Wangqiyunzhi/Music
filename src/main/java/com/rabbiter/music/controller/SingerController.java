package com.rabbiter.music.controller;

import com.alibaba.fastjson.JSONObject;
import com.rabbiter.music.pojo.Singer;
import com.rabbiter.music.service.SingerService;
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
@RequestMapping("/singer")
@Api(tags = "Singer")
public class SingerController {

    @Autowired
    private SingerService singerService;

    private String getParam(HttpServletRequest request, String key) {
        String value = request.getParameter(key);
        return value == null ? "" : value.trim();
    }

    private Date parseBirthOrNow(String birth) {
        if (birth == null || birth.trim().isEmpty()) {
            return new Date();
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(birth.trim());
        } catch (ParseException e) {
            return new Date();
        }
    }

    @ApiOperation(value = "Add singer")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object addSinger(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();

        String name = getParam(request, "name");
        String sexText = getParam(request, "sex");
        String pic = getParam(request, "pic");
        String birth = getParam(request, "birth");
        String location = getParam(request, "location");
        String introduction = getParam(request, "introduction");

        if (name.isEmpty()) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "name required");
            return jsonObject;
        }

        byte sex = 0;
        if (!sexText.isEmpty()) {
            try {
                sex = Byte.parseByte(sexText);
            } catch (NumberFormatException ignored) {
                sex = 0;
            }
        }

        if (pic.isEmpty()) {
            pic = "/img/user.jpg";
        }

        Singer singer = new Singer();
        singer.setName(name);
        singer.setSex(sex);
        singer.setPic(pic);
        singer.setBirth(parseBirthOrNow(birth));
        singer.setLocation(location);
        singer.setIntroduction(introduction);

        boolean flag = singerService.insert(singer);
        if (flag) {
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "add success");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE, 0);
        jsonObject.put(Consts.MSG, "add failed");
        return jsonObject;
    }

    @ApiOperation(value = "Update singer")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Object updateSinger(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();

        String idText = getParam(request, "id");
        String name = getParam(request, "name");
        String sexText = getParam(request, "sex");
        String birth = getParam(request, "birth");
        String location = getParam(request, "location");
        String introduction = getParam(request, "introduction");

        if (idText.isEmpty()) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "singer id required");
            return jsonObject;
        }

        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException e) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "singer id format error");
            return jsonObject;
        }

        byte sex = 0;
        if (!sexText.isEmpty()) {
            try {
                sex = Byte.parseByte(sexText);
            } catch (NumberFormatException ignored) {
                sex = 0;
            }
        }

        Singer singer = new Singer();
        singer.setId(id);
        singer.setName(name);
        singer.setSex(sex);
        singer.setBirth(parseBirthOrNow(birth));
        singer.setLocation(location);
        singer.setIntroduction(introduction);

        boolean flag = singerService.update(singer);
        if (flag) {
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "update success");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE, 0);
        jsonObject.put(Consts.MSG, "update failed");
        return jsonObject;
    }

    @ApiOperation(value = "Delete singer")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Object deleteSinger(HttpServletRequest request) {
        String id = getParam(request, "id");
        if (id.isEmpty()) {
            return false;
        }
        return singerService.delete(Integer.parseInt(id));
    }

    @ApiOperation(value = "Query singer by id")
    @RequestMapping(value = "/selectByPrimaryKey", method = RequestMethod.GET)
    public Object selectByPrimaryKey(HttpServletRequest request) {
        String id = getParam(request, "id");
        return singerService.selectByPrimaryKey(Integer.parseInt(id));
    }

    @ApiOperation(value = "Query all singers")
    @RequestMapping(value = "/allSinger", method = RequestMethod.GET)
    public Object allSinger(HttpServletRequest request) {
        return singerService.allSinger();
    }

    @ApiOperation(value = "Search singer by name")
    @RequestMapping(value = "/singerOfName", method = RequestMethod.GET)
    public Object singerOfName(HttpServletRequest request) {
        String name = getParam(request, "name");
        return singerService.singerOfName("%" + name + "%");
    }

    @ApiOperation(value = "Search singer by sex")
    @RequestMapping(value = "/singerOfSex", method = RequestMethod.GET)
    public Object singerOfSex(HttpServletRequest request) {
        String sex = getParam(request, "sex");
        return singerService.singerOfSex(Integer.parseInt(sex));
    }

    @ApiOperation(value = "Update singer pic")
    @RequestMapping(value = "/updateSingerPic", method = RequestMethod.POST)
    public Object updateSingerPic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id") int id) {
        JSONObject jsonObject = new JSONObject();
        if (avatorFile.isEmpty()) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "upload failed");
            return jsonObject;
        }

        String fileName = System.currentTimeMillis() + avatorFile.getOriginalFilename();
        String filePath = PathUtils.getClassLoadRootPath() + "/img/singerPic/";

        File file1 = new File(filePath);
        if (!file1.exists()) {
            file1.mkdir();
        }

        File dest = new File(filePath + fileName);
        String storeAvatorPath = "/img/singerPic/" + fileName;
        try {
            avatorFile.transferTo(dest);
            Singer singer = new Singer();
            singer.setId(id);
            singer.setPic(storeAvatorPath);
            boolean flag = singerService.update(singer);
            if (flag) {
                jsonObject.put(Consts.CODE, 1);
                jsonObject.put(Consts.MSG, "upload success");
                jsonObject.put("pic", storeAvatorPath);
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
}
