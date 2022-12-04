package com.xa.xpensauditor;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
//import com.xa.xpensauditor.databinding.ActivityHomeBinding;

/**
 * Activity for Home page.
 */
public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int SMS_PERMISSION_CODE = 101;

//    private ActivityHomeBinding binding;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    //    private ViewPager viewPager;
    FirebaseAuth auth;
    ImageView userImage;

    private Firebase mRootRef;
    private Firebase RefUid;
    private Firebase RefName, RefEmail;
    private static int currentpage = 0;
    private AllTransactionsFragment transactionsFragment;
    TextView tvHeaderName, tvHeaderMail;
    //todo
    //StorageReference storageReference, filepath,storageRef;
    Uri imageUri = null;
    String Uid;

    /**
     * onCreate for HomeActivity.
     * @param savedInstanceState For bundling info
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("XpensAuditor");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddTransactionActivity.class);
                startActivity(i);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Firebase.setAndroidContext(this);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            //Toast.makeText(getApplicationContext(), auth.getCurrentUser().toString() , Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        }
        mRootRef = new Firebase("https://xpense-auditor-default-rtdb.firebaseio.com");
        mRootRef.keepSynced(true);
        Uid = auth.getUid();
        RefUid = mRootRef.child(Uid);
        RefName = RefUid.child("Name");
        RefEmail = RefUid.child("Email");


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View navHeaderView = navigationView.getHeaderView(0);
        tvHeaderName = (TextView) navHeaderView.findViewById(R.id.headerName);
        tvHeaderMail = (TextView) navHeaderView.findViewById(R.id.headerEmail);
        userImage = (ImageView) navHeaderView.findViewById(R.id.imageView);
// todo
//        storageReference = FirebaseStorage.getInstance().getReference();
//        storageRef=storageReference.child("Profile Image").child(Uid+".jpg");
//
//
//        try {
//            final File localFile = File.createTempFile("images", "jpg");
//            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
//                    userImage.setImageBitmap(bitmap);
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                }
//            });
//        } catch (IOException e ) {}


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        transactionsFragment = new AllTransactionsFragment();
        ft.replace(R.id.fragmentContainerView, transactionsFragment);
        ft.commit();


        navigationView.setNavigationItemSelectedListener(this);


        RefName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (auth.getCurrentUser() != null) {
                    auth.getCurrentUser().reload();
                }
                if (auth.getCurrentUser() != null) {
                    try {
                        tvHeaderName.setText(dataSnapshot.getValue().toString().trim());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(getApplicationContext(), "Failed to load details", Toast.LENGTH_SHORT).show();
            }
        });

        RefEmail.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (auth.getCurrentUser() != null) {
                    auth.getCurrentUser().reload();
                }
                if (auth.getCurrentUser() != null) {

                    try {
                        tvHeaderMail.setText(dataSnapshot.getValue().toString().trim());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

//        viewPager = (ViewPager) findViewById(R.id.viewpager);
//        setupViewPager(viewPager);
//        viewPager.setCurrentItem(currentpage);
//
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                if(position==0)
//                {
//                    currentpage=0;
//                    Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
//                    startActivity(intent);
//                }
//                if(position==1)
//                {
//                    currentpage=1;
//                    Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
//                    startActivity(intent);
//
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });

//
//        tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(viewPager);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.account_settings) {

            Intent i = new Intent(this, AccountSettingsActivity.class);
            startActivity(i);
        } else if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(), "To be updated in later versions", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.action_contact_us) {
            Intent i = new Intent(this, ContactUs.class);
            startActivity(i);
        } else if (id == R.id.action_sort) {

        } else if (id == R.id.action_amount_sort) {
            transactionsFragment.sortList(0);
            Toast.makeText(getApplicationContext(), "Sorted by Amount", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.action_date_sort) {
            transactionsFragment.sortList(1);
            Toast.makeText(getApplicationContext(), "Sorted by date", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.action_category_sort) {
            transactionsFragment.sortList(2);
            Toast.makeText(getApplicationContext(), "Sorted by category", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.action_memo_sort) {
            transactionsFragment.sortList(3);
            Toast.makeText(getApplicationContext(), "Sorted by memo", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void refreshTransactionList() {
        transactionsFragment.refreshTransactionList();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_group) {
            Intent i = new Intent(HomeActivity.this, GroupListActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_profile) {
            Intent i = new Intent(this, ProfileActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_show_analysis) {
            Intent i = new Intent(this, DashboardActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_settings) {
            Toast.makeText(getApplicationContext(), "To be updated in later versions", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_logout) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Are you sure you want to LogOut?");
            builder1.setCancelable(true);
            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            auth.signOut();
                            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(i);
                        }
                    });

            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        } else if (id == R.id.nav_rate) {
            Intent i = new Intent(this, Rate.class);
            startActivity(i);

        } else if (id == R.id.nav_suggest) {
            Intent i = new Intent(this, Suggest.class);
            startActivity(i);

        } else if (id == R.id.nav_share) {

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "I recommend you to try this app and comment about it";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "XpensAuditor");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        } else if (id == R.id.nav_refresh) {
            Intent i = new Intent(this, SMSReaderActivity.class);
            if (isSmsPermissionGranted()) {
                startActivity(i);
            } else {
                requestReadAndSendSmsPermission();
            }


        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private AlertDialog AskSignOutOption() {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(getApplicationContext())
                // set message, title, and icon
                .setTitle("SignOut")
                .setMessage("Do you Really want to SignOut ?")

                .setPositiveButton("SignOut", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        auth.signOut();
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();

        return myQuittingDialogBox;
    }


    public boolean isSmsPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestReadAndSendSmsPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, SMS_PERMISSION_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case SMS_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent i = new Intent(this, SMSReaderActivity.class);
                    startActivity(i);

                } else {
                    Toast.makeText(getApplicationContext(), "SMS read permission is required for this feature to work, Enabled it in under app settings", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(this, HomeActivity.class);
                    startActivity(i);
                }
                return;
            }

        }
    }
}