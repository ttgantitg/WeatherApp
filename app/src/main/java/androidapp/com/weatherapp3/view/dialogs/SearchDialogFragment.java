package androidapp.com.weatherapp3.view.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import androidapp.com.weatherapp3.R;

public class SearchDialogFragment extends DialogFragment {

	public interface SearchDialogListener {
		void onDialogPositiveClick(DialogFragment dialog);
	}

	private SearchDialogListener mListener;
	private Button cancelButton;
	private Button selectButton;

	@Override
	public void onAttach(@NonNull Context context) {
		super.onAttach(context);
		Activity activity;
		if (context instanceof Activity) {
			activity = (Activity) context;
			mListener = (SearchDialogListener) activity;
		}
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final LayoutInflater inflater = getActivity().getLayoutInflater();
		final View dialogView = inflater.inflate(R.layout.request_dialog, null);
		cancelButton = dialogView.findViewById(R.id.cancel_button);
		selectButton = dialogView.findViewById(R.id.select_button);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SearchDialogFragment.this.getDialog().cancel();
			}
		});
		selectButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.onDialogPositiveClick(SearchDialogFragment.this);
				dismiss();
			}
		});
		builder.setView(dialogView);
		return builder.create();
	}
}
