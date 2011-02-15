/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\\Users\\Roman\\eclipse\\RadDet\\src\\edu\\ucla\\raddet\\radiation\\IDataCollector.aidl
 */
package edu.ucla.raddet.radiation;
//Android Interface Definition Language for DataCollector Service

public interface IDataCollector extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements edu.ucla.raddet.radiation.IDataCollector
{
private static final java.lang.String DESCRIPTOR = "edu.ucla.raddet.radiation.IDataCollector";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an edu.ucla.raddet.radiation.IDataCollector interface,
 * generating a proxy if needed.
 */
public static edu.ucla.raddet.radiation.IDataCollector asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof edu.ucla.raddet.radiation.IDataCollector))) {
return ((edu.ucla.raddet.radiation.IDataCollector)iin);
}
return new edu.ucla.raddet.radiation.IDataCollector.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_startDataCollection:
{
data.enforceInterface(DESCRIPTOR);
this.startDataCollection();
reply.writeNoException();
return true;
}
case TRANSACTION_stopDataCollection:
{
data.enforceInterface(DESCRIPTOR);
this.stopDataCollection();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements edu.ucla.raddet.radiation.IDataCollector
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
//Starts the periodic recording of radiation data

public void startDataCollection() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_startDataCollection, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//Stop the periodic recording of radiation data

public void stopDataCollection() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_stopDataCollection, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_startDataCollection = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_stopDataCollection = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
//Starts the periodic recording of radiation data

public void startDataCollection() throws android.os.RemoteException;
//Stop the periodic recording of radiation data

public void stopDataCollection() throws android.os.RemoteException;
}
