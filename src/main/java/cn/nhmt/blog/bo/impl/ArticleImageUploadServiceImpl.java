package cn.nhmt.blog.bo.impl;

import cn.nhmt.blog.bo.ArticleImageUploadService;
import cn.nhmt.blog.bo.FileUploadService;
import cn.nhmt.blog.bo.exception.FileUploadException;
import cn.nhmt.blog.dao.ArticleDao;
import cn.nhmt.blog.dto.Message;
import cn.nhmt.blog.dto.upload.EditormdImageUploadMessage;
import cn.nhmt.blog.ao.FileAo;
import cn.nhmt.blog.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: article图片上传实现
 * @Date: 2020-05-22 17:59
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Service
public class ArticleImageUploadServiceImpl implements ArticleImageUploadService {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private FileUploadService fileUploadService;

    @Transactional
    @Override
    public Message createCoverImage(int id, MultipartFile multipartFile, User user) {
        FileAo fa;
        fa = fileUploadService.uploadWithAnymous(multipartFile);
        articleDao.updateImage(id, fa.getName(), user.getId());
        return new Message(true).put("url", fa.getUrl());
    }

    @Transactional
    @Override
    public Message DeleteCoverImage(int id, User user) {
        articleDao.deleteImage(id, user.getId());
        return new Message(true).put("id", id);
    }

    @Override
    public EditormdImageUploadMessage createInnerImage(MultipartFile multipartFile) {
        FileAo fuau;
        EditormdImageUploadMessage eium = new EditormdImageUploadMessage();
        try {
            fuau = fileUploadService.uploadWithAnymous(multipartFile);
        } catch (FileUploadException ignore) {
            eium.setSuccess(0);
            eium.setMessage("IO异常");
            return eium;
        }
        eium.setSuccess(1);
        eium.setMessage("");
        eium.setUrl(fuau.getUrl());
        return eium;
    }

}
