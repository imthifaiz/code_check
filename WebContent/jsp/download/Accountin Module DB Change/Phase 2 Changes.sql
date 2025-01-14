USE [SCAN2TRACK]
GO


SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[test_FIN_INVOICEMST](
	[PLANT] [varchar](30) NOT NULL,
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[SUPPLIER] varchar (50) NULL,
	[INVOICENO] [varchar](50) NOT NULL,
	[crat]  [varchar] (14) NULL,
	[crby]  [varchar] (50) NULL,
	[upat]  [varchar](14) NULL,
	[upby]  [varchar] (50) NULL,
		
	PRIMARY KEY CLUSTERED 
(
	[PLANT] ASC,
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

/****** Object:  Table [dbo].[test_ACCOUNT]    Script Date: 6/9/2019 11:20:51 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[test_FIN_ORDERLEDGERMST](
	[PLANT] [varchar](30) NOT NULL,
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[SUPPLIER] varchar (50) NULL,
	[ORDERLEDGER] [varchar](50) NOT NULL,
	[crat]  [varchar] (14) NULL,
	[crby]  [varchar] (50) NULL,
	[upat]  [varchar](14) NULL,
	[upby]  [varchar] (50) NULL,
		
	PRIMARY KEY CLUSTERED 
(
	[PLANT] ASC,
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[test_FIN_CHARTOFACCOUNT](
	[PLANT] [varchar](30) NOT NULL,
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[ACCOUNTTYPE] varchar (100) NULL,
	[DETAILTYPE] varchar (100) NOT NULL,
	[ACCOUNTNAME] [varchar](100) NOT NULL,
	[ACCOUNTDESC] [varchar](500) NULL,
	[ISSUBACCOUNT] tinyint NULL,
	[DEFAULTTAXCODE] varchar(50) NULL,
	[OPENINGBALANCE] [float] NULL,
	[BALANCEDATE] varchar(20) NULL,
	[crat]  [varchar] (14) NULL,
	[crby]  [varchar] (50) NULL,
	[upat]  [varchar](14) NULL,
	[upby]  [varchar] (50) NULL,
		
	PRIMARY KEY CLUSTERED 
(
	[PLANT] ASC,
	[ID] ASC,
	[DETAILTYPE] ASC,
	[ACCOUNTNAME]  ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO




CREATE TABLE [dbo].[test_FIN_PURCHASEHDR](
	[PLANT] [varchar](30) NOT NULL,
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[TRANID] varchar (40)  NOT NULL,
	[TRANDT] varchar (20)  NULL,
	[CUSTCODE] varchar (50) NOT NULL,
	[ORDERLEDGER] [varchar](50)  NOT NULL,
	[INVOICENO] [varchar](50) NOT NULL,
	[INVOICEDATE] [varchar](20) NULL,
	[crat]  [varchar] (14) NULL,
	[crby]  [varchar] (50) NULL,
	[upat]  [varchar](14) NULL,
	[upby]  [varchar] (50) NULL,
		
	PRIMARY KEY CLUSTERED 
(
	[PLANT] ASC,
	[ID] ASC,
	[TRANID] ASC,
	[CUSTCODE]  ASC,
	[ORDERLEDGER]  ASC,
	[INVOICENO]  ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO


CREATE TABLE [dbo].[test_FIN_PURCHASEDET](
	[PLANT] [varchar](30) NOT NULL,
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[TRANID] varchar (40)  NOT NULL,
	[GRNO] [varchar](50)  NOT NULL,
	[LNNO] int NOT NULL,
	[PONO] varchar (50)  NULL,
	[ITEM] varchar (50)  NULL,
	[ITEMDESC] varchar (100)  NULL,
	[QTY] [decimal](18,3)  NULL,
	[UNITPRICE] [float]  NULL,
	[DISCOUNT] [float] NULL,
	[TAX] [varchar] NULL, --INBOUND_GST from test_POHDR table
	[crat]  [varchar] (14) NULL,
	[crby]  [varchar] (50) NULL,
	[upat]  [varchar](14) NULL,
	[upby]  [varchar] (50) NULL,
		
	PRIMARY KEY CLUSTERED 
(
	[PLANT] ASC,
	[ID] ASC,
	[TRANID] ASC,
	[GRNO]  ASC,
	[LNNO]  ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO







