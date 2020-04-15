// Generated code from Butter Knife. Do not modify!
package com.kitchenbazaar;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MyProfile_ViewBinding implements Unbinder {
  private MyProfile target;

  @UiThread
  public MyProfile_ViewBinding(MyProfile target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MyProfile_ViewBinding(MyProfile target, View source) {
    this.target = target;

    target.back = Utils.findRequiredViewAsType(source, R.id.back, "field 'back'", ImageView.class);
    target.emailId = Utils.findRequiredViewAsType(source, R.id.emailId, "field 'emailId'", EditText.class);
    target.mobile = Utils.findRequiredViewAsType(source, R.id.mobile, "field 'mobile'", EditText.class);
    target.address = Utils.findRequiredViewAsType(source, R.id.address, "field 'address'", EditText.class);
    target.location = Utils.findRequiredViewAsType(source, R.id.location, "field 'location'", TextView.class);
    target.name = Utils.findRequiredViewAsType(source, R.id.name, "field 'name'", TextView.class);
    target.appVersion = Utils.findRequiredViewAsType(source, R.id.appversion, "field 'appVersion'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MyProfile target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.back = null;
    target.emailId = null;
    target.mobile = null;
    target.address = null;
    target.location = null;
    target.name = null;
    target.appVersion = null;
  }
}
