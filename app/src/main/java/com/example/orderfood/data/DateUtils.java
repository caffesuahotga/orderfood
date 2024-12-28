package com.example.orderfood.data;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtils {

    public DateUtils() {
    }



    // Phương thức trả về danh sách ngày giữa hai mốc
    public static List<String> getDatesBetween(Date startDate, Date endDate, SimpleDateFormat dateFormat) {
        List<String> dateList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        // Bắt đầu từ ngày startDate
        calendar.setTime(startDate);

        while (!calendar.getTime().after(endDate)) {
            // Thêm ngày vào danh sách
            dateList.add(dateFormat.format(calendar.getTime()));

            // Tăng thêm 1 ngày
            calendar.add(Calendar.DATE, 1);
        }

        return dateList;
    };
    public static String getFormattedValue(String date) {
        if (date != null && !date.isEmpty()) {
            // Chỉ lấy ngày và tháng từ chuỗi ngày
            String[] parts = date.split("/");
            if (parts.length >= 2) {
                return parts[0] + "/" + parts[1];
            }
        }
        return ""; // Trả về chuỗi rỗng nếu không hợp lệ
    }

}

