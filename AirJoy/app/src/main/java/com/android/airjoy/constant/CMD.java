
package com.android.airjoy.constant;
/**
 * Created by AddBean on 2016/3/20.
 */
public  class CMD
{
    public CMD(){
        
    }
    

    public static final class MouseEventFlag
    {
        public static final String Move = "0x0001";
        public static final String LeftDown = "0x0002";
        public static final String LeftUp = "0x0004";
        public static final String RightDown = "0x0008";
        public static final String RightUp = "0x0010";
        public static final String MiddleDown = "0x0020";
        public static final String MiddleUp = "0x0040";
        public static final String XDown = "0x0080";
        public static final String XUp = "0x0100";
        public static final String Wheel = "0x0800";
        public static final String VirtualDesk = "0x4000";
        public static final String Absolute = "0x8000";
    };
    public static final class CmdToComputer
    {
        public static final String Shutdown ="C:shutdown -s -t "+"1"+" -c '���ڹػ�' -f:End";
        public static final String Restart ="C:shutdown -r:End";
        public static final String Sleep="C:rundll32 powrprof.dll,SetSuspendState:End";
        public static final String ShutdownBytime ="C:shutdown -s -t "+"time"+" -c 'shutdown' -f:End";
        public static final String LogOff ="C:shutdown -l:End";
        public static final String LockComputer ="C:rundll32.exe user32.dll,LockWorkStation:End";
        public static final String KillAirjoy="C:taskkill /f /im Airjoy.exe:End";
        public static final String RestartExplorer="C:taskkill /f /im explorer.exe & start explorer.exe:End";
        public static final String SuspendState="C:rundll32.exe powrprof.dll SetSuspendState:End";
    }
    public static final class HackCmd{
        public static final String  Popup="C:start mshta vbscript:msgbox(\""+"%s"+"\",VbExclamation,\""+"%s"+"\")(window.close):End";
        public static final String  PopupStop="C:taskkill /f /im cmd.exe:End";
        public static final String RestartLoop="C:schtasks /create /sc daily /tn 123 /tr \"shutdown /r /f /t 0\" /du 0005:00 /ri 1 /Z:End";
        public static final String PopupFolder="C:@for /l %%i in (1,1,20) do @explorer:End";
        public static final String PopupCmdForever="C:for /l %%i in (1,1,100) do (start %0):End";
       
    
    }
    public static final class JoyCmd{
        public static final String XButton="69";
        public static final String YButton="83";
        public static final String AButton="32";
        public static final String BButton="74";
        public static final String Button1="66";
        public static final String Button2="72";
        public static final String UpKey="87";
        public static final String DownKey="83";
        public static final String LeftKey="65";
        public static final String RightKey="68";
    }
}
