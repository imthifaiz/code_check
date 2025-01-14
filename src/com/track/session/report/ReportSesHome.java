package com.track.session.report;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

@Deprecated
public interface ReportSesHome extends EJBHome {
	public ReportSesRemote create() throws RemoteException, CreateException;
}