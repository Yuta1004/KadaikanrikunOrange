package work.nityc_nyuta.kadaikanrikun;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import javax.security.auth.Subject;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    int sort_num = 0;   //しない - 0, 昇順 - 1, 降順 - 2
    String sort_target = "";
    Boolean isShiborikomi = false;
    String shiborikomi_target = "";
    String shiborikomi_word = "";

    @Override
    //アプリ起動時
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ツールバー
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //ドロワー
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //ナビゲーションドロワー設定
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //FloatngActionButton
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.kadai_add_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Realm.init(MainActivity.this);
                Realm realm = Realm.getDefaultInstance();
                RealmQuery<SubjectDatabase> data = realm.where(SubjectDatabase.class);
                RealmResults<SubjectDatabase> result = data.findAll();
                if(result.size() > 0){
                    Intent intent = new Intent(MainActivity.this, KadaiAddActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, "科目を追加してください", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setTitle("課題管理くん");
    }

    @Override
    public void onResume(){
        super.onResume();
        showList();
    }

    public void showList(){
        //Realm
        Realm.init(this);
        final Realm realm = Realm.getDefaultInstance();
        RealmQuery<KadaiDatabase> kadai_data = realm.where(KadaiDatabase.class);
        RealmResults<KadaiDatabase> nofinal_kadai_result = null;

        //絞り込み ソート
        if(sort_num != 0) { //ソートするorしない
            if (sort_num == 1) { //昇順ソート
                if(isShiborikomi){  //絞り込みするorしない
                    nofinal_kadai_result = kadai_data.equalTo(shiborikomi_target,shiborikomi_word).findAllSorted(sort_target, Sort.ASCENDING);
                }else{
                    nofinal_kadai_result = kadai_data.findAllSorted(sort_target, Sort.ASCENDING);
                }
            }else if (sort_num == 2) { //降順ソート
                if(isShiborikomi){  //絞り込みするorしない
                    nofinal_kadai_result = kadai_data.equalTo(shiborikomi_target,shiborikomi_word).findAllSorted(sort_target, Sort.DESCENDING);
                }else{
                    nofinal_kadai_result = kadai_data.findAllSorted(sort_target, Sort.DESCENDING);
                }
            }
        }else{
            if(isShiborikomi){
                nofinal_kadai_result = kadai_data.equalTo(shiborikomi_target,shiborikomi_word).findAll();
            }else{
                nofinal_kadai_result = kadai_data.findAllSorted("date",Sort.ASCENDING);
            }
        }

        final RealmResults<KadaiDatabase> kadai_result = nofinal_kadai_result;

        ListView kadai_view_list = (ListView)findViewById(R.id.kadai_view_list);
        ArrayList<KadaiShowList> list = new ArrayList<>();
        KadaiShowListAdapter adapter = new KadaiShowListAdapter(this);
        adapter.setKadaiShowLists(list);
        kadai_view_list.setAdapter(adapter);

        //アイテムタップ
        kadai_view_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Viewセット
                LayoutInflater factory = LayoutInflater.from(MainActivity.this);
                View kadai_show_popup = factory.inflate(R.layout.kadai_show_popup, null);

                    //科目名
                    RealmResults<SubjectDatabase> subjectid_result = realm.where(SubjectDatabase.class).equalTo("subjectId",kadai_result.get(position).getSubjectId()).findAll();
                    if(subjectid_result.size() > 0){
                        ((TextView)kadai_show_popup.findViewById(R.id.kadai_show_subject)).setText(subjectid_result.get(0).getName());
                    }else{
                        ((TextView) kadai_show_popup.findViewById(R.id.kadai_show_subject)).setText("未登録");
                    }

                    //課題名 メモ
                    ((TextView)kadai_show_popup.findViewById(R.id.kadai_show_name)).setText(kadai_result.get(position).getName());
                    ((TextView)kadai_show_popup.findViewById(R.id.kadai_show_memo)).setText(kadai_result.get(position).getMemo());

                    //期限 通知
                    String date_and_notify[] = new String[]{kadai_result.get(position).getDate(), kadai_result.get(position).getNotify()};
                    int date_and_notify_id[] = new int[]{R.id.kadai_show_date,R.id.kadai_show_notify_date};
                    for (int i = 0; i < 2; i++){
                        if("".equals(date_and_notify[i])){
                            ((TextView)kadai_show_popup.findViewById(date_and_notify_id[i])).setText("未登録");
                        }else{
                            String date[] = date_and_notify[i].split("/");
                            ((TextView)kadai_show_popup.findViewById(date_and_notify_id[i])).setText(date[0] + "/" + date[1] + "/" + date[2] + " " + date[3] + ":" + date[4]);
                        }
                    }
                //ダイアログ生成
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setView(kadai_show_popup);
                alertDialogBuilder.setTitle("");
                alertDialogBuilder.setPositiveButton("OK",null);
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        //アイテム長押し
        kadai_view_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                ((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(50);

                //動作確認
                LayoutInflater factory = LayoutInflater.from(MainActivity.this);
                View longtap_popup = factory.inflate(R.layout.longtap_popup, null);
                final ListView selectList = (ListView)longtap_popup.findViewById(R.id.dialog_listview);
                ArrayAdapter<String> select_dialog_adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1);
                select_dialog_adapter.add("課題編集");
                select_dialog_adapter.add("削除");
                selectList.setAdapter(select_dialog_adapter);

                //ダイアログ生成
                String subjectName = "";
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setView(longtap_popup);
                realm.beginTransaction();
                RealmResults<SubjectDatabase> subjectid_result = realm.where(SubjectDatabase.class).equalTo("subjectId",kadai_result.get(position).getSubjectId()).findAll();
                if(subjectid_result.size() == 1){
                    subjectName = subjectid_result.get(0).getName();
                }else{
                    subjectName = "課題未登録";
                }
                final String finalSubjectName = subjectName;
                realm.commitTransaction();
                alertDialogBuilder.setTitle(finalSubjectName + " : " + kadai_result.get(position).getName());
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                //アイテムタップ(動作確認ダイアログ)
                selectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent_dialog, View view_dialog, final int position_dialog, long id_dialog) {
                        switch (position_dialog){
                            case 0: //編集
                                alertDialog.dismiss();
                                Realm.init(MainActivity.this);
                                RealmQuery<SubjectDatabase> data = realm.where(SubjectDatabase.class);
                                RealmResults<SubjectDatabase> result = data.findAll();
                                if(result.size() > 0){
                                    Intent intent = new Intent(MainActivity.this,KadaiEditActivity.class);
                                    intent.putExtra("subjectID",kadai_result.get(position).getKadaiId());
                                    startActivity(intent);
                                    showList();
                                }else{
                                    Toast.makeText(MainActivity.this, "科目を追加してください", Toast.LENGTH_SHORT).show();
                                }
                                break;

                            case 1: //削除
                                alertDialog.dismiss();
                                final AlertDialog.Builder alertDialogBuilder_delete = new AlertDialog.Builder(MainActivity.this);
                                alertDialogBuilder_delete.setTitle("削除確認");
                                alertDialogBuilder_delete.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) { return; }
                                });
                                alertDialogBuilder_delete.setMessage(finalSubjectName + " : " + kadai_result.get(position).getName() + " を削除してよろしいですか？");
                                alertDialogBuilder_delete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(MainActivity.this, finalSubjectName + " : " + kadai_result.get(position).getName() + " を削除しました", Toast.LENGTH_SHORT).show();
                                        Notification notification = new Notification();
                                        notification.cancelLocalNotification(MainActivity.this,kadai_result.get(0).getKadaiId());
                                        realm.beginTransaction();
                                        kadai_result.get(position).deleteFromRealm();
                                        realm.commitTransaction();
                                        showList();
                                    }});
                                AlertDialog alertDialog_delete = alertDialogBuilder_delete.create();
                                alertDialog_delete.show();
                                break;
                        }
                    }
                });
                return true;
            }
        });


        //データセット
        for (int i = 0; i < kadai_result.size(); i++) {
            KadaiShowList kadaiList = new KadaiShowList();

            //SubjectIdから科目名&色取得 ~ 登録
            realm.beginTransaction();
            RealmResults<SubjectDatabase> subjectid_result = realm.where(SubjectDatabase.class).equalTo("subjectId",kadai_result.get(i).getSubjectId()).findAll();
            if(subjectid_result.size() == 1){
                kadaiList.setID_Name(subjectid_result.get(0).getName() + " : " + kadai_result.get(i).getName());
                kadaiList.setColor_r(subjectid_result.get(0).getColor_r());
                kadaiList.setColor_g(subjectid_result.get(0).getColor_g());
                kadaiList.setColor_b(subjectid_result.get(0).getColor_b());
            }else{
                kadaiList.setID_Name("科目未登録 : " + kadai_result.get(i).getName());
                kadaiList.setColor_r(99);
                kadaiList.setColor_g(99);
                kadaiList.setColor_b(99);
            }
            realm.commitTransaction();

            //日付データを整える ~ 登録
            String date_tmp = kadai_result.get(i).getDate();
            if("".equals(date_tmp)){
                kadaiList.setDate("期限 未登録");
            }else{
                String date[] = date_tmp.split("/");
                kadaiList.setDate("期限 " + date[0] + "/" + date[1] + "/" + date[2] + " " + date[3] + ":" + date[4]);
            }
            list.add(kadaiList);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    //戻るボタンが押された時
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    //オプションメニュー作成
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    //オプションメニューが選択された
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //課題全削除
        if (id == R.id.action_all_delete) {final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
            alertDialogBuilder.setTitle("削除確認");
            alertDialogBuilder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) { return; }
            });
            alertDialogBuilder.setMessage("課題データを全て削除してよろしいですか？");
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Realm.init(MainActivity.this);
                    Realm realm = Realm.getDefaultInstance();
                    final RealmQuery<KadaiDatabase> data = realm.where(KadaiDatabase.class);
                    RealmResults<KadaiDatabase> result = data.findAll();
                    for(int i = 0; i < result.size(); i++){
                        Notification notification = new Notification();
                        notification.cancelLocalNotification(MainActivity.this,result.get(i).getKadaiId());
                    }
                    realm.beginTransaction();
                    result.deleteAllFromRealm();
                    realm.commitTransaction();
                    Toast.makeText(MainActivity.this, "課題データを全削除しました", Toast.LENGTH_SHORT).show();
                    showList();
                }});
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return true;
        }

        if (id == R.id.action_shiborikomi) {
            return true;
        }

        if (id == R.id.action_sort){

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    //ナビゲーションドロワーが選択された
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_add_kadai){
            Realm.init(this);
            Realm realm = Realm.getDefaultInstance();
            RealmQuery<SubjectDatabase> data = realm.where(SubjectDatabase.class);
            RealmResults<SubjectDatabase> result = data.findAll();
            if(result.size() > 0){
                Intent intent = new Intent(this, KadaiAddActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(this, "科目を追加してください", Toast.LENGTH_SHORT).show();
            }

        }

        if (id == R.id.nav_add_kamoku){
            Intent intent = new Intent(this, SubjectAddActivity.class);
            startActivity(intent);
        }

        if (id == R.id.nav_show_kamoku){
            Intent intent = new Intent(this, SubjectViewActivity.class);
            startActivity(intent);
        }

        if (id == R.id.nav_settings){
            Toast.makeText(this, "未実装です", Toast.LENGTH_SHORT).show();
        }

        if (id == R.id.nav_credit){
            Toast.makeText(this, "未実装です", Toast.LENGTH_SHORT).show();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
