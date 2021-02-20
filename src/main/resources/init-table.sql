-- 用户信息表
CREATE TABLE IF NOT EXISTS blog_user
(
    user_Id int unsigned primary key NOT NULL AUTO_INCREMENT COMMENT '用户id',
    user_Name varchar(32) NOT NULL COMMENT '用户名',
    user_Password varchar(128) NOT NULL COMMENT '用户密码',
    user_Create_Gmt datetime NOT NULL COMMENT '创建时间',
    key k_user_name (user_Name)
) ENGINE = INNODB char set utf8 COMMENT '用户表';

-- 文章主体信息内容表
CREATE TABLE IF NOT EXISTS user_article
(
    article_Id int unsigned primary key NOT NULL AUTO_INCREMENT COMMENT '文章id',
    article_Title varchar(128) NOT NULL COMMENT '题目',
    article_Catatory varchar(64) DEFAULT '杂项' COMMENT '分类',
    article_Create_Gmt datetime NOT NULL COMMENT '创建时间',
    article_Last_Modify_Gmt datetime NOT NULL COMMENT '上次修改时间',
    article_Introduction varchar(512) NOT NULL COMMENT '简介',
    article_Markdown longtext NOT NULL COMMENT 'Markdown文本',
    article_Image_Url varchar(256) COMMENT '略缩图',
    article_User_Id int unsigned NOT NULL COMMENT '用户id外键',
    constraint fk_user_id_to_article foreign key (article_User_Id) references blog_user(user_Id) on delete cascade on update cascade,
    key k_ca_ti_cg_lm_in (article_Catatory, article_Title, article_Create_Gmt, article_Last_Modify_Gmt, article_Introduction(255))
) ENGINE = INNODB char set utf8 COMMENT '文章主体存储';

-- 一级评论
CREATE TABLE IF NOT EXISTS article_user_comment_lv1
(
    comment_Lv1_Id int unsigned primary Key NOT NULL AUTO_INCREMENT COMMENT '一级评论id',
    comment_Lv1_Content varchar(2048) NOT NULL COMMENT '评论',
    comment_Lv1_Create_Gmt datetime NOT NULL COMMENT '创建时间',
    comment_Lv1_Article_Id int unsigned NOT NULL COMMENT '外键被评论文章id',
    comment_Lv1_User_Id int unsigned NOT NULL COMMENT '外键评论者id',
    constraint fk_user_id_to_comment_lv1 foreign key (comment_Lv1_User_Id) references blog_user(user_Id) on delete cascade on update cascade,
    constraint fk_article_id_to_comment_lv1 foreign key (comment_Lv1_Article_Id) references user_article(article_Id) on delete cascade on update cascade
) ENGINE = INNODB char set utf8 COMMENT '一级评论';

-- 二级评论
CREATE TABLE IF NOT EXISTS comment_lv1_comment_lv2
(
    comment_Lv2_Id int unsigned primary key NOT NULL AUTO_INCREMENT COMMENT '二级评论id',
    comment_Lv2_Content varchar(2048) NOT NULL COMMENT '评论',
    comment_Lv2_Create_Gmt datetime NOT NULL COMMENT '创建时间',
    comment_Lv2_To_lv2_User_Id int unsigned default 0 COMMENT 'lv2内部评论用户id',
    comment_Lv2_Comment_Lv1_Id int unsigned NOT NULL COMMENT '外键对应的一级评论id',
    constraint fk_comment_lv1_id_to_comment_lv2 foreign key (comment_Lv2_Comment_Lv1_Id) references article_user_comment_lv1(comment_Lv1_Id) on delete cascade on update cascade
) ENGINE = INNODB char set utf8 COMMENT '二级评论';
