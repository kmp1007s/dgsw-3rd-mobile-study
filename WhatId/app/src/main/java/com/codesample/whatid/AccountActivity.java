package com.codesample.whatid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.codesample.whatid.data.Account;
import com.codesample.whatid.data.AppDatabase;
import com.codesample.whatid.data.Bookmark;
import com.codesample.whatid.data.Folder;
import com.codesample.whatid.databinding.ActivityAccountBinding;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AccountActivity extends AppCompatActivity {

    class Changeable {
        String url;
        String userId;
        String password;
        String memo;

        Changeable(String url, String userId, String password, String memo) {
            this.url = url;
            this.userId = userId;
            this.password = password;
            this.memo = memo;
        }
    }

    private ActivityAccountBinding binding;
    private AppDatabase db;

    private int accountId;
    private int selectedFolderId;

    private boolean withBookmark = false;
    private boolean pwVisible = false;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        accountId = intent.getIntExtra("accountId", -1); // 조회할 계정의 기본키
        selectedFolderId = intent.getIntExtra("folderId", -1);
        userId = intent.getStringExtra("userId");

        db = AppDatabase.getInstance(getApplicationContext());

        // 추가가 아닌 읽기로 들어왔다면
        if (accountId != -1)
            new AccountLoadTask().execute();

        binding.buttonComplete.setOnClickListener(v -> {
            String url = binding.editTextURL.getText().toString();
            String userId = binding.editTextID.getText().toString();
            String password = binding.editTextPW.getText().toString();
            String memo = binding.editTextMemo.getText().toString();

            Changeable c = new Changeable(url, userId, password, memo);

            if (accountId != -1) {
                new SaveTask().execute(c);
            } else {
                new AddTask().execute(c);
            }
        });

        binding.buttonCancel.setOnClickListener(v -> {
            finish();
        });

        binding.buttonFolder.setOnClickListener(v -> {
            new FolderLoadTask().execute();
        });

        binding.editTextPW.setOnTouchListener(new View.OnTouchListener() {
            EditText editText = binding.editTextPW;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        Log.i("AccountActivity", "Drawable Clicked");
                        pwVisible = !pwVisible;

                        if(pwVisible)
                            editText.setTransformationMethod(null);
                        else
                            editText.setTransformationMethod(new PasswordTransformationMethod());

                        return true;
                    }
                }
                return false;
            }
        });

        binding.buttonStar.setOnClickListener(v -> {
            new AlertDialog
                    .Builder(AccountActivity.this)
                    .setMessage("북마크와 함께 저장하시겠습니까?")
                    .setPositiveButton("예", (d, w) -> {
                        withBookmark = true;
                    })
                    .setNegativeButton("아니오", (d, w) -> {
                        withBookmark = false;
                    })
                    .create()
                    .show();
        });
    }

    class FolderLoadTask extends AsyncTask<Void, Void, List<Folder>> {
        @Override
        protected void onPostExecute(List<Folder> folders) {
            super.onPostExecute(folders);

            List<String> folderNames = new ArrayList<>();

            for(int i = 0; i < folders.size(); i++) {
                folderNames.add(folders.get(i).name);
            }

            final CharSequence[] items =  folderNames.toArray(new String[folderNames.size()]);
            final List SelectedItems = new ArrayList();
            int defaultItem = 0;

            AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);
            builder.setTitle("폴더 선택");
            builder.setSingleChoiceItems(items, defaultItem, (d,w) -> {
                SelectedItems.clear();
                SelectedItems.add(w);
            });

            builder.setPositiveButton("완료", (d,w) -> {
                if (!SelectedItems.isEmpty()) {
                    int index = (int) SelectedItems.get(0);
                    selectedFolderId = folders.get(index).id;
                }
            });

            builder.setNegativeButton("취소", (d,w) -> {

            });

            builder.show();
        }

        @Override
        protected List<Folder> doInBackground(Void... voids) {
            return db.getFolderDAO().getFoldersByUser(userId);
        }
    }

    class AccountLoadTask extends AsyncTask<Void, Void, Account> {
        @Override
        protected void onPostExecute(Account account) {
            super.onPostExecute(account);

            if(account != null) {
                binding.editTextID.setText(account.userId);
                binding.editTextPW.setText(account.password);
                binding.editTextMemo.setText(account.memo);
                binding.editTextURL.setText(account.url);

                selectedFolderId = account.folderId;
            }
        }

        @Override
        protected Account doInBackground(Void... voids) {
            return db.getAccountDAO().getAccount(accountId);
        }
    }

    class AddTask extends AsyncTask<Changeable, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(),"추가되었습니다",Toast.LENGTH_SHORT).show();
            finish();
        }

        @Override
        protected Void doInBackground(Changeable... changeables) {
            Changeable changeable = changeables[0];

            String url = changeable.url;
            String userId = changeable.userId;
            String password = changeable.password;
            String memo = changeable.memo;

            Account a = new Account(selectedFolderId, url, userId, password, memo);
            String now = LocalDateTime.now().toString();
            a.created = now;
            a.updated = now;

            long createdId = db.getAccountDAO().addAccount(a);
            Log.i("test", createdId + "");

            if(withBookmark)
                db.getBookmarkDAO().addBookmark(new Bookmark((int)createdId));


            return null;
        }
    }

    class SaveTask extends AsyncTask<Changeable, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            finish();
        }

        @Override
        protected Void doInBackground(Changeable... changeables) {
            Changeable changeable = changeables[0];

            String url = changeable.url;
            String userId = changeable.userId;
            String password = changeable.password;
            String memo = changeable.memo;
            String now = LocalDateTime.now().toString();

            Account a = db.getAccountDAO().getAccount(accountId);
            a.folderId = selectedFolderId;
            a.url = url;
            a.userId = userId;
            a.password = password;
            a.memo = memo;
            a.updated = now;

            db.getAccountDAO().saveAccount(a);

            if(withBookmark) {
                Bookmark exist = db.getBookmarkDAO().getBookmarkByAccount(a.id);

                // 기존에 북마크에 없으면
                if(exist == null)
                    db.getBookmarkDAO().addBookmark(new Bookmark(a.id));
            }

            return null;
        }
    }
}
