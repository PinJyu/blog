package cn.nhmt.blog.controller;

import cn.nhmt.blog.bo.exception.ErrorCode;
import cn.nhmt.blog.bo.exception.FileUploadException;
import cn.nhmt.blog.bo.exception.RestArticleException;
import cn.nhmt.blog.dto.upload.ImageSuccessAndUrlForEditormd;
import cn.nhmt.blog.dto.OkMessage;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Description: global exception controller
 * @Date: 2020-04-03 17:33
 * @Author: PinJyu
 * @Version: 1.0
 **/
@RestControllerAdvice
public class RestAdviceController {

    @ExceptionHandler({RuntimeException.class})
    OkMessage handleGloablRuntimeException(RuntimeException e) {
        e.printStackTrace();
        return new OkMessage(false);
    }

    @ExceptionHandler({RestArticleException.class})
    OkMessage handleArticleException(RestArticleException e) {
        e.getOriginalException().printStackTrace();
        return OkMessage.withErrorCode(e.getErrorCode());
    }

    @ExceptionHandler({FileUploadException.FileUploadInMarkdownImgException.class})
    ImageSuccessAndUrlForEditormd handleInnerFileUploadException(FileUploadException.FileUploadInMarkdownImgException e) {
        e.getOriginalException().printStackTrace();
        ImageSuccessAndUrlForEditormd isaufe = new ImageSuccessAndUrlForEditormd();
        isaufe.setSuccess(0);
        isaufe.setMessage("");
        return isaufe;
    }

    @ExceptionHandler({FileUploadException.class})
    OkMessage handleFileUploadException(FileUploadException e) {
        e.getOriginalException().printStackTrace();
        return OkMessage.withErrorCode(ErrorCode.FileUpload_FAIL);
    }

}
