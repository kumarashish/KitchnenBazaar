// Generated code from Butter Knife. Do not modify!
package com.kitchenbazaar;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class History_ViewBinding implements Unbinder {
  private History target;

  @UiThread
  public History_ViewBinding(History target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public History_ViewBinding(History target, View source) {
    this.target = target;

    target.back = Utils.findRequiredViewAsType(source, R.id.back, "field 'back'", ImageView.class);
    target.productList = Utils.findRequiredViewAsType(source, R.id.productList, "field 'productList'", ListView.class);
    target.heading = Utils.findRequiredViewAsType(source, R.id.heading, "field 'heading'", TextView.class);
    target.progress = Utils.findRequiredViewAsType(source, R.id.progressbar, "field 'progress'", ProgressBar.class);
    target.layout = Utils.findRequiredViewAsType(source, R.id.swiperefresh, "field 'layout'", SwipeRefreshLayout.class);
    target.noItem = Utils.findRequiredViewAsType(source, R.id.noItem, "field 'noItem'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    History target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.back = null;
    target.productList = null;
    target.heading = null;
    target.progress = null;
    target.layout = null;
    target.noItem = null;
  }
}
