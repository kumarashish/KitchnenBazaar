// Generated code from Butter Knife. Do not modify!
package launchingscreens;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.kitchenbazaar.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SignUp_ViewBinding implements Unbinder {
  private SignUp target;

  @UiThread
  public SignUp_ViewBinding(SignUp target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SignUp_ViewBinding(SignUp target, View source) {
    this.target = target;

    target.back = Utils.findRequiredViewAsType(source, R.id.back, "field 'back'", ImageView.class);
    target.Name = Utils.findRequiredViewAsType(source, R.id.Name, "field 'Name'", EditText.class);
    target.emailId = Utils.findRequiredViewAsType(source, R.id.emailId, "field 'emailId'", EditText.class);
    target.mobile = Utils.findRequiredViewAsType(source, R.id.mobile, "field 'mobile'", EditText.class);
    target.password = Utils.findRequiredViewAsType(source, R.id.password, "field 'password'", EditText.class);
    target.address = Utils.findRequiredViewAsType(source, R.id.address, "field 'address'", EditText.class);
    target.submit_btn = Utils.findRequiredViewAsType(source, R.id.submit_btn, "field 'submit_btn'", Button.class);
    target.progressbar = Utils.findRequiredViewAsType(source, R.id.progressbar, "field 'progressbar'", ProgressBar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SignUp target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.back = null;
    target.Name = null;
    target.emailId = null;
    target.mobile = null;
    target.password = null;
    target.address = null;
    target.submit_btn = null;
    target.progressbar = null;
  }
}
