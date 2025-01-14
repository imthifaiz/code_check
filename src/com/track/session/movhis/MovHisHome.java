package com.track.session.movhis;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
@Deprecated
public interface MovHisHome extends EJBHome {
	public MovHisRemote create() throws RemoteException, CreateException;
}