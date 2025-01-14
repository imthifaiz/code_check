package com.track.session.customer;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
@Deprecated
public interface CustomerHome extends EJBHome {
	public CustomerRemote create() throws RemoteException, CreateException;
}