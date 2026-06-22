package com.rabbiter.music.controller;

import com.rabbiter.music.pojo.Song;
import com.rabbiter.music.service.SongService;
import com.rabbiter.music.utils.Consts;
import com.rabbiter.music.utils.PathUtils;
import com.rabbiter.music.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/song")
public class SongController {

    @Autowired// 可以理解为对讲机
    private SongService songService;

    @Value("${web.upload-path:C:/w002/music_upload/}")
    private String filePath;


    @GetMapping("/allSong")
    public List<Song> allSong() {
        return songService.allSong();
    }

    // =====================  =====================

    /** ID?song/singer/detail?singerId=5 */
    @GetMapping("/singer/detail")
    public List<Song> songOfSingerId(@RequestParam Integer singerId) {
        if (singerId == null) return Collections.emptyList();
        return songService.songOfSingerId(singerId);
    }

    /** ?song/songOfSongName?songName=??*/
    @GetMapping("/songOfSongName")
    public List<Song> songOfSongName(@RequestParam String songName) {
        if (!StringUtils.hasText(songName)) return Collections.emptyList();
        return songService.songOfName(songName);
    }

    /** ?song/likeSongOfName?name=?*/
    @GetMapping("/likeSongOfName")
    public List<Song> likeSongOfName(
            @RequestParam(name = "songName", required = false) String songName,
            @RequestParam(name = "name", required = false) String name
    ) {
        String q = StringUtils.hasText(songName) ? songName : name;
        if (!StringUtils.hasText(q)) return Collections.emptyList();
        return songService.likeSongOfName(q);
    }

    // ===================== ??=====================

    @PostMapping("/add")
    public R add(Song song, @RequestParam("file") MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) return R.error(Consts.ERROR);

            String filePath = saveSongFile(file);
            song.setUrl(filePath);
            if (!StringUtils.hasText(song.getPic())) {
                song.setPic("/img/song-default.jpg");
            }

            boolean ok = songService.insert(song);
            return ok ? R.success(Consts.SUCCESS, 1) : R.error(Consts.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(Consts.ERROR);
        }
    }

    @PostMapping("/update")
    public R update(Song song) {
        boolean ok = songService.update(song);
        return ok ? R.success(Consts.SUCCESS, 1) : R.error(Consts.ERROR);
    }

    @GetMapping("/delete")
    public R delete(@RequestParam Integer id) {
        boolean ok = songService.delete(id);
        return ok ? R.success(Consts.SUCCESS, 1) : R.error(Consts.ERROR);
    }

    @PostMapping("/updateSongUrl")
    public R updateSongUrl(@RequestParam("file") MultipartFile file, @RequestParam Integer id) {
        try {
            if (file == null || file.isEmpty()) return R.error(Consts.ERROR);

            String filePath = saveSongFile(file);

            Song song = new Song();
            song.setId(id);
            song.setUrl(filePath);

            boolean ok = songService.update(song);
            return ok ? R.success(Consts.SUCCESS, 1) : R.error(Consts.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(Consts.ERROR);
        }
    }

    @PostMapping("/updateSongPic")
    public R updateSongPic(@RequestParam("file") MultipartFile file, @RequestParam Integer id) {
        try {
            if (file == null || file.isEmpty()) return R.error(Consts.ERROR);

            String fileName = saveSongPic(file);

            Song song = new Song();
            song.setId(id);
            song.setPic(fileName);

            boolean ok = songService.update(song);
            return ok ? R.success(Consts.SUCCESS, 1) : R.error(Consts.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(Consts.ERROR);
        }
    }

    @PostMapping("/updateSongLyric")
    public R updateSongLyric(@RequestParam("file") MultipartFile file, @RequestParam Integer id) {
        try {
            if (file == null || file.isEmpty()) return R.error(Consts.ERROR);

            String text = new String(file.getBytes(), StandardCharsets.UTF_8);
            if (text.startsWith("\uFEFF")) text = text.substring(1);

            Song song = new Song();
            song.setId(id);
            song.setLyric(text);

            boolean ok = songService.update(song);
            return ok ? R.success(Consts.SUCCESS, 1) : R.error(Consts.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(Consts.ERROR);
        }
    }

    private String saveToUploadDir(MultipartFile file) throws Exception {
        String original = file.getOriginalFilename();
        String cleanName = StringUtils.hasText(original) ? Paths.get(original).getFileName().toString() : "file.bin";
        String fileName = System.currentTimeMillis() + "_" + cleanName;

        Path dir = Paths.get(filePath);
        if (!Files.exists(dir)) Files.createDirectories(dir);

        Path target = dir.resolve(fileName);
        Files.write(target, file.getBytes());

        return fileName;
    }

    private String saveSongPic(MultipartFile file) throws Exception {
        String original = file.getOriginalFilename();
        String cleanName = StringUtils.hasText(original) ? Paths.get(original).getFileName().toString() : "song.jpg";
        String fileName = System.currentTimeMillis() + "_" + cleanName;

        Path dir = Paths.get(PathUtils.getClassLoadRootPath(), "img", "songPic");
        if (!Files.exists(dir)) Files.createDirectories(dir);

        Path target = dir.resolve(fileName);
        Files.write(target, file.getBytes());

        return "/img/songPic/" + fileName;
    }

    private String saveSongFile(MultipartFile file) throws Exception {
        String original = file.getOriginalFilename();
        String cleanName = StringUtils.hasText(original) ? Paths.get(original).getFileName().toString() : "song.mp3";
        String fileName = System.currentTimeMillis() + "_" + cleanName;

        Path dir = Paths.get(PathUtils.getClassLoadRootPath(), "song");
        if (!Files.exists(dir)) Files.createDirectories(dir);

        Path target = dir.resolve(fileName);
        Files.write(target, file.getBytes());

        return "/song/" + fileName;
    }
}




