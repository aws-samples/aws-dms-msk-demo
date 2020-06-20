CREATE TABLE `dmstest` (
  `orderId` bigint(20) NOT NULL,
  `source` varchar(45) NOT NULL DEFAULT 'andriod',
  `amount` varchar(45) NOT NULL DEFAULT '0',
  `state` varchar(45) NOT NULL DEFAULT 'New Jersey',
  `date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`orderId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;