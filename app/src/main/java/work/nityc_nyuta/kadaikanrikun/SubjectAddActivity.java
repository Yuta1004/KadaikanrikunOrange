package work.nityc_nyuta.kadaikanrikun;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import javax.security.auth.Subject;

import io.realm.Realm;

public class SubjectAddActivity extends AppCompatActivity {

    int seek_R_value = 0, seek_G_value = 0,seek_B_value = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_add);
        setTitle("科目追加");

        final TextView color_view = (TextView)findViewById(R.id.color_view);
        final TextView R_view = (TextView)findViewById(R.id.themeColor_R_text);
        final TextView G_view = (TextView)findViewById(R.id.themeColor_G_text);
        final TextView B_view = (TextView)findViewById(R.id.themeColor_B_text);
        SeekBar seek_R = (SeekBar)findViewById(R.id.themeColor_R);
        SeekBar seek_G = (SeekBar)findViewById(R.id.themeColor_G);
        SeekBar seek_B = (SeekBar)findViewById(R.id.themeColor_B);

        //Log.d("color",String.valueOf(seek_R_value) + String.valueOf(seek_G_value) + String.valueOf(seek_B_value));

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
    //オプションメニュー作成
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.subject, menu);
        return true;
    }

    @Override
    //オプションメニューが選択された
    public boolean onOptionsItemSelected(MenuItem item) {
        final EditText name = (EditText)findViewById(R.id.subject_name);
        final EditText teacher = (EditText)findViewById(R.id.subject_teacher);
        SeekBar seek_R = (SeekBar)findViewById(R.id.themeColor_R);
        SeekBar seek_G = (SeekBar)findViewById(R.id.themeColor_G);
        SeekBar seek_B = (SeekBar)findViewById(R.id.themeColor_B);
        int id = item.getItemId();

        if (id == R.id.action_home){
            finish();
        }

        if (id == R.id.action_subject_add && isFill()) {
            SharedPreferences preferences = getSharedPreferences("DataBaseInfo",MODE_PRIVATE);
            if(preferences.getInt("subject_id_val",9999999) == 9999999){
                SharedPreferences.Editor editor  =preferences.edit();
                editor.putInt("subject_id_val",0);
                editor.commit();
            }
            final int subject_id_val = preferences.getInt("subject_id_val",9999999);
            //Realmの設定
            Realm.init(this);
            Realm realm = Realm.getDefaultInstance();

            //データ書き込み
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm realm) {
                    SubjectDatabase subject = realm.createObject(SubjectDatabase.class);
                    subject.setSubjectId(subject_id_val + 1);
                    subject.setName(name.getText().toString());
                    subject.setTeacher(teacher.getText().toString());
                    subject.setColor_r(seek_R_value);
                    subject.setColor_g(seek_G_value);
                    subject.setColor_b(seek_B_value);
                }
            });
            Toast.makeText(this, name.getText().toString() + " を追加しました", Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor  =preferences.edit();
            editor.putInt("subject_id_val",subject_id_val + 1);
            editor.commit();
            name.setText("");
            teacher.setText("");
            seek_R.setProgress(0);
            seek_G.setProgress(0);
            seek_B.setProgress(0);
            return  true;
        }

        return super.onOptionsItemSelected(item);
    }

}
