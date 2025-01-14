--multi uom changes----
--rental order S2TCONFIG constant file and import template changes

insert into test_USER_MENU
(um_pkey,plant,url_name,lang,col,row,text,url,status,sequence) values
(376,'test','viewinventoryprodmultiuom','E','1','15','Inventory Summary With Total Quantity Multi UOM','viewInventoryGroupByProdMultiUOM.jsp','1','8') 

insert into USER_MENU
(um_pkey,plant,url_name,lang,col,row,text,url,status,sequence) values
(376,'track','viewinventoryprodmultiuom','E','1','15','Inventory Summary With Total Quantity Multi UOM','viewInventoryGroupByProdMultiUOM.jsp','1','8') 

insert into TEST_USER_LEVEL (ul_pkey,plant,level_name,url_name,remarks,created_by,created_on,updated_by,updated_on,authorise_by,authorise_on)
values (376,'test','DefaultGroup','viewinventoryprodmultiuom','','admin','20190324','admin','20190324','admin','20190324')


insert into USER_LEVEL (ul_pkey,plant,level_name,url_name,remarks,created_by,created_on,updated_by,updated_on,authorise_by,authorise_on)
values (376,'track','DefaultGroup','viewinventoryprodmultiuom','','admin','20190324','admin','20190324','admin','20190324')

insert into test_USER_MENU
(um_pkey,plant,url_name,lang,col,row,text,url,status,sequence) values
(377,'test','viewinventorybatchmultiuom','E','1','15','Inventory Summary With Batch/Sno Multi UOM','view_inv_list_withoutpriceqtyMultiUOM.jsp','1','9') 

insert into USER_MENU
(um_pkey,plant,url_name,lang,col,row,text,url,status,sequence) values
(377,'track','viewinventorybatchmultiuom','E','1','15','Inventory Summary With Batch/Sno Multi UOM','view_inv_list_withoutpriceqtyMultiUOM.jsp','1','9') 

insert into TEST_USER_LEVEL (ul_pkey,plant,level_name,url_name,remarks,created_by,created_on,updated_by,updated_on,authorise_by,authorise_on)
values (377,'test','DefaultGroup','viewinventorybatchmultiuom','','admin','20190324','admin','20190324','admin','20190324')

insert into USER_LEVEL (ul_pkey,plant,level_name,url_name,remarks,created_by,created_on,updated_by,updated_on,authorise_by,authorise_on)
values (377,'track','DefaultGroup','viewinventorybatchmultiuom','','admin','20190324','admin','20190324','admin','20190324')

USE [Scan2Track]
GO

/***** Object:  Table [dbo].[test_DODET_REMARKS]    Script Date: 5/20/2019 1:58:51 PM *****/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

SET ANSI_PADDING ON
GO

CREATE TABLE [dbo].[test_LOANDET_REMARKS]( --New SP Included in PLANTMSTDAO
	[PLANT] [varchar](20) NOT NULL,
	[ID_REMARKS] [int] IDENTITY(1,1) NOT NULL,
	[ORDNO] [varchar](50) NOT NULL,
	[ORDLNNO] [int] NOT NULL,
	[ITEM] [varchar](50) NOT NULL,
	[REMARKS] [varchar](100) NULL,
	[CRAT] [varchar](20) NULL,
	[CRBY] [varchar](30) NULL,
	[UPAT] [varchar](20) NULL,
	[UPBY] [varchar](30) NULL
) ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO


ALTER TABLE test_LOANHDR ADD ORDERTYPE varchar(100);

ALTER TABLE test_LOANHDR ADD CURRENCYID varchar(20);

ALTER TABLE test_LOANHDR ADD RENTAL_GST float;

ALTER TABLE test_LOANHDR ADD EMPNO varchar(100);

ALTER TABLE test_LOANHDR ADD DELIVERYDATE varchar(20);

ALTER TABLE test_LOANHDR ADD PAYMENTTYPE varchar(100);

ALTER TABLE test_LOANHDR ADD SHIPPINGCUSTOMER varchar(200);

ALTER TABLE test_LOANHDR ADD ORDERDISCOUNT float;

ALTER TABLE test_LOANHDR ADD SHIPPINGCOST float;

ALTER TABLE test_LOANDET ADD RENTALPRICE float;

ALTER TABLE test_LOANDET ADD PRODGST float;

insert into test_COMPANY_CONFIG
(plant,Value,configkey,gsttype,gstdesc,gstpercentage,remarks) values
('test',NULL,'RENTAL','RENTAL','VAT FOR RENTAL','6',NULL)


ALTER TABLE test_LOANHDR ALTER COLUMN Remark1 varchar(1000);

--rental and order props changes

UPDATE test_POSDET SET UNITMO='' WHERE UNITMO IS NULL
GO                
ALTER TABLE test_POSDET ALTER COLUMN UNITMO VARCHAR(20) NOT NULL
GO

ALTER TABLE test_POSDET
DROP CONSTRAINT PK_test_POSDET;

ALTER TABLE test_POSDET
ADD  CONSTRAINT [PK_test_POSDET] PRIMARY KEY CLUSTERED 
(
	[PLANT] ASC,
	[POSTRANID] ASC,
	[ITEM] ASC,
	[BATCH] ASC,
	[LOC] ASC,
	[UNITPRICE] ASC,
	[UNITMO] ASC
)

-- Rental Order Order Config

alter table test_LOAN_RECIPT_HDR add DELDATE nvarchar(30) 
alter table test_LOAN_RECIPT_HDR add EMPNAME nvarchar(30)
alter table test_LOAN_RECIPT_HDR add ORDDISCOUNT nvarchar(30)
alter table test_LOAN_RECIPT_HDR add PREPAREDBY nvarchar(30) 
alter table test_LOAN_RECIPT_HDR add SELLER nvarchar(30)
alter table test_LOAN_RECIPT_HDR add SELLERAUTHORIZED nvarchar(30)
alter table test_LOAN_RECIPT_HDR add BUYER nvarchar(30)
alter table test_LOAN_RECIPT_HDR add BUYERAUTHORIZED nvarchar(30)
alter table test_LOAN_RECIPT_HDR add COMPANYSTAMP nvarchar(30)
alter table test_LOAN_RECIPT_HDR add COMPANYNAME nvarchar(30) 
alter table test_LOAN_RECIPT_HDR add SHIPPINGCOST nvarchar(30)
alter table test_LOAN_RECIPT_HDR add SIGNATURE nvarchar(30)
alter table test_LOAN_RECIPT_HDR add VAT nvarchar(30)
alter table test_LOAN_RECIPT_HDR add CUSTOMERVAT nvarchar(30)
alter table test_LOAN_RECIPT_HDR add TOTALAFTERDISCOUNT nvarchar(30)
alter table test_LOAN_RECIPT_HDR add ORDDATE nvarchar(30)
alter table test_LOAN_RECIPT_HDR add SHIPTO nvarchar(30)
alter table test_LOAN_RECIPT_HDR add NETAMT nvarchar(30)
alter table test_LOAN_RECIPT_HDR add TAXAMT nvarchar(30)
alter table test_LOAN_RECIPT_HDR add TOTALAMT nvarchar(30)
alter table test_LOAN_RECIPT_HDR add PAYMENTTYPE nvarchar(30)
alter table test_LOAN_RECIPT_HDR add RATE nvarchar(30)
alter table test_LOAN_RECIPT_HDR add AMOUNT nvarchar(30)
alter table test_LOAN_RECIPT_HDR add TOTALTAX nvarchar(30)
alter table test_LOAN_RECIPT_HDR add TOTALWITHTAX nvarchar(30)

alter table test_LOANDET add LISTPRICE float

ALTER TABLE [TEST_SALES_DETAIL]
ALTER COLUMN Qty decimal(18,3);

ALTER TABLE [TEST_POSDET]
ALTER COLUMN Qty decimal(18,3);



CREATE TABLE [dbo].[test123_LOAN_RECIPT_HDR](
	[PLANT] [varchar](30) NOT NULL,
	[ORDERHEADER] [nvarchar](30) NOT NULL,
	[TOHEADER] [nvarchar](30) NULL,
	[FROMHEADER] [nvarchar](30) NULL,
	[DATE] [nvarchar](30) NULL,
	[ORDERNO] [nvarchar](30) NULL,
	[REFNO] [nvarchar](30) NULL,
	[SONO] [nvarchar](30) NULL,
	[ITEM] [nvarchar](30) NULL,
	[DESCRIPTION] [nvarchar](30) NULL,
	[ORDERQTY] [nvarchar](30) NULL,
	[UOM] [nvarchar](30) NULL,
	[FOOTER1] [nvarchar](30) NULL,
	[FOOTER2] [nvarchar](30) NULL,
	[FOOTER3] [nvarchar](30) NULL,
	[FOOTER4] [nvarchar](30) NULL,
	[CRAT] [nvarchar](30) NULL,
	[CRBY] [nvarchar](30) NULL,
	[UPAT] [nvarchar](14) NULL,
	[UPBY] [nvarchar](30) NULL,
	[REMARK1] [nvarchar](30) NULL,
	[PRINTORIENTATION] [nvarchar](30) NULL,
	[ORDDATE] [nvarchar](30) NULL,
	[SHIPTO] [nvarchar](30) NULL,
	[DELDATE] [nvarchar](30) NULL,
	[EMPNAME] [nvarchar](30) NULL,
	[PREPAREDBY] [nvarchar](30) NULL,
	[SELLER] [nvarchar](30) NULL,
	[SELLERAUTHORIZED] [nvarchar](30) NULL,
	[BUYER] [nvarchar](30) NULL,
	[BUYERAUTHORIZED] [nvarchar](30) NULL,
	[COMPANYNAME] [nvarchar](30) NULL,
	[COMPANYSTAMP] [nvarchar](30) NULL,
	[SIGNATURE] [nvarchar](30) NULL,
	[ORDDISCOUNT] [nvarchar](30) NULL,
	[SHIPPINGCOST] [nvarchar](30) NULL,
	[RCBNO] [nvarchar](30) NULL,
	[CUSTOMERRCBNO] [nvarchar](30) NULL,
	[PAYMENTTYPE] [nvarchar](30) NULL,
	[RATE] [nvarchar](30) NULL,
	[TAXAMOUNT] [nvarchar](30) NULL,
	[AMT] [nvarchar](30) NULL,
	[SUBTOTAL] [nvarchar](30) NULL,
	[TOTALTAX] [nvarchar](30) NULL,
	[TOTAL] [nvarchar](30) NULL,
	[TOTALAFTERDISCOUNT] [nvarchar](30) NULL,
	[NETRATE] [nvarchar](30) NULL
) ON [PRIMARY]
GO

insert into TEST_USER_LEVEL (ul_pkey,plant,level_name,url_name,remarks,created_by,created_on,updated_by,updated_on,authorise_by,authorise_on)
values (378,'test','DefaultGroup','loanOrderSummaryWithPrice','','admin','20190720','admin','20190720','admin','20190720')

insert into USER_LEVEL (ul_pkey,plant,level_name,url_name,remarks,created_by,created_on,updated_by,updated_on,authorise_by,authorise_on)
values (378,'track','DefaultGroup','loanOrderSummaryWithPrice','','admin','20190720','admin','20190720','admin','20190720')

insert into test_USER_MENU
(um_pkey,plant,url_name,lang,col,row,text,url,status,sequence) values
(378,'test','loanOrderSummaryWithPrice','E','1','9', 'Summary - Rental Order Details (With Price)','loanOrderSummaryWithPrice.jsp','1', '1') 

insert into USER_MENU
(um_pkey,plant,url_name,lang,col,row,text,url,status,sequence) values
(378,'track','loanOrderSummaryWithPrice','E','1','9','Summary - Rental Order Details (With Price)','loanOrderSummaryWithPrice.jsp','1', '1')

insert into TEST_USER_LEVEL (ul_pkey,plant,level_name,url_name,remarks,created_by,created_on,updated_by,updated_on,authorise_by,authorise_on)
values (379,'test','DefaultGroup','RentalOrderSummary','','admin','20190720','admin','20190720','admin','20190720')

insert into USER_LEVEL (ul_pkey,plant,level_name,url_name,remarks,created_by,created_on,updated_by,updated_on,authorise_by,authorise_on)
values (379,'track','DefaultGroup','RentalOrderSummary','','admin','20190720','admin','20190720','admin','20190720')

insert into test_USER_MENU
(um_pkey,plant,url_name,lang,col,row,text,url,status,sequence) values
(379,'test','RentalOrderSummary','E','1','20','Summary - Rental Order Details','RentalOrderSummary.jsp','1','1') 

insert into USER_MENU
(um_pkey,plant,url_name,lang,col,row,text,url,status,sequence) values
(379,'track','RentalOrderSummary','E','1','20','Summary - Rental Order Details','RentalOrderSummary.jsp','1','1')


UPDATE USER_MENU SET  sequence ='3' WHERE um_pkey='101';
UPDATE TEST_USER_MENU SET  sequence ='3' WHERE um_pkey='101';

insert into TEST_USER_LEVEL (ul_pkey,plant,level_name,url_name,remarks,created_by,created_on,updated_by,updated_on,authorise_by,authorise_on)
values (380,'test','DefaultGroup','LoanOrderSummaryByPrice','','admin','20190720','admin','20190720','admin','20190720')

insert into USER_LEVEL (ul_pkey,plant,level_name,url_name,remarks,created_by,created_on,updated_by,updated_on,authorise_by,authorise_on)
values (380,'track','DefaultGroup','LoanOrderSummaryByPrice','','admin','20190720','admin','20190720','admin','20190720')

insert into test_USER_MENU
(um_pkey,plant,url_name,lang,col,row,text,url,status,sequence) values
(380,'test','LoanOrderSummaryByPrice','E','1','20','Summary - Rental Order Details (With Price)','LoanOrderSummaryByPrice.jsp','1','2') 

insert into USER_MENU
(um_pkey,plant,url_name,lang,col,row,text,url,status,sequence) values
(380,'track','LoanOrderSummaryByPrice','E','1','20','Summary - Rental Order Details (With Price)','LoanOrderSummaryByPrice.jsp','1','2')

insert into TEST_USER_LEVEL (ul_pkey,plant,level_name,url_name,remarks,created_by,created_on,updated_by,updated_on,authorise_by,authorise_on)
values (381,'test','DefaultGroup','printLoanOrderInvoice','','admin','20190720','admin','20190720','admin','20190720')

insert into USER_LEVEL (ul_pkey,plant,level_name,url_name,remarks,created_by,created_on,updated_by,updated_on,authorise_by,authorise_on)
values (381,'track','DefaultGroup','printLoanOrderInvoice','','admin','20190720','admin','20190720','admin','20190720')


insert into test_USER_MENU
(um_pkey,plant,url_name,lang,col,row,text,url,status,sequence) values
(381,'test','printLoanOrderInvoice','E','1','20','Summary - Rental Order Details (By Price)','printLoanOrderInvoice.jsp','1','4') 

insert into USER_MENU
(um_pkey,plant,url_name,lang,col,row,text,url,status,sequence) values
(381,'track','printLoanOrderInvoice','E','1','20','Summary - Rental Order Details (By Price)','printLoanOrderInvoice.jsp','1','4')


---------------------------------------------------------------


alter table test_POSRECEIVE_RECIPT_HDR add UNITMO varchar(50)
alter table test_STOCKMOVE_HDR add UNITMO varchar(50) 
alter table test_POS_RECIPT_HDR add UNITMO varchar(50)  