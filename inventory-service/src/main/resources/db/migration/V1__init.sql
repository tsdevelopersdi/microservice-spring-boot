CREATE table `inventory` (
                          `id` bigint not null auto_increment,
                          `sku_code` varchar(255) not null,
                          `quantity` int(11),
                          primary key (`id`)
);