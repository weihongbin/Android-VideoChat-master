package com.nercms.ryong21;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;


public class AudRec2 extends Thread
{
	private AudioRecord m_in_rec ;
	private LinkedList m_in_q ;
	private int         m_in_buf_size ;
	private short []     m_in_bytes ;
	private byte []     m_in_bytes1 ;
	private boolean     m_keep_running ;
	private String ipAdd;
	private int dk;
	private Socket s = null;
	ObjectOutputStream oos =null;

	AudRec2(String ipAdd, int dk) {
		System.out.println("≤‚ ‘1");

		m_keep_running = true ;
		this.ipAdd=ipAdd;
		this.dk = dk;
		System.out.println("s1");
       /* try {
			//s = new Socket(ipAdd,dk,null,4777);
			oos = new ObjectOutputStream(new BufferedOutputStream(s.getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	public void init(LinkedList data_q) {
		m_in_q = data_q ;
		m_in_buf_size = android.media.AudioRecord.getMinBufferSize(8000,
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT);

		m_in_rec = new AudioRecord(MediaRecorder.AudioSource.MIC,
				8000,
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT,
				m_in_buf_size) ;

		m_in_bytes = new short [m_in_buf_size] ;
		m_in_bytes1 = new byte[m_in_buf_size];
		//System.out.println(data_q);
		m_keep_running = true ;
		System.out.println("s2");

	}

	public void free()
	{
		m_keep_running = false ;
		try {
			sleep(1000) ;
		} catch(Exception e) {
			logd("sleep exceptions...\n") ;
		}
	}

	public void run() {
		int bytes_read ;
		int i ;
		m_in_rec.startRecording() ;
		while(AudioTest.index==0) {
			bytes_read = m_in_rec.read(m_in_bytes1, 0, m_in_buf_size) ;
			//bytes_pkg = m_in_bytes.clone() ;
			byte[] sr = new byte[m_in_bytes.length];
			//sp.encode(m_in_bytes, 0, sr, bytes_read);
			try {
				DatagramSocket socket = new DatagramSocket();
				InetSocketAddress isa = new InetSocketAddress(ipAdd, dk);
				InetAddress add = isa.getAddress();
				DatagramPacket dp = new DatagramPacket(m_in_bytes1,
						m_in_bytes1.length, add, 4562);
				socket.send(dp);
				socket.close();
				help.index ++;
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("¥ÌŒÛ1");
			}

			//yield() ;
		}
		m_in_rec.stop() ;
		m_in_rec.release();
		m_in_rec = null ;
		m_in_bytes = null ;
	}
	/*public void run() {
        int bytes_read ;
        byte [] bytes_pkg ;
        int i ;
        System.out.println("a1");
        m_in_rec.startRecording() ;
        while(AudioTest.index==0) {
            bytes_read = m_in_rec.read(m_in_bytes, 0, m_in_buf_size) ;
            bytes_pkg = m_in_bytes.clone() ;
            synchronized(m_in_q) {
                m_in_q.add(bytes_pkg) ;
            }
            //yield() ;
        }

        m_in_rec.stop() ;
        m_in_rec = null ;
        m_in_bytes = null ;

    }*/
	public void goTo(){
		try {
			System.out.println("a");
			DatagramSocket socket = new DatagramSocket();
			InetSocketAddress isa = new InetSocketAddress(ipAdd, dk);
			InetAddress add = isa.getAddress();
			for (int i = 0; i < m_in_q.size(); i++) {
				System.out.println(i);
				byte[] bt=(byte[]) m_in_q.get(i);
				DatagramPacket dp = new DatagramPacket(bt, bt.length, add, 4562);
				socket.send(dp);
			}
			byte[] be = new byte[]{0};
			DatagramPacket dp = new DatagramPacket(be, be.length, add, 4562);
			socket.send(dp);
			socket.close();
			/*while (true) {
				synchronized (m_in_q) {
					if (m_in_q.isEmpty()) {
						byte[] bt=(byte[]) m_in_q.removeFirst();
						DatagramSocket socket = new DatagramSocket();
						InetSocketAddress isa = new InetSocketAddress(ipAdd, dk);
						InetAddress add = isa.getAddress();
						DatagramPacket dp = new DatagramPacket(bt, bt.length, add, 4562);
						socket.send(dp);
						socket.close();
					}else{
						break;
					}

				}
			}*/
			// new Thread(R1).start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("¥ÌŒÛ");
			e.printStackTrace();
		}
	}

	public void logd(String s) {
		Log.d("AudRec", s) ;
	}
}

