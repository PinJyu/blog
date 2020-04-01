CREATE TABLE IF NOT EXISTS blog.article(
aid int primary key AUTO_INCREMENT COMMENT '文章主键',
title varchar(128) NOT NULL COMMENT '题目',
catalog varchar(64) COMMENT '分类',
establishtime DATETIME NOT NULL COMMENT '创建时间',
lastmodifytime DATETIME NOT NULL COMMENT '上次修改时间',
introduction varchar(256) NOT NULL COMMENT '简介',
content LONGTEXT NOT NULL COMMENT 'HTML文本',
markdown LONGTEXT NOT NULL COMMENT 'Markdown文本'
) ENGINE = INNODB char set utf8;
