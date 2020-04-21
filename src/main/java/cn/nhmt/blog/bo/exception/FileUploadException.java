package cn.nhmt.blog.bo.exception;

/**
 * @Description: TODO
 * @Date: 2020-04-07 16:23
 * @Author: PinJyu
 * @Version: 1.0
 **/
public class FileUploadException extends Exception{

    private Exception originalException;

    public FileUploadException(String messge) {
        super(messge);
    }

    public FileUploadException() { }

    public Exception getOriginalException() {
        return originalException;
    }

    public void setOriginalException(Exception originalException) {
        this.originalException = originalException;
    }

    public static FileUploadInMarkdownImgException forMarkdownImg(Exception originalException) {
        FileUploadInMarkdownImgException fuimie = new FileUploadInMarkdownImgException();
        fuimie.setOriginalException(originalException);
        return fuimie;
    }

    public static FileUploadInArticleImgExcepiton forArticleImg(Exception originalException) {
        FileUploadInArticleImgExcepiton fuiaie = new FileUploadInArticleImgExcepiton();
        fuiaie.setOriginalException(originalException);
        return fuiaie;
    }

    public static class FileUploadInMarkdownImgException extends FileUploadException {

        public FileUploadInMarkdownImgException() {
            super();
        }

    }

    public static class FileUploadInArticleImgExcepiton extends FileUploadException {

        public FileUploadInArticleImgExcepiton() {
            super();
        }

    }

}
