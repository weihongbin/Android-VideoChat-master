package com.nercms.ryong21;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nercms.ImageUtils;
import com.nercms.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Random;

/**
 * 视频聊天活动
 * Created by zsg on 2016/6/3.
 */
public class VideoChatActivityDemo extends Activity {
    private ImageView view = null;
    private SurfaceView surfaceView;
    private Camera mCamera = null; //创建摄像头处理类
    private SurfaceHolder holder = null; //创建界面句柄，显示视频的窗口句柄
    boolean isRunning = false;
    public static final int STOPPED = 0;
    public static final int RECORDING = 1;
    public static boolean trueFlers = true;
    int status = STOPPED;

    protected LinkedList<byte[]> m_pkg_q;
    private Button bt = null;
    private Button bt1 = null;
    private TextView tv = null;
    private Button bt2 = null;
    private int indexS = 0;
    // private SocketTCP stcp  = new SocketTCP();
    public static Integer index = 0;
    //private Plays  pl = null;
    //private Record re = null;
    private EditText et = null;
    private EditText etDK = null;
    public static int indexMIC = 0;
    private AudRec2 m_recorder = null;
    public AudPly2 ap = new AudPly2();
    private String remote_ip;
    private int remote_port;
    /**
     * Called when the activity is first created.
     */
    AudioManager ar = null;
    DatagramSocket socket2;
    Button btn;
    boolean isF = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
              /*  case  1:
                    Bitmap BI= (Bitmap) msg.obj;
                    view.setImageBitmap(BI);
                    Log.e("whb", BI+"11111111111111111111111111" );
                    break;
                case 2:
                    Bitmap B= (Bitmap) msg.obj;
                    view.setImageBitmap(B);
                    Log.e("whb", B+"222222222222222222222222222222222" );
                    break;*/
                case 3:
                    Bitmap B3 = (Bitmap) msg.obj;
                    view.setImageBitmap(B3);
                    Log.e("whb", B3 + "222222222222222222222222222222222");
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_chat_test);
        remote_ip = getIntent().getStringExtra("remote_ip");
        remote_port=getIntent().getIntExtra("remote_port",8080);
        initView();
        btn = (Button) findViewById(R.id.btn);
        et = (EditText) findViewById(R.id.txtGO);
        etDK = (EditText) findViewById(R.id.dkGO);
        btn.setOnClickListener(new click());
        m_pkg_q = new LinkedList<byte[]>();
        ar = (AudioManager) VideoChatActivityDemo.this.getSystemService(Context.AUDIO_SERVICE);
        ar.setMicrophoneMute(true);
        help.ipAdd = getIPadd();
        ap.init(new byte[640]);
        ap.start();
        trueFlers = true;

    }
    class click implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            // TODO Auto-generated method stub
            if (!isF) {
                onClickRec();
                Log.e("TAG", "111111111111111111111");
                isF = true;
//                trueFlers = true;
                ((Button) v).setText("开始");
            } else {
                Log.e("TAG", "2222222222222222");
                onClickUP();
                isF = false;
//                trueFlers = false;
                ((Button) v).setText("结束");
            }
        }

    }

    public void onClickRec() {
        // TODO Auto-generated method stub
        if (indexS == 0) {
            index = 0;
                /*re = new Record();
				re.init();
				re.start();*/
            ar.setMicrophoneMute(false);
            String ipAdd = remote_ip;
            int dk = remote_port;
            m_recorder = new AudRec2(ipAdd, dk);
            m_pkg_q.clear();
            m_recorder.init(m_pkg_q);
            m_recorder.start();
            Logger lg = new Logger();
            lg.writeLog("asd");
            indexS = 1;
        }

    }

    public void onClickUP() {
        // TODO Auto-generated method stub
        index = 1;
        String ipAdd = remote_ip;
        int dk = remote_port;
        //m_recorder.goTo(ipAdd,dk);
        indexS = 0;
        //m_recorder = null;
        //m_recorder.goTo();
        ar.setMicrophoneMute(true);

    }
    public String getIPadd() {
        String IP_Address;
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface intf = en.nextElement();
                Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {

            ;
        }
        return "当前未连接互联网！！";
    }

    public String duanKou() {
        String str = getIPadd();
        str = str.substring(str.lastIndexOf(".") + 1);
        Random r = new Random();
        String dk = "";
        switch (str.length()) {
            case 1:
                for (int i = 0; i < 3; i++) {
                    dk += r.nextInt(9);
                }
                dk += str;
                break;
            case 2:
                for (int i = 0; i < 2; i++) {
                    dk += r.nextInt(9);
                }
                dk += str;
                break;
            case 3:
                for (int i = 0; i < 1; i++) {
                    dk += r.nextInt(9);
                }
                dk += str;
                break;
            default:
                break;
        }
        return dk;
    }
    private void initView() {
        view = (ImageView) this.findViewById(R.id.video_play);
        surfaceView = (SurfaceView) findViewById(R.id.surface_view);
        holder = surfaceView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                doStart();
            }
        }, 1000);

    }

    /**
     * 开启 接受 发送rtp线程  开启本地摄像头
     */
    public void doStart() {
        Recieve recieve = new Recieve();
        recieve.start();
        if (mCamera == null) {

            //摄像头设置，预览视频
            mCamera = Camera.open(1); //实例化摄像头类对象  0为后置 1为前置
            Camera.Parameters p = mCamera.getParameters(); //将摄像头参数传入p中
            p.setFlashMode("off");
            p.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
            p.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
            p.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            //p.setPreviewFormat(PixelFormat.YCbCr_420_SP); //设置预览视频的格式
            p.setPreviewFormat(ImageFormat.NV21);
            p.setPreviewSize(352, 288); //设置预览视频的尺寸，CIF格式352×288
            //p.setPreviewSize(800, 600);
            p.setPreviewFrameRate(15); //设置预览的帧率，15帧/秒
            mCamera.setParameters(p); //设置参数
            byte[] rawBuf = new byte[1400];
            mCamera.addCallbackBuffer(rawBuf);
            mCamera.setDisplayOrientation(90); //视频旋转90度
            try {
                mCamera.setPreviewDisplay(holder); //预览的视频显示到指定窗口
            } catch (IOException e) {
                e.printStackTrace();
            }
            mCamera.startPreview(); //开始预览

            //获取帧
            //预览的回调函数在开始预览的时候以中断方式被调用，每秒调用15次，回调函数在预览的同时调出正在播放的帧
            Callback a = new Callback();
            mCamera.setPreviewCallback(a);
        }
    }

    //mCamera回调的类
    class Callback implements Camera.PreviewCallback {
        @Override
        public void onPreviewFrame(byte[] frame, Camera camera) {
            Camera.Size size = camera.getParameters().getPreviewSize();
            Log.e("whb", "2222222222222222222222222222222222222222222");
            try {
                //调用image.compressToJpeg（）将YUV格式图像数据data转为jpg格式
                YuvImage image = new YuvImage(frame, ImageFormat.NV21, size.width, size.height, null);
                if (image != null) {
                    ByteArrayOutputStream outstream = new ByteArrayOutputStream();
                    image.compressToJpeg(new Rect(0, 0, size.width, size.height), 50, outstream);
                    outstream.flush();
//                    ByteArrayInputStream inputstream = new ByteArrayInputStream(image.getYuvData());
                    //启用线程将图像数据发送出去
                    Bitmap bmp = BitmapFactory.decodeByteArray(
                            outstream.toByteArray(), 0, outstream.size());
                    outstream.close();

                    Message msg = Message.obtain();
                    msg.obj = bmp;
                    msg.what = 2;
                    handler.sendMessage(msg);
                    Thread th = new MyThread(convertIconToString(bmp), remote_ip);
                    th.start();


                }
            } catch (Exception ex) {
                Log.e("Sys", "Error:" + ex.getMessage());
            }
        }
    }

    public static byte[] convertIconToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);

        byte[] appicon = baos.toByteArray();// 转为byte数组
        return appicon;

    }

    /**
     * 发送图片
     */
    class MyThread extends Thread {
        private byte byteBuffer[] = new byte[1024];
        private OutputStream outsocket;
        private byte[] myoutputstream;
        private String ipname;

        public MyThread(byte[] myoutputstream, String ipname) {
            this.myoutputstream = myoutputstream;
            this.ipname = ipname;
        }

        public void run() {
            try {
                socket2 = new DatagramSocket();
                DatagramPacket dp = new DatagramPacket(myoutputstream, myoutputstream.length, InetAddress.getByName(ipname), 8222);
                Log.e("whb", InetAddress.getByName(ipname) + "");
                socket2.send(dp);
                Log.e("whb长度", "发送"+ myoutputstream.length);
            } catch (Exception e) {
                // TODO: handle exception
                System.out.println("错误1");
            }
        }
//
    }

    /**
     * 接收rtp数据并解码 线程
     */
    class Recieve extends Thread {

        public void run() {

            while (true) {

                try {
                    DatagramSocket socket = new DatagramSocket(8222);

                    byte[] buf = new byte[352 * 288];  //定义byte数组
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);  //创建DatagramPacket对象

                    socket.receive(packet);  //通过套接字接收数据
                    Bitmap bmp = BitmapFactory.decodeByteArray(
                            buf, 0, packet.getLength());
                    Log.e("whb长度", "收到"+ packet.getLength());
                    Bitmap b = ImageUtils.rotateImage(270, bmp);
                    Message msg = Message.obtain();
                    msg.obj = b;
                    msg.what = 3;
                    handler.sendMessage(msg);
                    Log.e("whbw", "客户端发送的数据为：" + bmp);
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
//
    }



    /**
     * 关闭摄像头 并释放资源
     */
    public void close() {
        isRunning = false;
        //释放摄像头资源
        if (mCamera != null) {
            mCamera.setPreviewCallback(null); //停止回调函数
            mCamera.stopPreview(); //停止预览
            mCamera.release(); //释放资源
            mCamera = null; //重新初始化
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        close();
        trueFlers = false;
    }


}
