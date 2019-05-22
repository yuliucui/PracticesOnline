package net.lzzy.practicesonline.activities.activities;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import net.lzzy.practicesonline.R;
import android.app.AlertDialog;
import net.lzzy.practicesonline.activities.fragments.CharFragment;
import net.lzzy.practicesonline.activities.fragments.GridFragment;
import net.lzzy.practicesonline.activities.models.view.QuestionResult;
import java.util.List;
/**
 *
 * @author lzzy_gxy
 * @date 2019/5/13
 * Description:
 */
public class ResultActivity extends BaseActivity implements
        GridFragment.OnGridSelectedListener,CharFragment.OnCharSelectedListener{
    public static final  String ENSHRINE="enshrine";
    public static final  String QUESTION="question";
    public static final  String POSITION="position";
    public static final int RESULT_CODE = 1;
    private List<QuestionResult> results;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_result;
    }

    @Override
    protected int getContainerId() {
        return R.id.activity_result_container;
    }

    @Override
    protected Fragment createFragment() {
        results = getIntent().getParcelableArrayListExtra(QuestionActivity.EXTRA_RESULT);
        return GridFragment.newInstance(results);

    }




    @Override
    public void onGridSelected(int position) {
        Intent intent=new Intent();
        intent.putExtra(POSITION,position);
        setResult(RESULT_CODE,intent);
        finish();
    }

    @Override
    public void gotoChart() {
        getManager().beginTransaction().replace(R.id.activity_result_container,
                CharFragment.newInstance(results)).commit();

    }

    @Override
    public void gotoGrid() {
        getManager().beginTransaction().replace(R.id.activity_result_container,
                GridFragment.newInstance(results)).commit();

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("返回到哪里？")
                .setPositiveButton("返回题目", (dialog, which) -> {
                    finish();
                })
                .setNegativeButton("章节列表", (dialog, which) -> {
                    startActivity(new Intent(this, PracticesActivity.class));
                    finish();
                })
                .setNeutralButton("查看收藏", (dialog, which) -> {
                    Intent intent = new Intent();
                    intent.putExtra(ENSHRINE, true);
                    setResult(RESULT_OK, intent);
                    finish();
                })
                .show();
    }


    }
