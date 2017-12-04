package work.nityc_nyuta.kadaikanrikun;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import javax.security.auth.Subject;

import work.nityc_nyuta.kadaikanrikun.SubjectShowList;


public class SubjectShowListAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<SubjectShowList> subjectShowLists;

    public SubjectShowListAdapter(Context context){
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setSubjectShowLists(ArrayList<SubjectShowList> settingLists){
        this.subjectShowLists = settingLists;
    }

    @Override
    public int getCount() {
        return subjectShowLists.size();
    }

    @Override
    public Object getItem(int position) {
        return subjectShowLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return subjectShowLists.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.subject_show_list,parent,false);
        String r = String.format("%02x",subjectShowLists.get(position).getColor_r() & 0xff);
        String g = String.format("%02x",subjectShowLists.get(position).getColor_g() & 0xff);
        String b = String.format("%02x",subjectShowLists.get(position).getColor_b() & 0xff);
        ((ImageView)convertView.findViewById(R.id.themecolor)).setColorFilter(Color.parseColor("#"+r+g+b), PorterDuff.Mode.SRC_IN);
        ((TextView)convertView.findViewById(R.id.name)).setText(subjectShowLists.get(position).getName());
        ((TextView)convertView.findViewById(R.id.teacher)).setText(subjectShowLists.get(position).getTeacher());
        return convertView;
    }
}