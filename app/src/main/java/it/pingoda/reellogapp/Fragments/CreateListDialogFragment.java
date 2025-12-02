package it.pingoda.reellogapp.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import it.pingoda.reellogapp.R;

public class CreateListDialogFragment extends DialogFragment {

    public interface CreateListListener {
        void onListCreated(String name, String description);
    }

    CreateListListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (CreateListListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " deve implementare CreateListListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_create_list, null);

        final EditText nameInput = dialogView.findViewById(R.id.edit_list_name);
        final EditText descriptionInput = dialogView.findViewById(R.id.edit_list_description);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setView(dialogView)
                .setPositiveButton("Crea", (dialog, id) -> {
                    String name = nameInput.getText().toString().trim();
                    String description = descriptionInput.getText().toString().trim();

                    if (!name.isEmpty()) {
                        listener.onListCreated(name, description);
                    } else {
                        Toast.makeText(getActivity(), "Il nome della lista è obbligatorio", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Annulla", (dialog, id) -> dialog.cancel());

        return builder.create();
    }
}