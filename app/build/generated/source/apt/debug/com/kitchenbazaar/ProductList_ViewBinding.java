// Generated code from Butter Knife. Do not modify!
package com.kitchenbazaar;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ProductList_ViewBinding implements Unbinder {
  private ProductList target;

  @UiThread
  public ProductList_ViewBinding(ProductList target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ProductList_ViewBinding(ProductList target, View source) {
    this.target = target;

    target.back = Utils.findRequiredViewAsType(source, R.id.back, "field 'back'", ImageView.class);
    target.productList = Utils.findRequiredViewAsType(source, R.id.productList, "field 'productList'", ListView.class);
    target.category = Utils.findRequiredViewAsType(source, R.id.category, "field 'category'", TextView.class);
    target.progressbar = Utils.findRequiredViewAsType(source, R.id.progressbar, "field 'progressbar'", ProgressBar.class);
    target.badgeCount = Utils.findRequiredViewAsType(source, R.id.cart_item_count, "field 'badgeCount'", TextView.class);
    target.myCart = Utils.findRequiredView(source, R.id.myCart, "field 'myCart'");
    target.amount = Utils.findRequiredViewAsType(source, R.id.amount, "field 'amount'", TextView.class);
    target.search = Utils.findRequiredViewAsType(source, R.id.search, "field 'search'", EditText.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ProductList target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.back = null;
    target.productList = null;
    target.category = null;
    target.progressbar = null;
    target.badgeCount = null;
    target.myCart = null;
    target.amount = null;
    target.search = null;
  }
}
