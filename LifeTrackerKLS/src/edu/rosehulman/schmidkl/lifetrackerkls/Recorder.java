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
	private static String mFileName = null;

//	private RecordButton mRecordButton = null;
	private MediaRecorder mRecorder = null;

//	private PlayButton mPlayButton = null;
	private MediaPlayer mPlayer = null;
	
	boolean mStartRecording = true;
	
	public static final String KEY_VOICE_PATH = "KEY_VOICE_PATH";


	public Recorder() {
		mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
		mFileName += "/audiorecordtest.3gp";
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_recorder);
		
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
				returnIntent.putExtra(KEY_VOICE_PATH, mFileName);					
				setResult(RESULT_OK, returnIntent);
				finish();
			}
		});
		

//		LinearLayout ll = new LinearLayout(getActivity());
//		mRecordButton = new RecordButton(getActivity().getApplicationContext());
//		ll.addView(mRecordButton, new LinearLayout.LayoutParams(
//				ViewGroup.LayoutParams.WRAP_CONTENT,
//				ViewGroup.LayoutParams.WRAP_CONTENT, 0));
//		mPlayButton = new PlayButton(getActivity());
//		ll.addView(mPlayButton, new LinearLayout.LayoutParams(
//				ViewGroup.LayoutParams.WRAP_CONTENT,
//				ViewGroup.LayoutParams.WRAP_CONTENT, 0));
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
		mRecorder.setOutputFile(mFileName);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		try {
			mRecorder.prepare();
		} catch (IOException e) {
			Log.e(MainActivity.LT, "prepare() failed");
		}
		mRecorder.start();
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

//	class RecordButton extends Button {
//
//		OnClickListener clicker = new OnClickListener() {
//			public void onClick(View v) {
//				onRecord(mStartRecording);
//				if (mStartRecording) {
//					setText("Stop recording");
//				} else {
//					setText("Start recording");
//				}
//				mStartRecording = !mStartRecording;
//			}
//		};

//		public RecordButton(Context ctx) {
//			super(ctx);
//			setText("Start recording");
//			setOnClickListener(clicker);
//		}
//	}
//	class PlayButton extends Button {
//		boolean mStartPlaying = true;
//
//		OnClickListener clicker = new OnClickListener() {
//			public void onClick(View v) {
//				onPlay(mStartPlaying);
//				if (mStartPlaying) {
//					setText("Stop playing");
//				} else {
//					setText("Start playing");
//				}
//				mStartPlaying = !mStartPlaying;
//			}
//		};
//
//		public PlayButton(Context ctx) {
//			super(ctx);
//			setText("Start playing");
//			setOnClickListener(clicker);
//		}
//	}
//
//
//
//	
//
//	private void onPlay(boolean start) {
//		if (start) {
//			startPlaying();
//		} else {
//			stopPlaying();
//		}
//	}
//
//	private void startPlaying() {
//		mPlayer = new MediaPlayer();
//		try {
//			mPlayer.setDataSource(mFileName);
//			mPlayer.prepare();
//			mPlayer.start();
//		} catch (IOException e) {
//			Log.e(MainActivity.LT, "prepare() failed");
//		}
//	}
//	
//	
//	private void stopPlaying() {
//		mPlayer.release();
//		mPlayer = null;
//	}
}