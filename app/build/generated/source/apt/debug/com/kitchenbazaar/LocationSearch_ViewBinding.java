// Generated code from Butter Knife. Do not modify!
package com.kitchenbazaar;

import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LocationSearch_ViewBinding implements Unbinder {
  private LocationSearch target;

  @UiThread
  public LocationSearch_ViewBinding(LocationSearch target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LocationSearch_ViewBinding(LocationSearch target, View source) {
    this.target = target;

    target.back = Utils.findRequiredViewAsType(source, R.id.back, "field 'back'", ImageView.class);
    target.mAutocompleteTextView = Utils.findRequiredViewAsType(source, R.id.autoCompleteTextView, "field 'mAutocompleteTextView'", AutoCompleteTextView.class);
    target.locationRequest = Utils.findRequiredView(source, R.id.locationRequest, "field 'locationRequest'");
    target.pb = Utils.findRequiredViewAsType(source, R.id.progressbar, "field 'pb'", ProgressBar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    LocationSearch target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.back = null;
    target.mAutocompleteTextView = null;
    target.locationRequest = null;
    target.pb = null;
  }
}
