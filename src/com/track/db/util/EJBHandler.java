package com.track.db.util;

import com.track.session.LocMstHome;
import com.track.session.itemses.*;
import com.track.session.itemmap.*;
import com.track.session.itemloc.*;
import com.track.session.invses.*;
import com.track.session.tblControl.*;
import com.track.session.movhis.*;
import com.track.session.customer.*;
//import com.track.session.cyclecount.*;
import com.track.session.po.*;
import com.track.session.so.*;
import com.track.session.report.*;

import com.track.constants.*;
import com.track.gates.*;
import javax.naming.*;

@Deprecated
public class EJBHandler {
	public EJBHandler() {
	}

	/**
	 * method : getItemHome() description : bound the Home for Item Master
	 * 
	 * @return ItemSesHome
	 */
	public static ItemSesHome getItemHome() {
		ItemSesHome home = null;
		try {
			Context ctx = DbBean.getInitialContext();

			home = (ItemSesHome) ctx.lookup(IConstants.ITEM_HOME);
		} catch (Exception e) {
			System.out.println("Exception in lookup home for Item Home : "
					+ e.toString());
		}
		return home;
	}

	/**
	 * method : getItemLocHome() description : bound the Home for Item Location
	 * master
	 * 
	 * @return ItemLocHome
	 */

	public static ItemLocHome getItemLocHome() {
		ItemLocHome home = null;
		try {
			Context ctx = DbBean.getInitialContext();
			home = (ItemLocHome) ctx.lookup(IConstants.ITEM_LOC_HOME);
		} catch (Exception e) {
			System.out
					.println("Exception in lookup home for Item Location Home : "
							+ e.toString());
		}
		return home;
	}

	/**
	 * method : getItemMapHome() description : bound the Home for Item Map
	 * master
	 * 
	 * @return ItemMapHome
	 */

	public static ItemMapHome getItemMapHome() {
		ItemMapHome home = null;
		try {
			Context ctx = DbBean.getInitialContext();
			home = (ItemMapHome) ctx.lookup(IConstants.ITEM_MAP_HOME);
		} catch (Exception e) {
			System.out.println("Exception in lookup home for Item Map Home : "
					+ e.toString());
		}
		return home;
	}

	/**
	 * method : getInvSesHome() description : bound the Home for Item Map master
	 * 
	 * @return InvSesHome
	 */

	public static InvSesHome getInvSesHome() {
		InvSesHome home = null;
		try {
			Context ctx = DbBean.getInitialContext();
			home = (InvSesHome) ctx.lookup(IConstants.INV_HOME);
		} catch (Exception e) {
			System.out.println("Exception in lookup home for Inventory home : "
					+ e.toString());
		}
		return home;
	}

	/**
	 * method : getTblControlHome() description : bound the Home for TblControl
	 * 
	 * @return InvSesHome
	 */

	public static TblControlHome getTblControlHome() {
		TblControlHome home = null;
		try {
			Context ctx = DbBean.getInitialContext();
			home = (TblControlHome) ctx.lookup(IConstants.TBLCONTROL_HOME);
		} catch (Exception e) {
			System.out
					.println("Exception in lookup home for TblControl home : "
							+ e.toString());
		}
		return home;
	}

	/**
	 * method : getRedvHisHome() description : bound the Home for RecvHis
	 * 
	 * @return InvSesHome
	 */
	public static MovHisHome getMovHisHome() {
		MovHisHome home = null;
		try {
			Context ctx = DbBean.getInitialContext();
			home = (MovHisHome) ctx.lookup(IConstants.MOVHIS_HOME);
		} catch (Exception e) {
			System.out
					.println("Exception in lookup home for Movment history home : "
							+ e.toString());
		}
		return home;
	}

	/**
	 * method : getRedvHisHome() description : bound the Home for RecvHis
	 * 
	 * @return InvSesHome
	 */

	public static CustomerHome getCustomerHome() {
		CustomerHome home = null;
		try {
			Context ctx = DbBean.getInitialContext();
			home = (CustomerHome) ctx.lookup(IConstants.CUSTOMER_HOME);
		} catch (Exception e) {
			System.out.println("Exception in lookup home for Customer home : "
					+ e.toString());
		}
		return home;
	}

	/*public static CycleCountHome getCycleCountHome() {
		CycleCountHome home = null;
		try {
			Context ctx = DbBean.getInitialContext();
			home = (CycleCountHome) ctx.lookup(IConstants.CYCLECOUNT_HOME);
		} catch (Exception e) {
			System.out
					.println("Exception in lookup home for Cycount Count home : "
							+ e.toString());
		}
		return home;
	}*/

	public static POHome getPOHome() {
		POHome home = null;
		try {
			Context ctx = DbBean.getInitialContext();
			home = (POHome) ctx.lookup(IConstants.PO_HOME);
		} catch (Exception e) {
			System.out.println("Exception in lookup home for PO home : "
					+ e.toString());
		}
		return home;
	}

	public static SOHome getSOHome() {
		SOHome home = null;
		try {
			Context ctx = DbBean.getInitialContext();
			home = (SOHome) ctx.lookup(IConstants.SO_HOME);
		} catch (Exception e) {
			System.out.println("Exception in lookup home for SO home : "
					+ e.toString());
		}
		return home;
	}

	public static ReportSesHome getReportHome() {
		ReportSesHome home = null;
		try {
			Context ctx = DbBean.getInitialContext();
			home = (ReportSesHome) ctx.lookup(IConstants.REPORT_HOME);
		} catch (Exception e) {
			System.out
					.println("Exception in lookup home for Report Ses home : "
							+ e.toString());
		}
		return home;
	}

	public static LocMstHome LocMstHome() {
		LocMstHome home = null;
		try {
			Context ctx = DbBean.getInitialContext();
			home = (LocMstHome) ctx.lookup("EJBLOCMST");
		} catch (Exception e) {
			System.out.println("Exception in lookup home for locmst home : "
					+ e.toString());
		}
		return home;
	}

}