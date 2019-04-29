package net.lzzy.practicesonline.activities.activities;

import android.os.Bundle;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import net.lzzy.practicesonline.activities.utils.AppUtils;

/**
 *
 * @author lzzy_gxy
 * @date 2019/4/12
 * Description:
 */
public abstract class BaseActivity extends AppCompatActivity {
    private  Fragment fragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutRes());
        AppUtils.addActivity(this);
        FragmentManager manager=getSupportFragmentManager();
        fragment=manager.findFragmentById(getContainerId());
        if (fragment==null){
            fragment=createFragment();
            manager.beginTransaction().add(getContainerId(),fragment).commit();
        }

    }

    protected Fragment getFragment(){
        return fragment;

    }
    protected abstract  int getLayoutRes();

    protected  abstract  int getContainerId();



    protected abstract Fragment createFragment();
    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppUtils.removeActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppUtils.setRunning(getLocalClassName());
    }

    @Override
    protected void onStart() {
        super.onStart();
        AppUtils.setStopped(getLocalClassName());
    }


}

