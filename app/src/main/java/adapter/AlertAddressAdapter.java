package adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.kitchenbazaar.R;

import java.util.ArrayList;

import common.AppController;
import interfaces.OnDeleteClicked;
import model.DeliveryAddressModel;

public class AlertAddressAdapter  extends BaseAdapter {
    Activity activity;
    ArrayList<DeliveryAddressModel> list;
    LayoutInflater inflater;
    AppController controller;

    public AlertAddressAdapter(Activity activity, ArrayList<DeliveryAddressModel> list) {
        this.activity = activity;
        this.list = list;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        controller = (AppController) activity.getApplicationContext();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        final DeliveryAddressModel model = list.get(i);
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.address_row, null);
            holder.name = (TextView) view.findViewById(R.id.addressName);
            holder.address1 = (TextView) view.findViewById(R.id.addrss1);
            holder.address2 = (TextView) view.findViewById(R.id.address2);
            //holder.parent=(CardView)
            holder.delete = (Button) view.findViewById(R.id.delete);
            holder.delete.setVisibility(View.GONE);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.name.setText(model.getName());
        holder.address1.setText(model.getAddressLine1());
        holder.address2.setText(model.getAddressLine2() + ", " + model.getPinCode());


        view.setTag(holder);
        return view;
    }

    public class ViewHolder {
        CardView parent;
        TextView name, address1, address2;
        Button delete;

    }
}
