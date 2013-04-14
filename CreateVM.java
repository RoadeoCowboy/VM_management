/*================================================================================
Copyright (c) 2008 VMware, Inc. All Rights Reserved.

Redistribution and use in source and binary forms, with or without modification, 
are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, 
this list of conditions and the following disclaimer.

 * Redistributions in binary form must reproduce the above copyright notice, 
this list of conditions and the following disclaimer in the documentation 
and/or other materials provided with the distribution.

 * Neither the name of VMware, Inc. nor the names of its contributors may be used
to endorse or promote products derived from this software without specific prior 
written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
IN NO EVENT SHALL VMWARE, INC. OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
POSSIBILITY OF SUCH DAMAGE.
================================================================================*/

package Lab1;

import java.net.URL;

import com.vmware.vim25.Description;
import com.vmware.vim25.VirtualDeviceConfigSpec;
import com.vmware.vim25.VirtualDeviceConfigSpecFileOperation;
import com.vmware.vim25.VirtualDeviceConfigSpecOperation;
import com.vmware.vim25.VirtualDisk;
import com.vmware.vim25.VirtualDiskFlatVer2BackingInfo;
import com.vmware.vim25.VirtualEthernetCard;
import com.vmware.vim25.VirtualEthernetCardNetworkBackingInfo;
import com.vmware.vim25.VirtualLsiLogicController;
import com.vmware.vim25.VirtualMachineConfigSpec;
import com.vmware.vim25.VirtualMachineConfigInfo;
import com.vmware.vim25.VirtualMachineFileInfo;
import com.vmware.vim25.VirtualPCNet32;
import com.vmware.vim25.VirtualSCSISharing;
import com.vmware.vim25.mo.Datacenter;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ResourcePool;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.Task;
import java.net.URL;

import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;
import com.vmware.vim25.mo.util.*;

//import com.vmware.vim25.m;

/**
 * http://vijava.sf.net
 * @author Steve Jin
 */

public class CreateVM 
{
  public static void main(String[] args) throws Exception 
  {  
    /*if(args.length!=3)
    {
      System.out.println("Usage: java CreateVM <url> " +
            "<username> <password>");
      System.exit(0);
    }*/

    String dcName = "CMPE281";
    String vmName = "c94";
    long memorySizeMB = 80;
    int cupCount = 1;
    //String guestOsId="rhel3Guest";
    String guestOsId="ubuntu64Guest";	
    // String guestOsId = "rhel3_64Guest";
    //String guestOsId = "ubuntuGuest";
   // String guestOsId = "redhat";
    long diskSizeKB = 10;
    // mode: persistent|independent_persistent,
    // independent_nonpersistent
    String diskMode = "persistent";
    String datastoreName = "datastore1";
    String netName = "VM Network";
    String nicName = "Network Adapter 1";
    
    
      ServiceInstance si = new ServiceInstance(new URL(Config.getVmwareHostURL()), Config.getVmwareLogin(), Config.getVmwarePassword(), true);
      
    Folder rootFolder = si.getRootFolder();
    
       
   Datacenter dc = (Datacenter) new InventoryNavigator(
        rootFolder).searchManagedEntity("Datacenter", dcName);
     
        
    ResourcePool rp = (ResourcePool) new InventoryNavigator(dc).searchManagedEntities("ResourcePool")[0];
    
    Folder vmFolder = dc.getVmFolder();

    // create vm config spec
    VirtualMachineConfigSpec vmSpec = 
      new VirtualMachineConfigSpec();
    vmSpec.setName(vmName);
    vmSpec.setAnnotation("VirtualMachine Annotation");
    vmSpec.setMemoryMB(memorySizeMB);
    vmSpec.setNumCPUs(cupCount);
    vmSpec.setGuestId(guestOsId);

    // create virtual devices
    int cKey = 1000;
    VirtualDeviceConfigSpec scsiSpec = createScsiSpec(cKey);
    VirtualDeviceConfigSpec diskSpec = createDiskSpec(
        datastoreName, cKey, diskSizeKB, diskMode);
    VirtualDeviceConfigSpec nicSpec = createNicSpec(
        netName, nicName);

      
    vmSpec.setDeviceChange(new VirtualDeviceConfigSpec[] 
        {scsiSpec, diskSpec, nicSpec});
        
        
        
        
    
    // create vm file info for the vmx file
    VirtualMachineFileInfo vmfi = new VirtualMachineFileInfo();
    vmfi.setVmPathName("["+ datastoreName +"]");
    vmSpec.setFiles(vmfi);

    // call the createVM_Task method on the vm folder
    Task task = vmFolder.createVM_Task(vmSpec, rp, null);
    String result = task.waitForMe();       
    if(result == Task.SUCCESS) 
    {
      System.out.println("VM Created Sucessfully");
    }
    else 
    {
      System.out.println("VM could not be created. ");
    }
    

    
    
  }
  
  private static VirtualDevice[] getDefaultDevices(VirtualMachine vm) throws Exception 
   {
      VirtualMachineRuntimeInfo vmRuntimeInfo = vm.getRuntime();
      EnvironmentBrowser envBrowser = vm.getEnvironmentBrowser(); 
      ManagedObjectReference hmor = vmRuntimeInfo.getHost();
      
      VirtualMachineConfigOption cfgOpt = envBrowser.queryConfigOption(null,  new HostSystem(vm.getServerConnection(), hmor));
      
      VirtualDevice[] defaultDevs = null;

      if (cfgOpt == null) 
      {
    	  throw new Exception("No VirtualHardwareInfo found in ComputeResource");
      }
      else 
      {
         defaultDevs = cfgOpt.getDefaultDevice();
         if (defaultDevs == null) 
         {
            throw new Exception("No Datastore found in ComputeResource");
         }
      }
      return defaultDevs;
   }
   
   
  private static VirtualDevice getIDEController(VirtualMachine vm) throws Exception 
   {
      VirtualDevice ideCtlr = null;
      VirtualDevice [] defaultDevices = getDefaultDevices(vm);
      for (int i = 0; i < defaultDevices.length; i++) 
      {
         if (defaultDevices[i] instanceof VirtualIDEController) 
         {
            ideCtlr = defaultDevices[i];             
            break;
         }
      }
      return ideCtlr;
   }    

  private static DatastoreSummary getDataStoreSummary(VirtualMachine vm) throws Exception 
   {
      DatastoreSummary dsSum = null;
      VirtualMachineRuntimeInfo vmRuntimeInfo = vm.getRuntime();
      EnvironmentBrowser envBrowser = vm.getEnvironmentBrowser(); 
      ManagedObjectReference hmor = vmRuntimeInfo.getHost();
      
      if(hmor != null) 
      {       
    	  ConfigTarget configTarget = envBrowser.queryConfigTarget(new HostSystem(vm.getServerConnection(), hmor));       
    	  if(configTarget.getDatastore() != null)
    	  {
            for (int i = 0; i < configTarget.getDatastore().length; i++) 
            {
               VirtualMachineDatastoreInfo vdsInfo = configTarget.getDatastore()[i];
               DatastoreSummary dsSummary = vdsInfo.getDatastore();
               if (dsSummary.isAccessible()) 
               {
                  dsSum = dsSummary;
                  break;
               }
            }
         }
         return dsSum;
      }
      else 
      {
         System.out.println("No Datastore found");
         return null;
      }
   }
   
    
  
  static VirtualDeviceConfigSpec createScsiSpec(int cKey)
  {
    VirtualDeviceConfigSpec scsiSpec = 
      new VirtualDeviceConfigSpec();
    scsiSpec.setOperation(VirtualDeviceConfigSpecOperation.add);
    VirtualLsiLogicController scsiCtrl = 
        new VirtualLsiLogicController();
    scsiCtrl.setKey(cKey);
    scsiCtrl.setBusNumber(0);
    scsiCtrl.setSharedBus(VirtualSCSISharing.noSharing);
    scsiSpec.setDevice(scsiCtrl);
    return scsiSpec;
  }
  
  static VirtualDeviceConfigSpec createDiskSpec(String dsName, 
      int cKey, long diskSizeKB, String diskMode)
  {
    VirtualDeviceConfigSpec diskSpec = 
        new VirtualDeviceConfigSpec();
    diskSpec.setOperation(VirtualDeviceConfigSpecOperation.add);
    diskSpec.setFileOperation(
        VirtualDeviceConfigSpecFileOperation.create);
    
    VirtualDisk vd = new VirtualDisk();
    vd.setCapacityInKB(diskSizeKB);
    diskSpec.setDevice(vd);
    vd.setKey(0);
    vd.setUnitNumber(0);
    vd.setControllerKey(cKey);

    VirtualDiskFlatVer2BackingInfo diskfileBacking = 
        new VirtualDiskFlatVer2BackingInfo();
    String fileName = "["+ dsName +"]";
    diskfileBacking.setFileName(fileName);
    diskfileBacking.setDiskMode(diskMode);
    diskfileBacking.setThinProvisioned(true);
    vd.setBacking(diskfileBacking);
    return diskSpec;
  }
  
  static VirtualDeviceConfigSpec createNicSpec(String netName, 
      String nicName) throws Exception
  {
    VirtualDeviceConfigSpec nicSpec = 
        new VirtualDeviceConfigSpec();
    nicSpec.setOperation(VirtualDeviceConfigSpecOperation.add);

    VirtualEthernetCard nic =  new VirtualPCNet32();
    VirtualEthernetCardNetworkBackingInfo nicBacking = 
        new VirtualEthernetCardNetworkBackingInfo();
    nicBacking.setDeviceName(netName);

    Description info = new Description();
    info.setLabel(nicName);
    info.setSummary(netName);
    nic.setDeviceInfo(info);
    
    // type: "generated", "manual", "assigned" by VC
    nic.setAddressType("generated");
    nic.setBacking(nicBacking);
    nic.setKey(0);
   
    nicSpec.setDevice(nic);
    return nicSpec;
  }
}