package hearsilent.roundprogresstextview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

public class RoundProgressTextView extends AppCompatTextView {

	private static final float DEFAULT_PROGRESS_STOKE_WIDTH = 2.5f;
	private static final float DEFAULT_STOKE_WIDTH = 1f;
	private static final int DEFAULT_PROGRESS_COLOR = Color.RED;
	private static final int DEFAULT_PROGRESS_FILL_COLOR = Color.RED;
	private static final int DEFAULT_PROGRESS = 0;
	private static final int DEFAULT_MAX = 100;

	private int maxProgress;
	private float maxProgressConvert;
	private int progress;
	float progressConvert;

	private RectF progressRightOval = new RectF(), progressLeftOval = new RectF(), rightOval =
			new RectF(), leftOval = new RectF();
	private Paint progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG), paint =
			new Paint(Paint.ANTI_ALIAS_FLAG);

	private Path progressPath = new Path(), path = new Path();

	private float progressStrokeWidth, progressStrokeOffset;
	private float strokeWidth, strokeOffset;
	private float padding;
	private int progressColor, progressFillColor;

	public RoundProgressTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressTextView);
		progressStrokeWidth =
				a.getDimensionPixelSize(R.styleable.RoundProgressTextView_progress_stoke_width,
						convertDpToPixelSize(DEFAULT_PROGRESS_STOKE_WIDTH, context));
		progressStrokeOffset = progressStrokeWidth / 2;
		strokeWidth = a.getDimensionPixelSize(R.styleable.RoundProgressTextView_stoke_width,
				convertDpToPixelSize(DEFAULT_STOKE_WIDTH, context));
		strokeOffset = progressStrokeWidth - strokeWidth / 2;
		progressColor = a.getColor(R.styleable.RoundProgressTextView_progress_color,
				DEFAULT_PROGRESS_COLOR);
		progressFillColor = a.getColor(R.styleable.RoundProgressTextView_progress_fill_color,
				DEFAULT_PROGRESS_FILL_COLOR);
		progress = a.getInt(R.styleable.RoundProgressTextView_progress, DEFAULT_PROGRESS);
		maxProgress = a.getInt(R.styleable.RoundProgressTextView_max, DEFAULT_MAX);
		progress = progress > maxProgress ? maxProgress : progress;
		padding = progressStrokeOffset / 2;
		progressStrokeOffset += padding;
		strokeOffset += padding;
		a.recycle();

		setLayerType(View.LAYER_TYPE_SOFTWARE, null);

		setUpPaint();
	}

	private void setUpPaint() {
		progressPaint.setStrokeWidth(progressStrokeWidth);
		progressPaint.setColor(progressColor);
		progressPaint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(strokeWidth);
		paint.setColor(progressFillColor);
		paint.setStyle(Paint.Style.STROKE);
	}

	@Override
	protected void onSizeChanged(int width, int height, int oldwidth, int oldheight) {
		super.onSizeChanged(width, height, oldwidth, oldheight);
		if (width > 0 && height > 0) {
			setUp(width, height);
		}
	}

	private void setUp(float width, float height) {
		float _width = width;

		if (width != height) {
			float min = Math.min(width, height);
			width = min;
			height = min;
		}
		float disWidth = _width - width;

		rightOval.left = strokeOffset;
		rightOval.top = strokeOffset;
		rightOval.right = width - strokeOffset;
		rightOval.bottom = height - strokeOffset;
		rightOval.offset(disWidth - padding, 0);

		leftOval.left = strokeOffset;
		leftOval.top = strokeOffset;
		leftOval.right = width - strokeOffset;
		leftOval.bottom = height - strokeOffset;
		leftOval.offset(padding, 0);

		progressRightOval.left = progressStrokeOffset;
		progressRightOval.top = progressStrokeOffset;
		progressRightOval.right = width - progressStrokeOffset;
		progressRightOval.bottom = height - progressStrokeOffset;
		progressRightOval.offset(disWidth - padding, 0);

		progressLeftOval.left = progressStrokeOffset;
		progressLeftOval.top = progressStrokeOffset;
		progressLeftOval.right = width - progressStrokeOffset;
		progressLeftOval.bottom = height - progressStrokeOffset;
		progressLeftOval.offset(padding, 0);

		float semicircle = (width * (float) Math.PI / 2);
		maxProgressConvert = (disWidth + semicircle) * 2;

		path.reset();
		path.arcTo(leftOval, 90, 180);
		path.lineTo(rightOval.centerX(), strokeOffset);
		path.arcTo(rightOval, -90, 180);
		path.lineTo(leftOval.centerX(), height - strokeOffset);
		path.close();

		progressPath.reset();
		progressPath.moveTo((disWidth + width) / 2, progressStrokeOffset);
		progressPath.lineTo(progressRightOval.centerX(), progressStrokeOffset);
		progressPath.arcTo(progressRightOval, -90, 180);
		progressPath.lineTo(progressLeftOval.centerX(), height - progressStrokeOffset);
		progressPath.arcTo(progressLeftOval, 90, 180);
		progressPath.lineTo((disWidth + width) / 2, progressStrokeOffset);
		progressPath.close();

		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		progressConvert = maxProgressConvert * progress / (float) maxProgress;

		canvas.drawColor(Color.TRANSPARENT);

		canvas.drawPath(path, paint);
		canvas.drawPath(progressPath, progressPaint);
	}

	public void setProgressColor(int color, int fillColor) {
		progressPaint.setColor(color);
		paint.setColor(fillColor);
		invalidate();
	}

	public void setProgressColorNotInUiThread(int color, int fillColor) {
		progressPaint.setColor(color);
		paint.setColor(fillColor);
		postInvalidate();
	}

	public int getMaxProgress() {
		return maxProgress / 100;
	}

	public void setMaxProgress(int maxProgress) {
		this.maxProgress = maxProgress * 100;
	}

	public void setProgress(int progress, int duration) {
		progress = Math.min(maxProgress, progress * 100);
		if (this.progress == progress) {
			return;
		}
		PathMeasure pathMeasure = new PathMeasure(progressPath, false);
		final float length = pathMeasure.getLength();
		if (Math.abs(progress - this.progress) > 1) {
			ValueAnimator valueAnimator = ValueAnimator.ofInt(this.progress, progress);
			valueAnimator.setDuration(duration);
			valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

				@Override
				public void onAnimationUpdate(ValueAnimator valueAnimator) {
					RoundProgressTextView.this.progress = (int) valueAnimator.getAnimatedValue();
					int value = (int) valueAnimator.getAnimatedValue();
					PathEffect effect = new DashPathEffect(new float[]{length, length},
							((maxProgress - value) / (float) maxProgress) * length);
					progressPaint.setPathEffect(effect);
					invalidate();
				}
			});
			valueAnimator.start();
		} else {
			this.progress = progress;
			PathEffect effect = new DashPathEffect(new float[]{length, length},
					((maxProgress - progress) / (float) maxProgress) * length);
			progressPaint.setPathEffect(effect);
			invalidate();
		}
	}

	public void setProgressNotInUiThread(int progress, int duration) {
		progress = Math.min(maxProgress, progress * 100);
		if (this.progress == progress) {
			return;
		}
		PathMeasure pathMeasure = new PathMeasure(progressPath, false);
		final float length = pathMeasure.getLength();
		if (Math.abs(progress - this.progress) > 1) {
			ValueAnimator valueAnimator = ValueAnimator.ofInt(this.progress, progress);
			valueAnimator.setDuration(duration);
			valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

				@Override
				public void onAnimationUpdate(ValueAnimator valueAnimator) {
					RoundProgressTextView.this.progress = (int) valueAnimator.getAnimatedValue();
					int value = (int) valueAnimator.getAnimatedValue();
					PathEffect effect = new DashPathEffect(new float[]{length, length},
							((maxProgress - value) / (float) maxProgress) * length);
					progressPaint.setPathEffect(effect);
					postInvalidate();
				}
			});
			valueAnimator.start();
		} else {
			this.progress = progress;
			PathEffect effect = new DashPathEffect(new float[]{length, length},
					((maxProgress - progress) / (float) maxProgress) * length);
			progressPaint.setPathEffect(effect);
			postInvalidate();
		}
	}

	private DisplayMetrics getDisplayMetrics(Context context) {
		Resources resources = context.getResources();
		return resources.getDisplayMetrics();
	}

	/**
	 * This method converts dp unit to equivalent pixels, depending on device
	 * density.
	 *
	 * @param dp      A value in dp (density independent pixels) unit. Which we need
	 *                to convert into pixels
	 * @param context Context to get resources and device specific display metrics
	 * @return A float value to represent px equivalent to dp depending on
	 * device density
	 */
	private float convertDpToPixel(float dp, Context context) {
		return dp * (getDisplayMetrics(context).densityDpi / 160f);
	}

	/**
	 * This method converts dp unit to equivalent pixels, depending on device
	 * density.
	 *
	 * @param dp      A value in dp (density independent pixels) unit. Which we need
	 *                to convert into pixels
	 * @param context Context to get resources and device specific display metrics
	 * @return Value multiplied by the appropriate metric and truncated to
	 * integer pixels.
	 */
	private int convertDpToPixelSize(float dp, Context context) {
		float pixels = convertDpToPixel(dp, context);
		final int res = (int) (pixels + 0.5f);
		if (res != 0) {
			return res;
		} else if (pixels == 0) {
			return 0;
		} else if (pixels > 0) {
			return 1;
		}
		return -1;
	}
}