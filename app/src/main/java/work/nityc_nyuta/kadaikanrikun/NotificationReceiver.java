package work.nityc_nyuta.kadaikanrikun;


import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import javax.security.auth.Subject;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        int kadaiID;
        String subject_name = "";

        if(intent.getIntExtra("kadaiID",9999999) != 9999999){
            kadaiID = intent.getIntExtra("kadaiID",9999999);
        }else{
            return;
        }

        Realm.init(context);
        Realm realm = Realm.getDefaultInstance();
        RealmResults<KadaiDatabase> data = realm.where(KadaiDatabase.class).equalTo("kadaiId",Integer.valueOf(kadaiID)).findAll();
        RealmResults<SubjectDatabase> subject_id = realm.where(SubjectDatabase.class).findAll();

        if(data.size() == 0){
            return;
        }

        for(int i = 0; i < subject_id.size(); i++){
            if(subject_id.get(i).getSubjectId() == data.get(0).getSubjectId()){
                subject_name = subject_id.get(i).getName();
                break;
            }
        }

        if(subject_name == ""){
            subject_name = "科目未登録";
        }

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_check_black_24dp)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                        .setContentTitle(subject_name + " : " + data.get(0).getName())
                        .setContentText(data.get(0).getMemo())
                        .setDefaults(1);
        notificationManager.notify(kadaiID, builder.build());
    }

}
