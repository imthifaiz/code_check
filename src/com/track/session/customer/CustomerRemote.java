package com.track.session.customer;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.ejb.EJBObject;

@Deprecated
public interface CustomerRemote extends EJBObject {

	public boolean insertIntoCustomer(Hashtable ht) throws RemoteException,
			Exception;

	public boolean insertIntoVendor(Hashtable ht) throws RemoteException,
			Exception;

	public boolean deleteCustomer(String aCustno) throws RemoteException,
			Exception;

	public boolean deleteCustomer(String aCustno, String plant)
			throws RemoteException, Exception;

	public boolean deleteVendor(String aCustno, String plant)
			throws RemoteException, Exception;

	public boolean isExistsCustomer(String aMapItem) throws RemoteException,
			Exception;

	public ArrayList getVendorByName(String custname, String plant)
			throws RemoteException, Exception;

	public boolean isExistsCustomer(String aMapItem, String plant)
			throws RemoteException, Exception;

	public boolean isExistsVendor(String aMapItem, String plant)
			throws RemoteException, Exception;

	public ArrayList getCustomerList4ConMent() throws RemoteException,
			Exception;

	public ArrayList getCustomerList() throws RemoteException, Exception;

	public ArrayList getCustomerDetails(String aCust) throws RemoteException,
			Exception;

	public ArrayList getCustomerDetails(String aCust, String plant)
			throws RemoteException, Exception;

	public ArrayList getVendorDetails(String aCust, String plant)
			throws RemoteException, Exception;

	public boolean updateCustomer(Hashtable htUpdate, Hashtable htCondition)
			throws RemoteException, Exception;

	public ArrayList getCustomerListStartsWithName(String aCustName)
			throws RemoteException, Exception;

	public ArrayList getOutGoingCustomerDetails(String Plant, String aCustName,
			String cond) throws RemoteException, Exception;

	public ArrayList getVendorListStartsWithName(String aCustName,
			String plant, String cond) throws RemoteException, Exception;

	public ArrayList getCustomerListStartsWithName(String aCustName,
			String plant) throws RemoteException, Exception;

	public boolean updateVendor(Hashtable htUpdate, Hashtable htCondition)
			throws RemoteException, Exception;

	public boolean isExistsLoanAssignee(String aConsignee, String plant)
			throws RemoteException, Exception;

	public boolean insertIntoLoanAssignee(Hashtable ht) throws RemoteException,
			Exception;

	public boolean deleteLoanAssignee(String aConsignee, String plant)
			throws RemoteException, Exception;

	public boolean updateLoanAssignee(Hashtable htUpdate, Hashtable htCondition)
			throws RemoteException, Exception;

	public ArrayList getLoanAssigneeDetails(String aConsignee, String plant)
			throws RemoteException, Exception;

	public ArrayList getLoanAssigneeListStartsWithName(String aCustName,
			String aCode, String plant, String cond) throws RemoteException,
			Exception;

	public boolean isExistsToAssignee(String aMapItem, String plant)
			throws RemoteException, Exception;

	public ArrayList getToAssingeListStartsWithName(String aCustName,
			String plant) throws RemoteException, Exception;

	public boolean updateToAssignee(Hashtable htUpdate, Hashtable htCondition)
			throws RemoteException, Exception;

	public boolean insertIntoToAssignee(Hashtable ht) throws RemoteException,
			Exception;

	public boolean deleteToAssignee(String aCustno, String plant)
			throws RemoteException, Exception;

	public ArrayList getToAssigneeDetails(String aCustName, String plant)
			throws RemoteException, Exception;

	public ArrayList getOutGoingToAssigneeDetails(String aCustName,
			String plant, String cond) throws RemoteException, Exception;

}