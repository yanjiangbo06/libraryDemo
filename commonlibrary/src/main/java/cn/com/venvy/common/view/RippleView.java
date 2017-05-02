package cn.com.venvy.common.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import cn.com.venvy.common.utils.VenvyResourceUtil;
import cn.com.venvy.common.utils.VenvyUIUtil;
import cn.com.venvy.nineoldandroids.animation.AnimatorSet;
import cn.com.venvy.nineoldandroids.animation.ObjectAnimator;

/**
 * 波纹特效共同类
 */
public class RippleView extends FrameLayout {
	private LayoutParams waveLayout_rlp1;
	private FrameLayout waveLayout1;// 发光点布局
	private ImageView mWave1, mWave2;
	private AnimatorSet mAnimationSet1, mAnimationSet2;
	private static final int OFFSET = 500; // 每个动画的播放时间间隔
	private static final int MSG_WAVE1_ANIMATION = 1;
	private static final int MSG_WAVE2_ANIMATION = 2;
	private static final int MSG_WAVE3_ANIMATION = 3; // 波浪动画
	private static final int MSG_WAVE4_ANIMATION = 4; // tag光波
	private static final int MSG_WAVE5_ANIMATION = 5;
	private LayoutParams waveLayout_rlp2;
	private FrameLayout waveLayout2;
	private ImageView mWave3, mWave4;
	private AnimatorSet mAnimationSet3, mAnimationSet4;
    private Context mContext;
	private Handler mFirstHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_WAVE1_ANIMATION:
				mWave1.setVisibility(View.VISIBLE);
				mAnimationSet1.start();
				break;
			case MSG_WAVE2_ANIMATION:
				mWave2.setVisibility(View.VISIBLE);
				mAnimationSet2.start();
				break;

			case MSG_WAVE3_ANIMATION:

				mWave3.setVisibility(View.VISIBLE);
				mAnimationSet3.start();
				break;

			case MSG_WAVE4_ANIMATION:
				mWave4.setVisibility(View.VISIBLE);
				mAnimationSet4.start();
				break;

			case MSG_WAVE5_ANIMATION:
				//动画结束监听
				if(rippleListener!=null){
					rippleListener.onEnd();
				}
				break;
			}
		}
	};

	private RippleListener rippleListener;
	public void setRippleListener(RippleListener rippleListener){
		this.rippleListener = rippleListener;
	}
	public interface RippleListener{
		void onEnd();
	}
	public RippleView(Context context) {
		super(context);
		mContext = context;
		initView();

	}

	public void initView() {
		initWave1();
		initWave2();
	}
	// 第二轮波纹
	private void initWave2() {
		waveLayout2 = new FrameLayout(mContext);
		waveLayout_rlp2 = new LayoutParams(VenvyUIUtil.dip2px(
				mContext, 60), VenvyUIUtil.dip2px(mContext, 60));
		addView(waveLayout2, waveLayout_rlp2);

		mWave3 = new ImageView(mContext);
		mWave3.setImageResource(VenvyResourceUtil.getDrawableOrmipmapId(mContext,
				"venvy_iva_sdk_wave"));// 低版本方法
		LayoutParams mWave3_lp = new LayoutParams(
				VenvyUIUtil.dip2px(mContext, 30), VenvyUIUtil.dip2px(mContext,
						30));
		mWave3.setVisibility(View.INVISIBLE);
		mWave3_lp.gravity = Gravity.CENTER;
		waveLayout2.addView(mWave3, mWave3_lp);

		mWave4 = new ImageView(mContext);
		mWave4.setImageResource(VenvyResourceUtil.getDrawableOrmipmapId(mContext,
				"venvy_iva_sdk_wave"));// 低版本方法
		LayoutParams mWave4_lp = new LayoutParams(
				VenvyUIUtil.dip2px(mContext, 30), VenvyUIUtil.dip2px(mContext,
						30));
		mWave4.setVisibility(View.INVISIBLE);
		mWave4_lp.gravity = Gravity.CENTER;
		waveLayout2.addView(mWave4, mWave4_lp);
		
		mAnimationSet3 = initAnimationSet(mWave3);
		mAnimationSet4 = initAnimationSet(mWave4);
	}

	// 第一轮波纹
	private void initWave1() {
		// 发光点布局
		waveLayout1 = new FrameLayout(mContext);
		waveLayout_rlp1 = new LayoutParams(VenvyUIUtil.dip2px(
				mContext, 60), VenvyUIUtil.dip2px(mContext, 60));
		addView(waveLayout1, waveLayout_rlp1);

		mWave1 = new ImageView(mContext);
		mWave1.setImageResource(VenvyResourceUtil.getDrawableOrmipmapId(mContext,
				"venvy_iva_sdk_wave"));// 低版本方法
		LayoutParams mWave1_lp = new LayoutParams(
				VenvyUIUtil.dip2px(mContext, 30), VenvyUIUtil.dip2px(mContext,
						30));
		mWave1.setVisibility(View.INVISIBLE);
		mWave1_lp.gravity = Gravity.CENTER;
		waveLayout1.addView(mWave1, mWave1_lp);

		mWave2 = new ImageView(mContext);
		mWave2.setImageResource(VenvyResourceUtil.getDrawableOrmipmapId(mContext,
				"venvy_iva_sdk_wave"));// 低版本方法
		LayoutParams mWave2_lp = new LayoutParams(
				VenvyUIUtil.dip2px(mContext, 30), VenvyUIUtil.dip2px(mContext,
						30));
		mWave2.setVisibility(View.INVISIBLE);
		mWave2_lp.gravity = Gravity.CENTER;
		waveLayout1.addView(mWave2, mWave2_lp);
		
		mAnimationSet1 = initAnimationSet(mWave1);
		mAnimationSet2 = initAnimationSet(mWave2);
	}

	//竖屏小屏用
	private void resetLocation(int locationX,int locationY) {
		waveLayout_rlp2.leftMargin = (int) locationX;
		waveLayout_rlp2.topMargin = (int) locationY;
		waveLayout2.setLayoutParams(waveLayout_rlp2);

		// 发光点布局
		waveLayout_rlp1.leftMargin = (int) locationX;
		waveLayout_rlp1.topMargin = (int) locationY;
		waveLayout1.setLayoutParams(waveLayout_rlp1);
	}

	public void setLocation(FrameLayout.LayoutParams layoutParams){
//		waveLayout_rlp2.leftMargin = (int) locationX;
//		waveLayout_rlp2.topMargin = (int) locationY;
		waveLayout_rlp2.gravity = layoutParams.gravity;
		waveLayout_rlp2.rightMargin = layoutParams.rightMargin;
		waveLayout2.setLayoutParams(waveLayout_rlp2);

		// 发光点布局
//		waveLayout_rlp1.leftMargin = (int) locationX;
//		waveLayout_rlp1.topMargin = (int) locationY;
		waveLayout_rlp1.gravity = layoutParams.gravity;
		waveLayout_rlp1.rightMargin = layoutParams.rightMargin;
		waveLayout1.setLayoutParams(waveLayout_rlp1);
	}

	private AnimatorSet initAnimationSet(View view) {
		ObjectAnimator anim = ObjectAnimator
				.ofFloat(view, "scaleX", 1.0f, 2.0f);
		anim.setDuration(OFFSET * 3);
		ObjectAnimator anim1 = ObjectAnimator.ofFloat(view, "scaleY", 1.0f,
				2.0f);
		anim1.setDuration(OFFSET * 3);
		ObjectAnimator anim2 = ObjectAnimator.ofFloat(view, "alpha", 1, 0f);
		anim2.setDuration(OFFSET * 4);
		AnimatorSet animSet = new AnimatorSet();
		animSet.play(anim).with(anim1);
		animSet.play(anim1).with(anim2);
		return animSet;
	}

	public void clearRippleAnimation() {
		mFirstHandler.removeMessages(MSG_WAVE1_ANIMATION);
		mFirstHandler.removeMessages(MSG_WAVE2_ANIMATION);
		mFirstHandler.removeMessages(MSG_WAVE3_ANIMATION);
		mFirstHandler.removeMessages(MSG_WAVE4_ANIMATION);
		mFirstHandler.removeMessages(MSG_WAVE5_ANIMATION);
		mWave3.setVisibility(View.INVISIBLE);
		mWave1.clearAnimation();
		mWave2.clearAnimation();
		mWave3.clearAnimation();
		mWave4.clearAnimation();
	}

	public void startAnimation() {
		mFirstHandler.sendEmptyMessageDelayed(MSG_WAVE1_ANIMATION, OFFSET * 1);
		mFirstHandler.sendEmptyMessageDelayed(MSG_WAVE2_ANIMATION, OFFSET * 2);
		mFirstHandler.sendEmptyMessageDelayed(MSG_WAVE3_ANIMATION, OFFSET * 5);
		mFirstHandler.sendEmptyMessageDelayed(MSG_WAVE4_ANIMATION, OFFSET * 6);
		mFirstHandler.sendEmptyMessageDelayed(MSG_WAVE5_ANIMATION, OFFSET * 10);
	}
	
	@Override
	public void clearAnimation() {
		super.clearAnimation();
		clearRippleAnimation();
	}
}
