package com.example.orderfood.services;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orderfood.R;
import com.example.orderfood.data.HandleData;
import com.example.orderfood.models.Order;
import com.example.orderfood.models.dto.StatisticalDTO;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StatisticStatusActivity extends AppCompatActivity {

    private PieChart piechar;
    private boolean isChartDisplayed = false;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private SimpleDateFormat firebaseFormat = new SimpleDateFormat("MMMM d, yyyy 'at' hh:mm:ss a z");
    private Date startDate = null;
    private Date endDate = null;
    private TextView startDateText;
    private TextView endDateText ;

    public StatisticStatusActivity(){
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_status);

        // Tìm LineChart và các View khác
        piechar = findViewById(R.id.pieChart);
        TextView startDateText = findViewById(R.id.startDateText);
        TextView endDateText = findViewById(R.id.endDateText);
        Button confirmButton = findViewById(R.id.confirmButton);

        // Ẩn LineChart ban đầu
        piechar.setVisibility(View.GONE);

        // Calendar instance
        Calendar calendar = Calendar.getInstance();

        // Chọn ngày bắt đầu
        startDateText.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, month, dayOfMonth) -> {

                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(year, month, dayOfMonth);
                        startDate = selectedCalendar.getTime();

                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        startDateText.setText(selectedDate);

                        // Kích hoạt endDateText sau khi đã chọn ngày bắt đầu
                        endDateText.setEnabled(true);
                    },
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

            // Không cho chọn ngày tương lai
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        endDateText.setOnClickListener(v -> {
            // Kiểm tra nếu chưa chọn ngày bắt đầu
            if (startDateText.getText().toString().equals("Start day")) {
                Toast.makeText(this, "Vui lòng chọn ngày bắt đầu trước!", Toast.LENGTH_SHORT).show();
                return;
            }

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, month, dayOfMonth) -> {

                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(year, month, dayOfMonth);
                        endDate = selectedCalendar.getTime();
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        endDateText.setText(selectedDate);
                    },
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

            // Giới hạn ngày của endDateText
            Calendar startDate = Calendar.getInstance();
            String[] parts = startDateText.getText().toString().split("/");
            startDate.set(Integer.parseInt(parts[2]), Integer.parseInt(parts[1]) - 1, Integer.parseInt(parts[0]));

            datePickerDialog.getDatePicker().setMinDate(startDate.getTimeInMillis());
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        Button destroyButton = findViewById(R.id.destroy);
        destroyButton.setOnClickListener(v -> {
            // Tạo intent để tải lại Activity hiện tại
            Intent intent = getIntent();
            finish(); // Kết thúc Activity hiện tại
            startActivity(intent); // Bắt đầu lại Activity
        });

        // Xác nhận ngày đã chọn
        confirmButton.setOnClickListener(v -> {
            String startDate = startDateText.getText().toString();
            String endDate = endDateText.getText().toString();

            if (!isChartDisplayed) { // Nếu biểu đồ chưa được hiển thị
                if (startDate.equals("Start day") || endDate.equals("End day")) {
                    Toast.makeText(this, "Vui lòng chọn ngày bắt đầu và kết thúc!", Toast.LENGTH_SHORT).show();
                    return;
                }

                piechar.setVisibility(View.VISIBLE); // Hiển thị LineChart
                setupPieChartForStatus(); // Cài đặt dữ liệu cho biểu đồ

                // Vô hiệu hóa các thành phần nhập liệu
                startDateText.setEnabled(false);
                endDateText.setEnabled(false);
                confirmButton.setEnabled(false);

                isChartDisplayed = true; // Đánh dấu là biểu đồ đã được hiển thị
            }
        });
    }
    private void setupPieChartForStatus() {
        List<StatisticalDTO> statisticalData = getStatusStatistics();
        ArrayList<String> labels = new ArrayList<>();

        ArrayList<PieEntry> entries = new ArrayList<>();
        for (int i = 0; i < statisticalData.size(); i++) {
            StatisticalDTO data = statisticalData.get(i);
            if (data.getTotal() > 0) {  // Nếu có dữ liệu, thêm vào PieEntry
                entries.add(new PieEntry((float) data.getTotal(), data.getDate()));
                labels.add(data.getDate());
            }
        }

        // Tạo một piedataset cho biểu đồ hình tròn
        PieDataSet dataSet = new PieDataSet(entries, "Thống kê trạng thái đơn hàng");
        dataSet.setColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.grean),
                getResources().getColor(R.color.teal_200), getResources().getColor(R.color.selected_rating_color), getResources().getColor(R.color.yellow));
        PieData data = new PieData(dataSet);
        piechar.setData(data);
        piechar.invalidate();  // Làm mới biểu đồ

        // Cấu hình PieChart
        piechar.setExtraOffsets(10 ,10, 10, 10);  // Giảm khoảng cách giữa các phần trên biểu đồ

        // Thay đổi kích thước PieChart nếu cần
        ViewGroup.LayoutParams params = piechar.getLayoutParams();
        params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);  // Giảm kích thước xuống 60% chiều rộng màn hình
        params.height = (int) (getResources().getDisplayMetrics().heightPixels * 0.6); // Giảm kích thước xuống 40% chiều cao màn hình
        piechar.setLayoutParams(params);

    }

    public List<StatisticalDTO> getStatusStatistics() {
        HandleData handleData = new HandleData();
        List<Order> orderList = handleData.getAllOrders();

        // Khởi tạo một mảng để đếm đơn đặt hàng theo trạng thái
        double[] statusCount = new double[5]; // Để đếm cho 5 trạng thái

        // Lặp qua các đơn đặt hàng và tính theo trạng thái
        for (Order order : orderList) {
            int status = order.getStatus();  // Get the status of the order
            if (status >= 1 && status <= 5) {
                statusCount[status - 1]++;  // Increment the count for the corresponding status
            }
        }

        // Tạo một danh sách Thống kê cho từng trạng thái
        List<StatisticalDTO> statusStatistics = new ArrayList<>();
        statusStatistics.add(new StatisticalDTO("Chờ xác nhận", statusCount[0]));
        statusStatistics.add(new StatisticalDTO("Đã xác nhận", statusCount[1]));
        statusStatistics.add(new StatisticalDTO("Đang giao", statusCount[2]));
        statusStatistics.add(new StatisticalDTO("Đã giao - Hoàn thành", statusCount[3]));
        statusStatistics.add(new StatisticalDTO("Hủy", statusCount[4]));

        return statusStatistics;
    }
}