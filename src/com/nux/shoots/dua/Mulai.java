package com.nux.shoots.dua;

import java.util.Timer;
import java.util.TimerTask;

import net.rim.blackberry.api.menuitem.ApplicationMenuItem;
import net.rim.blackberry.api.menuitem.ApplicationMenuItemRepository;
import net.rim.device.api.applicationcontrol.ApplicationPermissions;
import net.rim.device.api.applicationcontrol.ApplicationPermissionsManager;
import net.rim.device.api.system.Alert;
import net.rim.device.api.system.Application;
import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.ApplicationManager;
import net.rim.device.api.system.ApplicationManagerException;
import net.rim.device.api.system.EventInjector;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.component.Dialog;

public class Mulai extends Application{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		_assertHasPermissions();
		Mulai ml = new Mulai(args);
		ml.enterEventDispatcher();
	}
	
	public Mulai(String[] args) {
		if(args.length>0){
			if(args[0].startsWith("do")){
				try{
					int a = Integer.parseInt(args[0].substring(2));
					doTimer(a);
				}catch(Exception e){
					System.exit(0);
				}
			}else{
				System.exit(0);
			}
		}else{
			ApplicationMenuItemRepository amir = ApplicationMenuItemRepository.getInstance();
			amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_SYSTEM,new puluhdetik());
			amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_SYSTEM,new limadetik());
			System.exit(0);
		}
	}
	
	private static class limadetik extends ApplicationMenuItem {
		limadetik() {
			super(0x00010000);
		}
		public String toString() {
			return "Timer 5 second";
		}
		public Object run(Object context) {
			launch(5);
			return null;
		}
	}

	private static class puluhdetik extends ApplicationMenuItem {
		puluhdetik() {
			super(0x00010000);
		}
		public String toString() {
			return "Timer 10 second";
		}
		public Object run(Object context) {
			launch(10);
			return null;
		}
	}
	
	public static void launch(int a){
		boolean lanjut = false;
		ApplicationDescriptor[] app = ApplicationManager.getApplicationManager().getVisibleApplications();
		for(int n=0;n<app.length;n++){
			if(app[n].getName().startsWith("Camera")){
				lanjut = true;
				//System.out.println(app[n].);
				//ApplicationManager.getApplicationManager().requestForeground(ApplicationManager.getApplicationManager().getProcessId(app[n]));
			}
		}
		if(!lanjut){
			Application.getApplication().invokeLater(new Runnable(){
				public void run() {
					Dialog.alert("Click this on Camera Application");	
				}
			});
			return;
		}else{
			try {
				ApplicationManager.getApplicationManager().launch("Nux_Shoot_2?do"+a);
			} catch (ApplicationManagerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	static Timer loadingTimer = new Timer();
    static TimerTask loadingTask;
    static int count = 0;
	public static void doTimer(final int a){	
		
		count=0;
		
		loadingTask = new TimerTask() {
            public void run() {
                count++;
                if (count == a * 10) {
                	loadingTimer.cancel();
                	loadingTask.cancel();
                	
                	//all os
                	//EventInjector.NavigationEvent ev = new NavigationEvent(EventInjector.NavigationEvent.NAVIGATION_CLICK, 0, 0, 0);
                	//ev.post();
                	
                	EventInjector.KeyCodeEvent ke = new EventInjector.KeyCodeEvent(EventInjector.KeyCodeEvent.KEY_DOWN,(char)Keypad.KEY_CONVENIENCE_1,0);
                	ke.post();
                	//storm
                	//EventInjector.TouchEvent et = new EventInjector.TouchEvent(EventInjector.TouchEvent.CLICK,168,426,191,444,-1);
                	//et.post();
                	System.exit(0);
                }else if(count == 1*10 || count == 2*10 || count == 3*10 ||
                		count == 4*10 ||count == 5*10 ||count == 6*10 ||
                		count == 7*10 ||count == 8*10 ||count == 9*10){
                	playTune();
                }
                

            }
        };
        loadingTimer.scheduleAtFixedRate(loadingTask, 100, 100);
	}
	private static final short[] tune = { 2349, 115, 0 };
	public static final boolean playTune() {
        if ( Alert.isAudioSupported() ) {
            Alert.startAudio(tune, 100);
            return true;
        }
        if ( Alert.isBuzzerSupported() ) {
            Alert.startBuzzer(tune, 100 );
            return true;
        }
        return false;
    }
	
	 // ASK FOR PERMISSIONS
	private static void _assertHasPermissions() {

		// Capture the current state of permissions and check against the requirements.
		ApplicationPermissionsManager apm = ApplicationPermissionsManager.getInstance();
		ApplicationPermissions original = apm.getApplicationPermissions();
		boolean permissionsOk = false;

		if (original.getPermission(ApplicationPermissions.PERMISSION_CROSS_APPLICATION_COMMUNICATION) ==
		    ApplicationPermissions.VALUE_ALLOW
		    &&
		    original.getPermission(ApplicationPermissions.PERMISSION_INPUT_SIMULATION) ==
		    ApplicationPermissions.VALUE_ALLOW
		    &&
		    original.getPermission(ApplicationPermissions.PERMISSION_DEVICE_SETTINGS) ==
		    ApplicationPermissions.VALUE_ALLOW)
		{
			permissionsOk = true;
		}else{
		  ApplicationPermissions permRequest = new ApplicationPermissions();
		  permRequest.addPermission(ApplicationPermissions.PERMISSION_CROSS_APPLICATION_COMMUNICATION);
		  permRequest.addPermission(ApplicationPermissions.PERMISSION_INPUT_SIMULATION);
		  permRequest.addPermission(ApplicationPermissions.PERMISSION_DEVICE_SETTINGS);

		  permissionsOk = apm.invokePermissionsRequest(permRequest);

		}

		if (!permissionsOk) {
			_assertHasPermissions();
		}
	}
}
