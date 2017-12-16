package work.nityc_nyuta.kadaikanrikun;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

public class SubjectViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_view);
        setTitle("登録済み科目一覧");

        showList();
    }

    @Override
    protected void onResume(){
        super.onResume();
        showList();
    }

    public void showList(){

        //データ表示
        Realm.init(this);
        final Realm realm = Realm.getDefaultInstance();
        final RealmQuery<SubjectDatabase> data = realm.where(SubjectDatabase.class);
        final RealmResults<SubjectDatabase> result = data.findAllSorted("subjectId", Sort.ASCENDING);

        //Listview設定
        final ListView subject_view_list = (ListView)findViewById(R.id.subject_view_list);
        ArrayList<SubjectShowList> list = new ArrayList<>();
        final SubjectShowListAdapter adapter = new SubjectShowListAdapter(this);
        adapter.setSubjectShowLists(list);
        subject_view_list.setAdapter(adapter);

        //アイテムタップ
        subject_view_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        //アイテム長押し
        subject_view_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                ((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(50);

                //動作確認Listview
                LayoutInflater factory = LayoutInflater.from(SubjectViewActivity.this);
                View longtap_popup = factory.inflate(R.layout.longtap_popup, null);
                final ListView selectList = (ListView)longtap_popup.findViewById(R.id.dialog_listview);
                ArrayAdapter<String> select_dialog_adapter = new ArrayAdapter<String>(SubjectViewActivity.this,android.R.layout.simple_list_item_1);
                    select_dialog_adapter.add("科目編集");
                    select_dialog_adapter.add("削除");
                selectList.setAdapter(select_dialog_adapter);

                //ダイアログ生成
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SubjectViewActivity.this);
                alertDialogBuilder.setView(longtap_popup);
                alertDialogBuilder.setTitle(result.get(position).getName());
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                //アイテムタップ(動作確認ダイアログ)
                selectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent_dialog, View view_dialog, final int position_dialog, long id_dialog) {
                        switch (position_dialog){
                            case 0:
                                alertDialog.dismiss();
                                Intent intent = new Intent(SubjectViewActivity.this,SubjectEditActivity.class);
                                intent.putExtra("subjectID",result.get(position).getSubjectId());
                                startActivity(intent);
                                showList();
                                break;

                            case 1: //削除
                                alertDialog.dismiss();
                                final AlertDialog.Builder alertDialogBuilder_delete = new AlertDialog.Builder(SubjectViewActivity.this);
                                alertDialogBuilder_delete.setTitle("削除確認");
                                alertDialogBuilder_delete.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) { return; }
                                });
                                alertDialogBuilder_delete.setMessage(result.get(position).getName() + " を削除してよろしいですか？");
                                alertDialogBuilder_delete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(SubjectViewActivity.this, result.get(position).getName() + " を削除しました", Toast.LENGTH_SHORT).show();
                                        realm.beginTransaction();
                                        result.get(position).deleteFromRealm();
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
        for (int i = 0; i < result.size(); i++) {
            SubjectShowList subjectList = new SubjectShowList();
            subjectList.setName(result.get(i).getName());
            subjectList.setTeacher("講師 : " + result.get(i).getTeacher());
            subjectList.setColor_r(result.get(i).getColor_r());
            subjectList.setColor_g(result.get(i).getColor_g());
            subjectList.setColor_b(result.get(i).getColor_b());
            list.add(subjectList);
            //Log.d("data",String.valueOf(result.get(i)));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    //オプションメニュー作成
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.subject_show, menu);
        return true;
    }

    @Override
    //オプションメニューが選択された
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_home){
            finish();
        }

        //全削除
        if (id == R.id.action_all_delete) {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SubjectViewActivity.this);
            alertDialogBuilder.setTitle("削除確認");
            alertDialogBuilder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) { return; }
            });
            alertDialogBuilder.setMessage("科目データを全て削除してよろしいですか？");
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Realm.init(SubjectViewActivity.this);
                    Realm realm = Realm.getDefaultInstance();
                    final RealmQuery<SubjectDatabase> data = realm.where(SubjectDatabase.class);
                    RealmResults<SubjectDatabase> result = data.findAll();
                    realm.beginTransaction();
                    result.deleteAllFromRealm();
                    realm.commitTransaction();
                    Toast.makeText(SubjectViewActivity.this, "科目データを全削除しました", Toast.LENGTH_SHORT).show();
                    showList();
                }});
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
