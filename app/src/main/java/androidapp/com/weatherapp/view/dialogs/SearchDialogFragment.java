package androidapp.com.weatherapp.view.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import androidapp.com.weatherapp.R;

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

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final LayoutInflater inflater = getActivity().getLayoutInflater();
		final View dialogView = inflater.inflate(R.layout.request_dialog, null);
		TextInputEditText inputEditText = dialogView.findViewById(R.id.search_city_text);
		inputEditText.requestFocus();
		cancelButton = dialogView.findViewById(R.id.cancel_button);
		selectButton = dialogView.findViewById(R.id.select_button);
		cancelButton.setOnClickListener(v -> SearchDialogFragment.this.getDialog().cancel());
		selectButton.setOnClickListener(v -> {
			mListener.onDialogPositiveClick(SearchDialogFragment.this);
			dismiss();
		});
		inputEditText.setOnEditorActionListener((textView, i, keyEvent) -> {
			if (i == EditorInfo.IME_ACTION_SEARCH) {
				selectButton.callOnClick();
			}
			return false;
		});
		builder.setView(dialogView);
		return builder.create();
	}
}
