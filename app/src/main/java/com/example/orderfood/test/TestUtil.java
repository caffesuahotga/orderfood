package com.example.orderfood.test;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.orderfood.R;
import com.example.orderfood.data.DateUtils;
import com.example.orderfood.models.dto.StatisticalDTO;
import com.example.orderfood.services.StatisticalActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestUtil extends AppCompatActivity {

    private static final String TAG = "ProductTest";
    private Date myDate1 = null;
    private Date myDate2 = null;
    private String sdsd;
    public List<StatisticalDTO> dsdsdsdsd;
    public StatisticalDTO dsdsdsdsd1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.test_util_activity);
        TextView textView = findViewById(R.id.myTextView);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String dateString = "12/08/2024";
        String date = "12/20/2024";

        try {
            // Parse the string into a Date object
            myDate1 = dateFormat.parse(dateString);
            myDate2 = dateFormat.parse(date);
            sdsd = dateFormat.format(myDate1);

            // Lấy danh sách các ngày giữa 2 ngày
            List<String> a = DateUtils.getDatesBetween(myDate1, myDate2,dateFormat);
            StatisticalActivity sdsd =  new StatisticalActivity();
            dsdsdsdsd1 = sdsd.getTotalPriceByDate(a.get(0));

            dsdsdsdsd = sdsd.dataForStatistical(a);

            // In ra từng ngày trong danh sách
            for (String s : a) {
                Log.e(TAG, "Date: " + s);  // Thay vì log.e(s), dùng Log.e với TAG
            }


            // Hiển thị kết quả trong TextView



        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Date parsing error", Toast.LENGTH_SHORT).show();  // Thông báo lỗi nếu không parse được ngày
        }
    }
}
