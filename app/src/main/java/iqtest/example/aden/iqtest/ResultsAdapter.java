package iqtest.example.aden.iqtest;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.iqchamp1.pack.iqtest.R;

import java.util.ArrayList;
import java.util.HashMap;


public class ResultsAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList questionsNames;
    private HashMap<String, ArrayList<String>> questionsDetails;

    public ResultsAdapter(Context context, ArrayList<String> questionsNames, HashMap<String, ArrayList<String>> questionsDetails) {
        this.context = context;
        this.questionsNames = questionsNames;
        this.questionsDetails = questionsDetails;
    }

        @Override
    public int getGroupCount() {
        return this.questionsNames.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.questionsDetails.get(this.questionsNames.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.questionsNames.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.questionsDetails.get(this.questionsNames.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.question_names, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.parent_list);
        lblListHeader.setTextSize(18);
        lblListHeader.setTextColor(Color.BLACK);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.questions_details, null);

        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.child_list);
        txtListChild.setTextSize(15);
        txtListChild.setTextColor(Color.BLUE);
        txtListChild.setText(childText);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
