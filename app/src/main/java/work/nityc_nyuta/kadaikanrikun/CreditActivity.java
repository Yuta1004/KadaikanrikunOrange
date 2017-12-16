package work.nityc_nyuta.kadaikanrikun;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class CreditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);

        ImageView github_icon = (ImageView)findViewById(R.id.github_icon);
        github_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://github.com/Yuta1004/Kadaikanrikun-Orange-forAndroid");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });

        setTitle("クレジット");
    }

    @Override
    //オプションメニュー作成
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.credit, menu);
        return true;
    }

    @Override
    //オプションメニューが選択された
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_home){
            finish();
        }

        return false;
    }
}
