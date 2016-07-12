package ws.dyt.refresh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ws.dyt.refresh.base.SingleFragmentActivity;
import ws.dyt.refresh.main.MainTabFragment;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void listTestOnClick(View view) {
        Intent intent = new Intent(this, SingleFragmentActivity.class);
        intent.putExtra(SingleFragmentActivity.CLASS_FRAGMENT, TestFragment.class.getName());
        startActivity(intent);
    }

    public void libsTestOnClick(View view) {
        Intent intent = new Intent(this, SingleFragmentActivity.class);
        intent.putExtra(SingleFragmentActivity.CLASS_FRAGMENT, MainTabFragment.class.getName());
        startActivity(intent);
    }

}
