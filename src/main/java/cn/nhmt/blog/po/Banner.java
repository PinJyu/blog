package cn.nhmt.blog.po;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: banner 地址禁用表
 * @Date: 2020-06-29 14:48
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Data
@NoArgsConstructor
public class Banner implements Serializable {

    private int id;
    private String address;
    private int minutes;
    private Date createGmt;
    private Date lastModifyGmt;

}
