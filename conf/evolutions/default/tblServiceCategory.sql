/*
Navicat PGSQL Data Transfer

Source Server         : macrocoupon
Source Server Version : 90504
Source Host           : localhost:5432
Source Database       : macrocoupon
Source Schema         : public

Target Server Type    : PGSQL
Target Server Version : 90504
File Encoding         : 65001

Date: 2016-10-21 09:34:10
*/


-- ----------------------------
-- Table structure for tblServiceCategory
-- ----------------------------
DROP TABLE IF EXISTS "public"."tblServiceCategory";
CREATE TABLE "public"."tblServiceCategory" (
"servicecategory_id" int4 DEFAULT nextval('tblservice_service_id_seq'::regclass) NOT NULL,
"servicecategory_name" varchar(255) COLLATE "default",
"servicecategory_description" varchar(255) COLLATE "default"
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of tblServiceCategory
-- ----------------------------
INSERT INTO "public"."tblServiceCategory" VALUES ('2', 'Mobile Service', 'Test2 Description');
INSERT INTO "public"."tblServiceCategory" VALUES ('4', 'Test4 Services', 'Test4 Description');
INSERT INTO "public"."tblServiceCategory" VALUES ('5', 'CleaningHome Service', 'Test2 Description');
INSERT INTO "public"."tblServiceCategory" VALUES ('6', 'AirConditioner Service', 'Test2 Description');
INSERT INTO "public"."tblServiceCategory" VALUES ('8', 'Service Car', 'car cleaning');
INSERT INTO "public"."tblServiceCategory" VALUES ('9', 'Agency Service', 'Test2 Description');
INSERT INTO "public"."tblServiceCategory" VALUES ('11', 'Teaching Service', 'Test2 Description');
INSERT INTO "public"."tblServiceCategory" VALUES ('12', 'Car Solution Service', 'Repairing Car');
INSERT INTO "public"."tblServiceCategory" VALUES ('13', 'Test2 Services', 'Test2 Description');
INSERT INTO "public"."tblServiceCategory" VALUES ('14', 'Test Services', 'Test Description');
INSERT INTO "public"."tblServiceCategory" VALUES ('15', 'Test Services', 'Test Description');
INSERT INTO "public"."tblServiceCategory" VALUES ('16', 'Test Services', 'Test Description');
INSERT INTO "public"."tblServiceCategory" VALUES ('17', 'Test Services', 'Test Description');
INSERT INTO "public"."tblServiceCategory" VALUES ('18', 'Test Services', 'Test Description');
INSERT INTO "public"."tblServiceCategory" VALUES ('19', 'Test19 Services', 'Test19 Description');
INSERT INTO "public"."tblServiceCategory" VALUES ('20', 'Test Services', 'Test Description');
INSERT INTO "public"."tblServiceCategory" VALUES ('21', 'Test Services', 'Test Description');
INSERT INTO "public"."tblServiceCategory" VALUES ('22', 'Test Services', 'Test Description');
INSERT INTO "public"."tblServiceCategory" VALUES ('23', 'Test Services', 'Test Description');
INSERT INTO "public"."tblServiceCategory" VALUES ('25', 'Test hello', 'hello des');

-- ----------------------------
-- Alter Sequences Owned By 
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table tblServiceCategory
-- ----------------------------
ALTER TABLE "public"."tblServiceCategory" ADD PRIMARY KEY ("servicecategory_id");
