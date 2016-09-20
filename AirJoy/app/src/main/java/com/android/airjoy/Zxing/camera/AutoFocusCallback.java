/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.airjoy.Zxing.camera;

import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * �����Զ��۽�
 */
final class AutoFocusCallback implements Camera.AutoFocusCallback
{

    private static final String TAG = AutoFocusCallback.class.getSimpleName();

    /**
     * �����Զ��۽���ʱ���ӳ٣���ʵҲ����ʱ����
     */
    private static final long AUTOFOCUS_INTERVAL_MS = 1500;

    /**
     * �����Զ��۽���handler(CaptureActivityHandler)
     */
    private Handler autoFocusHandler;
    /**
     * �Զ��۽��ķ�����Ϣ
     */
    private int autoFocusMessage;

    void setHandler(Handler autoFocusHandler, int autoFocusMessage)
    {
        this.autoFocusHandler = autoFocusHandler;
        this.autoFocusMessage = autoFocusMessage;
    }

    public void onAutoFocus(boolean success, Camera camera)
    {
        if (autoFocusHandler != null)
        {
            Message message = autoFocusHandler.obtainMessage(autoFocusMessage,
                    success);
            // Simulate continuous autofocus by sending a focus request every
            // AUTOFOCUS_INTERVAL_MS milliseconds.
            // Log.d(TAG, "Got auto-focus callback; requesting another");
            /**
             * �ӳ�һ����ʱ�䷢���Զ��۽�����Ϣ
             */
            autoFocusHandler.sendMessageDelayed(message, AUTOFOCUS_INTERVAL_MS);
            /**
             * ���ô�����Ϊ��,��Ϊ����Ϣ�����ʱ�򣬻���������handler
             */
            autoFocusHandler = null;
        }
        else
        {
            Log.d(TAG, "Got auto-focus callback, but no handler for it");
        }
    }

}
