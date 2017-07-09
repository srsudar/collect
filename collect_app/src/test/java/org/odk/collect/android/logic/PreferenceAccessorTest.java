package org.odk.collect.android.logic;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.odk.collect.android.BuildConfig;
import org.odk.collect.android.R;
import org.odk.collect.android.database.ActivityLogger;
import org.odk.collect.android.preferences.AdminKeys;
import org.odk.collect.android.utilities.TestableToastUtils;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static android.content.Context.MODE_PRIVATE;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.odk.collect.android.preferences.AdminPreferencesFragment.ADMIN_PREFERENCES;

/**
 * Created by sudars on 7/9/17.
 */

@RunWith(RobolectricTestRunner.class)
@Config(
    constants = BuildConfig.class
)
public class PreferenceAccessorTest {
  @Mock
  private TestableToastUtils toastUtils;
  @Mock
  private ActivityLogger activityLogger;
  @Mock
  private Context context;
  @Mock
  private SharedPreferences.Editor adminEditor;
  @Mock
  private SharedPreferences adminPreferences;

  private PreferenceAccessor accessor;

  @Before
  public void before() {
    MockitoAnnotations.initMocks(this);
    when(context.getSharedPreferences(eq(ADMIN_PREFERENCES),
        eq(MODE_PRIVATE))).thenReturn(adminPreferences);
    when(adminPreferences.edit()).thenReturn(adminEditor);

    accessor = new PreferenceAccessor(toastUtils, activityLogger);
  }

  @Test
  public void saveAdminPassword_savesString() {
    String pwd = "passw0rd";
    accessor.saveAdminPassword(context, pwd);

    verify(adminEditor, times(1)).putString(AdminKeys.KEY_ADMIN_PW, pwd);
    verify(adminEditor, times(1)).apply();

    // apply() also must be called after putString, or else it will be a no-op.
    InOrder order = inOrder(adminEditor);
    order.verify(adminEditor).putString(AdminKeys.KEY_ADMIN_PW, pwd);
    order.verify(adminEditor).apply();
  }

  @Test
  public void saveAdminPassword_toastLogsDisabled() {
    saveAdminPasswordLogHelper("", R.string.admin_password_disabled,
        "DISABLED");
  }

  @Test
  public void saveAdminPassword_toastLogsChanged() {
    saveAdminPasswordLogHelper("catsanddogs", R.string.admin_password_changed,
        "CHANGED");
  }

  private void saveAdminPasswordLogHelper(String pwd, int toastStringId,
      String activityLoggerStr) {
    accessor.saveAdminPassword(context, pwd);

    verify(toastUtils, times(1)).showShortToast(context, toastStringId);
    verify(activityLogger, times(1)).logAction(accessor, "AdminPasswordDialog",
        activityLoggerStr);
  }
}
