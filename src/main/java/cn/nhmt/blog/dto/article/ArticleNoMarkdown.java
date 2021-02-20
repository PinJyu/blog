package cn.nhmt.blog.dto.article;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Description: artice introduction
 * @Date: 2020-04-20 16:48
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Data
@NoArgsConstructor
public class ArticleNoMarkdown {

    private int id;
    private String title;
    private String catatory;
    private Date createGmt;
    private Date lastModifyGmt;
    private String introduction;
    private String imageUrl;
    private String userName;

}
