package ws.dyt.refresh.base;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by yangxiaowei
 */
public class SingleFragmentActivity extends AppCompatActivity {
	private static final String PARAM_CLASS_FRAGMENT	 = "param_class_fragment";
	private static final String PARAM_TO_FRAGMENT		 = "param_to_fragment";


	private static Intent toIntent(Context context) {
		Intent intent = new Intent(context, SingleFragmentActivity.class);
		return intent;
	}

	public static Intent generateIntent(Class classFragment, Bundle args) {
		Intent intent = new Intent();
		intent.putExtra(PARAM_CLASS_FRAGMENT, classFragment.getName());
		intent.putExtra(PARAM_TO_FRAGMENT, args);
		return intent;
	}

	public static void to(Context context, Class classFragment, Bundle args) {
//		Intent intent = toIntent(context);
		Intent intent = generateIntent(classFragment, args);
		intent.setClass(context, SingleFragmentActivity.class);
		intent.putExtra(PARAM_CLASS_FRAGMENT, classFragment.getName());
		intent.putExtra(PARAM_TO_FRAGMENT, args);
		context.startActivity(intent);
	}

	protected Fragment fragment = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if(null == savedInstanceState){
			String clazz = getIntent().getStringExtra(PARAM_CLASS_FRAGMENT);
			try {
				fragment = (Fragment) Class.forName(clazz).newInstance();
				fragment.setArguments(getIntent().getBundleExtra(PARAM_TO_FRAGMENT));
				getSupportFragmentManager().beginTransaction().replace(android.R.id.content, fragment, "_fragment_").commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			fragment = getSupportFragmentManager().findFragmentByTag("_fragment_");
		}
	}

	public Fragment getFragment(){
		return fragment;
	}

}
