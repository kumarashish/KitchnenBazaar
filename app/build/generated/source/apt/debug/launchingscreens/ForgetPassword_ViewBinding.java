// Generated code from Butter Knife. Do not modify!
package launchingscreens;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.kitchenbazaar.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ForgetPassword_ViewBinding implements Unbinder {
  private ForgetPassword target;

  @UiThread
  public ForgetPassword_ViewBinding(ForgetPassword target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ForgetPassword_ViewBinding(ForgetPassword target, View source) {
    this.target = target;

    target.back = Utils.findRequiredViewAsType(source, R.id.back, "field 'back'", ImageView.class);
    target.emailId = Utils.findRequiredViewAsType(source, R.id.emailId, "field 'emailId'", EditText.class);
    target.submit = Utils.findRequiredViewAsType(source, R.id.submit_btn, "field 'submit'", Button.class);
    target.progressbar = Utils.findRequiredViewAsType(source, R.id.progressbar, "field 'progressbar'", ProgressBar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ForgetPassword target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.back = null;
    target.emailId = null;
    target.submit = null;
    target.progressbar = null;
  }
}
