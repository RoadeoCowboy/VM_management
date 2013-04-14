package Lab1;

import java.net.URL;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;
import java.io.*;
import java.net.URL;
import com.vmware.vim25.mo.*;
import com.vmware.vim25.*;

/**
 * Write a description of class MyVM here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MyVM
{
    // instance variables 
    private String vmname ;
    private ServiceInstance si ;
    private VirtualMachine vm ;

    /**
     * Constructor for objects of class MyVM
     */
    public MyVM( ) 
    {
        // initialise instance variables
        try {
            // your code here
        } catch ( Exception e ) 
        { System.out.println( e.toString() ) ; }

    }

    /**
     * Destructor for objects of class MyVM
     */
    protected void finalize() throws Throwable
    {
       // your code here
    } 

    /**
     * Power On the Virtual Machine
     */
    public void powerOn(String name) 
    {
        try
       {
        ServiceInstance si = new ServiceInstance(
            new URL(Config.getVmwareHostURL()), Config.getVmwareLogin(), Config.getVmwarePassword(), true);
            
             Folder rootFolder = si.getRootFolder();
      
             ManagedEntity[] vms = new InventoryNavigator(rootFolder).searchManagedEntities(
             new String[][] { {"VirtualMachine", "name" }, }, true);
              
             boolean flag=true;
             
        for(int i=0; i<vms.length; i++)
        {
            
             VirtualMachine vm = (VirtualMachine) vms[i];
             System.out.println((vm.getName()));
             VirtualMachineSummary summary = (VirtualMachineSummary) (vm.getSummary());
             System.out.println(summary.toString());
             VirtualMachineRuntimeInfo vmri = (VirtualMachineRuntimeInfo) vm.getRuntime();
                     
                 if(vm.getName().equals(name))
                 {
                 
                     flag=false;
                     if(vmri.getPowerState() == VirtualMachinePowerState.poweredOff || vmri.getPowerState() == VirtualMachinePowerState.suspended    )
                   {   
                        Task task = vm.powerOnVM_Task(null);
                        task.waitForMe();
                        System.out.println("vm:" + vm.getName() + " powered on.");
                          
                   }
                         
                } 
        }
        
        if(flag)
        {
           System.out.println("Couldnt find the machine...Enter the valid name");
        }

        
        si.getServerConnection().logout();
    
    
    }
        
        catch(Exception e)
        {
        
        }
 
    }

    /**
     * Power Off the Virtual Machine
     */
    public void powerOff(String name) 
    {
        try
       {
        ServiceInstance si = new ServiceInstance(
            new URL(Config.getVmwareHostURL()), Config.getVmwareLogin(), Config.getVmwarePassword(), true);
            
             Folder rootFolder = si.getRootFolder();
      
             ManagedEntity[] vms = new InventoryNavigator(rootFolder).searchManagedEntities(
             new String[][] { {"VirtualMachine", "name" }, }, true);
              
             boolean flag=true;
             
        for(int i=0; i<vms.length; i++)
        {
            
             VirtualMachine vm = (VirtualMachine) vms[i];
             System.out.println((vm.getName()));
             System.out.println("Done");
             VirtualMachineSummary summary = (VirtualMachineSummary) (vm.getSummary());
             System.out.println(summary.toString());
             VirtualMachineRuntimeInfo vmri = (VirtualMachineRuntimeInfo) vm.getRuntime();
                     
                 if(vm.getName().equals(name))
                 {
                 
                     flag=false;
                     if(vmri.getPowerState() == VirtualMachinePowerState.poweredOn || vmri.getPowerState() == VirtualMachinePowerState.suspended    )
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

        
        si.getServerConnection().logout();
    
    
    }
        
        catch(Exception e)
        {
        
        }
     }

     /**
     * Reset the Virtual Machine
     */

    public void reset(String name) 
    {
        
        
        try
       {
        ServiceInstance si = new ServiceInstance(
            new URL(Config.getVmwareHostURL()), Config.getVmwareLogin(), Config.getVmwarePassword(), true);
            
             Folder rootFolder = si.getRootFolder();
      
             ManagedEntity[] vms = new InventoryNavigator(rootFolder).searchManagedEntities(
             new String[][] { {"VirtualMachine", "name" }, }, true);
              
             boolean flag=true;
             
        for(int i=0; i<vms.length; i++)
        {
            
             VirtualMachine vm = (VirtualMachine) vms[i];
             System.out.println((vm.getName()));
             System.out.println("Done");
             VirtualMachineSummary summary = (VirtualMachineSummary) (vm.getSummary());
             System.out.println(summary.toString());
             VirtualMachineRuntimeInfo vmri = (VirtualMachineRuntimeInfo) vm.getRuntime();
                     
                 if(vm.getName().equals(name))
                 {
                 
                     flag=false;
                     if(vmri.getPowerState() == VirtualMachinePowerState.poweredOn || vmri.getPowerState() == VirtualMachinePowerState.poweredOff  )
                   {   
                        Task task = vm.resetVM_Task();
                        task.waitForMe();
                        System.out.println("vm:" + vm.getName() + " powered Suspended.");
                          
                   }
                         
                } 
        }
        
        if(flag)
        {
           System.out.println("Couldnt find the machine...Enter the valid name");
        }

        
        si.getServerConnection().logout();
    
    
    }
        
        catch(Exception e)
        {
        
        }
 
        
    }


     /**
     * Suspend the Virtual Machine
     */
 
    public void suspend(String name) 
    {
        try
       {
        ServiceInstance si = new ServiceInstance(
            new URL(Config.getVmwareHostURL()), Config.getVmwareLogin(), Config.getVmwarePassword(), true);
            
             Folder rootFolder = si.getRootFolder();
      
             ManagedEntity[] vms = new InventoryNavigator(rootFolder).searchManagedEntities(
             new String[][] { {"VirtualMachine", "name" }, }, true);
              
             boolean flag=true;
             
        for(int i=0; i<vms.length; i++)
        {
            
             VirtualMachine vm = (VirtualMachine) vms[i];
             System.out.println((vm.getName()));
             System.out.println("Done");
             VirtualMachineSummary summary = (VirtualMachineSummary) (vm.getSummary());
             System.out.println(summary.toString());
             VirtualMachineRuntimeInfo vmri = (VirtualMachineRuntimeInfo) vm.getRuntime();
                     
                 if(vm.getName().equals(name))
                 {
                 
                     flag=false;
                     if(vmri.getPowerState() == VirtualMachinePowerState.poweredOn  )
                   {   
                        Task task = vm.suspendVM_Task();
                        task.waitForMe();
                        System.out.println("vm:" + vm.getName() + " powered Suspended.");
                          
                   }
                         
                } 
        }
        
        if(flag)
        {
           System.out.println("Couldnt find the machine...Enter the valid name");
        }

        
        si.getServerConnection().logout();
    
    
    }
        
        catch(Exception e)
        {
        
        }
 
    }
    
    public void ForceShutDown(String name)
    {
         
        try
       {
        ServiceInstance si = new ServiceInstance(
            new URL(Config.getVmwareHostURL()), Config.getVmwareLogin(), Config.getVmwarePassword(), true);
            
             Folder rootFolder = si.getRootFolder();
      
             ManagedEntity[] vms = new InventoryNavigator(rootFolder).searchManagedEntities(
             new String[][] { {"VirtualMachine", "name" }, }, true);
              
             boolean flag=true;
             
        for(int i=0; i<vms.length; i++)
        {
            
             VirtualMachine vm = (VirtualMachine) vms[i];
             System.out.println((vm.getName()));
             System.out.println("Done");
             VirtualMachineSummary summary = (VirtualMachineSummary) (vm.getSummary());
             System.out.println(summary.toString());
             VirtualMachineRuntimeInfo vmri = (VirtualMachineRuntimeInfo) vm.getRuntime();
                     
                 if(vm.getName().equals(name))
                 {
                 
                     flag=false;
                     if(vmri.getPowerState() == VirtualMachinePowerState.poweredOn ||vmri.getPowerState() == VirtualMachinePowerState.poweredOn
                               || vmri.getPowerState() == VirtualMachinePowerState.suspended    )
                   {
                        
                       
                         if( vmri.getPowerState() == VirtualMachinePowerState.suspended)
                          {
                               Task task = vm.powerOnVM_Task(null);
                               task.waitForMe();
                            
                               System.out.println("vm:" + vm.getName() + " powered onn.");  
                          }
                       
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

        
        si.getServerConnection().logout();
    
    
    }
        
        catch(Exception e)
        {
        
        }
        
    }
    
    public void ShutDownAll()
    {
        
try
       {
        ServiceInstance si = new ServiceInstance(
            new URL(Config.getVmwareHostURL()), Config.getVmwareLogin(), Config.getVmwarePassword(), true);
            
             Folder rootFolder = si.getRootFolder();
      
             ManagedEntity[] vms = new InventoryNavigator(rootFolder).searchManagedEntities(
             new String[][] { {"VirtualMachine", "name" }, }, true);
                
        for(int i=0; i<vms.length; i++)
        {
            
             VirtualMachine vm = (VirtualMachine) vms[i];
             System.out.println((vm.getName()));
             System.out.println("Done");
             VirtualMachineSummary summary = (VirtualMachineSummary) (vm.getSummary());
             System.out.println(summary.toString());
             VirtualMachineRuntimeInfo vmri = (VirtualMachineRuntimeInfo) vm.getRuntime();
                     
                 if(vm.getName().equals("Anujkumar_naik_anuj.naik07@gmail.com"))
                 {
                      
                 }
                 
                else if(vmri.getPowerState() == VirtualMachinePowerState.poweredOn ||vmri.getPowerState() == VirtualMachinePowerState.poweredOn
                               || vmri.getPowerState() == VirtualMachinePowerState.suspended    )
                   {
                        
                       
                         if( vmri.getPowerState() == VirtualMachinePowerState.suspended)
                          {
                               Task task = vm.powerOnVM_Task(null);
                               task.waitForMe();
                            
                               System.out.println("vm:" + vm.getName() + " powered onn.");  
                          }
                       
                        Task task = vm.powerOffVM_Task();
                        task.waitForMe();
                        System.out.println("vm:" + vm.getName() + " powered off.");
                          
                   }
        
        }

        
        si.getServerConnection().logout();
    
    
    }
        
        catch(Exception e)
        {
        
        }
        
    
    }
    
   
    public static void main(String[] args)
    {
    
        MyVM ob = new MyVM();
        
        int x;
  BufferedReader object = new BufferedReader
     (new InputStreamReader(System.in));
  System.out.println("Enter your choice:");
  System.out.println("Enter 1 to PowewrOn Virtual Machine");
  System.out.println("Enter 2 to PowerOff Virtual Machine");
  System.out.println("Enter 3 to Reset Virtual Machine");
  System.out.println("Enter 4 to Suspend your Virtual Machine");
  System.out.println("Enter 5 to ForceShutDown Virtual Machine");
  System.out.println("Enter 6 to ShutDownAll Virtual Machine");
  try{
  x = Integer.parseInt(object.readLine());
  
  InputStreamReader isr =new InputStreamReader(System.in);
  BufferedReader br = new BufferedReader(isr);
  
  String name= br.readLine();
  
  switch (x){
     
  case 1:
  
  ob.powerOn(name);
  
  break;
  
  case 2:
  
  ob.powerOff(name);
  
  break;
  
  case 3:
  
  ob.reset(name);
  
  break;
  
  case 4:
  ob.suspend(name);
  
  break;
  
      
  case 5:
   
     ob.ForceShutDown(name);
  
   break;
  
 case 6:
 
    ob.ShutDownAll();  
 
  break;
  
  default:
  System.out.println("Invalid Entry!");
  }
        
        
        
    }
    
    catch(Exception E)
    {
    }
}
}
    

