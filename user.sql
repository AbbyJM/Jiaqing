DROP TABLE IF EXISTS `USER`;
CREATE TABLE `user`(
   `id` int not null auto_increment,
   `username` varchar(50) default null,
   `password` varchar(100)  default null,
   `email`  varchar(100) default null,
   `active`  tinyint(1) default 1,
   `roles`   varchar(100) default null,
   `avatar`  varchar(100) default null,
    primary key(`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into user (username,password,email,active,roles,avatar)values(
    'abby','$2a$10$wmC5RGMkyYqOw6SRQBNJB.HFT650/qaHn4hKo77I/KOf9Jp5mGfIK','1358890401@qq.com','1','ROLE_SUPERUSER','http://pztxvdloz.bkt.clouddn.com/avatar.jpg'
)




