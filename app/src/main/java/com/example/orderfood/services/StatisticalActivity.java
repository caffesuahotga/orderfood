package com.example.orderfood.services;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.orderfood.R;
import com.example.orderfood.data.DateUtils;
import com.example.orderfood.data.HandleData;
import com.example.orderfood.models.Order;
import com.example.orderfood.models.dto.StatisticalDTO;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StatisticalActivity extends BaseTopBottomViewActivity {

    private LineChart lineChart;
    private boolean isChartDisplayed = false;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private SimpleDateFormat firebaseFormat = new SimpleDateFormat("MMMM d, yyyy 'at' hh:mm:ss a z");
    private Date startDate = null;
    private Date endDate = null;
    private  TextView startDateText;
    private TextView endDateText ;

    public StatisticalActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getLayoutInflater().inflate(R.layout.activity_statistical, findViewById(R.id.content_frame_top_bot));



        // Tìm LineChart và các View khác
        lineChart = findViewById(R.id.lineChart);
        TextView startDateText = findViewById(R.id.startDateText);
        TextView endDateText = findViewById(R.id.endDateText);
        Button confirmButton = findViewById(R.id.confirmButton);

        // Ẩn LineChart ban đầu
        lineChart.setVisibility(View.GONE);

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
                // code tiếp theo







                lineChart.setVisibility(View.VISIBLE); // Hiển thị LineChart
                setupLineChart(); // Cài đặt dữ liệu cho biểu đồ

                // Vô hiệu hóa các thành phần nhập liệu
                startDateText.setEnabled(false);
                endDateText.setEnabled(false);
                confirmButton.setEnabled(false);

                isChartDisplayed = true; // Đánh dấu là biểu đồ đã được hiển thị
            }
        });

    }


    private void setupLineChart() {
        // Giả sử bạn có danh sách các StatisticalDTO đã được lấy từ cơ sở dữ liệu hoặc tính toán
        List<StatisticalDTO> statisticalData = dataForStatistical(DateUtils.getDatesBetween(startDate, endDate, dateFormat));

        // Tạo danh sách nhãn (labels) từ các ngày
        List<String> labels = new ArrayList<>();
        ArrayList<Entry> entries = new ArrayList<>();


        // Chuyển dữ liệu từ StatisticalDTO thành Entry và thêm nhãn
        for (int i = 0; i < statisticalData.size(); i++) {
            StatisticalDTO data = statisticalData.get(i);
            entries.add(new Entry(i, (float) data.getTotal()));
            labels.add(DateUtils.getFormattedValue(data.getDate())); // Lấy ngày để làm nhãn
        }

        // Tạo LineDataSet (Dữ liệu cho biểu đồ)
        LineDataSet dataSet = new LineDataSet(entries, "Dữ liệu của tôi");
        dataSet.setColor(getResources().getColor(R.color.colorPrimary)); // Màu cho đường line
        dataSet.setValueTextColor(getResources().getColor(R.color.black)); // Màu cho các giá trị

        // Tạo LineData từ LineDataSet
        LineData lineData = new LineData(dataSet);

        // Gán dữ liệu vào biểu đồ
        lineChart.setData(lineData);

        // Cấu hình trục X
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);  // Đặt trục X dưới đáy
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels)); // Sử dụng nhãn
        xAxis.setGranularity(1f); // Hiển thị từng nhãn
        xAxis.setGranularityEnabled(true);

        // Cấu hình trục Y
        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setAxisMinimum(0f); // Thiết lập giá trị tối thiểu của trục Y

        // Vô hiệu hóa trục Y bên phải
        lineChart.getAxisRight().setEnabled(false);

        // Làm mới biểu đồ
        lineChart.invalidate();
    }


    public StatisticalDTO getTotalPriceByDate(String date) {
        HandleData handleData = new HandleData();
        List<Order> orderList = handleData.getAllOrderList();
        String formattedDate=null;

        double total = 0.0;

        for (Order order : orderList) {
            formattedDate = dateFormat.format(order.getDate());
            if(formattedDate.equals(date))
                    total += order.getTotalPrice();

        }
        return new StatisticalDTO(date,total) ;

    }

    public List<StatisticalDTO> dataForStatistical(List<String> Date){
        List<StatisticalDTO>  statisticalDTOList  = new ArrayList<StatisticalDTO>();
        for (String date: Date){
            statisticalDTOList.add(getTotalPriceByDate(date));
        }
        return statisticalDTOList;
    }
    
}
