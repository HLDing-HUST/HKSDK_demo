package com.example.cameratest1_17;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cameratest1_17.Control.SDKGuider;
import com.example.cameratest1_17.jna.HCNetSDKByJNA;
import com.example.cameratest1_17.jna.HCNetSDKJNAInstance;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.INT_PTR;
import com.hikvision.netsdk.NET_DVR_JPEGPARA;
import com.hikvision.netsdk.NET_DVR_PREVIEWINFO;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private SurfaceView m_surfaceView = null;
    private int m_iPreviewHandle = -1;
    private SurfaceHolder holder;
    private String userip = "222.20.79.135";
    private String username = "admin";
    private String userpassward = "admin12345";
    private int port = 8000;
    private ImageView imageview;
    private Button start, refresh, stop;
    private int lUserID;
    private boolean play = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = findViewById(R.id.button_preview_start);
        m_surfaceView = findViewById(R.id.Surface_Preview_Play);
        holder = m_surfaceView.getHolder();
        imageview = findViewById(R.id.image);
        refresh = findViewById(R.id.button_refresh);
        m_surfaceView.getHolder().addCallback(this);
        m_surfaceView.setZOrderOnTop(true);
        stop = findViewById(R.id.button_stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SDKGuider.g_sdkGuider.m_comPreviewGuider.RealPlay_Stop_jni(m_iPreviewHandle);
                play = false;
//                Canvas canvas = null;
//                canvas=holder.lockCanvas();
//                Paint p = new Paint();
//                Rect r = new Rect(10, 5, 30, 25);
//                canvas.drawRect(r, p);
//                holder.unlockCanvasAndPost(canvas);
            }
        });
        HCNetSDK.getInstance().NET_DVR_Init();
        if(m_iPreviewHandle != -1){
            SDKGuider.g_sdkGuider.m_comPreviewGuider.RealPlay_Stop_jni(m_iPreviewHandle);
        }
        HCNetSDKByJNA.NET_DVR_USER_LOGIN_INFO loginInfo = new HCNetSDKByJNA.NET_DVR_USER_LOGIN_INFO();
        System.arraycopy(userip.getBytes(), 0, loginInfo.sDeviceAddress, 0, userip.length());
        System.arraycopy(username.getBytes(), 0, loginInfo.sUserName, 0, username.length());
        System.arraycopy(userpassward.getBytes(), 0, loginInfo.sPassword, 0, userpassward.length());
        loginInfo.wPort = (short) port;
        HCNetSDKByJNA.NET_DVR_DEVICEINFO_V40 deviceInfo = new HCNetSDKByJNA.NET_DVR_DEVICEINFO_V40();
        loginInfo.write();
        lUserID = HCNetSDKJNAInstance.getInstance().NET_DVR_Login_V40(loginInfo.getPointer(), deviceInfo.getPointer());
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_surfaceView.getHolder().addCallback(MainActivity.this);
                m_surfaceView.setZOrderOnTop(true);
                m_surfaceView.setVisibility(View.VISIBLE);
                SDKGuider.g_sdkGuider.m_comPreviewGuider.RealPlaySurfaceChanged_jni(m_iPreviewHandle, 0, holder);
                NET_DVR_PREVIEWINFO struPlayInfo = new NET_DVR_PREVIEWINFO();
                struPlayInfo.lChannel = 1;
                struPlayInfo.dwStreamType = 0;
                struPlayInfo.bBlocked = 1;
                struPlayInfo.hHwnd = m_surfaceView.getHolder();
                m_iPreviewHandle = SDKGuider.g_sdkGuider.m_comPreviewGuider.RealPlay_V40_jni(0,struPlayInfo, null);
                if (m_iPreviewHandle < 0)
                {
                    Toast.makeText(MainActivity.this,"NET_DVR_RealPlay_V40 fail, Err:"+ SDKGuider.g_sdkGuider.GetLastError_jni(),Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(MainActivity.this,"NET_DVR_RealPlay_V40 Succ " ,Toast.LENGTH_SHORT).show();
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SDKGuider.g_sdkGuider.m_comPreviewGuider.RealPlay_Stop_jni(m_iPreviewHandle);
                m_surfaceView.setVisibility(View.INVISIBLE);
                play = true;
                refreshThread();
            }
        });
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        m_surfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        if (-1 == m_iPreviewHandle) {
            return;
        }
        Surface surface = holder.getSurface();
        if (surface.isValid()) {
            if (-1 == SDKGuider.g_sdkGuider.m_comPreviewGuider.RealPlaySurfaceChanged_jni(m_iPreviewHandle, 0, holder))
                Toast.makeText(MainActivity.this,"NET_DVR_PlayBackSurfaceChanged"+ SDKGuider.g_sdkGuider.GetLastError_jni(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        if (-1 == m_iPreviewHandle) {
            return;
        }
        if (holder.getSurface().isValid()) {
            if (-1 == SDKGuider.g_sdkGuider.m_comPreviewGuider.RealPlaySurfaceChanged_jni(m_iPreviewHandle, 0, null))
            {
                Toast.makeText(MainActivity.this,"NET_DVR_RealPlaySurfaceChanged"+ SDKGuider.g_sdkGuider.GetLastError_jni(),Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void refreshThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                NET_DVR_JPEGPARA PARA = new NET_DVR_JPEGPARA();
                PARA.wPicQuality = 0;
                PARA.wPicSize = 4;
                int iBufferSize = 800*600;
                while(play){
                    byte[] sbuffer = new byte[iBufferSize];
                    INT_PTR bytesRerned = new INT_PTR();
                    boolean flag = HCNetSDK.getInstance().NET_DVR_CaptureJPEGPicture_NEW(lUserID, 1, PARA, sbuffer, iBufferSize, bytesRerned);
                    if (flag){
                        Bitmap bitmap = BitmapFactory.decodeByteArray(sbuffer,0,sbuffer.length);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageview.setImageBitmap(bitmap);
                            }
                        });
                    }
                    else {
                        Log.e("frame","NET_DVR_ERROR:"+HCNetSDK.getInstance().NET_DVR_GetLastError());
                    }
                }
            }
        }).start();
    }
}