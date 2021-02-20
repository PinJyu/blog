package cn.nhmt.blog.dto;

/**
 * @Description: TODO
 * @Date: 2020-04-22 15:05
 * @Author: PinJyu
 * @Version: 1.0
 **/
public interface Po2Dto<P, D> {

    D transfer(P po);

}
