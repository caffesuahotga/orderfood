package com.example.orderfood.services;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.orderfood.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class StatisticsPieCharActivity extends AppCompatActivity {

    private EditText etStartDate, etEndDate;
    private Button btnStatistics;
    private PieChart pieChart;
    private FirebaseFirestore firestore;
    private String startDate, endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_pie_char);

        // Khởi tạo các views
        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        btnStatistics = findViewById(R.id.btnStatistics);
        pieChart = findViewById(R.id.pieChart);

        firestore = FirebaseFirestore.getInstance();

        // Thiết lập bộ chọn ngày
        etStartDate.setOnClickListener(v -> showDatePickerDialog(date -> {
            startDate = date;
            etStartDate.setText(date);
        }));

        etEndDate.setOnClickListener(v -> showDatePickerDialog(date -> {
            endDate = date;
            etEndDate.setText(date);
        }));

        // Bấm vào nút để tạo số liệu thống kê
        btnStatistics.setOnClickListener(v -> {
            if (startDate == null || endDate == null) {
                Toast.makeText(this, "Vui lòng chọn ngày bắt đầu và kết thúc!", Toast.LENGTH_SHORT).show();
                return;
            }
            generateStatistics();
        });
    }

    private void showDatePickerDialog(DatePickerCallback callback) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    callback.onDateSelected(sdf.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()); // No future dates
        datePickerDialog.show();
    }

    private void generateStatistics() {
        try {
            // Chuyển đổi startDate và endDate sang kiểu Date
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);

            // Convert to Firestore timestamps
            long startTimestamp = start.getTime();
            long endTimestamp = end.getTime();

            // Thực hiện truy vấn Firestore để lấy các đơn hàng trong khoảng thời gian
            firestore.collection("order")
                    .whereGreaterThanOrEqualTo("date", new Timestamp(start)) // So sánh với startDate
                    .whereLessThanOrEqualTo("date", new Timestamp(end)) // So sánh với endDate
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (queryDocumentSnapshots.isEmpty()) {
                            Toast.makeText(this, "Không có dữ liệu trong khoảng thời gian này", Toast.LENGTH_SHORT).show();
                        }

                        int[] statusCounts = new int[5]; // 5 trạng thái
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Timestamp orderDate = document.getTimestamp("date"); // Lấy trường date
                            if (orderDate != null) {
                                long orderTimestamp = orderDate.getSeconds() * 1000; // Chuyển Timestamp thành mili giây

                                // Kiểm tra xem thời gian của đơn hàng có nằm trong khoảng startDate và endDate không
                                if (orderTimestamp >= startTimestamp && orderTimestamp <= endTimestamp) {
                                    Long status = document.getLong("status");
                                    if (status != null && status >= 1 && status <= 5) {
                                        statusCounts[status.intValue() - 1]++;
                                    }
                                }
                            }
                        }
                        updatePieChart(statusCounts);
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Lỗi khi tải dữ liệu!", Toast.LENGTH_SHORT).show());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi xử lý ngày", Toast.LENGTH_SHORT).show();
        }
    }

    private void updatePieChart(int[] statusCounts) {
        List<PieEntry> entries = new ArrayList<>();
        String[] labels = {"Chờ xác nhận", "Đã xác nhận", "Đang giao", "Hoàn thành", "Đã hủy"};
        for (int i = 0; i < statusCounts.length; i++) {
            if (statusCounts[i] > 0) {
                entries.add(new PieEntry(statusCounts[i], labels[i]));
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "Thống kê trạng thái");
        dataSet.setColors(new int[]{
                getResources().getColor(R.color.grean),
                getResources().getColor(R.color.purple_200),
                getResources().getColor(R.color.teal_200),
                getResources().getColor(R.color.yellow),
                getResources().getColor(R.color.selected_rating_color)
        });

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate(); // Refresh chart
    }

    private interface DatePickerCallback {
        void onDateSelected(String date);
    }
}