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
import com.vmware.vim25.mo.VirtualMachine;
import com.vmware.vim25.mo.Datastore;
import com.vmware.vim25.HostVMotionCompatibility;
import com.vmware.vim25.TaskInfo;
import com.vmware.vim25.VirtualMachineMovePriority;
import com.vmware.vim25.VirtualMachinePowerState;
import com.vmware.vim25.mo.ComputeResource;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.VirtualMachine;
import com.vmware.vim25.mo.*;
import com.vmware.vim25.*;
import java.net.URL;
import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;
import com.vmware.vim25.mo.util.*;
import java.io.*;



public class coldMigrate
{

public static void main(String [] args) throws Exception
{

//InputStreamReader isr =new InputStreamReader(System.in);
//BufferedReader br = new BufferedReader(isr);
//System.out.println("Enter New Host Name:");
//String new_host=br.readLine();
//System.out.println("Enter Virtual Machine Name:");
//String vmName=br.readLine();
  
   String new_host = "130.65.157.137";  
   String new_datastore= "datastore2";
   String vmName= "c91";
   
   ServiceInstance si = new ServiceInstance(new URL(Config.getVmwareHostURL()), Config.getVmwareLogin(), Config.getVmwarePassword(), true);

   Folder rootFolder= si.getRootFolder();
   
   
/*ManagedEntity[] vms = new InventoryNavigator(rootFolder).searchManagedEntities(
new String[][] { {"VirtualMachine", "vnName" }, }, true);
boolean flag=true;
for(int i=0; i<vms.length; i++)
{
VirtualMachine vm = (VirtualMachine) vms[i];
VirtualMachineRuntimeInfo vmri = (VirtualMachineRuntimeInfo) vm.getRuntime();
if(vm.getName().equals(vmName))
{
flag=false;
if(vmri.getPowerState() == VirtualMachinePowerState.poweredOn || vmri.getPowerState() == VirtualMachinePowerState.suspended )
{
Task task = vm.powerOffVM_Task();
task.waitForMe();
System.out.println("vm:" + vm.getName() + " powered off.");
}
}
}
if(flag)
{
  System.out.println("Couldnt find the machine...Enter the valid name");
}
  */ 
   System.out.println("\n Cold Migrating.....");
   
   try
   {
        HostSystem host= (HostSystem) new InventoryNavigator(rootFolder).searchManagedEntity("HostSystem",new_host);
        System.out.println("Migrate To Host:" + host.getName());
        
        HostDatastoreBrowser hdb= host.getDatastoreBrowser();
        System.out.println("hdb"+ hdb);
        System.out.println("datastores");
        Datastore[] ds = hdb.getDatastores();
        
        for(int i=0; ds!=null && i<ds.length;i++)
        {
           System.out.println(" datastore["+i+"]:");
           DatastoreInfo di= ds[i].getInfo();
           System.out.println("Name:" +di.getName());
           System.out.println("FreeSpace:" + di.getFreeSpace());
           System.out.println(" MaxFileSize:" + di.getMaxFileSize());            
        }
        
        System.out.println("Migrate to Datastore:" +ds[0].getName());
        
        ResourcePool rp=null;
        
        ManagedEntity [] rps = new InventoryNavigator(rootFolder).searchManagedEntities("ResourcePool") ;
        
        int poolIndex = 0;
        
        for(int i=0;i<rps.length;i++)
        {
           System.out.format(" [%d] => %s\n",i,rps[poolIndex].getName());
            
           if (rps[i].getName().equals(new_host))
           poolIndex=i;
        }
           VirtualMachineRelocateSpec spec= new VirtualMachineRelocateSpec();
           
           spec.setHost(host.getMOR());
           spec.setDatastore(ds[0].getMOR());
           spec.setPool(rps[poolIndex].getMOR());
            
           VirtualMachine vm = (VirtualMachine) new InventoryNavigator(
        rootFolder).searchManagedEntity(
            "VirtualMachine", vmName);
           
          Task task= vm.relocateVM_Task(spec);
           if(task.waitForMe()==Task.SUCCESS)
           {
             System.out.println("VMotioned!");
               
           }
           
           else
           {
              System.out.println("Vmotioned failed!");
              TaskInfo info = task.getTaskInfo();
              System.out.println(info.getError().getFault());
           }
           
           
        }
   
        catch (Exception e)
   {
    
   }
   /*
flag=true;
for(int i=0; i<vms.length; i++)
{
VirtualMachine vm = (VirtualMachine) vms[i];
VirtualMachineRuntimeInfo vmri = (VirtualMachineRuntimeInfo) vm.getRuntime();
if(vm.getName().equals(vmName))
{
   flag=false;
if(vmri.getPowerState() == VirtualMachinePowerState.poweredOff )
{
   Task task = vm.powerOnVM_Task(null);
   task.waitForMe();
   System.out.println("vm:" + vm.getName() + " powered on.");
}
}
}*/
   
}

}
