package com.example.controller;


import cn.hutool.core.io.file.FileNameUtil;
import com.example.common.Result;
import com.example.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    private static final String imgDir=System.getProperty("user.dir")+"/img";

    @PostMapping("/uploadImg")
    public Result uploadImg(MultipartFile img) throws IOException {
        if(img==null){
            throw new CustomException("500","文件不能为空");
        }
        String originalFilename = img.getOriginalFilename();
        String ext = FileNameUtil.extName(originalFilename);
        String newName= UUID.randomUUID()+"."+ext;
        File dir=new File(imgDir);
        if (!dir.exists()){
            boolean b = dir.mkdirs();
            if (!b){
                log.info("创建文件夹失败");
            }
        }


        try (
            InputStream in = img.getInputStream(); // 获取文件输入流
            OutputStream out = new FileOutputStream(imgDir + "/"+newName)
        ) {

            byte[] buffer=new byte[1024];
            int len;//有效长度

            while ((len=in.read(buffer))!=-1){
                out.write(buffer,0,len);

            }
            out.close();//先关闭写
            in.close();//再关闭读
            return Result.success("http://localhost:8080/"+newName);
        }
    }
}
