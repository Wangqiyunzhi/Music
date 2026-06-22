package com.rabbiter.music.controller;

import com.rabbiter.music.pojo.Banner;
import com.rabbiter.music.service.BannerService;
import com.rabbiter.music.utils.Consts;
import com.rabbiter.music.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/banner")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @Value("${web.upload-path:C:/w002/music_upload/}")
    private String uploadPath;

    /** ?*/
    @GetMapping("/all")
    public List<Banner> all() {
        return bannerService.allBanner();
    }

    /** ?*/
    @GetMapping("/home")
    public List<Banner> home() {
        return bannerService.bannerForHome();
    }

    /** ?*/
    @GetMapping("/status")
    public List<Banner> getByStatus(@RequestParam Integer status) {
        return bannerService.bannerOfStatus(status);
    }

    /** ?*/
    @PostMapping("/status")
    public R updateStatus(@RequestParam Integer id, @RequestParam Integer status) {
        try {
            Banner b = new Banner();
            b.setId(id);
            b.setStatus(status);
            boolean ok = bannerService.update(b);
            return ok ? R.success(Consts.SUCCESS, 1) : R.error(Consts.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(Consts.ERROR);
        }
    }

    /** ?*/
    @PostMapping("/add")
    public R add(@RequestParam(value = "file", required = false) MultipartFile file, Banner banner) {
        try {
            if (banner.getSort() == null) banner.setSort(0);
            if (banner.getStatus() == null) banner.setStatus(1);
            if (file != null && !file.isEmpty()) {
                String filePath = saveFile(file); // /img/banner/xxx.png
                banner.setPic(filePath);
            }
            boolean ok = bannerService.insert(banner);
            return ok ? R.success(Consts.SUCCESS, 1) : R.error(Consts.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(Consts.ERROR);
        }
    }

    /**  */
    @PostMapping("/update")
    public R update(Banner banner) {
        try {
            boolean ok = bannerService.update(banner);
            return ok ? R.success(Consts.SUCCESS, 1) : R.error(Consts.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(Consts.ERROR);
        }
    }

    /**  */
    @GetMapping("/delete")
    public R delete(@RequestParam Integer id) {
        try {
            boolean ok = bannerService.delete(id);
            return ok ? R.success(Consts.SUCCESS, 1) : R.error(Consts.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(Consts.ERROR);
        }
    }

    /** ?*/
    @PostMapping("/updatePic")
    public R updatePic(@RequestParam("file") MultipartFile file, @RequestParam Integer id) {
        try {
            if (file == null || file.isEmpty()) return R.error(Consts.ERROR);

            String filePath = saveFile(file); //  /img/banner/xxx.png
            Banner b = new Banner();
            b.setId(id);
            b.setPic(filePath);

            boolean ok = bannerService.update(b);
            if (ok) {
                R r = R.success(Consts.SUCCESS, 1);
                r.put("pic", filePath);
                return r;
            }
            return R.error(Consts.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(Consts.ERROR);
        }
    }

    @PostMapping("/updateBannerPic")
    public R updateBannerPic(@RequestParam("file") MultipartFile file, @RequestParam Integer id) {
        return updatePic(file, id);
    }

    private String saveFile(MultipartFile file) throws Exception {
        String original = file.getOriginalFilename();
        String cleanName = StringUtils.hasText(original) ? Paths.get(original).getFileName().toString() : "banner.png";
        String fileName = System.currentTimeMillis() + "_" + cleanName;

        Path dir = Paths.get(uploadPath);
        if (!Files.exists(dir)) Files.createDirectories(dir);

        Path target = dir.resolve(fileName);
        Files.write(target, file.getBytes());

        return "/img/banner/" + fileName;
    }
}


