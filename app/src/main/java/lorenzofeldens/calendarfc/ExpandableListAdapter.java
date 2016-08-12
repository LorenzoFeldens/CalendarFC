package lorenzofeldens.calendarfc;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private HashMap<String, List<String>> listDataChild;
    private List<String> listDataHeader;

    private int condition;
    private ArrayList<ArrayList<String>> listChecked;
    private int type;

    public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String,
            List<String>> listDataChild, int type) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listDataChild;
        this.condition = 0;
        this.type = type;

        listChecked = new ArrayList<>();
        for(int i=0; i<listDataHeader.size(); i++){
            listChecked.add(new ArrayList<String>());
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return listDataChild.get(listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (type == 0) {
                convertView = layoutInflater.inflate(R.layout.list_item_team, null);
            }else{
                convertView = layoutInflater.inflate(R.layout.list_item_game, null);
            }
        }

        if(type == 0){
            TextView textView = (TextView) convertView.findViewById(R.id.textView_list_item_team);
            textView.setText(childText);

            CheckBox checkBox_MyTeams = (CheckBox) convertView.findViewById(R.id.checkBox_MyTeams);
            CheckBox checkBox_AddTeam = (CheckBox) convertView.findViewById(R.id.checkBox_AddTeam);

            if(condition == 0){
                setCheckBoxInvisible(checkBox_MyTeams);
                setCheckBoxInvisible(checkBox_AddTeam);
            }else{
                if(condition == 1){
                    setCheckBoxVisible(checkBox_MyTeams,groupPosition,childText);
                    setCheckBoxInvisible(checkBox_AddTeam);
                }else{
                    setCheckBoxInvisible(checkBox_MyTeams);
                    setCheckBoxVisible(checkBox_AddTeam,groupPosition,childText);
                }
            }
        }else{
            String[] content = childText.split("#");
            String[] title = content[0].split(" - ");
            String[] date = content[1].split(" ");

            TextView textView_teams = (TextView) convertView
                    .findViewById(R.id.textView_list_item_game_teams);
            TextView textView_tournament = (TextView) convertView
                    .findViewById(R.id.textView_list_item_game_tournament);
            TextView textView_date = (TextView) convertView
                    .findViewById(R.id.textView_list_item_game_date);

            textView_teams.setText(title[0]);
            textView_tournament.setText(title[1]);
            textView_date.setText(date[1]);
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listDataChild.get(listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.textView_list_group);
        lblListHeader.setText(headerTitle);

        if(type == 0 && condition == 1){
            ImageButton plus = (ImageButton) convertView.findViewById(R.id.imageButton_plus_list_group);
            plus.setVisibility(View.VISIBLE);
            plus.setClickable(true);
            plus.setFocusable(false);
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private void setCheckBoxInvisible(CheckBox checkBox) {
        checkBox.setVisibility(View.INVISIBLE);
        checkBox.setEnabled(false);
        checkBox.setClickable(false);
        checkBox.setOnClickListener(null);
    }

    private void setCheckBoxVisible(final CheckBox checkBox, final int group, final String textCheckBox) {
        checkBox.setVisibility(View.VISIBLE);
        checkBox.setEnabled(true);
        checkBox.setClickable(true);
        checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                if (checkBox.isChecked()) {
                    listChecked.get(group).add(textCheckBox);
                }else{
                    listChecked.get(group).remove(textCheckBox);
                }
            }
        });
    }

    public void setCondition(int paramInt) {
        this.condition = paramInt;
    }

    public ArrayList<ArrayList<String>> getListChecked() {
        return this.listChecked;
    }
}

