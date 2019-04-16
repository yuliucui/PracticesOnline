package net.lzzy.practicesonline.activities.activities;

import androidx.fragment.app.Fragment;

import net.lzzy.practicesonline.R;
import net.lzzy.practicesonline.activities.fragments.PracticesFragment;

/**
 *
 * @author lzzy_gxy
 * @date 2019/4/16
 * Description:
 */
public class PracticesActivity extends BaseActivity {
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_pratices;
    }

    @Override
    protected int getContainerId() {
        return R.id.activity_practices_contanins;
    }

    @Override
    protected Fragment createFragment() {
        return new PracticesFragment();
    }
}
