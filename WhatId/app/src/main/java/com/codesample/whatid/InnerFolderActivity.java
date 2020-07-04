package com.codesample.whatid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.codesample.whatid.adapter.InnerFolderAdpater;
import com.codesample.whatid.data.Account;
import com.codesample.whatid.data.AppDatabase;
import com.codesample.whatid.data.Bookmark;
import com.codesample.whatid.databinding.ActivityInnerFolderBinding;

import java.util.ArrayList;
import java.util.List;

public class InnerFolderActivity extends AppCompatActivity implements InnerFolderAdpater.OnListItemClickListener{

    private ActivityInnerFolderBinding binding;
    private AppDatabase db;

    private InnerFolderAdpater adapter;
    private int folderId = -1;
    private String userId;

    private boolean displayBookmark = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInnerFolderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        folderId = getIntent().getIntExtra("folderId", -1);
        userId = getIntent().getStringExtra("userId");

        db = AppDatabase.getInstance(getApplicationContext());
        adapter = new InnerFolderAdpater(this);

        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AccountActivity.class);
            intent.putExtra("folderId", folderId);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        binding.buttonStar.setOnClickListener(v -> {
            displayBookmark = !displayBookmark;

            if(displayBookmark)
                new LoadByBookmarkTask().execute();
            else
                new LoadTask().execute();
        });

        binding.editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                new SearchTask().execute(str);
            }
        });

        binding.buttonSort.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, binding.buttonSort);

            popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

            Menu menu = popup.getMenu();

            popup.setOnMenuItemClickListener(item -> {
                new FilterTask().execute(item.getTitle().toString());
                return true;
            });

            popup.show();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        new LoadTask().execute();
    }

    @Override
    public void onListItemClick(Account account) {
        Intent intent = new Intent(this, AccountActivity.class);
        intent.putExtra("folderId", folderId);
        intent.putExtra("accountId", account.id);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    @Override
    public void onListItemLongClick(Account account) {
        new AlertDialog
                .Builder(this)
                .setMessage("삭제하시겠습니까?")
                .setPositiveButton("예", (d, w) -> {
                    new DeleteTask().execute(account);
                })
                .setNegativeButton("아니오", (d, w) -> {})
                .create()
                .show();
    }

    @Override
    public void onStarClick(Account account, View v) {
        ImageView img = (ImageView)v;

        if(img.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.ic_star_empty).getConstantState())
            img.setImageResource(R.drawable.ic_star);
        else
            img.setImageResource(R.drawable.ic_star_empty);


        new UpdateBookmarkTask().execute(account);
    }

    class LoadTask extends AsyncTask<Void, Void, List<Account>> {
        @Override
        protected void onPostExecute(List<Account> accounts) {
            super.onPostExecute(accounts);
            adapter.updateData(accounts);
        }

        @Override
        protected List<Account> doInBackground(Void... voids) {
            new LoadBookmarkTask().execute();
            return db.getAccountDAO().getAccountsByFolder(folderId);
        }
    }

    class DeleteTask extends AsyncTask<Account, Void, List<Account>> {
        @Override
        protected void onPostExecute(List<Account> accounts) {
            super.onPostExecute(accounts);
            adapter.updateData(accounts);
        }

        @Override
        protected List<Account> doInBackground(Account... accounts) {
            db.getAccountDAO().deleteAccount(accounts[0]);
            return db.getAccountDAO().getAccountsByFolder(folderId);
        }
    }

    class LoadByBookmarkTask extends AsyncTask<Void, Void, List<Account>> {
        @Override
        protected void onPostExecute(List<Account> accounts) {
            super.onPostExecute(accounts);
            adapter.updateData(accounts);
        }

        @Override
        protected List<Account> doInBackground(Void... voids) {
            new LoadBookmarkTask().execute();

            List<Bookmark> bookmarks = db.getBookmarkDAO().getBookmarks();
            List<Account> accounts = new ArrayList<>();

            for(Bookmark b : bookmarks) {
                Account account = db.getAccountDAO().getAccount(b.accountId);

                if(account.folderId == folderId)
                    accounts.add(account);
            }
            return accounts;
        }
    }

    class UpdateBookmarkTask extends AsyncTask<Account, Void, Integer> {

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            switch(integer) {
                case 1: Toast.makeText(getApplicationContext(),"북마크에 추가했습니다",Toast.LENGTH_SHORT).show();
                break;

                case 2: Toast.makeText(getApplicationContext(),"북마크에서 삭제했습니다",Toast.LENGTH_SHORT).show();
                break;
            }
        }

        @Override
        protected Integer doInBackground(Account... accounts) {
            Bookmark exist = db.getBookmarkDAO().getBookmarkByAccount(accounts[0].id);

            // 북마크에 추가되어있지 않다면
            if(exist == null) {
                Bookmark bookmark = new Bookmark(accounts[0].id);
                db.getBookmarkDAO().addBookmark(bookmark);

                return 1;
            } else {
                db.getBookmarkDAO().deleteBookmark(exist);

                return 2;
            }
        }
    }

    class LoadBookmarkTask extends AsyncTask<Void, Void, List<Bookmark>> {
        @Override
        protected void onPostExecute(List<Bookmark> bookmarks) {
            super.onPostExecute(bookmarks);

            List<Integer> accountIds = new ArrayList<>();

            for(Bookmark b : bookmarks) {
                accountIds.add(b.accountId);
            }

            adapter.updateBookmark(accountIds);
        }

        @Override
        protected List<Bookmark> doInBackground(Void... voids) {
            return db.getBookmarkDAO().getBookmarks();
        }
    }

    class SearchTask extends AsyncTask<String, Void, List<Account>> {
        @Override
        protected void onPostExecute(List<Account> accounts) {
            super.onPostExecute(accounts);
            adapter.updateData(accounts);
        }

        @Override
        protected List<Account> doInBackground(String... strings) {
            String query = strings[0];

            List<Account> searchResult = new ArrayList<>();
            List<Account> accountsByFolder = db.getAccountDAO().getAccountsByFolder(folderId);
            List<Account> accountsForSearch;

            if(displayBookmark) {
                new LoadBookmarkTask().execute();

                accountsForSearch = new ArrayList<>();

                for(Account a: accountsByFolder) {
                    Bookmark exist = db.getBookmarkDAO().getBookmarkByAccount(a.id);

                    if(exist != null)
                        accountsForSearch.add(a);
                }
            } else {
                accountsForSearch = accountsByFolder;
            }

            for(Account a: accountsForSearch) {
                if(a.url.contains(query) || a.memo.contains(query))
                    searchResult.add(a);
            }

            return searchResult;
        }
    }

    class FilterTask extends AsyncTask<String, Void, List<Account>> {
        @Override
        protected void onPostExecute(List<Account> accounts) {
            super.onPostExecute(accounts);
            adapter.updateData(accounts);
        }

        @Override
        protected List<Account> doInBackground(String... strings) {
            String selected = strings[0];
            List<Account> accounts;

            switch(selected) {
                case "내림차순(생성일)":
                    accounts = db.getAccountDAO().getAccountsByFolderDesc(folderId);
                    break;

                case "최근 수정 순서대로":
                    accounts = db.getAccountDAO().getAccountsByFolderOrderUpdated(folderId);
                    break;

                case "URL 순서대로":
                    accounts = db.getAccountDAO().getAccountsByFolderOrderUrl(folderId);
                    break;

                default:
                    accounts = db.getAccountDAO().getAccountsByFolder(folderId);
            }

            if(!displayBookmark)
                return accounts;

            new LoadBookmarkTask().execute();

            List<Integer> accountIds = new ArrayList<>();
            List<Integer> accountsBookmarked = adapter.getAccountsBookmarked();

            List<Account> toShow = new ArrayList<>();

            for(Account a: accounts) {
                accountIds.add(a.id);
            }

            for(int i = 0; i < accountIds.size(); i++) {
                if(accountsBookmarked.contains(accountIds.get(i)))
                    toShow.add(accounts.get(i));
            }

            return toShow;
        }
    }
}
