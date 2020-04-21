package cn.nhmt.blog.bo;

import cn.nhmt.blog.bo.exception.FileUploadException;
import cn.nhmt.blog.bo.service.FileUploadService;
import cn.nhmt.blog.dto.upload.FileNameAndUrl;
import cn.nhmt.blog.dto.OkMessage;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/**
 * @Description: TODO
 * @Date: 2020-04-10 20:44
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Service
public class FileUploadServiceImpl implements FileUploadService, InitializingBean, DisposableBean {
    
    @Autowired
    private ServletContext servletContext;

    private static final String IMAGEACCEPT = ".png, .jpg, .ico";
    private static final String IMAGEBACKUPDIR = "/home/pyu/backupimages/";
    private static final String EXTRABACKUPDIR = "/home/pyu/backupfiles/";
    private static final String IMAGEBASEURL = "/blog/images/";
    private static final String EXTRABASEURL = "/blog/files/";
    private String IMAGEBASEDIR = null;
    private String EXTRABASEDIR = null;

    @Override
    public FileNameAndUrl uploadWithFileName(MultipartFile multipartFile, String fileName, boolean isImage) throws FileUploadException {
        try {
            multipartFile.transferTo(new File(isImage ? IMAGEBASEDIR : EXTRABASEDIR, fileName));
        } catch (IOException e) {
            FileUploadException fue = new FileUploadException();
            fue.setOriginalException(e);
            throw fue;
        }
        FileNameAndUrl fnau = new FileNameAndUrl();
        fnau.setName(fileName);
        fnau.setUrl((isImage ? IMAGEBASEURL : EXTRABASEURL) + fileName);
        return fnau;
    }

    @Override
    public FileNameAndUrl uploadWithAnymous(MultipartFile multipartFile) throws FileUploadException {
        String o, suffix, unionName, fileFullName;
        String[] split = null;
        if ((o = multipartFile.getOriginalFilename()) != null && o.contains(".")) {
            split = o.split("\\.");
        }
        suffix = split != null ? split[split.length - 1] : "";
        unionName = UUID.randomUUID().toString();
        fileFullName = unionName + "." + suffix;
        return uploadWithFileName(multipartFile,  fileFullName, !suffix.equals("") && IMAGEACCEPT.contains(suffix));
    }

    @Override
    public void afterPropertiesSet() {
        IMAGEBASEDIR = servletContext.getRealPath("WEB-INF/static/images/");
        EXTRABASEDIR = servletContext.getRealPath("WEB-INF/static/files/");
        transferFiles(IMAGEBACKUPDIR, IMAGEBASEDIR);
        transferFiles(EXTRABACKUPDIR, EXTRABASEDIR);
    }

    @Override
    public void destroy() {
        transferFiles(IMAGEBASEDIR, IMAGEBACKUPDIR);
        transferFiles(EXTRABASEDIR, EXTRABACKUPDIR);
    }

    private void transferFiles(String fromDir, String toDir) {
        File from, to;
        if (!(from = new File(fromDir)).mkdirs() && ((to = new File(toDir)).exists() || to.mkdir())) {
            File[] fromFiles;
            if ((fromFiles = from.listFiles()) != null) {
                String toAbsolutePath = to.getAbsolutePath();
                for (File fromFile : fromFiles) {
                    try {
                        Files.copy(Path.of(fromFile.getAbsolutePath())
                                , Path.of(toAbsolutePath, fromFile.getName()));
                    } catch (IOException ignore) { }
                }
            }
        }
    }

}
