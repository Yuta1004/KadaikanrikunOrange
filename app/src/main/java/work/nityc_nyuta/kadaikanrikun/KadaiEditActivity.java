package work.nityc_nyuta.kadaikanrikun;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

public class KadaiEditActivity extends AppCompatActivity {
    
    int subjectID_idx = 0;
    int kadaiId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kadai_edit);
        setTitle("課題編集");
        Intent intent = getIntent();
        kadaiId = intent.getIntExtra("subjectID",0);
        Log.d("kadaiid",String.valueOf(kadaiId));

        //Realm(SubjectDataBase)
        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<SubjectDatabase> subject_data = realm.where(SubjectDatabase.class);
        RealmResults<SubjectDatabase> subject_result = subject_data.findAllSorted("subjectId", Sort.ASCENDING);

        //Spinner
        Spinner subject_names_spinner = (Spinner)findViewById(R.id.kadai_subjectNames);
        String subjectNames[] = new String[subject_result.size()];
        for(int i = 0; i < subject_result.size(); i++){
            subjectNames[i] = subject_result.get(i).getName();
        }
        ArrayAdapter<String> subject_names_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,subjectNames);
        subject_names_spinner.setAdapter(subject_names_adapter);
        subject_names_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subjectID_idx = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        //期限削除ボタン
        ((Button)findViewById(R.id.date_remove_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(KadaiEditActivity.this);
                alertDialogBuilder.setTitle("確認");
                alertDialogBuilder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) { return; }
                });
                alertDialogBuilder.setMessage("[期限]に入力された内容を削除しますか？");
                alertDialogBuilder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ((EditText)findViewById(R.id.kadai_date_date)).setText("");
                                ((EditText)findViewById(R.id.kadai_date_time)).setText("");
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        //通知削除ボタン
        ((Button)findViewById(R.id.notify_remove_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(KadaiEditActivity.this);
                alertDialogBuilder.setTitle("確認");
                alertDialogBuilder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) { return; }
                });
                alertDialogBuilder.setMessage("[通知]に入力された内容を削除しますか？");
                alertDialogBuilder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ((EditText)findViewById(R.id.kadai_notify_date)).setText("");
                                ((EditText)findViewById(R.id.kadai_notify_time)).setText("");
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        final TextView req_focus = (TextView)findViewById(R.id.req_focus);

        //Edittext(date_date)
        final EditText kadai_date_date = (EditText)findViewById(R.id.kadai_date_date);
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String year_str = String.valueOf(year);
                String month_str = String.format("%02d",month+1);
                String day_str = String.format("%02d",dayOfMonth);
                kadai_date_date.setText(year_str + "/" + month_str + "/" + day_str);
            }
        };
        kadai_date_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    return;
                }
                DatePickerDialog datePickerDialog = new DatePickerDialog(KadaiEditActivity.this,android.R.style.Theme_DeviceDefault_Light_Dialog,
                        dateSetListener,year,month,day);
                datePickerDialog.show();
                req_focus.requestFocus();
            }
        });

        //Edittext(date_time)
        final EditText kadai_date_time = (EditText)findViewById(R.id.kadai_date_time);
        final TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String hour_str = String.format("%02d",hourOfDay);
                String minute_str = String.format("%02d",minute);
                kadai_date_time.setText(hour_str + ":" + minute_str);
            }
        };
        kadai_date_time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    return;
                }
                req_focus.requestFocus();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                TimePickerDialog timePickerDialog = new TimePickerDialog(KadaiEditActivity.this,android.R.style.Theme_DeviceDefault_Light_Dialog,timeSetListener,
                        hour,0,true);
                timePickerDialog.show();
            }
        });

        //Edittext(notify_date)
        final EditText kadai_notify_date = (EditText)findViewById(R.id.kadai_notify_date);
        final DatePickerDialog.OnDateSetListener notifyDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String year_str = String.valueOf(year);
                String month_str = String.format("%02d",month+1);
                String day_str = String.format("%02d",dayOfMonth);
                kadai_notify_date.setText(year_str + "/" + month_str + "/" + day_str);
            }
        };
        kadai_notify_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    return;
                }
                DatePickerDialog notifyDatePickerDialog = new DatePickerDialog(KadaiEditActivity.this,android.R.style.Theme_DeviceDefault_Light_Dialog,
                        notifyDateSetListener,year,month,day);
                notifyDatePickerDialog.show();
                req_focus.requestFocus();
            }
        });

        //Edittext(date_time)
        final EditText kadai_notify_time = (EditText)findViewById(R.id.kadai_notify_time);
        final TimePickerDialog.OnTimeSetListener notifyTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String hour_str = String.format("%02d",hourOfDay);
                String minute_str = String.format("%02d",minute);
                kadai_notify_time.setText(hour_str + ":" + minute_str);
            }
        };
        kadai_notify_time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    return;
                }
                req_focus.requestFocus();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                TimePickerDialog notifyTimePickerDialog = new TimePickerDialog(KadaiEditActivity.this,android.R.style.Theme_DeviceDefault_Light_Dialog,notifyTimeSetListener,
                        hour,0,true);
                notifyTimePickerDialog.show();
            }
        });

        dataSet(kadaiId);
    }

    //初期データセット
    public void dataSet(int kadaiId){
        //Realm(SubjectDataBase)
        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();
        RealmResults<KadaiDatabase> kadai_result = realm.where(KadaiDatabase.class).equalTo("kadaiId",Integer.valueOf(kadaiId)).findAll();
        RealmResults<SubjectDatabase> subject_result = realm.where(SubjectDatabase.class).findAllSorted("subjectId", Sort.ASCENDING);
        Log.d("result",String.valueOf(kadai_result.get(0)));
        for(int i = 0; i < subject_result.size(); i++){
            if(kadai_result.get(0).getSubjectId() == subject_result.get(i).getSubjectId()){
                ((Spinner)findViewById(R.id.kadai_subjectNames)).setSelection(i);
            }
        }
        ((EditText)findViewById(R.id.kadai_name)).setText(kadai_result.get(0).getName());
        ((EditText)findViewById(R.id.kadai_memo)).setText(kadai_result.get(0).getMemo());
        if(!"".equals(kadai_result.get(0).getDate())){
            String date[] = kadai_result.get(0).getDate().split("/");
            ((EditText)findViewById(R.id.kadai_date_date)).setText(date[0]+"/"+date[1]+"/"+date[2]);
            ((EditText)findViewById(R.id.kadai_date_time)).setText(date[3]+":"+date[4]);
        }
        if(!"".equals(kadai_result.get(0).getNotify())) {
            String date_notify[] = kadai_result.get(0).getNotify().split("/");
            ((EditText) findViewById(R.id.kadai_notify_date)).setText(date_notify[0] + "/" + date_notify[1] + "/" + date_notify[2]);
            ((EditText) findViewById(R.id.kadai_notify_time)).setText(date_notify[3] + ":" + date_notify[4]);
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(KadaiEditActivity.this);
        alertDialogBuilder.setTitle("確認");
        alertDialogBuilder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) { return; }
        });
        alertDialogBuilder.setMessage("作業は保存されていません。終了しますか？");
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {finish();}
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    //オプションメニュー作成
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.kadai_edit, menu);
        return true;
    }

    @Override
    //オプションメニューが選択された
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_back){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(KadaiEditActivity.this);
            alertDialogBuilder.setTitle("確認");
            alertDialogBuilder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) { return; }
            });
            alertDialogBuilder.setMessage("作業は保存されていません。終了しますか？");
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {finish();}
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return false;
        }

        if(id == R.id.action_save){

            final Boolean name = !"".equals(((EditText)findViewById(R.id.kadai_name)).getText().toString());
            final Boolean bo_date_date = !"".equals(((EditText)findViewById(R.id.kadai_date_date)).getText().toString());
            final Boolean bo_date_time = !"".equals(((EditText)findViewById(R.id.kadai_date_time)).getText().toString());
            final Boolean bo_notify_date = !"".equals(((EditText)findViewById(R.id.kadai_notify_date)).getText().toString());
            final Boolean bo_notify_time = !"".equals(((EditText)findViewById(R.id.kadai_notify_time)).getText().toString());

            if(!name) {
                Toast.makeText(this, "入力されていない箇所があります", Toast.LENGTH_SHORT).show();
                return false;
            }
            if(!(bo_date_date && bo_date_time) && (bo_date_date || bo_date_time)) {
                Toast.makeText(KadaiEditActivity.this, "入力されていない箇所があります", Toast.LENGTH_SHORT).show();
                return false;
            }
            if(!(bo_notify_date && bo_notify_time) && (bo_notify_date || bo_notify_time)) {
                Toast.makeText(KadaiEditActivity.this, "入力されていない箇所があります", Toast.LENGTH_SHORT).show();
                return false;
            }

            Realm.init(this);
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            RealmResults<KadaiDatabase> result = realm.where(KadaiDatabase.class).equalTo("kadaiId",kadaiId).findAll();
            RealmResults<SubjectDatabase> subject_result = realm.where(SubjectDatabase.class).findAllSorted("subjectId", Sort.ASCENDING);
            KadaiDatabase kadaiDatabase = result.get(0);
            kadaiDatabase.setSubjectId(subject_result.get(subjectID_idx).getSubjectId());
            kadaiDatabase.setName(((EditText)findViewById(R.id.kadai_name)).getText().toString());
            kadaiDatabase.setMemo(((EditText)findViewById(R.id.kadai_memo)).getText().toString());
            if(bo_date_date && bo_date_time) {
                kadaiDatabase.setDate(TimeToString(((EditText) findViewById(R.id.kadai_date_date)).getText().toString(), ((EditText) findViewById(R.id.kadai_date_time)).getText().toString()));
            }else{
                kadaiDatabase.setDate("");
            }
            if(bo_notify_date && bo_notify_time) {
                String date = TimeToString(((EditText) findViewById(R.id.kadai_notify_date)).getText().toString(), ((EditText) findViewById(R.id.kadai_notify_time)).getText().toString());
                kadaiDatabase.setNotify(date);

                //通知セット
                Calendar calendar = Calendar.getInstance();
                String date_add[] = date.split("/");
                calendar.set(Integer.valueOf(date_add[0]),Integer.valueOf(date_add[1])-1,Integer.valueOf(date_add[2]),Integer.valueOf(date_add[3]),Integer.valueOf(date_add[4]));
                long add_timemill = calendar.getTimeInMillis();
                long now_timemill = System.currentTimeMillis();
                Notification.setLocalNotification(KadaiEditActivity.this,kadaiId, (int) ((add_timemill - now_timemill)/1000));
            }else{
                Notification notification = new Notification();
                try {
                    Notification.cancelLocalNotification(KadaiEditActivity.this, kadaiId);
                }catch (Exception e){}
                kadaiDatabase.setNotify("");
            }
            realm.commitTransaction();
            Toast.makeText(this, "変更を保存しました", Toast.LENGTH_SHORT).show();
            finish();
            return false;
        }
        return false;
    }

    public String TimeToString(String date_nama, String time_nama){
        String date[] = date_nama.split("/");
        String time[] = time_nama.split(":");
        return date[0] + "/"+ date[1] + "/" + date[2] + "/" + time[0] + "/" + time[1];
    }
}
