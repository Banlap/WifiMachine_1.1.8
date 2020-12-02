package com.bigun.wifioscilloscope.util;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

public class AnimationUtils {
	private RotateAnimation rotateAnimation;
	private View view;

	public AnimationUtils(View view) {
		this.view = view;
	}

	// Ô²ÐÎ¶¯»­
	public void startRotate() {
		if (rotateAnimation == null) {
			rotateAnimation = new RotateAnimation(0f, 360f,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			rotateAnimation.setDuration(20000);
			rotateAnimation.setRepeatCount(Animation.INFINITE);
			rotateAnimation.setRepeatMode(Animation.RESTART);
			rotateAnimation.setStartTime(Animation.START_ON_FIRST_FRAME);
			LinearInterpolator lin = new LinearInterpolator();
			rotateAnimation.setInterpolator(lin);
		}
		view.startAnimation(rotateAnimation);
	}

	public void stopRotate() {
		view.clearAnimation();
	}

}
