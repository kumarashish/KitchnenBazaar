// Generated code from Butter Knife. Do not modify!
package launchingscreens;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.kitchenbazaar.R;
import common.Bold_TextView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class Login_ViewBinding implements Unbinder {
  private Login target;

  @UiThread
  public Login_ViewBinding(Login target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public Login_ViewBinding(Login target, View source) {
    this.target = target;

    target.login = Utils.findRequiredViewAsType(source, R.id.login_btn, "field 'login'", Button.class);
    target.signUp = Utils.findRequiredViewAsType(source, R.id.signup, "field 'signUp'", Bold_TextView.class);
    target.forgetPassword = Utils.findRequiredViewAsType(source, R.id.forgetPassword, "field 'forgetPassword'", ImageView.class);
    target.emailId = Utils.findRequiredViewAsType(source, R.id.emailId, "field 'emailId'", EditText.class);
    target.password = Utils.findRequiredViewAsType(source, R.id.password, "field 'password'", EditText.class);
    target.rememberme = Utils.findRequiredViewAsType(source, R.id.checkbox, "field 'rememberme'", CheckBox.class);
    target.progressbar = Utils.findRequiredViewAsType(source, R.id.progressbar, "field 'progressbar'", ProgressBar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    Login target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.login = null;
    target.signUp = null;
    target.forgetPassword = null;
    target.emailId = null;
    target.password = null;
    target.rememberme = null;
    target.progressbar = null;
  }
}
