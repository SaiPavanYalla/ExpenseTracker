package com.xa.xpensauditor;

import static java.lang.System.currentTimeMillis;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;

import org.apache.commons.collections4.map.MultiValueMap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.UUID;

public class AddTransactionActivity extends AppCompatActivity {

    private Firebase mRootRef;
    private Firebase RefUid;
    private Firebase RefTran1, RefCatSum1, RefCat; //, UnCatTran;
    private String tid;
    private ArrayList<String> categoryList = new ArrayList<>();
    private Button addButton;
    private EditText amountEditText;
    private EditText shopNameEditText;
    private TextView categoryTextView;
    Dialog dialog;
    String amountStr, shopNameStr, categoryStr;
    private DatePicker transactionDatePicker;
    String day, month, year;
    int d, m, y;
//    Activity activity;
    MultiValueMap<String, String> catgTrans1 = MultiValueMap.multiValueMap(new LinkedHashMap<String, Collection<String>>(), (Class<LinkedHashSet<String>>) (Class<?>) LinkedHashSet.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
//        activity = this;

        mRootRef = new Firebase("https://xpense-auditor-default-rtdb.firebaseio.com");

        mRootRef.keepSynced(true);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String Uid = auth.getUid();
        if(getIntent().getExtras()!=null && !getIntent().getExtras().getString("group_key").isEmpty())
        {
            Uid=getIntent().getExtras().getString("group_key");
        }
        RefUid = mRootRef.child(Uid);

        RefCat = RefUid.child("Categories");
//        UnCatTran = RefUid.child("UnCatTran");

        addButton = (Button) findViewById(R.id.btAddTransaction);
        amountEditText = (EditText) findViewById(R.id.addTransAmt);
        shopNameEditText = (EditText) findViewById(R.id.addShopName);

        categoryTextView = (TextView) findViewById(R.id.textViewCategory);

        transactionDatePicker = (DatePicker) findViewById(R.id.dateTrans);
        day = String.valueOf(transactionDatePicker.getDayOfMonth());
        month = String.valueOf(transactionDatePicker.getMonth() + 1);
        year = String.valueOf(transactionDatePicker.getYear());

        InputFilter filter = new InputFilter() {

            final int maxDigitsBeforeDecimalPoint = 8;
            final int maxDigitsAfterDecimalPoint = 2;

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                StringBuilder builder = new StringBuilder(dest);
                builder.replace(dstart, dend, source
                        .subSequence(start, end).toString());
                if (!builder.toString().matches(
                        "(([1-9]{1})([0-9]{0," + (maxDigitsBeforeDecimalPoint - 1) + "})?)?(\\.[0-9]{0," + maxDigitsAfterDecimalPoint + "})?"

                )) {
                    if (source.length() == 0)
                        return dest.subSequence(dstart, dend);
                    return "";
                }
                return null;

            }
        };


        amountEditText.setFilters(new InputFilter[]{filter});

        categoryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize dialog
                dialog=new Dialog(AddTransactionActivity.this);

                // set custom dialog
                dialog.setContentView(R.layout.dialog_searchable_spinner);

                // set custom height and width
                dialog.getWindow().setLayout(720,1080);

                // set transparent background
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // show dialog
                dialog.show();

                // Initialize and assign variable
                EditText editTextCat=dialog.findViewById(R.id.edittext_category);
                ListView listViewCat=dialog.findViewById(R.id.listview_category);
                Button btAddCategory = dialog.findViewById(R.id.btAddCategory);

                // Initialize array adapter
                ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(AddTransactionActivity.this, android.R.layout.simple_list_item_1, categoryList);

                // set adapter
                listViewCat.setAdapter(arrayAdapter);
                editTextCat.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        arrayAdapter.getFilter().filter(s.toString(), new Filter.FilterListener() {
                            public void onFilterComplete(int count) {
                                if (count == 0){
                                    btAddCategory.setVisibility(View.VISIBLE);
                                }
                                arrayAdapter.notifyDataSetChanged();
                            }
                        });
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                listViewCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // when item selected from list
                        // set selected item on textView
                        categoryTextView.setText(arrayAdapter.getItem(position));
                        categoryStr = arrayAdapter.getItem(position);
                        // Dismiss dialog
                        dialog.dismiss();
                    }
                });

                btAddCategory.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        categoryList.add(editTextCat.getText().toString());
                        arrayAdapter.notifyDataSetChanged();
                        categoryTextView.setText(editTextCat.getText().toString());
                        categoryStr = editTextCat.getText().toString();
                        // Dismiss dialog
                        dialog.dismiss();
                        Toast.makeText(AddTransactionActivity.this, "Added category - "+editTextCat.getText().toString(), Toast.LENGTH_SHORT).show();
                        RefCat.child(editTextCat.getText().toString()).setValue("");
                    }
                });
            }
        });


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean flag = false;
                Calendar calendar = Calendar.getInstance();

                int thisYear = calendar.get(Calendar.YEAR);
                // Log.d(TAG, "# thisYear : " + thisYear);

                int thisMonth = calendar.get(Calendar.MONTH) + 1;
                //Log.d(TAG, "@ thisMonth : " + thisMonth);

                int thisDay = calendar.get(Calendar.DAY_OF_MONTH);
                //Log.d(TAG, "$ thisDay : " + thisDay);
                d = (transactionDatePicker.getDayOfMonth());
                m = (transactionDatePicker.getMonth() + 1);
                y = (transactionDatePicker.getYear());
                day = String.valueOf(d);
                month = String.valueOf(m);
                year = String.valueOf(y);
                if (thisYear > y) {
                    flag = true;
                } else {
                    if (thisYear == y) {
                        if (thisMonth > m) {
                            flag = true;
                        } else {
                            if (thisMonth == m) {
                                if (thisDay >= d) {
                                    flag = true;
                                }
                            }
                        }
                    }
                }


                if (flag) {
                    amountStr = amountEditText.getText().toString().trim().replaceAll(",", "");
                    shopNameStr = shopNameEditText.getText().toString().trim();
                    if (!amountStr.isEmpty() && !shopNameStr.isEmpty()) {
                        tid = String.valueOf(currentTimeMillis());
                        ;

                        RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("Transactions").child(tid).child("Amount").setValue(amountStr);
                        RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("Transactions").child(tid).child("Category").setValue(categoryStr);
                        RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("Transactions").child(tid).child("Shop Name").setValue(shopNameStr);
                        RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("Transactions").child(tid).child("ZMessage").setValue("Entered Manually...");
                        RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("Transactions").child(tid).child("Day").setValue(day);
                        RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("Transactions").child(tid).child("Month").setValue(month);
                        RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("Transactions").child(tid).child("Year").setValue(year);


//                        if (selCat == "Uncategorised") {
//                            UnCatTran.child(Tid);
//                            UnCatTran.child(Tid).child("Amount").setValue(Amount);
//                            UnCatTran.child(Tid).child("Category").setValue(selCat);
//                            UnCatTran.child(Tid).child("Shop Name").setValue(ShopName);
//                            UnCatTran.child(Tid).child("ZMessage").setValue("Entered Manually...");
//                            UnCatTran.child(Tid).child("Day").setValue(day);
//                            UnCatTran.child(Tid).child("Month").setValue(month);
//                            UnCatTran.child(Tid).child("Year").setValue(year);
//                        } else {

                        RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("CatTran").child(categoryStr).child(tid).child("Amount").setValue(amountStr);
                        RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("CatTran").child(categoryStr).child(tid).child("Category").setValue(categoryStr);
                        RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("CatTran").child(categoryStr).child(tid).child("Shop Name").setValue(shopNameStr);
                        RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("CatTran").child(categoryStr).child(tid).child("ZMessage").setValue("Entered Manually...");
                        RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("CatTran").child(categoryStr).child(tid).child("Day").setValue(day);
                        RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("CatTran").child(categoryStr).child(tid).child("Month").setValue(month);
                        RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("CatTran").child(categoryStr).child(tid).child("Year").setValue(year);
//                        }


                        RefTran1 = RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("Transactions");

                        RefCatSum1 = RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("CatSum");


                        Toast.makeText(getApplicationContext(), "Transaction added", Toast.LENGTH_SHORT).show();
                        amountEditText.setText("");
                        shopNameEditText.setText("");
                        Toast.makeText(getApplicationContext(), "Add one more transaction or press back", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "Enter valid Amount and Shopname", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Enter valid date", Toast.LENGTH_LONG).show();
                }


                RefTran1.addChildEventListener(new com.firebase.client.ChildEventListener() {
                    String amount, cat, shname, shDay, shMonth, shYear;

                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        int i = 0;

                        for (DataSnapshot S : dataSnapshot.getChildren()) {

                            switch (i) {
                                case 0:
                                    amount = S.getValue().toString().trim();
                                    break;
                                case 1:
                                    cat = S.getValue().toString().trim();
                                    break;

                            }

                            i++;
                        }
                        catgTrans1.put(cat, amount);

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });


            }
        });
        RefCat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getKey().trim();
                categoryList.add(value);
//                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


        public void onBackPressed() {
            super.onBackPressed();

            Intent i = new Intent(AddTransactionActivity.this, HomeActivity.class);
            if(getIntent().getExtras()!=null && !getIntent().getExtras().getString("group_key").isEmpty())
            {
                i= new Intent(AddTransactionActivity.this, GroupActivity.class);
            }
            startActivity(i);
    }
}