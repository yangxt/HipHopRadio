package cz.lala.HipHopStage;

import com.spoledge.aacplayer.ArrayAACPlayer;
import com.spoledge.aacplayer.ArrayDecoder;
import com.spoledge.aacplayer.Decoder;
import com.spoledge.aacplayer.PlayerCallback;










import cz.lala.HipHopStage.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class PlayerActivity extends Activity implements View.OnClickListener, PlayerCallback{

    private com.spoledge.aacplayer.AACPlayer aacPlayer;
    private com.spoledge.aacplayer.AACFileChunkPlayer aacFileChunkPlayer;
    
    Handler uihandler;
    Button btnplay;
    Button btnfb;
    Button btnpho;
    Button btnnew;
    boolean a;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	setContentView(R.layout.act_playeractivity);
    	btnplay = (Button)findViewById(R.id.act_playeractivity_btnplay);
    	btnplay.setOnClickListener(this);
    	btnfb = (Button)findViewById(R.id.act_playeractivity_btnfb);
    	btnfb.setOnClickListener(this);
        btnpho = (Button)findViewById(R.id.act_playeractivity_btnpho);
        btnpho.setOnClickListener(this);
        btnnew = (Button)findViewById(R.id.act_playeractivity_btnnew);    
        btnnew.setOnClickListener(this);
        uihandler = new Handler();
        if(a)
        {
        	btnplay.setText("STOP");
        }
        
    	super.onCreate(savedInstanceState);
    }
	
	
	
	
	@Override
	public void playerStarted() {
		 uihandler.post( new Runnable() {
	            public void run() {
	            	btnplay.setText("STOP");
	            }
			});
	}

	@Override
	public void playerPCMFeedBuffer(boolean isPlaying, int audioBufferSizeMs,
			int audioBufferCapacityMs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playerStopped(int perf) {
		uihandler.post(new Runnable() {
		public void run() {
			btnplay.setText("START");
		}
	});
		
		a=false;
		
	}

	@Override
	public void playerException(Throwable t) {
		
		  if (a) playerStopped( 0 );
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.act_playeractivity_btnplay:
			if (a) {
				stop();
				a= false;
			}
			else {
				start();
				a = true;
				
			}
			
			break;
		case R.id.act_playeractivity_btnfb:
			try {
			    this.getPackageManager()
			            .getPackageInfo("com.facebook.katana", 0); //Checks if FB is even installed.
			    startActivity( new Intent(Intent.ACTION_VIEW,
			            Uri.parse("fb://page/75012764740"))); //Trys to make intent with FB's URI
			} catch (Exception e) {
				  startActivity( new Intent(Intent.ACTION_VIEW,
			            Uri.parse("https://www.facebook.com/HipHopStage.cz"))); //catches and opens a url to the desired page
			}
			
			break;
		case R.id.act_playeractivity_btnpho:
			Intent intent = new Intent(Intent.ACTION_CALL);
			intent.setData(Uri.parse("tel:222 264 860"));
			startActivity(intent); 
			break;
		case R.id.act_playeractivity_btnnew:
		Intent toNews = new Intent(this, NewsActivity.class);
		startActivity(toNews);
			break;
		default:
			break;
		}
		
	}
	 private void start() {
	        stop();
	        aacPlayer = new ArrayAACPlayer( ArrayDecoder.create(Decoder.DECODER_FAAD2), this,
	                                       1500, 700);
	        aacPlayer.playAsync("http://icecast2.play.cz:8000/HipHopStage.aac");
	    }


	    private void stop() {
	        if (aacFileChunkPlayer != null) { aacFileChunkPlayer.stop(); aacFileChunkPlayer = null; }
	        if (aacPlayer != null) { aacPlayer.stop(); aacPlayer = null; }
	        btnplay.setText("PLAY");
	    }

}
