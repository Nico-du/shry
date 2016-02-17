--创建唯一编号
CREATE OR REPLACE FUNCTION CNSF_GETGUID RETURN VARCHAR2
AS
BEGIN
RETURN SYS_GUID();         
END CNSF_GETGUID;

-------------------------------------------------------
--日用耗材得到物品总数量
CREATE OR REPLACE FUNCTION CNSF_GETWPZSL 
(WPLX IN VARCHAR2)
RETURN NUMBER
AS
TEMPNUM NUMBER:=0;
BEGIN
  SELECT SUM(SSSL) INTO TEMPNUM FROM CNST_RYHCMX_DATA WHERE GBDM=WPLX;
  IF TEMPNUM IS NULL THEN
    TEMPNUM:=0;
  END IF;
  RETURN TEMPNUM;
END CNSF_GETWPZSL;
--------------------------------------------------------
--日用耗材得到物品已用数量    -- 添加逻辑 申领结束/退库通过后才修改剩余数量
CREATE OR REPLACE FUNCTION CNSF_GETWPYYSL
(WPLX IN VARCHAR2)
RETURN NUMBER
AS
TEMPNUM NUMBER:=0;
TEMPLYNUM NUMBER:=0;
TEMPTKNUM NUMBER:=0;
BEGIN
SELECT SUM(LYDMX.SFSL) INTO TEMPLYNUM FROM CNST_LYDMX_DATA LYDMX INNER JOIN CNST_LYD_DATA LYD ON LYDMX.LYDID=LYD.LYDID AND LYDMX.GBDM=WPLX AND INSTR(LYD.LYZT,'结束') > 0;
  IF TEMPLYNUM IS NULL THEN
    TEMPLYNUM:=0;
  END IF;
  SELECT SUM(TKDMX.SJSL) INTO TEMPTKNUM FROM CNST_TKDMX_DATA TKDMX INNER JOIN CNST_TKD_DATA TKD ON TKDMX.TKDID=TKD.TKDID AND TKDMX.TKDMXTYPE=WPLX AND INSTR(TKD.ZT,'通过') > 0;
  IF TEMPTKNUM IS NULL THEN
    TEMPTKNUM:=0;
  END IF;
  TEMPNUM:=TEMPLYNUM-TEMPTKNUM;
  RETURN TEMPNUM;
END CNSF_GETWPYYSL;

--日用耗材得到批次物品已用数量
CREATE OR REPLACE FUNCTION CNSF_GETWPPCYYSL
(WPLX IN VARCHAR2,WPPCBH IN VARCHAR2)
RETURN NUMBER
AS
TEMPNUM NUMBER:=0;
TEMPLYNUM NUMBER:=0;
TEMPTKNUM NUMBER:=0;
BEGIN
SELECT  SUM(SQSL) INTO TEMPLYNUM FROM CNST_LYDMX_LYSL_DATA WHERE PCBH =WPPCBH AND GBDM=WPLX AND BGLX='领用' AND ZT='1';
  IF TEMPLYNUM IS NULL THEN
    TEMPLYNUM:=0;
  END IF;
  SELECT  SUM(SQSL) INTO TEMPTKNUM FROM CNST_LYDMX_LYSL_DATA WHERE PCBH =WPPCBH AND GBDM=WPLX AND BGLX='退库' AND ZT='1';
  IF TEMPTKNUM IS NULL THEN
    TEMPTKNUM:=0;
  END IF;
  TEMPNUM:=TEMPLYNUM-TEMPTKNUM;
  RETURN TEMPNUM;
END CNSF_GETWPPCYYSL;

--日用耗材得到物品单位
----------------------------------------------------------
CREATE OR REPLACE FUNCTION CNSF_GETWPDW
(WPLX IN VARCHAR2)
RETURN VARCHAR2
AS
TEMPDW VARCHAR2(100);
TEMPDWTYPE VARCHAR2(100);
BEGIN
  SELECT WPDW INTO TEMPDWTYPE FROM CNST_RYHCMX_DATA WHERE GBDM=WPLX AND ROWNUM=1;
  IF TEMPDWTYPE IS NULL THEN
    RETURN TEMPDW;
  END IF;
  SELECT CODENAME INTO TEMPDW FROM CNST_CODELIST_DATA WHERE CODEBS=TEMPDWTYPE AND CODETYPE='WZDW';
  RETURN TEMPDW;
END CNSF_GETWPDW;
----------------------------------------------------------
--物品领用总金额
CREATE OR REPLACE FUNCTION CNSF_GETLYZJE
(VAR_LYDID IN VARCHAR2)
RETURN NUMBER
AS
TEMPNUM NUMBER:=0;
BEGIN
  SELECT SUM(WPJE) INTO TEMPNUM FROM CNST_LYDMX_DATA WHERE LYDID=VAR_LYDID;
  IF TEMPNUM IS NULL THEN
    TEMPNUM:=0;
  END IF;
  RETURN TEMPNUM;
END CNSF_GETLYZJE;
-----------------------------------------------------------
--维修总金额
CREATE OR REPLACE FUNCTION CNSF_GETWXZJE
(VAR_WXDID IN VARCHAR2)
RETURN NUMBER
AS
TEMPNUM NUMBER:=0;
BEGIN
  SELECT SUM(WXJE) INTO TEMPNUM FROM CNST_WXDMX_DATA WHERE WXDID=VAR_WXDID;
  IF TEMPNUM IS NULL THEN
    TEMPNUM:=0;
  END IF;
  RETURN TEMPNUM;
END CNSF_GETWXZJE;
-----------------------------------------------------------
--报废总金额
CREATE OR REPLACE FUNCTION CNSF_GETBFZJE
(VAR_BFDID IN VARCHAR2)
RETURN NUMBER
AS
TEMPNUM NUMBER:=0;
BEGIN
  SELECT SUM(SJHSJE) INTO TEMPNUM FROM CNST_BFDMX_DATA WHERE BFDID=VAR_BFDID;
  IF TEMPNUM IS NULL THEN
    TEMPNUM:=0;
  END IF;
  RETURN TEMPNUM;
END CNSF_GETBFZJE;
-----------------------------------------------------------
--创建物资表
CREATE TABLE CNST_WZXX_DATA(
  WZXXID VARCHAR2(100) primary key,--物资信息ID
  WZXXBH VARCHAR2(200),--物资编号
  WZXXMC VARCHAR2(400) not null,--物资名称
  SSRID NUMBER,--使用人
  SSBMID NUMBER,--使用部门
  WZSL NUMBER not null,--物资数量
  WZDW VARCHAR2(100) not null,--物资单位
  WZZT VARCHAR2(100) not null,--物资状态
  WZLB VARCHAR2(100) not null,--物资类别   
  WZYJLX VARCHAR2(100),--一级类型
  WZEJLX VARCHAR2(100),--二级类型
  USABLE NUMBER(10) not null,--是否可用
  CTIME DATE not null,--创建时间
  CUSER VARCHAR2(100) not null,--创建人
  UTIME DATE not null,--更新时间
  UUSER VARCHAR2(100) not null,--更新人
  MEMO VARCHAR2(1000)--备注     
);
--创建视图
CREATE OR REPLACE VIEW CNSV_WZXX AS
SELECT T1.WZXXID,T1.WZXXBH,T1.WZXXMC,T1.SSRID,T2.ZSXM AS SSRNAME,T1.SSBMID,
T5.MC AS SSBMNAME,T1.WZSL,T1.WZDW,T1.WZZT,T1.WZLB,T1.WZYJLX,T1.WZEJLX,T1.USABLE,T1.CTIME,
T1.CUSER,T3.ZSXM AS CUSERNAME,T1.UTIME,T1.UUSER,T4.ZSXM AS UUSERNAME,T1.MEMO FROM CNST_WZXX_DATA T1
LEFT JOIN USERS T2 ON T1.SSRID=T2.ID
LEFT JOIN USERS T3 ON T1.CUSER=T3.ID
LEFT JOIN USERS T4 ON T1.UUSER=T4.ID
LEFT JOIN DEPT T5 ON T1.SSBMID=T5.ID WHERE T1.USABLE=1 ORDER BY T1.UTIME DESC;

SELECT * FROM CNSV_WZXX;
--添加外键
ALTER TABLE CNST_WZXX_DATA ADD CONSTRAINT CNSF_WZXX_SSRID FOREIGN KEY(SSRID) REFERENCES USERS(ID);
ALTER TABLE CNST_WZXX_DATA ADD CONSTRAINT CNSF_WZXX_SSBMID FOREIGN KEY(SSBMID) REFERENCES DEPT(ID);
--添加数据
insert into CNST_WZXX_DATA(WZXXID,WZXXBH,WZXXMC,SSRID,SSBMID,WZSL,WZDW,WZZT,WZLB,WZYJLX,WZEJLX,USABLE,CTIME,CUSER,UTIME,UUSER)
values(CNSF_GETGUID(),'20141109','铅笔',162,1004,13,'支','入库','日用耗材','ConsumeInfo','水笔',1,sysdate,1,sysdate,1);

select * from t_dictionary where PNAME like'%日用耗材%';
select * from tb_type;
--创建日用耗材表
CREATE TABLE CNST_RYHC_DATA(
  RYHCID VARCHAR2(100) primary key,--日用耗材ID
  RYHCBH VARCHAR2(100) not null,--日用耗材编号
  RYHCNAME VARCHAR2(200) not null,--日用耗材名称
  GLRID NUMBER not null,--管理人ID
  GLBMID NUMBER not null,--管理部门
  USABLE NUMBER(10) not null,--是否使用
  CTIME DATE not null,--创建时间
  CUSER VARCHAR2(100) not null,--创建人
  UTIME DATE not null,--更新时间
  UUSER VARCHAR2(100) not null,--更新人
  MEMO VARCHAR2(1000)--备注     
);
--添加外键
ALTER TABLE CNST_RYHC_DATA ADD CONSTRAINT CNSF_RYHC_GLRID FOREIGN KEY(GLRID) REFERENCES USERS(ID);
ALTER TABLE CNST_RYHC_DATA ADD CONSTRAINT CNSF_RYHC_GLBMID FOREIGN KEY(GLBMID) REFERENCES DEPT(ID);
--创建视图
CREATE OR REPLACE VIEW CNSV_RYHC AS
SELECT TT.*,TT.ZSL-TT.YYSL AS SYSL FROM (
SELECT T1.RYHCID,T1.RYHCBH,T1.RYHCNAME,T1.GLRID,T2.ZSXM AS GLRNAME,T1.GLBMID,T5.MC AS GLBMNAME,
CNSF_GETWPZSL(T1.RYHCBH) AS ZSL, CNSF_GETWPYYSL(T1.RYHCBH) AS YYSL,CNSF_GETWPDW(T1.RYHCBH) AS DW,
T1.USABLE,T1.CTIME,T1.CUSER,T3.ZSXM AS CUSERNAME,T1.UTIME,T1.UUSER,
T4.ZSXM AS UUSERNAME,T1.MEMO 
FROM CNST_RYHC_DATA T1 LEFT JOIN USERS T2 ON T1.GLRID=T2.ID 
LEFT JOIN USERS T3 ON T1.CUSER=T3.ID 
LEFT JOIN USERS T4 ON T1.UUSER=T4.ID 
LEFT JOIN DEPT T5 ON T1.GLBMID=T5.ID WHERE T1.USABLE=1 ORDER BY T1.UTIME DESC) TT;

SELECT * FROM CNSV_RYHC WHERE RYHCBH='QB' AND SYSL>10

select * from cnsv_ryhc;
delete from cnst_ryhc_data;
--入库单
CREATE TABLE CNST_RKD_DATA(
  RKDID VARCHAR2(100) primary key,--入库单ID
  RKDBH VARCHAR2(200) not null,--入库单编号
  JHDW VARCHAR2(200),--交货单位
  FPHM VARCHAR2(100),--发票号码
  RKDD VARCHAR2(100),--入库地点
  RKRQ DATE not null,--入库时间
  USABLE NUMBER(10) not null,--是否使用
  CTIME DATE not null,--创建时间
  CUSER VARCHAR2(100) not null,--创建人
  UTIME DATE not null,--更新时间
  UUSER VARCHAR2(100) not null,--更新人
  MEMO VARCHAR2(1000)--备注     
);
--创建视图
CREATE OR REPLACE VIEW CNSV_RKD AS
SELECT T1.RKDID,T1.RKDBH,T1.JHDW,T1.FPHM,CNSF_GETRKZJE(T1.RKDID) AS ZJJE,T1.RKDD,T1.RKRQ,T1.USABLE,T1.CTIME,T1.CUSER,T2.ZSXM AS CUSERNAME,
T1.UTIME,T1.UUSER,T3.ZSXM AS UUSERNAME,T1.MEMO FROM CNST_RKD_DATA T1 LEFT JOIN USERS T2 ON T1.CUSER=T2.ID 
LEFT JOIN USERS T3 ON T1.UUSER=T3.ID WHERE T1.USABLE=1 ORDER BY T1.UTIME DESC;

SELECT * FROM CNSV_RKD;
delete from CNST_RKD_DATA;

--查询入库单下总金额
CREATE OR REPLACE FUNCTION CNSF_GETRKZJE
(TEMPRKDID IN VARCHAR2) 
RETURN NUMBER
AS
TEMPNUM NUMBER:=0;
BEGIN
   SELECT SUM(WPJE) INTO TEMPNUM FROM CNST_RYHCMX_DATA WHERE RKDID=TEMPRKDID;
   IF TEMPNUM IS NULL THEN
     TEMPNUM:=0;
   END IF;
   RETURN TEMPNUM;
END CNSF_GETRKZJE;

--入库明细
CREATE TABLE CNST_RYHCMX_DATA(
  RYHCMXID VARCHAR2(100) primary key,--ID
  RYHCMXNAME VARCHAR2(200) not null,--名称
  RYHCMXBH VARCHAR2(200),--编号
  RYHCMXTYPE VARCHAR2(200) not null,--类型
  RKDID VARCHAR2(200) not null,--入库单号
  WPPP VARCHAR2(200), --品牌 
  WPXH VARCHAR2(200), --型号
  WPYC VARCHAR2(200),--用处
  WPCS VARCHAR2(200),--厂商         
  JKSL FLOAT not null,--交库数量
  SSSL FLOAT not null,--实收数量
  WPDW VARCHAR2(100),--单位
  WPDJ FLOAT,--单价
  WPJE FLOAT,--金额
  GGSL FLOAT,--规格数量
  GGMC VARCHAR2(100),--规格名称
  USABLE NUMBER(10) not null,--是否使用
  CTIME DATE not null,--创建时间
  CUSER VARCHAR2(100) not null,--创建人
  UTIME DATE not null,--更新时间
  UUSER VARCHAR2(100) not null,--更新人
  MEMO VARCHAR2(1000),--备注
  SYZT VARCHAR2(20), --状态(耗材/库存/固定资产)
  GBDM VARCHAR2(100) --国标代码
);
--添加外键
ALTER TABLE CNST_RYHCMX_DATA ADD CONSTRAINT CNSF_RYHC_RKDID FOREIGN KEY(RKDID) REFERENCES CNST_RKD_DATA(RKDID);
--创建视图
CREATE OR REPLACE VIEW CNSV_RYHCMX AS
SELECT T1.RYHCMXID,T1.RYHCMXNAME,T1.RYHCMXBH,T1.RYHCMXTYPE,T1.RKDID,T2.RKDBH,T1.WPPP,T1.WPXH,T1.WPYC,T1.WPCS,T1.JKSL,T1.SSSL,
T1.FPHM,T1.FKFS,T1.QTFKJE,T1.XJFPJE,T1.ZPHM,T1.WPMC,
cnsf_getwppcyysl(T1.GBDM,T1.RYHCMXBH) YYSL,(T1.SSSL - cnsf_getwppcyysl(T1.GBDM,T1.RYHCMXBH)) SYSL,
T1.WPDW,T1.WPDJ,T1.WPJE,T1.GGSL,T1.GGMC,T1.USABLE,T1.CTIME,T1.CUSER,T3.ZSXM AS CUSERNAME,T1.UTIME,T1.UUSER,T4.ZSXM AS UUSERNAME,
T1.MEMO,T1.SYZT,T1.GBDM FROM CNST_RYHCMX_DATA T1 LEFT JOIN CNST_RKD_DATA T2 ON T1.RKDID=T2.RKDID LEFT JOIN USERS T3 ON T1.CUSER=T3.ID 
LEFT JOIN USERS T4 ON T1.UUSER=T4.ID WHERE T1.USABLE=1 ORDER BY T1.UTIME DESC;

SELECT * FROM CNSV_RYHCMX;
SELECT * FROM CNST_RYHCMX_DATA;
delete from CNST_RYHCMX_DATA;
--领用单
CREATE TABLE CNST_LYD_DATA(
  LYDID VARCHAR2(100) primary key,--领用单ID
  LYDBH VARCHAR2(200) not null,--领用单编号
  SLRID NUMBER not null,--申领人  
  SLBMID NUMBER not null,--申领部门
  SLSJ DATE not null,--申领日期
  LYZJE FLOAT,--领用总金额
  LYZT VARCHAR2(200) not null,--申领状态，初稿，处理中，结束
  LYBZ VARCHAR2(200)not null,--申领步骤，初稿，部门领导，院领导，管理员
  BMLDID NUMBER,--部门领导
  YLDID NUMBER,--院领导
  GLYID NUMBER,--管理员      
  USABLE NUMBER(10) not null,--是否使用
  CTIME DATE not null,--创建时间
  CUSER VARCHAR2(100) not null,--创建人
  UTIME DATE not null,--更新时间
  UUSER VARCHAR2(100) not null,--更新人
  MEMO VARCHAR2(1000)--备注     
);
--创建外键
ALTER TABLE CNST_LYD_DATA ADD CONSTRAINT CNSF_LYD_SLRID FOREIGN KEY(SLRID) REFERENCES USERS(ID);
ALTER TABLE CNST_LYD_DATA ADD CONSTRAINT CNSF_LYD_SLBMID FOREIGN KEY(SLBMID) REFERENCES DEPT(ID);
--创建视图
CREATE OR REPLACE VIEW CNSV_LYD AS
SELECT T1.LYDID,T1.LYDBH,T1.SLRID,T2.ZSXM AS SLRNAME,T1.SLBMID,T3.MC AS SLBMNAME,T1.SLSJ,CNSF_GETLYZJE(T1.LYDID) AS LYZJE,T1.LYZT,
T1.LYBZ,T1.BMLDID,T1.YLDID,T1.GLYID,T1.USABLE,T1.CTIME,T1.CUSER,T4.ZSXM AS CUSERNAME,T1.UTIME,T1.UUSER,
T5.ZSXM AS UUSERNAME,T1.MEMO FROM CNST_LYD_DATA T1 LEFT JOIN USERS T2 ON T1.SLRID=T2.ID LEFT JOIN DEPT T3 ON
T1.SLBMID=T3.ID LEFT JOIN USERS T4 ON T1.CUSER=T4.ID LEFT JOIN USERS T5 ON T1.UUSER=T5.ID WHERE T1.USABLE=1 ORDER BY T1.UTIME DESC;

SELECT * FROM CNSV_LYD;
delete from CNST_LYD_DATA;
SELECT CODEBS AS VALUE,CODENAME AS LABEL  FROM CNST_CODELIST_DATA WHERE 1>0
SELECT ROWNUM AS RN,TMPT.* from CNSV_LYD TMPT where 1>0 and TMPT.lyzt = 'CG' and TMPT.cuser = '1'
--领用单明细
CREATE TABLE CNST_LYDMX_DATA(
  LYDMXID VARCHAR2(100) primary key,--领用明细ID
  LYDMXNAME VARCHAR2(100) not null,--名称
  LYDMXBH VARCHAR2(100),--编号
  LYDMXTYPE VARCHAR2(100) not null,--类型
  LYDID VARCHAR2(100) not null,--领用单号
  WPPP VARCHAR2(200), --品牌 
  WPXH VARCHAR2(200), --型号
  SBSL FLOAT not null,--申领数量
  SHSL FLOAT,--审核数量
  SFSL FLOAT,--实发数量
  WPDW VARCHAR2(100),--单位
  WPDJ FLOAT,--单价
  WPJE FLOAT,--金额
  GGSL FLOAT,--规格数量
  GGMC VARCHAR2(100),--规格名称      
  USABLE NUMBER(10) not null,--是否使用
  CTIME DATE not null,--创建时间
  CUSER VARCHAR2(100) not null,--创建人
  UTIME DATE not null,--更新时间
  UUSER VARCHAR2(100) not null,--更新人
  MEMO VARCHAR2(1000),--备注       
  GBDM VARCHAR2(100),--国标代码     
  PCBH VARCHAR2(100)--批次编号     
  WPMC VARCHAR2(200)--名称  
);
--创建外键
ALTER TABLE CNST_LYDMX_DATA ADD CONSTRAINT CNSF_LYDMX_LYDID FOREIGN KEY(LYDID) REFERENCES CNST_LYD_DATA(LYDID);
--创建视图
CREATE OR REPLACE VIEW CNSV_LYDMX AS
SELECT T1.LYDMXID,T1.LYDMXNAME,T1.LYDMXBH,T1.LYDMXTYPE,T1.LYDID,T2.LYDBH,T2.LYZT,T2.SLSJ,T5.ZSXM AS SLRNAME,T6.MC AS SLBMNAME,T1.WPPP,T1.WPXH,T1.SBSL,T1.SHSL,T1.SFSL,
T1.WPDW,C2.CODENAME WPDWNAME,T1.WPDJ,T1.WPJE,T1.GGSL,T1.GGMC,T1.USABLE,T1.CTIME,T1.CUSER,T3.ZSXM AS CUSERNAME,T1.UTIME,T1.UUSER,T4.ZSXM AS UUSERNAME,
T1.MEMO,T1.GBDM,T1.PCBH,T1.WPMC FROM CNST_LYDMX_DATA T1 LEFT JOIN CNST_LYD_DATA T2 ON T1.LYDID=T2.LYDID LEFT JOIN USERS T3 ON T1.CUSER=T3.ID 
LEFT JOIN USERS T4 ON T1.UUSER=T4.ID LEFT JOIN CNST_CODELIST_DATA C2 ON T1.WPDW=C2.CODEBS
 LEFT JOIN USERS T5 ON T2.SLRID=T5.ID LEFT JOIN DEPT T6 ON T2.SLBMID=T6.ID
 WHERE T1.USABLE=1 ORDER BY T1.UTIME DESC;


SELECT * FROM CNSV_LYDMX;
DELETE FROM CNST_LYDMX_DATA;
--退库单
CREATE TABLE CNST_TKD_DATA(
  TKDID VARCHAR2(100) primary key,--退库单ID
  TKDBH VARCHAR2(200) not null,--退库单编号
  TKRID NUMBER not null,--退库人
  TKBMID NUMBER not null,--退库部门
  TKRQ DATE not null,--退库日期
  TKDZT VARCHAR2(100),
  USABLE NUMBER(10) not null,--是否使用
  CTIME DATE not null,--创建时间
  CUSER VARCHAR2(100) not null,--创建人
  UTIME DATE not null,--更新时间
  UUSER VARCHAR2(100) not null,--更新人
  MEMO VARCHAR2(1000),--备注     
  ZT VARCHAR2(1000)--状态    
);
--创建外键
ALTER TABLE CNST_TKD_DATA ADD CONSTRAINT CNSF_TKD_TKRID FOREIGN KEY(TKRID) REFERENCES USERS(ID);
ALTER TABLE CNST_TKD_DATA ADD CONSTRAINT CNSF_TKD_TKBMID FOREIGN KEY(TKBMID) REFERENCES DEPT(ID);
--创建视图
CREATE OR REPLACE VIEW CNSV_TKD AS 
SELECT T1.TKDID,T1.TKDBH,T1.TKRID,T2.ZSXM AS TKRNAME,T1.TKBMID,T3.MC AS TKBMNAME,T1.TKRQ,T1.USABLE,
T1.CTIME,T1.CUSER,T4.ZSXM AS CUSERNAME,T1.UTIME,T1.UUSER,T5.ZSXM AS UUSERNAME,T1.MEMO,T1.ZT FROM CNST_TKD_DATA T1
LEFT JOIN USERS T2 ON T1.TKRID=T2.ID LEFT JOIN DEPT T3 ON T1.TKBMID=T3.ID 
LEFT JOIN USERS T4 ON T1.CUSER=T4.ID LEFT JOIN USERS T5 ON T1.UUSER=T5.ID WHERE T1.USABLE=1 ORDER BY T1.UTIME DESC;

SELECT * FROM CNSV_TKD;

--退库单明细
CREATE TABLE CNST_TKDMX_DATA(
  TKDMXID VARCHAR2(100) primary key,--退库明细ID
  TKDMXNAME VARCHAR2(200) not null,--名称
  TKDMXBH VARCHAR2(100),--编号
  TKDMXTYPE VARCHAR2(100) not null,--类型
  TKDID VARCHAR2(100) not null,--退库单ID
  WPPP VARCHAR2(200), --品牌 
  WPXH VARCHAR2(200), --型号
  TKSL FLOAT not null,--退库数量
  SHSL FLOAT,--审核数量
  SJSL FLOAT,--实际数量
  WPDW VARCHAR2(100),--单位
  WPDJ FLOAT,--单价
  WPJE FLOAT,--金额
  GGSL FLOAT,--规格数量
  GGMC VARCHAR2(100),--规格名称 
  USABLE NUMBER(10) not null,--是否使用
  CTIME DATE not null,--创建时间
  CUSER VARCHAR2(100) not null,--创建人
  UTIME DATE not null,--更新时间
  UUSER VARCHAR2(100) not null,--更新人
  MEMO VARCHAR2(1000),--备注     
  GBDM VARCHAR2(100), --国标代码
  PCBH VARCHAR2(100)--批次编号  
  WPMC VARCHAR2(200)--名称  
);
--创建外键
ALTER TABLE CNST_TKDMX_DATA ADD CONSTRAINT CNSF_TKDMX_TKDID FOREIGN KEY(TKDID) REFERENCES CNST_TKD_DATA(TKDID);
--创建视图
CREATE OR REPLACE VIEW CNSV_TKDMX AS 
SELECT T1.TKDMXID,T1.TKDMXNAME,T1.TKDMXBH,T1.TKDMXTYPE,T1.TKDID,T2.TKDBH,T2.ZT,T5.ZSXM AS TKRNAME,T6.MC AS TKBMNAME,T1.WPPP,T1.WPXH,T1.TKSL,T1.SHSL,T1.SJSL,
T1.WPDW,C2.CODENAME WPDWNAME,T1.WPDJ,T1.WPJE,T1.GGSL,T1.GGMC,T1.USABLE,T1.CTIME,T1.CUSER,T3.ZSXM AS CUSERNAME,T1.UTIME,T1.UUSER,T4.ZSXM AS UUSERNAME,
T1.MEMO,T1.GBDM,T1.PCBH,T1.WPMC FROM CNST_TKDMX_DATA T1 LEFT JOIN CNST_TKD_DATA T2 ON T1.TKDID=T2.TKDID LEFT JOIN USERS T3 ON T1.CUSER=T3.ID 
LEFT JOIN USERS T4 ON T1.UUSER=T4.ID LEFT JOIN CNST_CODELIST_DATA C2 ON T1.WPDW=C2.CODEBS 
 LEFT JOIN USERS T5 ON T2.TKRID=T5.ID LEFT JOIN DEPT T6 ON T2.TKBMID=T6.ID
WHERE T1.USABLE=1 ORDER BY T1.UTIME DESC;

SELECT * FROM CNSV_TKDMX;
--维修单信息
DROP TABLE CNST_WXD_DATA;
CREATE TABLE CNST_WXD_DATA(
  WXDID VARCHAR2(100) primary key,--维修单ID
  WXDBH VARCHAR2(200) not null,--维修单编号
  SQRID NUMBER not null,--申请人
  SQBMID NUMBER not null,--申请部门
  SQRQ DATE not null,--申请日期
  WXFPBH VARCHAR2(100),--发票编号
  WXZJE FLOAT,--维修总金额
  WXSQZT VARCHAR2(20),--维修申请状态，初稿，处理中，结束
  WXSQBZ VARCHAR2(20),--维修申请步骤，初稿，部门领导，院领导，管理员
  BMLDID NUMBER,--部门领导
  YLDID NUMBER,--院领导
  GLYID NUMBER,--管理员
  USABLE NUMBER(10) not null,--是否使用
  CTIME DATE not null,--创建时间
  CUSER VARCHAR2(100) not null,--创建人
  UTIME DATE not null,--更新时间
  UUSER VARCHAR2(100) not null,--更新人
  MEMO VARCHAR2(1000)--备注     
);
--创建外键
ALTER TABLE CNST_WXD_DATA ADD CONSTRAINT CNSF_WXD_SLRID FOREIGN KEY(SQRID) REFERENCES USERS(ID);
ALTER TABLE CNST_WXD_DATA ADD CONSTRAINT CNSF_WXD_SLBMID FOREIGN KEY(SQBMID) REFERENCES DEPT(ID);
--创建视图
CREATE OR REPLACE VIEW CNSV_WXD AS
SELECT T1.WXDID,T1.WXDBH,T1.SQRID,T2.ZSXM AS SQRNAME,T1.SQBMID,T3.MC AS SQBMNAME,T1.SQRQ,T1.WXFPBH,CNSF_GETWXZJE(T1.WXDID) AS WXZJE,
T1.WXSQZT,T1.WXSQBZ,T1.BMLDID,T1.YLDID,T1.GLYID,T1.USABLE,T1.CTIME,T1.CUSER,T4.ZSXM AS CUSERNAME,T1.UTIME,T1.UUSER,
T5.ZSXM AS UUSERNAME,T1.MEMO FROM CNST_WXD_DATA T1 LEFT JOIN USERS T2 ON T1.SQRID=T2.ID LEFT JOIN DEPT T3 ON T1.SQBMID=T3.ID 
LEFT JOIN USERS T4 ON T1.CUSER=T4.ID LEFT JOIN USERS T5 ON T1.UUSER=T5.ID WHERE T1.USABLE=1 ORDER BY T1.UTIME DESC;

SELECT * FROM CNSV_WXD;
--维修单明细
DROP TABLE CNST_WXDMX_DATA;
CREATE TABLE CNST_WXDMX_DATA(
  WXDMXID VARCHAR2(100) primary key,--维修明细ID
  WXDMXNAME VARCHAR2(200) not null,--名称
  WXDMXBH VARCHAR2(100) not null,--编号
  WXDMXTYPE VARCHAR2(100) not null,--类型
  WXDID VARCHAR2(100) not null,--维修单ID
  WXFZR VARCHAR2(100),--维修负责人
  WXDW VARCHAR2(100),--维修单位
  LXFS VARCHAR2(200),--联系方式
  SQWXSL FLOAT not null,--维修申请数量
  SHWXSL FLOAT ,--维修审核数量
  SJWXSL FLOAT ,--实际维修数量
  WXJE FLOAT not null,--维修金额 
  USABLE NUMBER(10) not null,--是否使用
  CTIME DATE not null,--创建时间
  CUSER VARCHAR2(100) not null,--创建人
  UTIME DATE not null,--更新时间
  UUSER VARCHAR2(100) not null,--更新人
  MEMO VARCHAR2(1000),--备注     
  ASSETTABLENAME VARCHAR2(100),--资产表名
  ASSETID VARCHAR2(100),--资产ID
  ASSETIDCOLUMN VARCHAR2(100),--资产ID列名
  ASSETBH VARCHAR2(100),--资产编号
  PCBH VARCHAR2(100)--批次编号
  WPMC VARCHAR2(200)--名称  
);
--创建外键
ALTER TABLE CNST_WXDMX_DATA ADD CONSTRAINT CNSF_WXDMX_WXDID FOREIGN KEY(WXDID) REFERENCES CNST_WXD_DATA(WXDID);
--创建视图
CREATE OR REPLACE VIEW CNSV_WXDMX AS
SELECT T1.WXDMXID,T1.WXDMXNAME,T1.WXDMXBH,T1.WXDMXTYPE,T1.WXDID,T2.WXDBH,T1.WXFZR,T1.WXDW,T1.LXFS,T1.SQWXSL,T1.SHWXSL,
T1.SJWXSL,T1.WXJE,T1.USABLE,T1.CTIME,T1.CUSER,T3.ZSXM AS CUSRNAME,T1.UTIME,T1.UUSER,T4.ZSXM AS UUSERNAME,T1.MEMO,
T1.ASSETTABLENAME,T1.ASSETID,T1.ASSETIDCOLUMN,T1.ASSETBH,T1.PCBH,T1.WPMC
FROM CNST_WXDMX_DATA T1 LEFT JOIN CNST_WXD_DATA T2 ON T1.WXDID=T2.WXDID LEFT JOIN USERS T3 ON T1.CUSER=T3.ID 
LEFT JOIN USERS T4 ON T1.UUSER=T4.ID WHERE T1.USABLE=1 ORDER BY T1.UTIME DESC;

SELECT * FROM CNSV_WXDMX;
--报废单信息
CREATE TABLE CNST_BFD_DATA(
  BFDID VARCHAR2(100) primary key,--报废单ID
  BFDBH VARCHAR2(200) not null,--报废单编号
  DJRID NUMBER not null,--登记人
  DJBMID NUMBER not null,--登记部门
  DJRQ DATE not null,--登记日期
  BFZJE FLOAT,--报废总金额
  BFSQZT VARCHAR2(20),--报废状态，初稿，处理中，结束
  BFSQBZ VARCHAR2(20),--报废申请步骤，初稿，部门领导，院领导，管理员
  BMLDID NUMBER,--部门领导
  YLDID NUMBER,--院领导
  GLYID NUMBER,--管理员
  USABLE NUMBER(10) not null,--是否使用
  CTIME DATE not null,--创建时间
  CUSER VARCHAR2(100) not null,--创建人
  UTIME DATE not null,--更新时间
  UUSER VARCHAR2(100) not null,--更新人
  MEMO VARCHAR2(1000)--备注     
);
--创建外键
ALTER TABLE CNST_BFD_DATA ADD CONSTRAINT CNSF_BFD_DJRID FOREIGN KEY(DJRID) REFERENCES USERS(ID);
ALTER TABLE CNST_BFD_DATA ADD CONSTRAINT CNSF_BFD_DJBMID FOREIGN KEY(DJBMID) REFERENCES DEPT(ID);
--创建视图
CREATE OR REPLACE VIEW CNSV_BFD AS
SELECT T1.BFDID,T1.BFDBH,T1.DJRID,T2.ZSXM AS DJRNAME,T1.DJBMID,T3.MC AS DJBMNAME,T1.DJRQ,CNSF_GETBFZJE(T1.BFDID) AS BFZJE,T1.BFSQZT,T1.BFSQBZ,
T1.BMLDID,T1.YLDID,T1.GLYID,T1.USABLE,T1.CTIME,T1.CUSER,T4.ZSXM AS CUSERNAME,T1.UTIME,T1.UUSER,T5.ZSXM AS UUSERNAME,T1.MEMO 
FROM CNST_BFD_DATA T1 LEFT JOIN USERS T2 ON T1.DJRID=T2.ID LEFT JOIN DEPT T3 ON T1.DJBMID=T3.ID LEFT JOIN USERS T4 
ON T1.CUSER=T4.ID LEFT JOIN USERS T5 ON T1.UUSER=T5.ID WHERE T1.USABLE=1 ORDER BY T1.UTIME DESC;

SELECT * FROM CNSV_BFD;
--报废单明细
DROP TABLE CNST_BFDMX_DATA;
CREATE TABLE CNST_BFDMX_DATA(
  BFDMXID VARCHAR2(100) primary key,--报废明细ID
  BFDMXNAME VARCHAR2(200) not null,--名称
  BFDMXBH VARCHAR2(100) not null,--编号
  BFDMXTYPE VARCHAR2(100) not null,--类型
  BFDID VARCHAR2(100) not null,--报废单ID
  HSFZR VARCHAR2(100),--回收负责人
  HSDW VARCHAR2(100),--回收单位
  HSRLX VARCHAR2(200),--回收人联系方式
  SQBFSL FLOAT not null,--报废申请数量
  SHBFSL FLOAT not null,--报废审核数量
  SJBFSL FLOAT not null,--实际报废数量
  YGHSJE FLOAT,--预估回收金额
  SJHSJE FLOAT,--实际回收金额
  USABLE NUMBER(10) not null,--是否使用
  CTIME DATE not null,--创建时间
  CUSER VARCHAR2(100) not null,--创建人
  UTIME DATE not null,--更新时间
  UUSER VARCHAR2(100) not null,--更新人
  MEMO VARCHAR2(1000),--备注     
  ASSETTABLENAME VARCHAR2(100),--资产表名
  ASSETID VARCHAR2(100),--资产ID
  ASSETIDCOLUMN VARCHAR2(100),--资产ID列名
  ASSETBH VARCHAR2(100),--资产编号
  PCBH VARCHAR2(100)--批次编号
  WPMC VARCHAR2(200)--名称  
);
--创建外键
ALTER TABLE CNST_BFDMX_DATA ADD CONSTRAINT CNSF_BFDMX_BFDID FOREIGN KEY(BFDID) REFERENCES CNST_BFD_DATA(BFDID);
--创建视图
CREATE OR REPLACE VIEW CNSV_BFDMX AS
SELECT T1.BFDMXID,T1.BFDMXNAME,T1.BFDMXBH,T1.BFDMXTYPE,T1.BFDID,T2.BFDBH,T1.HSFZR,T1.HSDW,T1.HSRLX,T1.SQBFSL,T1.SHBFSL,
T1.SJBFSL,T1.YGHSJE,T1.SJHSJE,T1.USABLE,T1.CTIME,T1.CUSER,T3.ZSXM AS CUSERNAME,T1.UTIME,T1.UUSER,T4.ZSXM AS UUSERNAME,
T1.MEMO,T1.ASSETTABLENAME,T1.ASSETID,T1.ASSETIDCOLUMN,T1.ASSETBH,T1.PCBH,T1.WPMC 
FROM CNST_BFDMX_DATA T1 LEFT JOIN CNST_BFD_DATA T2 ON T1.BFDID=T2.BFDID LEFT JOIN USERS T3 ON T1.CUSER=T3.ID 
LEFT JOIN USERS T4 ON T1.UUSER=T4.ID WHERE T1.USABLE=1 ORDER BY T1.UTIME DESC;

SELECT * FROM CNSV_BFDMX;

---- 日常服务  相关资料的 左边 目录
--创建文件目录表   
CREATE TABLE CNST_DIRECTORY_DATA(
  DIRECTORYID VARCHAR2(100) primary key,--目录ID
  DIRECTORYNAME VARCHAR2(100) not null,--目录名称
  LASTDIRECTORYID VARCHAR2(100),--上级目录ID
  DIRECTORYPATH VARCHAR2(1000),--目录路径
  USABLE NUMBER(10) not null,--是否使用
  CTIME DATE not null,--创建时间
  CUSER VARCHAR2(100) not null,--创建人
  UTIME DATE not null,--更新时间
  UUSER VARCHAR2(100) not null,--更新人
  MEMO VARCHAR2(1000),--备注
  constraint CNSUK_DIRECTORY_DIRECTORYNAME unique(DIRECTORYNAME)         
);
--创建资料表   日常服务  相关资料表
CREATE TABLE CNST_DATUM_DATA(
  DATUMID VARCHAR2(100) primary key,--资料ID
  DATUMNAME VARCHAR2(200) not null,--资料名称
  DATUMNAMEEX VARCHAR2(200),--资料别名
  DIRECTORYID VARCHAR2(100) not null,--所在目录
  FILESIZE INTEGER,--资料大小
  FILEPATH VARCHAR2(500) not null,--资料路径
  USABLE NUMBER(10) not null,--是否使用
  CTIME DATE not null,--创建时间
  CUSER VARCHAR2(100) not null,--创建人
  UTIME DATE not null,--更新时间
  UUSER VARCHAR2(100) not null,--更新人
  MEMO VARCHAR2(1000)--备注     
);
--外键
ALTER TABLE CNST_DATUM_DATA ADD CONSTRAINT CNSF_DATUM_DIRECTORYID FOREIGN KEY(DIRECTORYID) 
REFERENCES CNST_DIRECTORY_DATA(DIRECTORYID);
--资料目录
SELECT rowid,t.* FROM CNST_DIRECTORY_DATA t;
delete from CNST_DIRECTORY_DATA;
--资料
SELECT * FROM CNST_DATUM_DATA;

----代码表
DROP TABLE CNST_CODELIST_DATA;
CREATE TABLE CNST_CODELIST_DATA(
  CODEID VARCHAR2(100) primary key,--主键
  CODETYPE VARCHAR2(100) not null,--代码表类型
  CODEBS VARCHAR2(100) not null,--标识
  CODENAME VARCHAR2(100) not null,--名称
  CODEMEMO VARCHAR2(1000)--备注
);
--创建视图
CREATE OR REPLACE VIEW CNSV_CODELIST AS
SELECT T1.CODEID,T1.CODETYPE,T2.CODENAME AS CODETYPENAME,T1.CODEBS,T1.CODENAME,T1.CODEMEMO FROM CNST_CODELIST_DATA T1
LEFT JOIN CNST_CODELIST_DATA T2 ON T1.CODETYPE=T2.CODEBS ORDER BY T1.Codememo ASC;

INSERT INTO CNST_CODELIST_DATA(CODEID,CODETYPE,CODEBS,CODENAME)
VALUES(CNSF_GETGUID(),'UNKNOW','DMB','代码表');
INSERT INTO CNST_CODELIST_DATA(CODEID,CODETYPE,CODEBS,CODENAME)
VALUES(CNSF_GETGUID(),'DMB','RYHC','日用耗材');

select *  from CNSV_CODELIST;
select * from users;
select * from tb_type;
--插入代码表数据
INSERT INTO CNST_CODELIST_DATA(CODEID,CODETYPE,CODEBS,CODENAME) 
select CNSF_GETGUID() as CODEID,'RYHC' as codetype,REMARKS AS CODEBS,name as codename from t_dictionary where pid='3379';

select rowid,t.* from t_dictionary t where t.pid='3379';

select * from users;
--菜单
select * from menus;
select * from users_menus;
--工作流步骤定义表
CREATE TABLE CNST_WFSTEP_DATA(
 WFSTEPID VARCHAR2(100) PRIMARY KEY,--标识
 WFSTEPNAME VARCHAR2(200) NOT NULL,--步骤名称
 WFSTEPTYPE VARCHAR2(100) NOT NULL,--所属类型，代码表控制，如领用申请:LYSQ
 STEPBTNNAME VARCHAR2(100) NOT NULL,--按钮名称
 STEPORDER NUMBER NOT NULL,--步骤顺序
 NEXTSTEP VARCHAR2(4000),--下一步操作
 DEFAULTUSER VARCHAR2(100),--默认操作人
 USABLE NUMBER(10) not null,--是否使用
 CTIME DATE not null,--创建时间
 CUSER VARCHAR2(100) not null,--创建人
 UTIME DATE not null,--更新时间
 UUSER VARCHAR2(100) not null,--更新人
 MEMO VARCHAR2(1000)--备注 
)
--创建试图
CREATE OR REPLACE VIEW CNSV_WFSTEP AS
SELECT T1.WFSTEPID,T1.WFSTEPNAME,T1.WFSTEPTYPE,T1.STEPBTNNAME,T1.STEPORDER,T1.NEXTSTEP,T1.DEFAULTUSER,T2.ZSXM AS DEFAULTUSERNAME,T1.USABLE,
T1.CTIME,T1.CUSER,T3.ZSXM AS CUSERNAME,T1.UTIME,T1.UUSER,T4.ZSXM AS UUSERNAME,T1.MEMO FROM CNST_WFSTEP_DATA T1 LEFT JOIN USERS T2 ON T1.DEFAULTUSER=T2.ID 
LEFT JOIN USERS T3 ON T1.CUSER=T3.ID LEFT JOIN USERS T4 ON T1.UUSER=T4.ID WHERE T1.USABLE=1 ORDER BY STEPORDER ASC;
-------------------------------------------------------------------------------------------------------------------------------------
INSERT INTO CNST_WFSTEP_DATA(WFSTEPID,WFSTEPNAME,WFSTEPTYPE,STEPBTNNAME,STEPORDER,NEXTSTEP,DEFAULTUSER,USABLE,CTIME,CUSER,UTIME,UUSER)
VALUES(CNSF_GETGUID(),'开始','BFSQ','提交',0,'oo',1,1,SYSDATE,1,SYSDATE,1);
-------------------------------------------------------------------------------------------------------------------------------------
SELECT rowid,t.* FROM CNST_WFSTEP_DATA t;
SELECT * FROM CNSV_WFSTEP;
SELECT * FROM CNSV_WFSTEP A1 WHERE A1.WFSTEPTYPE='LYSQ';
--工作流历史操作步骤
CREATE TABLE CNST_WFHISTORY_DATA(
 WFHISTORYID VARCHAR2(100) PRIMARY KEY,--主键
 WFHISTORYNAME VARCHAR2(200) NOT NULL,--操作历史步骤名称
 WFHSTEPID VARCHAR2(100) NOT NULL,--操作步骤ID
 WFDATAID VARCHAR2(100) NOT NULL,--数据ID
 WFDOUSERID VARCHAR2(100) NOT NULL,--操作人
 WFDOCONTENT VARCHAR2(2000),--操作内容
 USABLE NUMBER(10) not null,--是否使用
 CTIME DATE not null,--创建时间
 CUSER VARCHAR2(100) not null,--创建人
 UTIME DATE not null,--更新时间
 UUSER VARCHAR2(100) not null,--更新人
 MEMO VARCHAR2(1000)--备注 
)
--创建视图
CREATE OR REPLACE VIEW CNSV_WFHISTORY AS
SELECT T1.WFHISTORYID,T1.WFHISTORYNAME,T1.WFHSTEPID,T1.WFDATAID,T1.WFDOUSERID,T2.ZSXM AS WFDOUSERNAME,T1.WFDOCONTENT,
T1.USABLE,T1.CTIME,T1.CUSER,T3.ZSXM AS CUSERNAME,T1.UTIME,T1.UUSER,T4.ZSXM AS UUSERNAME,T1.MEMO 
FROM CNST_WFHISTORY_DATA T1 LEFT JOIN USERS T2 ON T1.WFDOUSERID=T2.ID 
LEFT JOIN USERS T3 ON T1.CUSER=T3.ID LEFT JOIN USERS T4 ON T1.UUSER=T4.ID WHERE T1.USABLE=1 ORDER BY UTIME DESC;

SELECT * FROM CNSV_WFHISTORY;
DELETE FROM CNST_WFHISTORY_DATA;

--系统提醒
CREATE TABLE CNST_NOTICE 
(
  NTID VARCHAR2(100) NOT NULL --主键 
, WFID VARCHAR2(100) --流程单表ID/耗材ID
, NTTYPE VARCHAR2(20) --提醒类型
, ISREAD VARCHAR2(1) DEFAULT 1 --是否已阅
, CTIME DATE --创建时间
, CUSER VARCHAR2(20) --创建人
, UTIME DATE --更新人
, UUSER VARCHAR2(20) --更新时间
, TZRID VARCHAR2(100) --被通知人ID
, CONSTRAINT CNST_NOTICE_PK PRIMARY KEY 
  (
    NTID 
  )
  ENABLE 
);
--创建视图
CREATE OR REPLACE VIEW CNSV_NOTICE AS
SELECT T1.NTID,T1.WFID,T1.NTTYPE,T1.ISREAD,T1.CTIME,T1.TZRID,T2.ZSXM AS TZRNAME, 
T1.CUSER,T3.ZSXM AS CUSERNAME,T1.UTIME,T1.UUSER,T4.ZSXM AS UUSERNAME 
FROM CNST_NOTICE T1 LEFT JOIN USERS T2 ON T1.TZRID=T2.ID 
LEFT JOIN USERS T3 ON T1.CUSER=T3.ID LEFT JOIN USERS T4 ON T1.UUSER=T4.ID WHERE T1.ISREAD=1 ORDER BY UTIME DESC
--任务提醒查询SQL

--耗材数量提醒查询SQL


--创建视图,固定资产国标代码+耗材代码 视图
CREATE OR REPLACE VIEW CNSV_ALLDICTIONARY AS 
SELECT DICID AS ID,DICID,DICNAME,PARENTID,PARENTNAME,ROOTID,ROOTNAME FROM CNST_DICTIONARY_DATA 
UNION ALL 
SELECT CODEID AS ID,CODEBS AS DICID,CODENAME AS DICNAME,'3282C2A6C0574CB2811D1B5EF67D2305' as parentid,'日用耗材' as parentname,'3282C2A6C0574CB2811D1B5EF67D2305' as rootid,'日用耗材' as rootname from cnst_codelist_data where codetype='RYHC';

--创建FUNCTION 检测是否有子节点 ,生成国标树时分多次加载,检测是否有子节点
CREATE OR REPLACE FUNCTION CNSF_HASCHILDREN
(INDICID IN VARCHAR2)
RETURN VARCHAR2
AS
RETURNVAL VARCHAR2(2):='N';
CCOUNT INT:=0;
BEGIN
  SELECT COUNT(0) INTO CCOUNT FROM CNST_DICTIONARY_DATA DIC WHERE DIC.PARENTID = INDICID;
  IF CCOUNT>0 THEN
    RETURNVAL := 'Y';
  END IF;
  RETURN RETURNVAL;
END CNSF_HASCHILDREN;

--创建视图  加入"是否有子节点" 字段
CREATE OR REPLACE VIEW CNSV_DICTIONARY 
AS 
SELECT DICID AS ID,DICID,DICNAME,PARENTID,PARENTNAME,ROOTID,ROOTNAME,CNSF_HASCHILDREN(DICID) AS HASCHILDREN FROM CNST_DICTIONARY_DATA;


-------------------------------------------------------
--创建视图
--领用/退库记录 信息
CREATE OR REPLACE VIEW CNSV_LYDMX_LYSL AS 
SELECT LYDMXSL.SQDMXID SQDMXID,LYDMXSL.SQDMXBH,LYDMXSL.SQSL LYSL,LYDMXSL.BGLX BGLX,LYDMXSL.ZT ZT,LYDMXSL.BGRQ BGRQ,LYDMXSL.SQR SQR,LYDMXSL.SQBM SQBM,
LYDMXSL.RYHCMXID RYHCMXID,LYDMXSL.PCBH RYHCMXBH,LYDMXSL.WPMC RYHCMXNAME,LYDMXSL.GBDM GBDM,RYHCMX.WPPP,RYHCMX.WPXH,RYHCMX.JKSL,RYHCMX.SSSL,RYHCMX.WPDW,RYHCMX.WPDJ
,CNSF_GETWPPCYYSL(LYDMXSL.GBDM,LYDMXSL.PCBH) YYSL,(RYHCMX.SSSL - CNSF_GETWPPCYYSL(LYDMXSL.GBDM,LYDMXSL.PCBH)) SYSL
FROM  CNST_LYDMX_LYSL_DATA  LYDMXSL INNER JOIN  CNST_RYHCMX_DATA  RYHCMX ON LYDMXSL.RYHCMXID =  RYHCMX.RYHCMXID 

--创建视图
--报表  部门季度耗材领用情况视图
CREATE OR REPLACE VIEW CNSV_REPORT_BMJDLYZK AS 
SELECT SUM(WPJE) WPZJE,SUM(SFSL) SFZSL,SLBMNAME,COUNT(1) COUNTCS,TO_CHAR(SLSJ,'YYYY-Q') SLJD FROM CNSV_LYDMX 
GROUP BY SLBMNAME,TO_CHAR(SLSJ,'YYYY-Q') ORDER BY SLBMNAME,SLJD;

---SQL语句 查询所有部门某年每个季度使用情况
SELECT T1.ID DEPTID,T1.MC DEPTNAME,T1.SHOW_ORDER SHOWORDER,
T2.WPZJE FIRWPZJE,T2.SFZSL FIRSFZSL,T2.COUNTCS FIRCOUNTCS,
T3.WPZJE SECWPZJE,T3.SFZSL SECSFZSL,T3.COUNTCS SECCOUNTCS,
T4.WPZJE THIRWPZJE,T4.SFZSL THIRSFZSL,T4.COUNTCS THIRCOUNTCS,
T5.WPZJE FOURWPZJE,T5.SFZSL FOURSFZSL,T5.COUNTCS FOURCOUNTCS
FROM DEPT T1 
LEFT JOIN CNSV_REPORT_BMJDLYZK T2 ON  T1.MC=T2.SLBMNAME AND T2.SLJD='2015-1'
LEFT JOIN CNSV_REPORT_BMJDLYZK T3 ON  T1.MC=T3.SLBMNAME AND T3.SLJD='2015-2'
LEFT JOIN CNSV_REPORT_BMJDLYZK T4 ON  T1.MC=T4.SLBMNAME AND T4.SLJD='2015-3'
LEFT JOIN CNSV_REPORT_BMJDLYZK T5 ON  T1.MC=T5.SLBMNAME AND T5.SLJD='2015-4'
ORDER BY SHOWORDER



--///合同管理

--创建视图
--合同登记
CREATE OR REPLACE VIEW CNSV_COMPACT AS 
SELECT T1.COMPACTID ,T1.COMPACTNAME,T1.HTBH,T1.CPTYPE,T1.SIGNDATE,T1.VALIDITYDATE,T1.HTWCRQ,
T1.DFDW ,T1.HTZJE ,T1.HTNR ,T1.MEMO,
T1.JBR AS JBRID,T2.ZSXM AS JBRNAME,T1.ASSCOMPACTID,T5.COMPACTNAME AS GLHTMC,
T1.CTIME,T1.CUSER,T3.ZSXM AS CUSERNAME,T1.UTIME,T1.UUSER,T4.ZSXM AS UUSERNAME
FROM  CNST_COMPACT_DATA T1 LEFT JOIN USERS T2 ON T1.JBR=T2.ID 
LEFT JOIN USERS T3 ON T1.CUSER=T3.ID LEFT JOIN USERS T4 ON T1.UUSER=T4.ID
LEFT JOIN CNST_COMPACT_DATA T5 ON T1.ASSCOMPACTID=T5.COMPACTID
ORDER BY UTIME DESC;

--创建视图
--付款登记
CREATE OR REPLACE VIEW CNSV_CPFKDJ AS 
SELECT T1.FKDJID ,T1.FKBH,T1.FKSJ,T1.FKJE,T1.FPH,T1.HTWCJD,T1.BQLXQK,T1.MEMO,
CNSF_GETLJFKJE(T1.COMPACTID) AS LJFKJE,CNSF_GETSYWFKJE(T1.COMPACTID) AS SYWFKJE,CNSF_GETLJFKBL(T1.COMPACTID) AS LJFKBL,
T1.JBR AS JBRID,T2.ZSXM AS JBRNAME,T1.COMPACTID,T5.COMPACTNAME AS COMPACTNAME,
T1.CTIME,T1.CUSER,T3.ZSXM AS CUSERNAME,T1.UTIME,T1.UUSER,T4.ZSXM AS UUSERNAME
FROM  CNST_CPFKDJ_DATA T1 LEFT JOIN USERS T2 ON T1.JBR=T2.ID 
LEFT JOIN USERS T3 ON T1.CUSER=T3.ID LEFT JOIN USERS T4 ON T1.UUSER=T4.ID
LEFT JOIN CNST_COMPACT_DATA T5 ON T1.COMPACTID=T5.COMPACTID
ORDER BY UTIME DESC;

--创建视图
--维护登记
CREATE OR REPLACE VIEW CNSV_CPWHDJ AS 
SELECT T1.WHDJID ,T1.WHBH,T1.WHR,T1.WHSJ,T1.LXFS,T1.WHNR,T1.WHLX,T1.MEMO,
T1.COMPACTID,T5.COMPACTNAME AS COMPACTNAME,
T1.CTIME,T1.CUSER,T3.ZSXM AS CUSERNAME,T1.UTIME,T1.UUSER,T4.ZSXM AS UUSERNAME
FROM  CNST_CPWHDJ_DATA T1 
LEFT JOIN USERS T3 ON T1.CUSER=T3.ID LEFT JOIN USERS T4 ON T1.UUSER=T4.ID
LEFT JOIN CNST_COMPACT_DATA T5 ON T1.COMPACTID=T5.COMPACTID
ORDER BY UTIME DESC;

--创建方法
--获取累计付款金额
CREATE OR REPLACE FUNCTION CNSF_GETLJFKJE
(VAR_COMPACTID IN VARCHAR2)
RETURN NUMBER
AS
TEMPNUM NUMBER:=0;
BEGIN
  SELECT SUM(FKJE) INTO TEMPNUM FROM CNST_CPFKDJ_DATA WHERE COMPACTID=VAR_COMPACTID;
  IF TEMPNUM IS NULL THEN
    TEMPNUM:=0;
  END IF;
  RETURN TEMPNUM;
END CNSF_GETLJFKJE;

--创建方法
--剩余未付款金额
CREATE OR REPLACE FUNCTION CNSF_GETSYWFKJE
(VAR_COMPACTID IN VARCHAR2)
RETURN NUMBER
AS
TEMPHTNUM NUMBER:=0;
TEMPNUM NUMBER:=0;
BEGIN
  SELECT SUM(FKJE) INTO TEMPNUM FROM CNST_CPFKDJ_DATA WHERE COMPACTID=VAR_COMPACTID;
  IF TEMPNUM IS NULL THEN
    TEMPNUM:=0;
  END IF;
  SELECT HTZJE INTO TEMPHTNUM FROM CNST_COMPACT_DATA WHERE COMPACTID=VAR_COMPACTID;
  IF TEMPHTNUM IS NULL THEN
    TEMPHTNUM:=0;
  END IF;
  IF TEMPHTNUM < TEMPNUM  THEN
   RETURN 0;
  END IF;
  RETURN TEMPHTNUM-TEMPNUM;
END CNSF_GETSYWFKJE;

--创建方法
--累计付款比例 百分比
CREATE OR REPLACE FUNCTION CNSF_GETLJFKBL
(VAR_COMPACTID IN VARCHAR2)
RETURN NUMBER
AS
TEMPYFKNUM NUMBER:=0;
TEMPWFKNUM NUMBER:=0;
BEGIN
  SELECT CNSF_GETLJFKJE(VAR_COMPACTID) INTO TEMPYFKNUM FROM DUAL;
  IF TEMPYFKNUM = 0  THEN
   RETURN 0;
  END IF;
   SELECT CNSF_GETSYWFKJE(VAR_COMPACTID) INTO TEMPWFKNUM FROM DUAL;
  IF TEMPWFKNUM = 0  THEN
   RETURN 100;
  END IF;
  RETURN (TEMPYFKNUM*100)/(TEMPWFKNUM+TEMPYFKNUM);
END CNSF_GETLJFKBL;


