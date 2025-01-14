update test_recvdet set Lnno = 1 where Lnno is null;

select * from test_shiphis 

ALTER TABLE [dbo].[test_RECVDET]
ALTER COLUMN [PONO]VARCHAR( 50 ) NOT NULL ;
ALTER TABLE [dbo].[test_RECVDET]
ALTER COLUMN [ITEM]VARCHAR( 50 ) NOT NULL;
ALTER TABLE [dbo].[test_RECVDET]
ALTER COLUMN [BATCH]VARCHAR( 40 ) NOT NULL ;
ALTER TABLE [dbo].[test_RECVDET]
ALTER COLUMN [LOC]VARCHAR( 50 ) NOT NULL ;
ALTER TABLE [dbo].[test_RECVDET]
ALTER COLUMN [CRAT]VARCHAR( 14 ) NOT NULL ;
ALTER TABLE [dbo].[test_RECVDET]
ALTER COLUMN [LNNO] VARCHAR( 20 ) NOT NULL ;
ALTER TABLE [dbo].[test_RECVDET]
ADD CONSTRAINT [test_RECVDET_pk] PRIMARY KEY ( [PLANT], [CRAT], [PONO], [LNNO], [ITEM], [BATCH], [LOC] );

update test_shiphis set dolno = 1 where dolno is null;
-- CHANGE "NULLABLE" OF "FIELD "DONO" --------------------------
ALTER TABLE [dbo].[test_SHIPHIS]
ALTER COLUMN [DONO]VARCHAR( 50 ) NOT NULL 
GO
-- -------------------------------------------------------------
-- CHANGE "NULLABLE" OF "FIELD "DOLNO" -------------------------
ALTER TABLE [dbo].[test_SHIPHIS]
ALTER COLUMN [DOLNO]NCHAR( 10 ) NOT NULL 
GO
-- -------------------------------------------------------------

-- CHANGE "NULLABLE" OF "FIELD "ITEM" --------------------------
ALTER TABLE [dbo].[test_SHIPHIS]
ALTER COLUMN [ITEM]VARCHAR( 50 ) NOT NULL 
GO
-- -------------------------------------------------------------

-- CHANGE "NULLABLE" OF "FIELD "BATCH" -------------------------
ALTER TABLE [dbo].[test_SHIPHIS]
ALTER COLUMN [BATCH]VARCHAR( 40 ) NOT NULL 
GO
-- -------------------------------------------------------------

-- CHANGE "NULLABLE" OF "FIELD "CRAT" --------------------------
ALTER TABLE [dbo].[test_SHIPHIS]
ALTER COLUMN [CRAT]VARCHAR( 14 ) NOT NULL 
GO
-- -------------------------------------------------------------

-- CHANGE "NULLABLE" OF "FIELD "LOC" --------------------------
ALTER TABLE [dbo].[test_SHIPHIS]
ALTER COLUMN [LOC]VARCHAR( 50 ) NOT NULL 
GO
-- -------------------------------------------------------------


ALTER TABLE [dbo].[test_SHIPHIS]
ADD CONSTRAINT [test_SHIPHIS_pk] PRIMARY KEY ( [PLANT], [CRAT] , [DONO], [DOLNO], [ITEM], [BATCH], [LOC])
GO
alter table test_FIN_TRANSACTION_DETAIL alter column amount float(53) not null;
alter table test_FIN_TRANSACTION_RECONCILE alter column amount float(53) not null;