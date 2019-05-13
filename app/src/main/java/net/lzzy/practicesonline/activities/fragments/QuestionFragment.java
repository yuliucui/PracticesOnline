package net.lzzy.practicesonline.activities.fragments;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.app.AlertDialog;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import net.lzzy.practicesonline.R;
import net.lzzy.practicesonline.activities.models.FavoriteFactory;
import net.lzzy.practicesonline.activities.models.Option;
import net.lzzy.practicesonline.activities.models.Question;
import net.lzzy.practicesonline.activities.models.QuestionFactory;
import net.lzzy.practicesonline.activities.models.UserCookies;
import net.lzzy.practicesonline.activities.models.view.QuestionType;
import java.util.List;

/**
 *
 * @author lzzy_gxy
 * @date 2019/4/30
 * Description:
 */
public class QuestionFragment extends BaseFragment {
    private static final String ARG_QUESTION_ID = "argQuestionId";
    private static final String ARGS_POS = "argsPos";
    private static final String ARGS_IS_COMMITTED = "argsIsCommitted";
    private Question question;
    private int pos;
    private boolean isCommitted;
    private TextView tvType;
    private ImageView imgFavorite;
    private TextView tvContent;
    private RadioGroup rgOptions;
    private boolean isMulti=false;

    public static  QuestionFragment newInstance(String questionId,int pos, boolean isCommitted){
        QuestionFragment fragment=new QuestionFragment();
        Bundle args=new Bundle();
        args.putString(ARG_QUESTION_ID,questionId);
        args.putInt(ARGS_POS,pos);
        args.putBoolean(ARGS_IS_COMMITTED,isCommitted);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            pos=getArguments().getInt(ARGS_POS);
            isCommitted=getArguments().getBoolean(ARGS_IS_COMMITTED);
            question= QuestionFactory.getInstance().getById(getArguments().getString(ARG_QUESTION_ID));
        }
    }
    @Override
    protected void populate() {
        initViews();
        isMulti =question.getType()==QuestionType.MULTI_CHOICE;
        //生成选项
        generateOptions();
        //显示题目内容
        displayQuestion();
    }

    private void displayQuestion() {

        int label=pos + 1;
        String qType=label + "."+question.getType().toString();
        tvType.setText(qType);
        tvContent.setText(question.getContent());
        int starId= FavoriteFactory.getInstance().isQuestionStarred(question.getId().toString())?
                android.R.drawable.star_on : android.R.drawable.star_off;
        imgFavorite.setImageResource(starId);
        imgFavorite.setOnClickListener(v -> switchStar());

    }

    private void switchStar() {
        FavoriteFactory factory=FavoriteFactory.getInstance();
        if (factory.isQuestionStarred(question.getId().toString())){

            factory.cancelStarQuestion(question.getId());
            imgFavorite.setImageResource( android.R.drawable.star_off);
        }else {
            factory.starQuestion(question.getId());
            imgFavorite.setImageResource( android.R.drawable.star_on);
        }
    }

    private void generateOptions() {
        List<Option> options=question.getOptions();
        for (Option option:options){
            CompoundButton btn= isMulti ? new CheckBox(getContext()):new RadioButton(getContext());
            String content=option.getLabel()+"."+option.getContent();
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                btn.setButtonTintList(ColorStateList.valueOf(Color.GRAY));
            }
            btn.setText(content);
            btn.setEnabled(!isCommitted);


            btn.setOnCheckedChangeListener((buttonView,isChecked)->
                    UserCookies.getInstance().changeOptionState(option,isChecked,isMulti));
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                btn.setTextAppearance(R.style.NormalText);
            }
            rgOptions.addView(btn);
            boolean shouldCheck=UserCookies.getInstance().isOptionSelected(option);
            if (isMulti){
                btn.setChecked(shouldCheck);
            }else if (shouldCheck){
                rgOptions.check(btn.getId());
            }


            if (isCommitted && option.isAnswer()){
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    btn.setTextColor(getResources().getColor(R.color.colorGreen,null));
                }else {
                    btn.setTextColor(getResources().getColor(R.color.colorGreen));
                }
            }
        }
    }
    private void initViews() {
        tvContent = find(R.id.fragment_question_tv_content);
        rgOptions = find(R.id.fragment_question_option_container);
        tvType = find(R.id.fragment_question_tv_type);
        imgFavorite = find(R.id.fragment_question_img_favorite);
        if (isCommitted){
        rgOptions.setOnClickListener(v ->
                new AlertDialog.Builder(getContext())
                .setMessage(question.getAnalysis())
                .show());
        }


    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_question;
    }

    @Override
    public void search(String kw) {


    }
}
