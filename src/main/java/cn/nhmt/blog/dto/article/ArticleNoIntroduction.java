package cn.nhmt.blog.dto.article;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: article detail
 * @Date: 2020-04-20 15:10
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Data
@NoArgsConstructor
public class ArticleNoIntroduction implements Serializable {

    private int id;
    private String title;
    private String catatory;
    private Date createGmt;
    private Date lastModifyGmt;
    private String markdown;
    private String userName;

}
