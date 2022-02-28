package vian.mobile.hash;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity implements  AdapterView.OnItemSelectedListener{

    TextView output, algo;
    Button hashBtn, infoBtn;
    EditText input;
    int choice = 0;
    Spinner spin;
    ArrayAdapter<CharSequence> adapter;
    String in, out;
    InputMethodManager inMan;
    public AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        builder = new AlertDialog.Builder(this);
        input = findViewById(R.id.inputTxt);
        output = findViewById(R.id.outputTxt);
        hashBtn = findViewById(R.id.convertBtn);
        infoBtn = findViewById(R.id.alertBtn);
        spin = findViewById(R.id.hashType);
        adapter = ArrayAdapter.createFromResource(this, R.array.hash_types, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.spinner_list);
        algo = findViewById(R.id.algo);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);
        inMan = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        hashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checks(choice)){
                    Toast.makeText(MainActivity.this, "Choose an Algorithm!", Toast.LENGTH_SHORT).show();
                }
                in = input.getText().toString();
                setAlgo(choice);
                try {
                    out = toHex(convert(in, choice));
                    output.setText(out);
                    if(in.isEmpty()){
                        Toast.makeText(MainActivity.this, "Hash of the empty String! Enter something!", Toast.LENGTH_LONG).show();
                    }
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                try {
                    inMan.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                catch (Exception e){
                    // TODO
                }
            }
        });

        hashBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                output.setText("");
                input.setText("");
                in = "";
                return true;
            }
        });

        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup();
            }
        });
    }

    public static byte[] convert(String input, int choice) throws NoSuchAlgorithmException {
        MessageDigest md;
        if (choice==1) {
            md = MessageDigest.getInstance("MD5");
            return md.digest(input.getBytes(StandardCharsets.UTF_8));
        }
        else if (choice==2) {
            md = MessageDigest.getInstance("SHA-1");
            return md.digest(input.getBytes(StandardCharsets.UTF_8));
        }
        else if (choice==3) {
            md = MessageDigest.getInstance("SHA-256");
            return md.digest(input.getBytes(StandardCharsets.UTF_8));
        }
        else if (choice==4) {
            md = MessageDigest.getInstance("SHA-384");
            return md.digest(input.getBytes(StandardCharsets.UTF_8));
        }
        else if (choice==5) {
            md = MessageDigest.getInstance("SHA-512");
            return md.digest(input.getBytes(StandardCharsets.UTF_8));
        }
        else {
            md = MessageDigest.getInstance("MD1");
            return md.digest(input.getBytes(StandardCharsets.UTF_8));
        }
    }

    public static String toHex(byte[] hash){
        BigInteger bigInt = new BigInteger(1, hash);
        StringBuilder hex = new StringBuilder(bigInt.toString(16));

        while (hex.length() < 32){
            hex.insert(0, '0');
        }
        return hex.toString();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        choice = i;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public boolean checks(int c){
        return c != 0;
    }

    @SuppressLint("SetTextI18n")
    public void setAlgo(int c){
        if(c==1){
            algo.setText("MD5");
        }
        else if(c==2){
            algo.setText("SHA-1");
        }
        else if(c==3){
            algo.setText("SHA-256");
        }
        else if(c==4){
            algo.setText("SHA-384");
        }
        else if(c==5){
            algo.setText("SHA-512");
        }
    }

    public void popup(){
        builder.setCancelable(true);
        builder.setTitle("About");
        builder.setMessage(R.string.about);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }
        );
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}