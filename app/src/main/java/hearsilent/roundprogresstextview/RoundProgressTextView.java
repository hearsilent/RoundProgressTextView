package hearsilent.roundprogresstextview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;

public class RoundProgressTextView extends TextView {

	public static final float DEFAULT_PROGRESS_STOKE_WIDTH = 3.5f;
	public static final float DEFAULT_STOKE_WIDTH = 1f;
	public static final int DEFAULT_PROGRESS_COLOR = Color.RED;
	public static final int DEFAULT_PROGRESS = 0;
	public static final int DEFAULT_MAX = 100;

	private int maxProgress;
	private float maxProgressConvert;
	private int progress;
	float progressConvert;

	private RectF progressRightOval = new RectF(), progressLeftOval = new RectF(), rightOval =
			new RectF(), leftOval = new RectF();
	private Paint progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG), paint =
			new Paint(Paint.ANTI_ALIAS_FLAG);

	private float progressStrokeWidth;
	private float strokeWidth;
	private int progressColor;

	private int width;
	private int height;
	int _width;
	private int disWidth;
	private float semicircle;

	public RoundProgressTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressTextView);
		progressStrokeWidth = a.getFloat(R.styleable.RoundProgressTextView_progress_stoke_width,
				DEFAULT_PROGRESS_STOKE_WIDTH);
		strokeWidth =
				a.getFloat(R.styleable.RoundProgressTextView_stoke_width, DEFAULT_STOKE_WIDTH);
		progressColor = a.getColor(R.styleable.RoundProgressTextView_progress_color,
				DEFAULT_PROGRESS_COLOR);
		progress = a.getInt(R.styleable.RoundProgressTextView_progress, DEFAULT_PROGRESS);
		maxProgress = a.getInt(R.styleable.RoundProgressTextView_max, DEFAULT_MAX);
		progress = progress > maxProgress ? maxProgress : progress;
		a.recycle();

		setUpPaint();
	}

	private void setUpPaint() {
		progressPaint.setStrokeWidth(progressStrokeWidth);
		progressPaint.setColor(progressColor);
		progressPaint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(strokeWidth);
		paint.setColor(progressColor);
		paint.setStyle(Paint.Style.STROKE);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (changed) {
			width = this.getWidth();
			height = this.getHeight();
			_width = this.getWidth();

			if (width != height) {
				int min = Math.min(width, height);
				width = min;
				height = min;
			}
			disWidth = _width - width;

			rightOval.left = progressStrokeWidth;
			rightOval.top = progressStrokeWidth;
			rightOval.right = width - progressStrokeWidth;
			rightOval.bottom = height - progressStrokeWidth;
			rightOval.offset(disWidth, 0);

			leftOval.left = progressStrokeWidth;
			leftOval.top = progressStrokeWidth;
			leftOval.right = width - progressStrokeWidth;
			leftOval.bottom = height - progressStrokeWidth;

			progressRightOval.left = progressStrokeWidth / 2;
			progressRightOval.top = progressStrokeWidth / 2;
			progressRightOval.right = width - progressStrokeWidth / 2;
			progressRightOval.bottom = height - progressStrokeWidth / 2;
			progressRightOval.offset(disWidth, 0);

			progressLeftOval.left = progressStrokeWidth / 2;
			progressLeftOval.top = progressStrokeWidth / 2;
			progressLeftOval.right = width - progressStrokeWidth / 2;
			progressLeftOval.bottom = height - progressStrokeWidth / 2;

			semicircle = (float) (width * Math.PI / 2);
			maxProgressConvert = ((float) disWidth + semicircle) * 2;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		progressConvert = maxProgressConvert * progress / (float) maxProgress;

		canvas.drawColor(Color.TRANSPARENT);

		canvas.drawArc(rightOval, -91, 182, false, paint);
		canvas.drawArc(leftOval, 90, 180, false, paint);
		canvas.drawLine(width / 2, progressStrokeWidth, disWidth + width / 2, progressStrokeWidth,
				paint);
		canvas.drawLine(width / 2, height - progressStrokeWidth, disWidth + width / 2,
				height - progressStrokeWidth, paint);

		if (progressConvert > disWidth / 2) {
			canvas.drawLine((disWidth + width) / 2, progressStrokeWidth / 2, disWidth + width / 2,
					progressStrokeWidth / 2, progressPaint);
		} else if (progressConvert > 0) {
			canvas.drawLine((disWidth + width) / 2, progressStrokeWidth / 2,
					(disWidth + width) / 2 + progressConvert, progressStrokeWidth / 2,
					progressPaint);
		}
		if (progressConvert > disWidth / 2 + semicircle) {
			canvas.drawArc(progressRightOval, -91, 182, false, progressPaint);
		} else if (progressConvert > disWidth / 2) {
			float angle = (progressConvert - disWidth / 2) / semicircle * 180;
			canvas.drawArc(progressRightOval, -90, angle, false, progressPaint);
		}
		if (progressConvert > disWidth * 3 / 2 + semicircle) {
			canvas.drawLine(width / 2, height - progressStrokeWidth / 2, disWidth + width / 2,
					height - progressStrokeWidth / 2, progressPaint);
		} else if (progressConvert > disWidth / 2 + semicircle) {
			float offset = progressConvert - (disWidth / 2 + semicircle);
			canvas.drawLine(disWidth + width / 2, height - progressStrokeWidth / 2,
					disWidth + width / 2 - offset, height - progressStrokeWidth / 2, progressPaint);
		}
		if (progressConvert > disWidth * 3 / 2 + semicircle * 2) {
			canvas.drawArc(progressLeftOval, 90, 180, false, progressPaint);
		} else if (progressConvert > disWidth * 3 / 2 + semicircle) {
			float angle = (progressConvert - (disWidth * 3 / 2 + semicircle)) / semicircle * 180;
			canvas.drawArc(progressLeftOval, 90, angle, false, progressPaint);
		}
		if (progress == maxProgress) {
			canvas.drawLine(width / 2, progressStrokeWidth / 2, (disWidth + width) / 2,
					progressStrokeWidth / 2, progressPaint);
		} else if (progressConvert > disWidth * 3 / 2 + semicircle * 2) {
			float offset = progressConvert - (disWidth * 3 / 2 + semicircle * 2);
			canvas.drawLine(width / 2, progressStrokeWidth / 2, width / 2 + offset,
					progressStrokeWidth / 2, progressPaint);
		}
	}

	public int getMaxProgress() {
		return maxProgress / 100;
	}

	public void setMaxProgress(int maxProgress) {
		this.maxProgress = maxProgress * 100;
	}

	public void setProgress(int progress) {
		progress = progress * 100 > maxProgress ? maxProgress : progress * 100;
		if (Math.abs(progress - this.progress) > 1) {
			final ValueAnimator valueAnimator = ValueAnimator.ofInt(this.progress, progress);
			valueAnimator.setDuration(800);
			valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

				@Override
				public void onAnimationUpdate(ValueAnimator valueAnimator) {
					RoundProgressTextView.this.progress = (int) valueAnimator.getAnimatedValue();
					invalidate();
				}
			});
			valueAnimator.start();
		} else {
			this.progress = progress * 100 > maxProgress ? maxProgress : progress * 100;
			invalidate();
		}
	}

	public void setProgressNotInUiThread(int progress) {
		progress = progress * 100 > maxProgress ? maxProgress : progress * 100;
		if (Math.abs(progress - this.progress) > 1) {
			final ValueAnimator valueAnimator = ValueAnimator.ofInt(this.progress, progress);
			valueAnimator.setDuration(800);
			valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

				@Override
				public void onAnimationUpdate(ValueAnimator valueAnimator) {
					RoundProgressTextView.this.progress = (int) valueAnimator.getAnimatedValue();
					postInvalidate();
				}
			});
		} else {
			this.progress = progress * 100 > maxProgress ? maxProgress : progress * 100;
			postInvalidate();
		}
	}
}