package edu.rosehulman.schmidkl.lifetrackerkls;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Recorder extends Activity {

//	private static final String LOG_TAG = "AudioRecordTest";
	private static String mFilePath = null;

//	private RecordButton mRecordButton = null;
	private MediaRecorder mRecorder = null;

//	private PlayButton mPlayButton = null;
	private MediaPlayer mPlayer = null;
	
	boolean mStartRecording = true;
	
	public static final String KEY_VOICE_PATH = "KEY_VOICE_PATH";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recorder);
		
		Intent data = getIntent();
		
		long listID = data.getLongExtra(ItemActivity.KEY_LIST_ID, 0);
		long itemID = data.getLongExtra(ItemActivity.KEY_VOICE_ITEM_ID, 0);
		
		mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
		mFilePath += "/audio" + listID + "_" + itemID + ".3gp";
		
		final Button recordButton = (Button) findViewById(R.id.startAndStopButton);
		
		recordButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onRecord(mStartRecording);
				if (mStartRecording) {
					recordButton.setText(R.string.stop_recording);
				} else {
					recordButton.setText(R.string.start_recording);
				}
				mStartRecording = !mStartRecording;				
			}
		});
		
		Button mDoneButton = (Button) findViewById(R.id.doneButton);
		
		mDoneButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent returnIntent = new Intent();		
				returnIntent.putExtra(KEY_VOICE_PATH, mFilePath);					
				setResult(RESULT_OK, returnIntent);
				finish();
			}
		});
	}
	
	private void onRecord(boolean start) {
		if (start) {
			startRecording();
		} else {
			stopRecording();
		}
	}
	
	private void startRecording() {
		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mRecorder.setOutputFile(mFilePath);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		try {
			mRecorder.prepare();
			mRecorder.start();
		} catch (IOException e) {
			Log.e(MainActivity.LT, "prepare() failed");
		}
	}
	
	private void stopRecording() {
		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (mRecorder != null) {
			mRecorder.release();
			mRecorder = null;
		}

		if (mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
	}
}