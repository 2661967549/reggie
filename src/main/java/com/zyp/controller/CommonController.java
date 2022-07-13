package com.zyp.controller;

import com.zyp.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/aip/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {
        //1.请求结束前文件会在本地tomcat的某个临时文件夹中，请求结束后会自动消失，需要转存才能保存

        //1.5 创建要保存文件的那个目录，当该目录不存在时创建
        File dir = new File(basePath);
        if (!dir.exists()){
            dir.mkdirs();
        }
            //获得原始文件名，再获得后缀的开始位置索引
        String originalFilename = file.getOriginalFilename();
        int i = originalFilename.lastIndexOf(".");

            //使用UUID重新生成文件名(加上原始文件名的后缀)，防止文件名称重复造成文件覆盖
        String fileName = UUID.randomUUID().toString()+originalFilename.substring(i);

        file.transferTo(new File(basePath+fileName));
        return R.success(fileName);
    }

    /**
     * 文件下载
     */
    @GetMapping("download")
    public R<String> download(String name, HttpServletResponse response) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(basePath+name));
        ServletOutputStream outputStream = response.getOutputStream();

        //设置返回文件类型
        response.setContentType("image/jpeg");

        int len = 0;
        byte[] bytes = new byte[1024];
        while ((len = fileInputStream.read(bytes)) != -1){
            outputStream.write(bytes,0,len);
        }

        fileInputStream.close();
        outputStream.close();
        return null;
    }
}














