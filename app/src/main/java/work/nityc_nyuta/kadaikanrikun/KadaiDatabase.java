package work.nityc_nyuta.kadaikanrikun;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Yuta on
 * 2017/11/11.
 */

public class KadaiDatabase extends RealmObject {
    private int subjectId;
    private String name;
    private String memo;
    private String date;
    private String notify;

    //subjectidのgetter,setter
    public int getSubjectId(){ return subjectId; }
    public void setSubjectId(int subjectId){ this.subjectId = subjectId; }

    //nameのgetter,setter
    public String getName(){ return name; }
    public void setName(String name){ this.name = name; }

    //memoのgetter,setter
    public String getMemo(){ return memo; }
    public void setMemo(String memo){ this.memo = memo; }

    //dateのgetter,setter
    public String getDate(){ return date;}
    public void setDate(String date){ this.date = date;}

    //notifydateのgetter,setter
    public String getNotify(){ return notify;}
    public void setNotify(String notify){ this.notify = notify;}
}