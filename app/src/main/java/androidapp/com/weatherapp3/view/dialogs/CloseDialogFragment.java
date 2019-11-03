package androidapp.com.weatherapp3.view.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import androidapp.com.weatherapp3.R;

public class CloseDialogFragment extends DialogFragment {

	private Button cancelCloseButton;
	private Button closeAppButton;

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final LayoutInflater inflater = getActivity().getLayoutInflater();
		final View dialogView = inflater.inflate(R.layout.app_close_dialog, null);
		cancelCloseButton = dialogView.findViewById(R.id.cancel_close_button);
		closeAppButton = dialogView.findViewById(R.id.close_app_button);
		cancelCloseButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CloseDialogFragment.this.getDialog().cancel();
			}
		});
		closeAppButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				System.exit(0);
			}
		});
		builder.setView(dialogView);
		return builder.create();
	}
}
