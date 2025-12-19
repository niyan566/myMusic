package com.example.service;


import com.example.entity.Music;
import com.example.mapper.MusicMapper;
import com.example.vo.MusicVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.datatype.Artwork;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class MusicService {

    @Resource
    public MusicMapper musicMapper;

    //System.getProperty("user.dir") 获取项目根路径
    //文件上传目录的路径
    private static final String musicDirPath=System.getProperty("user.dir")+"/music/";
    private static final String coverDirPath=System.getProperty("user.dir")+"/cover/";

    public void upload(MultipartFile file) {
        Music music = handleFiles(file);
        musicMapper.insert(music);
    }

    public List<Music> selectAll() {
        return musicMapper.selectAll();
    }

    public void updateById(Integer id, MultipartFile file) {
        Music music = handleFiles(file);
        music.setId(id);
        musicMapper.updateById(music);
    }

    public Music handleFiles(MultipartFile file){
        try {
            // 1. 创建目录
            File musicDir = new File(musicDirPath);
            File coverDir=new File(coverDirPath);
            if (!musicDir.exists()) {
                musicDir.mkdirs();
            }

            if (!coverDir.exists()){
                coverDir.mkdirs();
            }

            // 2. 处理歌曲文件
            String originalName = file.getOriginalFilename();
            String extension = null;
            if (originalName != null) {
                extension = originalName.substring(originalName.lastIndexOf("."));//.mp3
            }
            String newMusicName = UUID.randomUUID() + extension;//设置uuid 防止重名 uuid.mp3
            Path realMusicPath = Paths.get(musicDirPath + newMusicName);//真实路径
            Files.write(realMusicPath, file.getBytes());

            // 3. 解析元数据
            AudioFile audioFile = AudioFileIO.read(realMusicPath.toFile());
            Tag tag = audioFile.getTag();
            if (tag == null) {
                throw new RuntimeException("该音频文件没有ID3标签，无法提取元数据");
            }

            String newCoverName = null;
            String newCoverPath;
            List<Artwork> artworks = tag.getArtworkList();
            if (!artworks.isEmpty()) {
                Artwork artwork = artworks.getFirst(); // 取第一张封面
                byte[] coverBytes = artwork.getBinaryData(); // 封面二进制数据
                String coverExtension = artwork.getMimeType().split("/")[1]; // 图片格式（jpg/png）

                // 生成封面文件名（和音频文件同名，方便关联）
                if (extension != null) {
                    newCoverName = newMusicName.replace(extension, "." + coverExtension);
                }
                // 封面保存路径
                newCoverPath = coverDirPath + newCoverName;

                // 写入封面图片到本地
                try (FileOutputStream fos = new FileOutputStream(newCoverPath)) {
                    fos.write(coverBytes);
                }
                log.info("封面图片保存成功：{}", newCoverPath);
            } else {
                log.warn("该音频文件没有封面图片");
            }

            Music music = new Music();
            music.setTitle(tag.getFirst(FieldKey.TITLE));
            music.setSinger(tag.getFirst(FieldKey.ARTIST));
            music.setPath("http://localhost:8080/"+newMusicName);
            music.setCoverPath("http://localhost:8080/"+newCoverName);
            music.setDuration(audioFile.getAudioHeader().getTrackLength());

            //设置上传时间
            music.setUploadTime(LocalDateTime.now());

            return music;

        } catch (IOException | CannotReadException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
            throw new RuntimeException(e);
        }
    }

    public List<MusicVo> selectAllWithUserId(Integer userId) {
        return musicMapper.selectAllWithUserId(userId);
    }
}
