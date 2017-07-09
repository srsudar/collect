package org.odk.collect.android.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import org.odk.collect.android.R;
import org.odk.collect.android.application.Collect;
import org.odk.collect.android.logic.PreferenceAccessor;
import org.odk.collect.android.utilities.TestableToastUtils;


/**
 * Created by sudars on 7/7/17.
 */

public class ChangePasswordDialogFragment extends DialogFragment {
  EditText passwordEditText = null;
  CheckBox passwordCheckBox = null;
  Button positiveButton = null;
  Button negativeButton = null;
  TestableToastUtils toastUtils = null;
  PreferenceAccessor preferenceAccessor = null;

  public ChangePasswordDialogFragment() {
    toastUtils = new TestableToastUtils();
    preferenceAccessor = new PreferenceAccessor(toastUtils, Collect
        .getInstance().getActivityLogger());
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    LayoutInflater factory = LayoutInflater.from(getActivity());
    final View dialogView = factory.inflate(R.layout.password_dialog_layout, null);
    passwordEditText = (EditText) dialogView.findViewById(R.id.pwd_field);
    passwordCheckBox = (CheckBox) dialogView.findViewById(R.id.checkBox2);
    passwordCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (!passwordCheckBox.isChecked()) {
          passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
          passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
      }
    });
    builder.setTitle(R.string.change_admin_password);
    builder.setView(dialogView);
    builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        preferenceAccessor.saveAdminPassword(getActivity(),
            getPassword());
      }
    });
    builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        Collect.getInstance().getActivityLogger().logAction(this, "AdminPasswordDialog", "CANCELLED");
      }
    });

    builder.setCancelable(false);

    AlertDialog result = builder.create();

    result.setOnShowListener(new DialogInterface.OnShowListener() {
      @Override
      public void onShow(DialogInterface dialog) {
        AlertDialog alertDialog = (AlertDialog) dialog;
        positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
      }
    });

    return result;
  }

  String getPassword() {
    return passwordEditText.getText().toString();
  }
}
