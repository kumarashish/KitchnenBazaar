// Generated code from Butter Knife. Do not modify!
package com.kitchenbazaar;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MyCart_ViewBinding implements Unbinder {
  private MyCart target;

  @UiThread
  public MyCart_ViewBinding(MyCart target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MyCart_ViewBinding(MyCart target, View source) {
    this.target = target;

    target.back = Utils.findRequiredViewAsType(source, R.id.back, "field 'back'", ImageView.class);
    target.productList = Utils.findRequiredViewAsType(source, R.id.productList, "field 'productList'", ListView.class);
    target.noItemView = Utils.findRequiredViewAsType(source, R.id.noItem, "field 'noItemView'", TextView.class);
    target.totalCost = Utils.findRequiredViewAsType(source, R.id.totalCost, "field 'totalCost'", TextView.class);
    target.cancelOrder = Utils.findRequiredViewAsType(source, R.id.cancelOrder, "field 'cancelOrder'", Button.class);
    target.placeOrder = Utils.findRequiredViewAsType(source, R.id.placeOrder, "field 'placeOrder'", Button.class);
    target.footer = Utils.findRequiredViewAsType(source, R.id.linearLayout, "field 'footer'", LinearLayout.class);
    target.progressBar = Utils.findRequiredViewAsType(source, R.id.progressbar, "field 'progressBar'", ProgressBar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MyCart target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.back = null;
    target.productList = null;
    target.noItemView = null;
    target.totalCost = null;
    target.cancelOrder = null;
    target.placeOrder = null;
    target.footer = null;
    target.progressBar = null;
  }
}
