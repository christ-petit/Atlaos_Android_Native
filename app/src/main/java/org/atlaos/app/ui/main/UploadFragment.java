package org.atlaos.app.ui.main;

import android.content.ContentResolver;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Looper;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Tasks;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import org.atlaos.app.R;
import org.atlaos.app.databinding.FragmentUploadBinding;
import org.atlaos.app.model.DescribeRecordModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class UploadFragment extends Fragment implements MediaHttpUploaderProgressListener {
    private FragmentUploadBinding binding;
    private DescribeRecordModel model;
    private static final String TAG = "UploadFragment";
    private ProgressBar activeprogressBar;
    private  String appName;
    public UploadFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding=FragmentUploadBinding.inflate(inflater, container, false);
        model = new ViewModelProvider(requireActivity()).get(DescribeRecordModel.class);
        ApplicationInfo applicationInfo = getActivity().getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        appName = stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : getActivity().getString(stringId);

        return binding.getRoot();

    }





    public void submit() {

        //Connecting as user
  /*      googleSignInAccount=GoogleSignIn.getLastSignedInAccount(this);
        if (googleSignInAccount == null) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
   */






        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    // Instance of the JSON factory
                    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

                    // Instance of the scopes required
                    List scopes = new ArrayList();
                    scopes.add(DriveScopes.DRIVE);

                    // Http transport creation
                    HttpTransport httpTransport = new NetHttpTransport();

                    java.io.File licenseFile = getSecretFile();
                    GoogleCredential credential = new GoogleCredential.Builder()
                            .setTransport(httpTransport)
                            .setJsonFactory(jsonFactory)
                            .setServiceAccountId("test-spreadsheetapi-account@collect-cp.iam.gserviceaccount.com")
                            .setServiceAccountScopes(scopes)
                            .setServiceAccountPrivateKeyFromP12File(licenseFile)
                            .build();
                    Drive driveService = new Drive.Builder(httpTransport, jsonFactory, credential)
                            .setApplicationName("Atlaos-android-native")
                            .build();
                    String photoUrl="";
                    if (model.getPhotoUri() != null) {
                        activeprogressBar = binding.fragmentUploadPictureProgress;
                     photoUrl = uploadToDrive(driveService, R.string.drive_folder_img_id, model.getPhotoUri());
                   } else {
                      binding.fragmentUploadPictureProgress.setVisibility(View.GONE);
                   }
                    activeprogressBar=binding.fragmentUploadRecordProgress;
                    String recordUrl = uploadToDrive(driveService,R.string.drive_folder_record_id, model.getRecordUri());


                    Sheets service = new Sheets.Builder(httpTransport, jsonFactory, credential)
                            .setApplicationName("Atlaos-android-native")
                            .build();

                    service.spreadsheets().get("1tRz2vSWYV2B8Okt45eD-k72H81Kd5lhgF1zF_bQ1NV4");
                    ValueRange requestBody = new ValueRange();
                    SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yy");
                    SimpleDateFormat timeFormat=new SimpleDateFormat("dd/MM/yy hh:mm");
                    List values=new ArrayList();
                    values.add(dateFormat.format(new Date()));
                    values.add(timeFormat.format(new Date()));
                    values.add(model.getArtist()!= null ? model.getArtist():"");
                    values.add(model.getStoryName()!= null ? model.getStoryName():"");
                    values.add(model.getProvince()!= null ? model.getProvince():"");
                    values.add(model.getDistrict()!= null ? model.getDistrict():"");
                    values.add(model.getVillage()!= null ? model.getVillage():"");
                    values.add(model.getType()!= null ? model.getType():"");
                    values.add(model.getType_other()!= null ? model.getType_other():"");
                    values.add(model.getLatLng() != null ? model.getLatLng().latitude+","+model.getLatLng().longitude:"");
                    values.add("");
                    values.add("");
                    values.add(model.getLatLng() != null ?model.getLatLng().longitude+"":"");
                    values.add(model.getLatLng() != null ? model.getLatLng().latitude+"":"");
                    values.add(recordUrl != null? recordUrl:"");
                    values.add("");
                    values.add((photoUrl) != null ? photoUrl:"");
                    values.add("");
                    values.add("uuid:"+model.getUuid());

                    requestBody.setValues(Collections.singletonList(values));
                   /* requestBody.setValues(
                            Arrays.asList(
                                    Arrays.asList("30/05/21", "30/05/21 16:41", "conte 1", "toto"),
                                    Arrays.asList("30/05/21", "30/05/21 16:42", "conte 2", "titi"))); */
                    Sheets.Spreadsheets.Values.Append request =
                            service.spreadsheets().values().append("1tRz2vSWYV2B8Okt45eD-k72H81Kd5lhgF1zF_bQ1NV4", "Sheet1!A1:S1", requestBody);
                    request.setValueInputOption("RAW");
                    request.setInsertDataOption("INSERT_ROWS");

                    AppendValuesResponse response = request.execute();
                    Log.w(TAG, "success api call");

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                          binding.fragmentUploadDone.setVisibility(View.VISIBLE);

                        }
                    });


                } catch (Exception e) {
                    Log.e(TAG, "could not update spreadsheet", e);
                   getActivity().runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           Toast.makeText(getActivity(), "error uploading record :"+e.getMessage(), Toast.LENGTH_LONG).show();
                       }
                   });


                }

            }


            private String uploadToDrive(Drive service, int folderIDRessource, Uri uri) throws IOException {
                String folderId = getString(folderIDRessource);

                InputStreamContent content = new InputStreamContent(getMimeType(uri), getActivity().getContentResolver().openInputStream(uri));
                content.setLength(getSize(uri));


                com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File()
                        .setName(model.getUuid().toString())
                        .setParents(Collections.singletonList(folderId))
                        .setViewersCanCopyContent(true);
                com.google.api.services.drive.Drive.Files.Create create = service.files()
                        .create(fileMetadata, content)
                        .setFields("id, name, webContentLink");
                        create.getMediaHttpUploader().setProgressListener(UploadFragment.this);
                        create.getMediaHttpUploader().setDirectUploadEnabled(false);
                        create.getMediaHttpUploader().setChunkSize(MediaHttpUploader.MINIMUM_CHUNK_SIZE);

                 com.google.api.services.drive.model.File file= create.execute();


                Log.w(TAG, "id:" + file.getId() + "\n link:" + file.getWebContentLink());

                return file.getWebContentLink();
            }

        });
        thread.start();
    }

    public String getMimeType(Uri uri) {
        String mimeType = null;
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            ContentResolver cr = getActivity().getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    public java.io.File getSecretFile() throws IOException {
        File f = File.createTempFile("account-certificate.p12", null, getActivity().getCacheDir());
        if (f.exists()) {
            f.delete();
        }
        try {
            InputStream is = getResources().openRawResource(R.raw.account_certificate);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();


            FileOutputStream fos = new FileOutputStream(f);
            fos.write(buffer);
            fos.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return f;
    }
    public class RunnableUpdate implements Runnable {
        double p=0;
        long s;
        @Override
        public void run() {
            activeprogressBar.setProgress((int) p);
        }
        public void setProgress(double i) {
            p=i;
        }
        public void setFileSize(long s) {
            this.s=s;
        }
    };
    private RunnableUpdate update=new RunnableUpdate();

    @Override
    public void progressChanged(MediaHttpUploader uploader) throws IOException {
        if (activeprogressBar != null) {

            update.setProgress(uploader.getProgress()*100d);
            getActivity().runOnUiThread(update);
        }
    }
    private long getSize(Uri uri) {
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            Cursor returnCursor =
                    getActivity().getContentResolver().query(uri, null, null, null, null);
            if (returnCursor != null && returnCursor.moveToFirst()) {
                int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                return returnCursor.getLong(sizeIndex);
            }}
        if(ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            File file = new File(uri.getPath());
            return file.length();
        }

        return -1;
    }
}