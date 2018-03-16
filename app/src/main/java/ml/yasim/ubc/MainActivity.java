package ml.yasim.ubc;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity {

	private LinearLayout body;
	private WebView main;
	private LinearLayout linear1;
	private ImageView logo;
	private LinearLayout linear2;
	private TextView ezx;
	private ImageView animation_icon;
	private ImageView no_internet;
	private TextView copy_right;
	private WebView ads;

	private boolean connected;
	private String command = "";
	private double status = 0;
	private double int_status = 0;


	private Timer _timer = new Timer();
	private TimerTask animation;
	private TimerTask startup;
	private TimerTask internet_timer;
	private AlertDialog.Builder dialog;
	private Intent recreater = new Intent();
	private ObjectAnimator yasim = new ObjectAnimator();
	private TimerTask waiter;
	private Intent public_intent = new Intent();
	private SharedPreferences update;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initialize();
		initializeLogic();
	}

	private void  initialize() {
		body = (LinearLayout) findViewById(R.id.body);
		main = (WebView) findViewById(R.id.main);
		main.getSettings().setJavaScriptEnabled(true);
		main.getSettings().setSupportZoom(true);
		main.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView _view,final String _url, Bitmap _favicon) {
				_animate();
				status = 1;
				if (_url.contains("update_ubc")) {
					main.stopLoading();
					showMessage("Download Update!");
					public_intent.setAction(Intent.ACTION_VIEW);
					public_intent.setData(Uri.parse(_url));
					startActivity(public_intent);
				}
				if (_url.contains("tel:")) {
					main.stopLoading();
					public_intent.setAction(Intent.ACTION_CALL);
					public_intent.setData(Uri.parse(_url));
					startActivity(public_intent);
				}
				if (_url.contains("whatsapp://")) {
					main.stopLoading();
					public_intent.setAction(Intent.ACTION_VIEW);
					public_intent.setData(Uri.parse(_url));
					startActivity(public_intent);
				}
				if (_url.contains("chrome")) {
					main.stopLoading();
					public_intent.setAction(Intent.ACTION_VIEW);
					public_intent.setData(Uri.parse(_url));
					startActivity(public_intent);
				}
				super.onPageStarted(_view, _url, _favicon);
			}
			@Override
			public void onPageFinished(WebView _view,final String _url) {
				_finished();
				super.onPageFinished(_view, _url);
			}
		});
		linear1 = (LinearLayout) findViewById(R.id.linear1);
		logo = (ImageView) findViewById(R.id.logo);
		linear2 = (LinearLayout) findViewById(R.id.linear2);
		ezx = (TextView) findViewById(R.id.ezx);
		animation_icon = (ImageView) findViewById(R.id.animation_icon);
		no_internet = (ImageView) findViewById(R.id.no_internet);
		copy_right = (TextView) findViewById(R.id.copy_right);
		ads = (WebView) findViewById(R.id.ads);
		ads.getSettings().setJavaScriptEnabled(true);
		ads.getSettings().setSupportZoom(true);
		ads.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView _view,final String _url, Bitmap _favicon) {
				if (_url.contains("tel:")) {
					ads.stopLoading();
					public_intent.setAction(Intent.ACTION_CALL);
					public_intent.setData(Uri.parse(_url));
					startActivity(public_intent);
				}
				if (_url.contains("chrome")) {
					ads.stopLoading();
					public_intent.setAction(Intent.ACTION_VIEW);
					public_intent.setData(Uri.parse(_url));
					startActivity(public_intent);
				}
				super.onPageStarted(_view, _url, _favicon);
			}
			@Override
			public void onPageFinished(WebView _view,final String _url) {
				ads.setVisibility(View.VISIBLE);
				yasim.setTarget(ads);
				yasim.setPropertyName("alpha");
				yasim.setFloatValues((float)(0.1d), (float)(1));
				yasim.setDuration((int)(1200));
				yasim.start();
				super.onPageFinished(_view, _url);
			}
		});




		dialog = new AlertDialog.Builder(this);




		update = getSharedPreferences("update", Activity.MODE_PRIVATE);






	}

	private void  initializeLogic() {

		main.setVisibility(View.GONE);
		linear1.setVisibility(View.VISIBLE);
		ads.setVisibility(View.GONE);
		no_internet.setVisibility(View.GONE);
		animation_icon.setVisibility(View.VISIBLE);
		ezx.setVisibility(View.GONE);
		_animate();
	}

	@Override
	public void onBackPressed() {
				if (status == 1) {
					ads.setAlpha((float)(0));
					dialog.setTitle("ubc");
					dialog.setMessage("Do you want to exit?");
					dialog.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
													ads.loadUrl("http://sync.a0001.net/ads");
								status = 0;
								main.setVisibility(View.GONE);
								ezx.setVisibility(View.VISIBLE);
						}
					});
					dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
					
						}
					});
					dialog.setNeutralButton("Reload", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
													main.loadUrl(main.getUrl());
						}
					});
					dialog.create().show();
				}
				else {
					Intent startMain = new Intent(Intent.ACTION_MAIN); startMain.addCategory(Intent.CATEGORY_HOME); startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); startActivity(startMain);
					if (int_status == 1) {
						waiter = new TimerTask() {
									@Override
										public void run() {
											runOnUiThread(new Runnable() {
											@Override
												public void run() {
																		status = 1;
												ezx.setVisibility(View.GONE);
												main.setVisibility(View.VISIBLE);
												int_status = 0;
												_checker();
												}
											});
										}
									};
									_timer.schedule(waiter, (int)(2000));
					}
				}
	}
	@Override
	protected void onPostCreate(Bundle _savedInstanceState) {
		super.onPostCreate(_savedInstanceState);
				_checker();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
				recreater.setClass(getApplicationContext(), MainActivity.class);
	}
	@Override
	public void onResume() {
		super.onResume();

	}

	private void _internet () {
		ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
		connected = networkInfo != null && networkInfo.isConnected();
	}
	private void _animate () {
		main.setVisibility(View.GONE);
		animation_icon.setVisibility(View.VISIBLE);
		no_internet.setVisibility(View.GONE);
		animation = new TimerTask() {
					@Override
						public void run() {
							runOnUiThread(new Runnable() {
							@Override
								public void run() {
										animation_icon.setRotation((float)(animation_icon.getRotation() + 5));
								}
							});
						}
					};
					_timer.scheduleAtFixedRate(animation, (int)(0), (int)(80));
	}
	private void _finished () {
		main.setVisibility(View.VISIBLE);
			animation.cancel();
		animation_icon.setVisibility(View.INVISIBLE);
	}
	private void _checker () {
		startup = new TimerTask() {
					@Override
						public void run() {
							runOnUiThread(new Runnable() {
							@Override
								public void run() {
										_internet();
								if (connected) {
									_setup();
								}
								else {
									animation_icon.setVisibility(View.GONE);
									no_internet.setVisibility(View.VISIBLE);
									ads.setVisibility(View.GONE);
									internet_timer = new TimerTask() {
												@Override
													public void run() {
														runOnUiThread(new Runnable() {
														@Override
															public void run() {
																								_internet();
															if (connected) {
																	internet_timer.cancel();
																_setup();
															}
															else {

															}
															}
														});
													}
												};
												_timer.scheduleAtFixedRate(internet_timer, (int)(0), (int)(15000));
								}
								}
							});
						}
					};
					_timer.schedule(startup, (int)(2000));
	}
	private void _setup () {
		main.loadUrl("http://sync.a0001.net/app/ubc/1");
		int_status = 1;
		ads.loadUrl("http://sync.a0001.net/ads");
	}




	// created automatically
	private void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}

	private int getLocationX(View _v) {
		 int _location[] = new int[2];
		 _v.getLocationInWindow(_location);
		 return _location[0];
	}

	private int getLocationY(View _v) {
		 int _location[] = new int[2];
		 _v.getLocationInWindow(_location);
		 return _location[1];
	}

	private int getRandom(int _minValue ,int _maxValue){
		Random random = new Random();
		return random.nextInt(_maxValue - _minValue + 1) + _minValue;
	}

	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
				_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}

	private float getDip(int _input){
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}

	private int getDisplayWidthPixels(){
		return getResources().getDisplayMetrics().widthPixels;
	}

	private int getDisplayHeightPixels(){
		return getResources().getDisplayMetrics().heightPixels;
	}


}
