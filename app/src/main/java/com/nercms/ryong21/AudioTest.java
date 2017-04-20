package com.nercms.ryong21;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.nercms.R;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Random;


public class AudioTest extends Activity
{
    /* menu id define */
	//public static final int MENU_START_ID = Menu.FIRST ;
	//public static final int MENU_STOP_ID = Menu.FIRST + 1 ;
	//public static final int MENU_EXIT_ID = Menu.FIRST + 2 ;

	// protected AudPly     m_player ;
	public static final int STOPPED = 0;
	public static final int RECORDING = 1;
	public static boolean trueFlers = true;
	int status = STOPPED;

	protected LinkedList<byte []> m_pkg_q ;
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
	public static int indexMIC=0;
	private AudRec2 m_recorder = null;
	public  AudPly2 ap = new AudPly2();
	/** Called when the activity is first created. */
	AudioManager ar =null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		et = (EditText)findViewById(R.id.txtGO);
		etDK = (EditText) findViewById(R.id.dkGO);
		tv = (TextView)findViewById(R.id.txt);
		m_pkg_q = new LinkedList<byte[]>();
		ar= (AudioManager)AudioTest.this.getSystemService(Context.AUDIO_SERVICE);
		ar.setMicrophoneMute(true);
		tv.setText("您的IP为："+getIPadd());
		help.ipAdd = getIPadd();
        /*SeberSocket2 ss = new SeberSocket2(dkH);
      	ss.start();*/
		ap.init(new byte[640]);
		ap.start();

		trueFlers = true;

       /* Trem t = new Trem();
        t.start();   */
	}

	class click implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

		/*	if (index==0) {
				index=1;
				stcp.start();
			}*/

			//stcp.chanchu(index,tv);
		}

	}
	public void onClickRec() {
		// TODO Auto-generated method stub
		if (indexS==0) {
			index =0;
				/*re = new Record();
				re.init();
				re.start();*/
			ar.setMicrophoneMute(false);
			String ipAdd = et.getText().toString();
			int dk = Integer.parseInt(etDK.getText().toString());
			m_recorder = new AudRec2(ipAdd,dk) ;
			m_pkg_q.clear();
			m_recorder.init(m_pkg_q) ;
			m_recorder.start() ;
			Logger lg = new Logger();
			lg.writeLog("asd");
			indexS =1;

				/*recorderInstance = new PcmRecorder();
				Thread th = new Thread(recorderInstance);
				th.start();
				recorderInstance.setRecording(true);*/
		}

	}
	public void onClickUP() {
		// TODO Auto-generated method stub
		index=1;
		String ipAdd = et.getText().toString();
		int dk = Integer.parseInt(etDK.getText().toString());
		//m_recorder.goTo(ipAdd,dk);
		indexS= 0;
		//m_recorder = null;
		//m_recorder.goTo();
		ar.setMicrophoneMute(true);

	}




	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode== KeyEvent.KEYCODE_VOLUME_DOWN) {
			onClickRec();
			return true;
		}else if(keyCode== KeyEvent.KEYCODE_VOLUME_UP){
			onClickRec();
			return true;
		}else if(keyCode== KeyEvent.KEYCODE_BACK){
			trueFlers=false;
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode== KeyEvent.KEYCODE_VOLUME_DOWN) {
			onClickUP();
			return true;
		}else if(keyCode== KeyEvent.KEYCODE_VOLUME_UP){
			onClickUP();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	public String getIPadd(){
		String IP_Address;
		try {
			Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
			while(en.hasMoreElements())
			{
				NetworkInterface intf = en.nextElement();
				Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
				while(enumIpAddr.hasMoreElements())
				{
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress())
					{
						return inetAddress.getHostAddress();
					}
				}
			}
		} catch (SocketException ex) {

			;
		}
		return "当前未连接互联网！！";
	}
	public String duanKou(){
		String str= getIPadd();
		str = str.substring(str.lastIndexOf(".")+1);
		Random r = new Random();
		String dk = "";
		switch (str.length()) {
			case 1:
				for (int i = 0; i < 3; i++) {
					dk+=r.nextInt(9);
				}
				dk+=str;
				break;
			case 2:
				for (int i = 0; i < 2; i++) {
					dk+=r.nextInt(9);
				}
				dk+=str;
				break;
			case 3:
				for (int i = 0; i < 1; i++) {
					dk+=r.nextInt(9);
				}
				dk+=str;
				break;
			default:
				break;
		}
		return dk;
	}
}
