DROP TABLE IF EXISTS `image`;
CREATE TABLE `image`(
    `id` int not null auto_increment,
    `media_id` varchar(255) default null,
    `url` varchar(100) default null,
    `time` varchar(15) default null,
    `name` varchar(255) default null,
    primary key(`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8