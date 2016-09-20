package com.android.airjoy.user.bean;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.airjoy.app.pcfile.bean.Base;

/**
 * �û��ࣻ
 * 
 * @author �ֶ�
 * 
 */
public class User extends Base
{

    private Boolean res;//��ǰ�Ƿ��¼��ȥ��
    private String name;// �û���ƣ�
    private String password; // �˻����룻
    private String androidIp;// ��׿IP��Ϣ��
    private String email;//email��
    private String androidupdatetime;// ��׿����¼�¼���
    private boolean androidState;// ��׿��¼״̬��
    private String pcIp;// pc��Ip��
    private boolean pcState;// pc��¼״̬��
    private String pcupdatetime; // ����¼ʱ�䣻
    private String webupdatetime;// web�˵�¼ʱ�䣻
    private String addtime;//ע��ʱ�䣻
    private String devtag;//�豸��
    // json������
    public static User parse(String json)
    {
        User user = new User();
        try
        {
                JSONObject jsonObject = new JSONObject(json);
                
                user.setRes(jsonObject.getBoolean("res"));
                user.setName(jsonObject.getString("name"));
                user.setPassword(jsonObject.getString("password"));
                user.setAndroidIp(jsonObject.getString("androidIp"));
                user.setEmail(jsonObject.getString("email"));
                user.setAndroidupdatetime(jsonObject.getString("androidupdatetime"));
                user.setAndroidState(jsonObject.getBoolean("androidState"));
                user.setPcIp(jsonObject.getString("pcIp"));
                user.setPcState(jsonObject.getBoolean("pcState"));
                user.setPcupdatetime(jsonObject.getString("pcupdatetime"));
                user.setWebupdatetime(jsonObject.getString("webupdatetime"));
                user.setAddtime(jsonObject.getString("addtime"));
                user.setDevtag(jsonObject.getString("devtag"));

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return user;
    }
    public Boolean getRes(){return res;}
    public void setRes(Boolean res){this.res = res;}
    
    public String getName(){return name;}
    public void setName(String name){this.name = name;}
    
    public String getPassword(){return password;}
    public void setPassword(String password){this.password = password;}
    
    public String getAndroidIp(){return androidIp;}
    public void setAndroidIp(String androidIp){this.androidIp = androidIp;}
    
    public String getEmail(){return email;}
    public void setEmail(String email){this.email = email;}
    
    public String getAndroidupdatetime(){return androidupdatetime;}
    public void setAndroidupdatetime(String androidupdatetime){this.androidupdatetime = androidupdatetime;}
    
    public Boolean getAndroidState(){return androidState;}
    public void setAndroidState(Boolean androidState){this.androidState = androidState;}
    
    public String getPcIp(){return pcIp;}
    public void setPcIp(String pcIp){this.pcIp = pcIp;}
    
    public Boolean getPcState(){return pcState;}
    public void setPcState(Boolean pcState){this.pcState = pcState;}
    
    public String getPcupdatetime(){return pcupdatetime;}
    public void setPcupdatetime(String pcupdatetime){this.pcupdatetime = pcupdatetime;}
    
    public String getWebupdatetime(){return webupdatetime;}
    public void setWebupdatetime(String webupdatetime){this.webupdatetime = webupdatetime;}
    
    public String getAddtime(){return addtime;}
    public void setAddtime(String addtime){this.addtime = addtime;}
    
    public String getDevtag(){return devtag;}
    public void setDevtag(String devtag){this.name = devtag;}
    
   
}
