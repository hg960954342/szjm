-- PRINT '------ 移库表------'
CREATE TABLE WMS_BA_BINSTA_R
(
  WH_NO                       VARCHAR2(50) NOT NULL,        
  AREA_NO                     VARCHAR2(50) NOT NULL,        
  BIN_NO                      VARCHAR2(50) NOT NULL,            
  PALLET_ID                   VARCHAR2(50) NOT NULL,          
  FINISHED                    INT,                     
  ERR_CODE                    VARCHAR2(50) NOT NULL,
  ERR_MSG                     VARCHAR2(50) NOT NULL,             
  FLAG                        VARCHAR2(50) NOT NULL,      
  CREATE_TIME                 DATE NOT NULL              
);

-- PRINT '------ 手动派车接口 ------'
CREATE TABLE WMS_DISPATCHING_INTERFACE
(
       WH_NO                       VARCHAR2(50) NOT NULL,
       AREA_NO                     VARCHAR2(50) NOT NULL,
       PALLET_ID                   VARCHAR2(50) NOT NULL,
       PRODUCT_ID                  VARCHAR2(50) NOT NULL,
       FACTORY_NO                  VARCHAR2(50) NOT NULL,
       MATERIEL_TYPE               VARCHAR2(50) NOT NULL,
       MATERIEL_NAME               VARCHAR2(50) NOT NULL,
       STATIONS                    VARCHAR2(50),
       PORT                        VARCHAR2(50) NOT NULL,
       FINISHED                    INT,
       ERR_CODE                    INT,
       ERR_MSG                     VARCHAR2(50),
       FLAG                        VARCHAR2(50),
       CREAT_TIME                  DATE
);

--出入表
CREATE TABLE WMS_RAW_TRK_INTERFACE
(
       TRK_DT                       VARCHAR2(50) NOT NULL,
       GROUP_ID                     VARCHAR2(50) NOT NULL,
       COMMAND_NO                   VARCHAR2(50) NOT NULL,
       WH_NO                        VARCHAR2(50) NOT NULL,
       AREA_NO                      VARCHAR2(50) NOT NULL,
       DEV_NO                       VARCHAR2(50) NOT NULL,
       BIN_NO                       VARCHAR2(50) NOT NULL,
       EMERGE                       INT,
       IO                           VARCHAR2(50) NOT NULL,
       WMS_BACK                     INT NOT NULL,
       WMS_ACTION                   VARCHAR2(50),
       WMS_SERIAL_NO                VARCHAR2(50),
       WMS_USER                     VARCHAR2(50),
       WMS_FLAG                     VARCHAR2(50),
       PALLET_ID                    VARCHAR2(50),
       PALLET_SIZE                  VARCHAR2(50) NOT NULL,
       WEIGHT                       number(10,3) NOT NULL,
       STATUS                       INT NOT NULL,
       FINISHED                     INT NOT NULL,
       ERR_CODE                     VARCHAR2(50),
       ERR_MSG                      VARCHAR2(50),
       WMS_ERR_CODE                 VARCHAR2(50),
       WMS_ERR_MSG                  VARCHAR2(50),
       PRODUCT_ID                   VARCHAR2(50),
       BOX_COUNT                    INT,
       PORT                         VARCHAR2(50),
       MAT_TYPE                     VARCHAR2(50) NOT NULL,
       STATIONS                     VARCHAR2(50) NOT NULL,
       TASK_TYPE                    VARCHAR2(50) NOT NULL,
       VENDOR_CODE                  VARCHAR2(50)
);
