// Generated code from Butter Knife. Do not modify!
package com.kitchenbazaar;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CurrentOrder_ViewBinding implements Unbinder {
  private CurrentOrder target;

  @UiThread
  public CurrentOrder_ViewBinding(CurrentOrder target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CurrentOrder_ViewBinding(CurrentOrder target, View source) {
    this.target = target;

    target.back = Utils.findRequiredViewAsType(source, R.id.back, "field 'back'", ImageView.class);
    target.orderList = Utils.findRequiredViewAsType(source, R.id.productList, "field 'orderList'", ListView.class);
    target.heading = Utils.findRequiredViewAsType(source, R.id.heading, "field 'heading'", TextView.class);
    target.progress = Utils.findRequiredViewAsType(source, R.id.progressbar, "field 'progress'", ProgressBar.class);
    target.noItem = Utils.findRequiredViewAsType(source, R.id.noItem, "field 'noItem'", TextView.class);
    target.layout = Utils.findRequiredViewAsType(source, R.id.swiperefresh, "field 'layout'", SwipeRefreshLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CurrentOrder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.back = null;
    target.orderList = null;
    target.heading = null;
    target.progress = null;
    target.noItem = null;
    target.layout = null;
  }
}
