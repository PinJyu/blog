package cn.nhmt.blog.bo.impl;

import cn.nhmt.blog.bo.exception.FileUploadException;
import cn.nhmt.blog.bo.FileUploadService;
import cn.nhmt.blog.ao.FileAo;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.UUID;

/**
 * @Description:
 * @Date: 2020-04-10 20:44
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Service
@PropertySource({"classpath:upload.properties"})
public class FileUploadServiceImpl implements FileUploadService, InitializingBean, DisposableBean {
    
    @Autowired
    private ServletContext servletContext;

    @Value("${upload.imagebackupdir}")
    private String imageBackupDir;

    @Value("${upload.filebackupdir}")
    private String fileBackupDir;

    private static final String imageBaseUrl = "/blog/images/";

    private static final String fileBaseUrl = "/blog/files/";

    @Value("#{'${upload.imageaccept}'.trim().split('\\s*,\\s*')}")
    private Set<String> imageaccept;

    private String imageBaseDir;

    private String fileBaseDir;

    @Override
    public FileAo uploadWithFileName(MultipartFile multipartFile, String fileName, String suffix) throws FileUploadException {
        String dir, fileFullName, fileUrl;
        try {
            if (StringUtils.hasText(fileName)) {
                boolean isImage = imageaccept.contains(suffix);
                dir = isImage ? imageBaseDir : fileBaseDir;
                fileFullName = StringUtils.hasText(suffix) ? fileName + "." + suffix : fileName;
                fileUrl = isImage ? imageBaseUrl + fileFullName : fileBaseUrl + fileFullName;
                multipartFile.transferTo(new File(dir, fileFullName));
            } else {
                throw new NullPointerException("FileName not have text on file upload.");
            }
        } catch (IOException | RuntimeException e) {
            throw new FileUploadException(e);
        }
        FileAo fnau = new FileAo();
        fnau.setName(fileFullName);
        fnau.setUrl(fileUrl);
        return fnau;
    }

    @Override
    public FileAo uploadWithAnymous(MultipartFile multipartFile) throws FileUploadException {
        String originalFilename, suffix, unionName;
        String[] split = null;
        if ((originalFilename = multipartFile.getOriginalFilename()) != null && originalFilename.contains(".")) {
            split = originalFilename.split("\\.");
        }
        suffix = split != null ? split[split.length - 1] : "";
        unionName = UUID.randomUUID().toString();
        return uploadWithFileName(multipartFile, unionName, suffix);
    }

    @Override
    public void afterPropertiesSet() {
        imageBaseDir = servletContext.getRealPath("WEB-INF/static/images/");
        fileBaseDir = servletContext.getRealPath("WEB-INF/static/files/");
        transferFiles(imageBackupDir, imageBaseDir);
        transferFiles(fileBackupDir, fileBaseDir);
    }

    @Override
    public void destroy() {
        backup();
    }

    @Scheduled(cron = "0 0 4 * * *", zone = "GMT+:08:00")
    void backup() {
        transferFiles(imageBaseDir, imageBackupDir);
        transferFiles(fileBaseDir, fileBackupDir);
    }

    private void transferFiles(String fromDir, String toDir) {
        File from, to;
        if (!(from = new File(fromDir)).mkdirs() && ((to = new File(toDir)).exists() || to.mkdir())) {
            File[] fromFiles;
            if ((fromFiles = from.listFiles()) != null) {
                String toAbsolutePath = to.getAbsolutePath();
                for (File fromFile : fromFiles) {
                    try {
                        Files.copy(fromFile.toPath() , new File(toAbsolutePath, fromFile.getName()).toPath());
                    } catch (IOException ignore) { }
                }
            }
        }
    }

}
