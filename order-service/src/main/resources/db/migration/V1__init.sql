CREATE table `orders` (
  `id` bigint not null auto_increment,
  `order_number` varchar(255) not null,
  `sku_code` varchar(255) not null,
  `price` decimal(19,2),
  `quantity` int(11),
  primary key (`id`)
);