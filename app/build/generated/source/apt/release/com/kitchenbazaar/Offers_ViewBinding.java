// Generated code from Butter Knife. Do not modify!
package com.kitchenbazaar;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class Offers_ViewBinding implements Unbinder {
  private Offers target;

  @UiThread
  public Offers_ViewBinding(Offers target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public Offers_ViewBinding(Offers target, View source) {
    this.target = target;

    target.back = Utils.findRequiredViewAsType(source, R.id.back, "field 'back'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    Offers target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.back = null;
  }
}
