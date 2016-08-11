package lorenzofeldens.calendarfc;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter
        extends BaseExpandableListAdapter
{
    private Context _context;
    private HashMap<String, List<String>> _listDataChild;
    private List<String> _listDataHeader;
    private int condicao;
    private ArrayList<ArrayList<String>> listAdd2;
    private int tipo;

    public ExpandableListAdapter(Context paramContext, List<String> paramList, HashMap<String, List<String>> paramHashMap, int paramInt)
    {
        this._context = paramContext;
        this._listDataHeader = paramList;
        this._listDataChild = paramHashMap;
        this.condicao = 0;
        this.tipo = paramInt;
        this.listAdd2 = new ArrayList();
        paramInt = 0;
        while (paramInt < paramList.size())
        {
            this.listAdd2.add(new ArrayList());
            paramInt += 1;
        }
    }

    private void setCheckBoxInvisible(CheckBox paramCheckBox)
    {
        paramCheckBox.setVisibility(4);
        paramCheckBox.setEnabled(false);
        paramCheckBox.setClickable(false);
        paramCheckBox.setOnClickListener(null);
        paramCheckBox.setHint("");
    }

    private void setCheckBoxVisible(final CheckBox paramCheckBox, final int paramInt1, final String paramString, final int paramInt2)
    {
        paramCheckBox.setVisibility(0);
        paramCheckBox.setEnabled(true);
        paramCheckBox.setClickable(true);
        paramCheckBox.setHint(String.valueOf(paramInt1));
        paramCheckBox.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                if (paramCheckBox.isChecked())
                {
                    if (paramInt2 == 0)
                    {
                        ((ArrayList)ExpandableListAdapter.this.listAdd2.get(paramInt1)).remove(paramString);
                        return;
                    }
                    ((ArrayList)ExpandableListAdapter.this.listAdd2.get(paramInt1)).add(paramString);
                    return;
                }
                if (paramInt2 == 0)
                {
                    ((ArrayList)ExpandableListAdapter.this.listAdd2.get(paramInt1)).add(paramString);
                    return;
                }
                ((ArrayList)ExpandableListAdapter.this.listAdd2.get(paramInt1)).remove(paramString);
            }
        });
    }

    public Object getChild(int paramInt1, int paramInt2)
    {
        return ((List)this._listDataChild.get(this._listDataHeader.get(paramInt1))).get(paramInt2);
    }

    public long getChildId(int paramInt1, int paramInt2)
    {
        return paramInt2;
    }

    public View getChildView(int paramInt1, int paramInt2, boolean paramBoolean, View paramView, ViewGroup paramViewGroup)
    {
        Object localObject1 = (String)getChild(paramInt1, paramInt2);
        paramViewGroup = paramView;
        int j;
        if (paramView == null)
        {
            paramView = (LayoutInflater)this._context.getSystemService("layout_inflater");
            if (this.tipo == 0) {
                paramViewGroup = paramView.inflate(2130968610, null);
            }
        }
        else
        {
            paramView = (TextView)paramViewGroup.findViewById(2131492973);
            if (this.tipo != 0) {
                break label338;
            }
            paramView.setText((CharSequence)localObject1);
            paramView = (CheckBox)paramViewGroup.findViewById(2131492974);
            localObject2 = (CheckBox)paramViewGroup.findViewById(2131492975);
            j = 0;
            paramInt2 = 0;
        }
        for (;;)
        {
            if (paramInt2 >= this.listAdd2.size()) {
                break label198;
            }
            int i = 0;
            for (;;)
            {
                if (i < ((ArrayList)this.listAdd2.get(paramInt2)).size())
                {
                    if (((String)localObject1).equalsIgnoreCase((String)((ArrayList)this.listAdd2.get(paramInt2)).get(i))) {
                        j = 1;
                    }
                    i += 1;
                    continue;
                    paramViewGroup = paramView.inflate(2130968611, null);
                    break;
                }
            }
            paramInt2 += 1;
        }
        label198:
        if ((this.condicao == 0) || (((String)localObject1).equalsIgnoreCase("Nenhum item selecionado")) || (((String)localObject1).equalsIgnoreCase("Nenhum item dispon��vel")))
        {
            setCheckBoxInvisible(paramView);
            setCheckBoxInvisible((CheckBox)localObject2);
        }
        for (;;)
        {
            paramView.setFocusable(false);
            ((CheckBox)localObject2).setFocusable(false);
            return paramViewGroup;
            if (this.condicao == 1)
            {
                setCheckBoxInvisible((CheckBox)localObject2);
                setCheckBoxVisible(paramView, paramInt1, (String)localObject1, 0);
                if (j != 0) {
                    paramView.setChecked(false);
                } else {
                    paramView.setChecked(true);
                }
            }
            else
            {
                setCheckBoxVisible((CheckBox)localObject2, paramInt1, (String)localObject1, 1);
                setCheckBoxInvisible(paramView);
                if (j != 0) {
                    ((CheckBox)localObject2).setChecked(true);
                } else {
                    ((CheckBox)localObject2).setChecked(false);
                }
            }
        }
        label338:
        localObject1 = ((String)localObject1).split("#");
        Object localObject2 = localObject1[0].split(" - ");
        paramView.setText(localObject2[0]);
        ((TextView)paramViewGroup.findViewById(2131492976)).setText(localObject2[1]);
        paramView = localObject1[1].split(" ");
        ((TextView)paramViewGroup.findViewById(2131492977)).setText(paramView[1]);
        return paramViewGroup;
    }

    public int getChildrenCount(int paramInt)
    {
        return ((List)this._listDataChild.get(this._listDataHeader.get(paramInt))).size();
    }

    public Object getGroup(int paramInt)
    {
        return this._listDataHeader.get(paramInt);
    }

    public int getGroupCount()
    {
        return this._listDataHeader.size();
    }

    public long getGroupId(int paramInt)
    {
        return paramInt;
    }

    public View getGroupView(int paramInt, boolean paramBoolean, View paramView, ViewGroup paramViewGroup)
    {
        String str = (String)getGroup(paramInt);
        paramViewGroup = paramView;
        if (paramView == null)
        {
            paramView = (LayoutInflater)this._context.getSystemService("layout_inflater");
            if (this.tipo != 0) {
                break label113;
            }
        }
        label113:
        for (paramViewGroup = paramView.inflate(2130968608, null);; paramViewGroup = paramView.inflate(2130968609, null))
        {
            paramView = (TextView)paramViewGroup.findViewById(2131492971);
            paramView.setTypeface(null, 1);
            paramView.setText(str);
            if ((this.tipo == 0) && (this.condicao == 1))
            {
                paramView = (ImageButton)paramViewGroup.findViewById(2131492972);
                paramView.setVisibility(0);
                paramView.setClickable(true);
                paramView.setFocusable(false);
            }
            return paramViewGroup;
        }
    }

    public ArrayList<ArrayList<String>> getListAdd2()
    {
        return this.listAdd2;
    }

    public boolean hasStableIds()
    {
        return false;
    }

    public boolean isChildSelectable(int paramInt1, int paramInt2)
    {
        return true;
    }

    public void setCondicao(int paramInt)
    {
        this.condicao = paramInt;
    }
}

