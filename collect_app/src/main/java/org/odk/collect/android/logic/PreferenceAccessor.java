package org.odk.collect.android.logic;

import android.content.Context;
import android.content.SharedPreferences;

import org.odk.collect.android.R;
import org.odk.collect.android.database.ActivityLogger;
import org.odk.collect.android.utilities.TestableToastUtils;

import static android.content.Context.MODE_PRIVATE;
import static org.odk.collect.android.preferences.AdminKeys.KEY_ADMIN_PW;
import static org.odk.collect.android.preferences.AdminPreferencesFragment.ADMIN_PREFERENCES;

/**
 * Created by sudars on 7/7/17.
 */

public class PreferenceAccessor {
  TestableToastUtils toastUtils;
  ActivityLogger activityLogger;

  public PreferenceAccessor(TestableToastUtils toastUtils,
      ActivityLogger activityLogger) {
    this.toastUtils = toastUtils;
    this.activityLogger = activityLogger;
  }

  protected SharedPreferences.Editor getAdminEditor(Context context) {
    return context.getSharedPreferences(ADMIN_PREFERENCES, MODE_PRIVATE)
        .edit();
  }

  public void saveAdminPassword(Context context, String password) {
    SharedPreferences.Editor editor = getAdminEditor(context);
    editor.putString(KEY_ADMIN_PW, password);
    editor.apply();

    if (password.equals("")) {
      toastUtils.showShortToast(context, R.string.admin_password_disabled);
      activityLogger.logAction(this, "AdminPasswordDialog", "DISABLED");
    } else {
      toastUtils.showShortToast(context, R.string.admin_password_changed);
      activityLogger.logAction(this, "AdminPasswordDialog", "CHANGED");
    }
  }
}
