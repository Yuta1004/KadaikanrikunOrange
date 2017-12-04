package work.nityc_nyuta.kadaikanrikun;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.nfc.tech.TagTechnology;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class KadaiAddActivity extends AppCompatActivity{

    int subjectID_idx = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kadai_add);

        setTitle("課題追加");

        //Realm(SubjectDataBase)
        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<SubjectDatabase> subject_data = realm.where(SubjectDatabase.class);
        RealmResults<SubjectDatabase> subject_result = subject_data.findAll();

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
                DatePickerDialog datePickerDialog = new DatePickerDialog(KadaiAddActivity.this,android.R.style.Theme_DeviceDefault_Light_Dialog,
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(KadaiAddActivity.this,android.R.style.Theme_DeviceDefault_Light_Dialog,timeSetListener,
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
                DatePickerDialog notifyDatePickerDialog = new DatePickerDialog(KadaiAddActivity.this,android.R.style.Theme_DeviceDefault_Light_Dialog,
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
                TimePickerDialog notifyTimePickerDialog = new TimePickerDialog(KadaiAddActivity.this,android.R.style.Theme_DeviceDefault_Light_Dialog,notifyTimeSetListener,
                        hour,0,true);
                notifyTimePickerDialog.show();
            }
        });
    }

    @Override
    public void onBackPressed(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(KadaiAddActivity.this);
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
        getMenuInflater().inflate(R.menu.kadai, menu);
        return true;
    }

    @Override
    //オプションメニューが選択された
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_home) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(KadaiAddActivity.this);
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
            return true;
        }

        if (id == R.id.action_kadai_add) {
            Spinner subjectNames = (Spinner)findViewById(R.id.kadai_subjectNames);
            final EditText kadai_name = (EditText)findViewById(R.id.kadai_name);
            final EditText kadai_memo = (EditText)findViewById(R.id.kadai_memo);
            final EditText kadai_date_date = (EditText)findViewById(R.id.kadai_date_date);
            final EditText kadai_date_time = (EditText)findViewById(R.id.kadai_date_time);
            final EditText kadai_notify_date = (EditText)findViewById(R.id.kadai_notify_date);
            final EditText kadai_notify_time = (EditText)findViewById(R.id.kadai_notify_time);
            final Boolean name = !"".equals(kadai_name.getText().toString());
            final Boolean bo_date_date = !"".equals(kadai_date_date.getText().toString());
            final Boolean bo_date_time = !"".equals(kadai_date_time.getText().toString());
            final Boolean bo_notify_date = !"".equals(kadai_notify_date.getText().toString());
            final Boolean bo_notify_time = !"".equals(kadai_notify_time.getText().toString());

            if(!name) {
                Toast.makeText(this, "入力されていない箇所があります", Toast.LENGTH_SHORT).show();
                return false;
            }

            //Realm
            Realm.init(this);
            Realm realm = Realm.getDefaultInstance();
            RealmQuery<SubjectDatabase> subject_data = realm.where(SubjectDatabase.class);
            final RealmResults<SubjectDatabase> subject_result = subject_data.findAll();

            if(!(bo_date_date && bo_date_time) && (bo_date_date || bo_date_time)) {
                Toast.makeText(KadaiAddActivity.this, "入力されていない箇所があります", Toast.LENGTH_SHORT).show();
                return false;
            }
            if(!(bo_notify_date && bo_notify_time) && (bo_notify_date || bo_notify_time)) {
                Toast.makeText(KadaiAddActivity.this, "入力されていない箇所があります", Toast.LENGTH_SHORT).show();
                return false;
            }

            //データセット
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm realm) {
                    KadaiDatabase kadai = realm.createObject(KadaiDatabase.class);
                    kadai.setSubjectId(subject_result.get(subjectID_idx).getSubjectId());
                    kadai.setName(kadai_name.getText().toString());
                    kadai.setMemo(kadai_memo.getText().toString());

                    if(bo_date_date && bo_date_time) {
                        kadai.setDate(TimeToString(kadai_date_date.getText().toString(), kadai_date_time.getText().toString()));
                    }else{
                        kadai.setDate("");
                    }
                    if(bo_notify_date && bo_notify_time) {
                        kadai.setNotify(TimeToString(kadai_notify_date.getText().toString(), kadai_notify_time.getText().toString()));
                    }else{
                        kadai.setNotify("");
                    }
                }
            });
            Toast.makeText(this, subjectNames.getItemAtPosition(subjectID_idx)+ " " + kadai_name.getText().toString() + " を追加しました",
                    Toast.LENGTH_SHORT).show();
            subjectNames.setSelection(0);
            kadai_name.setText("");
            kadai_memo.setText("");
            kadai_date_date.setText("");
            kadai_date_time.setText("");
            kadai_notify_date.setText("");
            kadai_notify_time.setText("");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String TimeToString(String date_nama, String time_nama){
        String date[] = date_nama.split("/");
        String time[] = time_nama.split(":");
        return date[0] + "/"+ date[1] + "/" + date[2] + "/" + time[0] + "/" + time[1];
    }
}
