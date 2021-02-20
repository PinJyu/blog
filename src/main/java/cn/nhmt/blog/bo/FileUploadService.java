package cn.nhmt.blog.bo;

import cn.nhmt.blog.bo.exception.FileUploadException;
import cn.nhmt.blog.ao.FileAo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: TODO
 * @Date: 2020-04-10 20:28
 * @Author: PinJyu
 * @Version: 1.0
 **/
public interface FileUploadService {

    FileAo uploadWithFileName(MultipartFile multipartFile, String fileName, String suffix) throws FileUploadException;

    FileAo uploadWithAnymous(MultipartFile multipartFile) throws FileUploadException;

}
