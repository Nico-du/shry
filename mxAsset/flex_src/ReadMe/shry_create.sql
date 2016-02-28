

--创建视图 风叶数据
CREATE OR REPLACE VIEW SHRY_FY_VIEW AS
SELECT T1.*,T3.ZSXM AS INPUTUSERNAME,T4.ZSXM AS UPDATEUSERNAME 
FROM SHRY_FY_DATA T1 
LEFT JOIN USERS T3 ON T1.INPUTUSER=T3.ID
LEFT JOIN USERS T4 ON T1.UPDATEUSER=T4.ID 
ORDER BY T1.UPDATEDATE DESC;

--创建视图 总成数据
CREATE OR REPLACE VIEW SHRY_ZC_VIEW AS
SELECT T1.*,T2.XH AS FYXH,T5.XH AS DJXH,
T3.ZSXM AS INPUTUSERNAME,T4.ZSXM AS UPDATEUSERNAME 
FROM SHRY_ZC_DATA T1 
LEFT JOIN SHRY_FY_DATA T2 ON T1.FYID=T2.FYID
LEFT JOIN SHRY_DJ_DATA T5 ON T1.DJID=T5.DJID 
LEFT JOIN USERS T3 ON T1.INPUTUSER=T3.ID
LEFT JOIN USERS T4 ON T1.UPDATEUSER=T4.ID 
ORDER BY T1.UPDATEDATE DESC;

--创建视图 试验单数据
CREATE OR REPLACE VIEW SHRY_SYD_VIEW AS
SELECT T1.*,T2.XH AS SZCXH,T5.XH AS SFYXH,
T3.ZSXM AS INPUTUSERNAME,T4.ZSXM AS UPDATEUSERNAME 
FROM SHRY_SYD_DATA T1 
LEFT JOIN SHRY_ZC_DATA T2 ON T1.ZCID=T2.ZCID
LEFT JOIN SHRY_FY_DATA T5 ON T1.ZCID=T5.FYID 
LEFT JOIN USERS T3 ON T1.INPUTUSER=T3.ID
LEFT JOIN USERS T4 ON T1.UPDATEUSER=T4.ID 
ORDER BY T1.UPDATEDATE DESC;
