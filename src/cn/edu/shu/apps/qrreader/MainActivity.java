package cn.edu.shu.apps.qrreader;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button startButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        android.widget.Toast.makeText(this, "Clicked", android.widget.Toast.LENGTH_SHORT).show();
    }
}
