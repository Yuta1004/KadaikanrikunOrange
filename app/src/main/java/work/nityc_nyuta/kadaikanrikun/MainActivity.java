package work.nityc_nyuta.kadaikanrikun;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import javax.security.auth.Subject;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<KadaiDatabase> kadai_data = realm.where(KadaiDatabase.class);
        final RealmResults<KadaiDatabase> kadai_result = kadai_data.findAll();
        for(int i = 0; i <  kadai_result.size(); i++){
            Log.d("data",String.valueOf(kadai_result.get(i)));
        }

        ListView kadai_view_list = (ListView)findViewById(R.id.kadai_view_list);
        ArrayList<KadaiShowList> list = new ArrayList<>();
        KadaiShowListAdapter adapter = new KadaiShowListAdapter(this);
        adapter.setKadaiShowLists(list);
        kadai_view_list.setAdapter(adapter);

        //アイテムタップ
        kadai_view_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(MainActivity.this, "アイテムタップ", Toast.LENGTH_SHORT).show();
            }
        });

        //アイテム長押し
        kadai_view_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "アイテム長押し", Toast.LENGTH_SHORT).show();
                return false;
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
                kadaiList.setID_Name("科目未登録 : " + kadai_result.get(0).getName());
                kadaiList.setColor_r(99);
                kadaiList.setColor_g(99);
                kadaiList.setColor_b(99);
            }
            realm.commitTransaction();

            //日付データを整える ~ 登録
            String date_tmp = kadai_result.get(i).getDate();
            if("".equals(date_tmp)){
                kadaiList.setDate("期限 ");
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
