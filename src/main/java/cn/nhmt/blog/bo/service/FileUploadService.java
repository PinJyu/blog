package cn.nhmt.blog.bo.service;

import cn.nhmt.blog.bo.exception.FileUploadException;
import cn.nhmt.blog.dto.OkMessage;
import cn.nhmt.blog.dto.upload.FileNameAndUrl;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Description: TODO
 * @Date: 2020-04-10 20:28
 * @Author: PinJyu
 * @Version: 1.0
 **/
public interface FileUploadService {

    FileNameAndUrl uploadWithFileName(MultipartFile multipartFile, String fileName, boolean isImage) throws FileUploadException;

    FileNameAndUrl uploadWithAnymous(MultipartFile multipartFile) throws FileUploadException;

}
