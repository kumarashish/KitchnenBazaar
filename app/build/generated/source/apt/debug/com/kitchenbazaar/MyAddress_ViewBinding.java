// Generated code from Butter Knife. Do not modify!
package com.kitchenbazaar;

import android.view.View;
import android.widget.ImageView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MyAddress_ViewBinding implements Unbinder {
  private MyAddress target;

  @UiThread
  public MyAddress_ViewBinding(MyAddress target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MyAddress_ViewBinding(MyAddress target, View source) {
    this.target = target;

    target.back = Utils.findRequiredViewAsType(source, R.id.back, "field 'back'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MyAddress target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.back = null;
  }
}
