package com.example.orderfood.component;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.orderfood.R;
import com.example.orderfood.data.CurrentUser;
import com.example.orderfood.models.Account;
import com.example.orderfood.models.dto.FavoriteDTO;
import com.example.orderfood.sqlLite.dao.FavoriteDAO;

class ParentClass {

    public static class NoteDialogFragment extends DialogFragment {


        private static final String ARG_ITEM = "arg_item";
        private Context context;
        private Account currentUser  = CurrentUser.getCurrentUser();
        private FavoriteDAO favoriteDAO ;

        public static NoteDialogFragment newInstance(FavoriteDTO item) {
            NoteDialogFragment fragment = new NoteDialogFragment();
            Bundle args = new Bundle();
            args.putSerializable(ARG_ITEM, item);
            fragment.setArguments(args);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.dialog_note, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            favoriteDAO = new FavoriteDAO(requireContext());
            EditText noteEditText = view.findViewById(R.id.note_edit_text);

            // Lấy dữ liệu từ arguments
            FavoriteDTO item1 = (FavoriteDTO) getArguments().getSerializable(ARG_ITEM);
            FavoriteDTO item =  favoriteDAO.getFavoriteByIdAndAccountID(item1.getID(), currentUser.getId());

            // Hiển thị dữ liệu ra giao diện
            TextView noteTextView = view.findViewById(R.id.note_text_view);
            noteTextView.setText(item.getDescription());

            // Thiết lập các nút bấm
            Button saveButton = view.findViewById(R.id.save_button);
            saveButton.setOnClickListener(v -> {
                String noteText = noteEditText.getText().toString();
                if (!noteText.isEmpty()) {
                    // Ví dụ: Hiển thị nội dung
                    Toast.makeText(view.getContext(), "Nội dung đã được lưu: " , Toast.LENGTH_SHORT).show();
                    favoriteDAO.updateDescription(item.getProductID(),noteText, currentUser.getId());
                    dismiss();


                } else {
                    Toast.makeText(view.getContext(), "Ghi chú trống!", Toast.LENGTH_SHORT).show();
                }

            });

        }
    }
}
