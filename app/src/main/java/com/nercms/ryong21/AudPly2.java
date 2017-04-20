package com.nercms.ryong21;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.LinkedList;


public class AudPly2 extends Thread implements Runnable
{
	private AudioTrack m_out_trk ;
	private LinkedList m_out_q  ;
	private int        m_out_buf_size ;
	private byte []    m_out_bytes ;
	private boolean    m_keep_running ;
	byte [] bytes_pkg;

	AudPly2() {
		m_keep_running = true ;
	}

	public void init(byte[] data_q) {
		//bytes_pkg = data_q ;


		m_out_buf_size = android.media.AudioTrack.getMinBufferSize(7000,
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT);

		m_out_trk = new AudioTrack(AudioManager.STREAM_VOICE_CALL, 7000,
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT,
				m_out_buf_size,
				AudioTrack.MODE_STREAM);//实时buffer

		m_keep_running = true ;
	}
	public void init2(LinkedList lkl){
    	/* for (int i = 0; i < data_q.size(); i++) {
			m_out_q.add(data_q.get(i));
		}*/
		m_out_q=lkl;
		m_out_buf_size = android.media.AudioTrack.getMinBufferSize(44100,
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT);

		m_out_trk = new AudioTrack(AudioManager.STREAM_VOICE_CALL, 44100,
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT,
				m_out_buf_size,
				AudioTrack.MODE_STREAM);

		m_keep_running = true ;

	}
	public void list(LinkedList lk){
		m_out_q = lk;
	}

	public void free() {
		m_keep_running = false ;
		try {
			sleep(1000) ;
		} catch(Exception e) {
			logd("sleep exceptions...\n") ;
		}
	}
	public void run(){
		try {
			m_out_trk.play();
			byte[] bte = new byte[640];

			DatagramSocket ds = new DatagramSocket(4562);
			DatagramPacket dp = new DatagramPacket(bte, 640);
			while (AudioTest.trueFlers) {
				ds.receive(dp);
				//short[] st = new short[sp.getFrameSize()];
				if (m_out_q==null) {
					m_out_q = new LinkedList();
				}
				//sp.decode(bte, st, sp.getFrameSize());
				m_out_q.add(bte);
				synchronized (m_out_q) {
					if (!m_out_q.isEmpty()) {
						m_out_bytes = (byte[]) (m_out_q.removeFirst());
						bytes_pkg = m_out_bytes.clone();
						m_out_bytes = null;
					}

				}
				if (bytes_pkg != null) {
					m_out_trk.write(bytes_pkg, 0, bytes_pkg.length);
				}
				bytes_pkg=null;
				//m_out_trk.write(bte, 0, m_out_buf_size);
				//
				//yield() ; 

			}

			m_out_trk.stop();
			m_out_trk.release();
			m_out_trk = null;
		} catch (Exception e) {
			// TODO: handle exception
			System.exit(0);
		}

	}
	public void p(){

	}

	public void logd(String s) {
		Log.d("AudPly", s) ;
	}
}
