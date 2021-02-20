package cn.nhmt.blog.bo;

import cn.nhmt.blog.bo.exception.ErrorCode;
import cn.nhmt.blog.bo.exception.FileUploadException;
import cn.nhmt.blog.bo.exception.RestArticleException;
import cn.nhmt.blog.bo.service.ArticleService;
import cn.nhmt.blog.bo.service.FileUploadService;
import cn.nhmt.blog.dao.ArticleDao;
import cn.nhmt.blog.dto.OkMessage;
import cn.nhmt.blog.dto.upload.FileNameAndUrl;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: TODO
 * @Date: 2020-04-19 15:57
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Service
public class ImageArticleServiceImpl implements ArticleService.Image {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private FileUploadService fileUploadService;

    @Override
    @Transactional
    public OkMessage handlePostMethod(int id, MultipartFile multipartFile) throws FileUploadException.FileUploadInArticleImgExcepiton {
        FileNameAndUrl fnau;
        try {
            fnau = fileUploadService.uploadWithAnymous(multipartFile);
        } catch (FileUploadException e) {
            throw FileUploadException.forArticleImg(e.getOriginalException());
        }
        try {
            articleDao.postImage(id, fnau.getUrl());
        } catch (RuntimeException e) {
            throw RestArticleException.forPost(ErrorCode.CREATE_IMAGE_FAIL, e);
        }
        OkMessage om = new OkMessage(true);
        om.put("imageUrl", fnau.getUrl());
        return om;
    }

    @Override
    @Transactional
    public OkMessage handlePutMethod(int id, MultipartFile multipartFile) throws FileUploadException.FileUploadInArticleImgExcepiton {
        FileNameAndUrl fnau;
        try {
            fnau = fileUploadService.uploadWithAnymous(multipartFile);
        } catch (FileUploadException e) {
            throw FileUploadException.forArticleImg(e.getOriginalException());
        }
        try {
            articleDao.putImage(id, fnau.getUrl());
        } catch (RuntimeException e) {
            throw RestArticleException.forPut(ErrorCode.UPDATE_IMAGE_FAIL, e);
        }
        OkMessage om = new OkMessage(true);
        om.put("imageUrl", fnau.getUrl());
        return om;
    }

    @Override
    @Transactional
    public OkMessage handleDeleteMethod(int id) {
        try {
            articleDao.deleteImage(id);
        } catch (RuntimeException e) {
            throw RestArticleException.forDelete(ErrorCode.DELETE_IMAGE_FAIL, e);
        }
        return new OkMessage(true);
    }

}
