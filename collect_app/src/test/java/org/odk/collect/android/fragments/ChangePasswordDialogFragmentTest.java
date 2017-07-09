package org.odk.collect.android.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.odk.collect.android.BuildConfig;
import org.odk.collect.android.logic.PreferenceAccessor;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.FragmentTestUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Created by sudars on 7/9/17.
 */
@RunWith(RobolectricTestRunner.class)
@Config(
    constants = BuildConfig.class
)
public class ChangePasswordDialogFragmentTest {
  @Mock
  PreferenceAccessor preferenceAccessor;

  ChangePasswordDialogFragment fragment;
  Dialog dialog;

  @Before
  public void before() {
    MockitoAnnotations.initMocks(this);

    fragment = new ChangePasswordDialogFragment();
    fragment.preferenceAccessor = preferenceAccessor;
    FragmentTestUtil.startFragment(fragment);
  }

  private void createDialog() {
    dialog = fragment.onCreateDialog(new Bundle());
  }

  @Test
  public void getPassword_getsContentsOfEditText() {
    createDialog();
    String pwd = "pAssword";
    fragment.passwordEditText.setText(pwd);

    String actual = fragment.passwordEditText.getText().toString();
    assertThat(actual).isEqualTo(pwd);
  }

  @Test
  public void checkingBoxShowsPassword() {
    createDialog();

    int expectedInputType = InputType.TYPE_CLASS_TEXT | InputType
        .TYPE_TEXT_VARIATION_PASSWORD;

    fragment.passwordCheckBox.setChecked(false);
    org.assertj.android.api.Assertions.assertThat(fragment.passwordEditText)
        .hasInputType(expectedInputType);
  }

  @Test
  public void uncheckingBoxHidesPassword() {
    createDialog();

    int expectedInputType = InputType.TYPE_TEXT_VARIATION_PASSWORD;

    fragment.passwordCheckBox.setChecked(true);
    org.assertj.android.api.Assertions.assertThat(fragment.passwordEditText)
        .hasInputType(expectedInputType);
  }

  @Test
  public void hittingOkSavesPassword() {
    createDialog();

    String pwd = "foobar";
    fragment.passwordEditText.setText(pwd);
    fragment.positiveButton.performClick();

    verify(preferenceAccessor, times(1)).saveAdminPassword(
        any(Activity.class) , eq(pwd));
  }

  @Test
  public void hittingCancelDoesNotSavePassword() {
    createDialog();

    String pwd = "foobar";
    fragment.passwordEditText.setText(pwd);
    fragment.negativeButton.performClick();

    verifyZeroInteractions(preferenceAccessor);
  }
}
