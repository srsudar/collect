package org.odk.collect.android.utilities;

import android.content.Context;
import android.widget.Toast;

import org.odk.collect.android.application.Collect;

/**
 * Created by sudars on 7/9/17.
 */

public class TestableToastUtils {
  public void showShortToast(Context context, String message) {
    showToast(context, message, Toast.LENGTH_SHORT);
  }

  public void showShortToast(Context context, int messageResource) {
    showToast(context, messageResource, Toast.LENGTH_SHORT);
  }

  public void showLongToast(Context context, String message) {
    showToast(context, message, Toast.LENGTH_LONG);
  }

  public void showLongToast(Context context, int messageResource) {
    showToast(context, messageResource, Toast.LENGTH_LONG);
  }

  private void showToast(Context context, String message, int duration) {
    Toast.makeText(context, message, duration).show();
  }

  private void showToast(Context context, int messageResource, int duration) {
    Toast.makeText(context, Collect.getInstance().getString(messageResource),
        duration).show();
  }
}
