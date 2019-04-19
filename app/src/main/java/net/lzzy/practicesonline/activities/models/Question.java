package net.lzzy.practicesonline.activities.models;

import net.lzzy.practicesonline.activities.models.view.QuestionType;
import net.lzzy.sqllib.Ignored;
import net.lzzy.sqllib.Sqlitable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
/**
 *
 * @author lzzy_gxy
 * @date 2019/4/16
 * Description:
 */
public class Question extends BaseEntity implements Sqlitable {
    public   static final String COL_PRACTICE_ID="practiceId";
    private String content;
    private int dbType;
    private String analysis;
    private UUID practiceId;
    @Ignored
    private List<Option> options;
    @Ignored
    private QuestionType type;


    public Question(){
        options=new ArrayList<>();
    }


    public QuestionType getType() {
        return type;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
        this.options.addAll(options);
    }



    public String getContens() {
        return content;
    }

    public void setContens(String content) {
        this.content = content;
    }

    public int getDbType() {
        return dbType;
    }

    public void setDbType(int dbType) {
        this.dbType = dbType;
        type=QuestionType.getInstance(dbType);
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public UUID getPracticeId() {
        return practiceId;
    }

    public void setPracticeId(UUID practiceId) {
        this.practiceId = practiceId;
    }




    @Override
    public boolean needUpdate() {
        return false;
    }
}
