package net.lzzy.practicesonline.activities.fragments;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import net.lzzy.practicesonline.R;
import net.lzzy.practicesonline.activities.models.Option;
import net.lzzy.practicesonline.activities.models.view.QuestionResult;
import net.lzzy.sqllib.GenericAdapter;
import net.lzzy.sqllib.ViewHolder;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lzzy_gxy
 * @date 2019/5/13
 * Description:
 */
public class GridFragment extends BaseFragment {
    public static final String ARG_RESULTS = "argResults";
    private List<QuestionResult> results;
    private OnGridSelectedListener listener;
    Option option=new Option();
    private GridView gv;
    private TextView tvCircle;
    private GenericAdapter<QuestionResult> adapter;

    public static GridFragment newInstance(List<QuestionResult> results){
        GridFragment fragment=new GridFragment();
        Bundle args=new Bundle();
        args.putParcelableArrayList(ARG_RESULTS,(ArrayList<? extends Parcelable>)results);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            results=getArguments().getParcelableArrayList(ARG_RESULTS);
        }
    }

    @Override
    protected void populate() {
        gv = find(R.id.fragment_grid_gv);
        tvCircle = find(R.id.fragment_grid_tv_go);
        adapter = new GenericAdapter<QuestionResult>(getContext(),
                R.layout.grid_item,results) {
            @Override
            public void populate(ViewHolder holder, QuestionResult result) {
                TextView tv=holder.getView(R.id.grid_item_tv);
                holder.setTextView(R.id.grid_item_tv,getPosition(result)+1+"");
                if (result.isRight()){
                    tv.setBackgroundResource(R.drawable.grid_2);
                }else {
                    tv.setBackgroundResource(R.drawable.grid_3);
                }
            }

            @Override
            public boolean persistInsert(QuestionResult questionResult) {
                return false;
            }

            @Override
            public boolean persistDelete(QuestionResult questionResult) {
                return false;
            }
        };
       gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onGridSelected(adapter.getPosition(results.get(position)));
            }
        });
        tvCircle.setOnClickListener(v -> listener.gotoChart());
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_grid;
    }

    @Override
    public void search(String kw) {

    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener= (OnGridSelectedListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"必须实现OnGridSelectedListener接口");
        }
    }

    public interface OnGridSelectedListener {
        void onGridSelected(int position);

        void gotoChart();


    }
}
