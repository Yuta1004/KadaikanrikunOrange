package work.nityc_nyuta.kadaikanrikun;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class SubjectEditActivity extends AppCompatActivity {
    int seek_R_value = 0, seek_G_value = 0,seek_B_value = 0;
    int subjectID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_edit);

        EditText name = (EditText)findViewById(R.id.subject_name);
        EditText teacher = (EditText)findViewById(R.id.subject_teacher);
        SeekBar seek_R = (SeekBar)findViewById(R.id.themeColor_R);
        SeekBar seek_G = (SeekBar)findViewById(R.id.themeColor_G);
        SeekBar seek_B = (SeekBar)findViewById(R.id.themeColor_B);
        final TextView color_view = (TextView)findViewById(R.id.color_view);
        final TextView R_view = (TextView)findViewById(R.id.themeColor_R_text);
        final TextView G_view = (TextView)findViewById(R.id.themeColor_G_text);
        final TextView B_view = (TextView)findViewById(R.id.themeColor_B_text);

        Intent intent = getIntent();
        subjectID = intent.getIntExtra("subjectID",0);

        //Realm
        Realm.init(SubjectEditActivity.this);
        Realm realm = Realm.getDefaultInstance();
        final RealmQuery<SubjectDatabase> data = realm.where(SubjectDatabase.class);
        realm.beginTransaction();
        RealmResults<SubjectDatabase> result = realm.where(SubjectDatabase.class).equalTo("subjectId",subjectID).findAll();
        realm.commitTransaction();

        //データセット
        setTitle("科目編集");
        name.setText(result.get(0).getName());
        teacher.setText(result.get(0).getTeacher());
        seek_R.setProgress(result.get(0).getColor_r());
        seek_R_value = result.get(0).getColor_r();
        R_view.setText(String.valueOf(seek_R_value));
        seek_G.setProgress(result.get(0).getColor_g());
        seek_G_value = result.get(0).getColor_g();
        G_view.setText(String.valueOf(seek_G_value));
        seek_B.setProgress(result.get(0).getColor_b());
        seek_B_value = result.get(0).getColor_b();
        B_view.setText(String.valueOf(seek_B_value));
        color_view.setTextColor(Color.rgb(seek_R_value,seek_G_value,seek_B_value));

        //テーマカラー
        //R値
        seek_R.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seek_R_value = progress;
                R_view.setText(String.valueOf(progress));
                color_view.setTextColor(Color.rgb(seek_R_value,seek_G_value,seek_B_value));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        //G値
        seek_G.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seek_G_value = progress;
                G_view.setText(String.valueOf(progress));
                color_view.setTextColor(Color.rgb(seek_R_value,seek_G_value,seek_B_value));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        //B値
        seek_B.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seek_B_value = progress;
                B_view.setText(String.valueOf(progress));
                color_view.setTextColor(Color.rgb(seek_R_value,seek_G_value,seek_B_value));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    //オプションメニュー作成
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.subject_edit, menu);
        return true;
    }

    public boolean isFill(){
        final EditText name = (EditText)findViewById(R.id.subject_name);
        final EditText teacher = (EditText)findViewById(R.id.subject_teacher);
        if(!"".equals(name.getText().toString())){
            return true;
        }else{
            Toast.makeText(this, "入力されていない箇所があります", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    //オプションメニューが選択された
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_back) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SubjectEditActivity.this);
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

        if (id == R.id.action_save && isFill()) {
            EditText name = (EditText)findViewById(R.id.subject_name);
            EditText teacher = (EditText)findViewById(R.id.subject_teacher);
            SeekBar seek_R = (SeekBar)findViewById(R.id.themeColor_R);
            SeekBar seek_G = (SeekBar)findViewById(R.id.themeColor_G);
            SeekBar seek_B = (SeekBar)findViewById(R.id.themeColor_B);

            //Realm
            Realm.init(SubjectEditActivity.this);
            Realm realm = Realm.getDefaultInstance();
            final RealmQuery<SubjectDatabase> data = realm.where(SubjectDatabase.class);
            realm.beginTransaction();
            RealmResults<SubjectDatabase> result = realm.where(SubjectDatabase.class).equalTo("subjectId",subjectID).findAll();
            SubjectDatabase subjectDatabase = result.get(0);
            subjectDatabase.setName(name.getText().toString());
            subjectDatabase.setTeacher(teacher.getText().toString());
            subjectDatabase.setColor_r(seek_R.getProgress());
            subjectDatabase.setColor_g(seek_G.getProgress());
            subjectDatabase.setColor_b(seek_B.getProgress());
            realm.commitTransaction();
            Toast.makeText(this, "変更を保存しました", Toast.LENGTH_SHORT).show();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
