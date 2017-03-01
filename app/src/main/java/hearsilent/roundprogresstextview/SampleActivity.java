package hearsilent.roundprogresstextview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;

public class SampleActivity extends AppCompatActivity {

	Handler handler;
	Runnable runnable;

	RoundProgressTextView mRoundProgressTextView;
	private static int PROGRESS_DELAYED = 800;

	int progress = 15;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sample);

		findView();
		setUpView();
	}

	private void findView() {
		mRoundProgressTextView = (RoundProgressTextView) findViewById(R.id.roundProgressTextView);
	}

	private void setUpView() {
		mRoundProgressTextView.setMaxProgress(15);
		mRoundProgressTextView.setProgress(progress, PROGRESS_DELAYED - 100);

		handler = new Handler(Looper.getMainLooper());
		runnable = new Runnable() {

			@Override
			public void run() {
				if (progress > 0) {
					progress -= 1;
				} else {
					progress = 15;
				}
				mRoundProgressTextView.setProgress(progress, PROGRESS_DELAYED - 100);
				handler.postDelayed(runnable, PROGRESS_DELAYED);
			}
		};
		handler.postDelayed(runnable, PROGRESS_DELAYED);
	}
}
