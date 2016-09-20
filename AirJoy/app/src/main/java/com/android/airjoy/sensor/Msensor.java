/*
 * ����������
 * #define SENSOR_TYPE_ACCELEROMETER 1 //���ٶ�
    * #define SENSOR_TYPE_MAGNETIC_FIELD 2 //����
    * #define SENSOR_TYPE_ORIENTATION 3 //����
    * #define SENSOR_TYPE_GYROSCOPE 4 //������
    * #define SENSOR_TYPE_LIGHT 5 //���߸�Ӧ
    * #define SENSOR_TYPE_PRESSURE 6 //ѹ��
    * #define SENSOR_TYPE_TEMPERATURE 7 //�¶�
    * #define SENSOR_TYPE_PROXIMITY 8 //�ӽ�
    * #define SENSOR_TYPE_GRAVITY 9 //����
    * #define SENSOR_TYPE_LINEAR_ACCELERATION 10//���Լ��ٶ�
    * #define SENSOR_TYPE_ROTATION_VECTOR 11//��תʸ��
 * */
package com.android.airjoy.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

public class Msensor implements SensorEventListener
{
    private SensorManager mSensorManager;
    String TAG = "Msensor";
    private int mX, mY, mZ;
    private Sensor mSensor;
    private Context _context;

    public Msensor(Context context)
    {
        this._context = context;
    }
    /*
     * �����˴���������;
     * */
    public Boolean Start()
    {
        try
        {
            mSensorManager = (SensorManager) _context.getSystemService(_context.SENSOR_SERVICE);
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);// TYPE_GRAVITY
            if (null == mSensorManager)
            {
                throw new Exception();
            }
            // ��������ľ�׼��
            mSensorManager.registerListener(this, mSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);// SENSOR_DELAY_GAME
            return true;
        }
        catch (Exception error)
        {
                Toast.makeText(this._context, "����ֻ�֧�ִ˴�������", Toast.LENGTH_SHORT).show();  
                return false;
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }
    /*
     * �رմ˴���������;
     * */
    public void CloseSensor()
    {
        this.mSensorManager.unregisterListener(this, this.mSensor);

    }
    /*
     * �������Ļص�����;
     * */
    public void onSensorChanged(SensorEvent event)
    {
        if (event.sensor == null)
        {
            return;
        }
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE)
        {
            float x = event.values[0];
            float y = event.values[1];
            float z =  event.values[2];

            Log.d(TAG, "pX:" + x + "  Y:" + y + "  Z:" + z );
            float maxvalue = getMaxValue(x, y, z);
            Toast.makeText(this._context, "pX:" + x + "  pY:" + y + "  pZ:" + z , Toast.LENGTH_SHORT).show();  

        }
    }

    /**
     * ��ȡһ�����ֵ
     * 
     * @param px
     * @param py
     * @param pz
     * @return
     */
    public float getMaxValue(float px, float py, float pz)
    {
        float max = 0;
        if (px > py && px > pz)
        {
            max = px;
        }
        else if (py > px && py > pz)
        {
            max = py;
        }
        else if (pz > px && pz > py)
        {
            max = pz;
        }

        return max;
    }

}
