注意：
转储DB到SQL文件后 要 打开SQL文件 将 视图 shry_fy_zs_group_view的创建语句移到末位，否则会执行SQL失败 


CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `shry_fy_zs_group_view` AS select `tt`.`zsid` AS `zsid`,`tt`.`fyid` AS `fyid`,`tt`.`csbz` AS `csbz`,`tt`.`speed` AS `speed`,`tt`.`noise` AS `noise`,`tt`.`memo` AS `memo`,`tt`.`inputdate` AS `inputdate`,`tt`.`inputuser` AS `inputuser`,`tt`.`updatedate` AS `updatedate`,`tt`.`updateuser` AS `updateuser`,`tt`.`FYXH` AS `FYXH`,`tt`.`INPUTUSERNAME` AS `INPUTUSERNAME`,`tt`.`UPDATEUSERNAME` AS `UPDATEUSERNAME` from `shry_fy_zs_view` `TT` group by `tt`.`fyid`;

