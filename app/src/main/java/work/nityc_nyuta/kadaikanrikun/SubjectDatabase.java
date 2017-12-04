package work.nityc_nyuta.kadaikanrikun;

import android.text.Editable;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Yuta on
 * 2017/11/11.
 */

public class SubjectDatabase extends RealmObject {
    private int subjectId;
    private String name;
    private String teacher;
    private int color_r;
    private int color_g;
    private int color_b;

    //subjectidのgetter,setter
    public int getSubjectId(){ return subjectId; }
    public void setSubjectId(int subjectId){ this.subjectId = subjectId; }

    //nameのgetter,setter
    public String getName(){ return name; }
    public void setName(String name){ this.name = name; }

    //teacherのgetter,setter
    public String getTeacher(){ return teacher; }
    public void setTeacher(String teacher){ this.teacher = teacher; }

    //color_rのgetter,setter
    public int getColor_r(){ return color_r; }
    public void setColor_r(int color_r) { this.color_r = color_r; }

    //color_gのgetter,setter
    public int getColor_g() { return color_g; }
    public void setColor_g(int color_g) {this.color_g = color_g;}

    //color_bのgetter,setter
    public int getColor_b() { return color_b; }
    public void setColor_b(int color_b) { this.color_b = color_b; }
}