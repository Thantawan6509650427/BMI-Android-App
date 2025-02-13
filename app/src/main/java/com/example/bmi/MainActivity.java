package com.example.bmi;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText weightInput = findViewById(R.id.editTextWeight);
        EditText heightInput = findViewById(R.id.editTextHeight);
        TextView bmiDisplay = findViewById(R.id.textBMIDisplay);
        TextView riskDisplay = findViewById(R.id.textRiskDisplay);
        Button calculateButton = findViewById(R.id.calculateButton);

        weightInput.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(8, 2)});
        heightInput.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(8, 2)});

        calculateButton.setOnClickListener(v -> {
            double weight = Double.parseDouble(weightInput.getText().toString());
            double height = Double.parseDouble(heightInput.getText().toString());

            double bmi = calculateBMI(weight, height);
            bmiDisplay.setText(formatter.format(bmi));

            // ตรวจสอบภาษาและแสดงข้อความตามนั้น
            String language = getResources().getConfiguration().locale.getLanguage();
            if (bmi < 18.5) {
                if (language.equals("th")) { // ภาษาไทย
                    riskDisplay.setText("น้ำหนักน้อย");
                } else { // ภาษาอื่น (ภาษาอังกฤษ)
                    riskDisplay.setText("underweight");
                }
                riskDisplay.setTextColor(getResources().getColor(R.color.low_risk));
            } else if (bmi < 24.9) {
                if (language.equals("th")) { // ภาษาไทย
                    riskDisplay.setText("น้ำหนักปกติ");
                } else { // ภาษาอื่น (ภาษาอังกฤษ)
                    riskDisplay.setText("normal weight");
                }
                riskDisplay.setTextColor(getResources().getColor(R.color.normal_risk));
            } else {
                if (language.equals("th")) { // ภาษาไทย
                    riskDisplay.setText("น้ำหนักเกิน");
                } else { // ภาษาอื่น (ภาษาอังกฤษ)
                    riskDisplay.setText("overweight");
                }
                riskDisplay.setTextColor(getResources().getColor(R.color.high_risk));
            }
        });
    }

    DecimalFormat formatter = new DecimalFormat("#,###.##");

    private double calculateBMI(double weight, double heightInCm) {
        double heightInM = heightInCm / 100;
        return weight / (heightInM * heightInM);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // หา TextView และ EditText ที่ต้องการ
        TextView bmiDisplay = findViewById(R.id.textBMIDisplay);
        TextView riskDisplay = findViewById(R.id.textRiskDisplay);
        TextView weightLabel = findViewById(R.id.textWeight);
        TextView heightLabel = findViewById(R.id.textHeight);
        TextView headText = findViewById(R.id.textHead);
        EditText editTextWeight = findViewById(R.id.editTextWeight);
        EditText editTextHeight = findViewById(R.id.editTextHeight);

        // ตั้งค่าขนาดฟอนต์ตามการปรับขนาดของระบบ
        headText.setTextSize(newConfig.fontScale * 24);
        bmiDisplay.setTextSize(newConfig.fontScale * 18);
        riskDisplay.setTextSize(newConfig.fontScale * 18);
        weightLabel.setTextSize(newConfig.fontScale * 18);
        heightLabel.setTextSize(newConfig.fontScale * 18);
        editTextWeight.setTextSize(newConfig.fontScale * 18);
        editTextHeight.setTextSize(newConfig.fontScale * 18);
    }

    class DecimalDigitsInputFilter implements InputFilter {
        private final Pattern mPattern;
        DecimalDigitsInputFilter(int digits, int digitsAfterZero) {
            mPattern = Pattern.compile("[0-9]{0," + (digits - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) +
                    "})?)||(\\.)?");
        }
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher matcher = mPattern.matcher(dest);
            if (!matcher.matches()) return "";
            return null;
        }
    }
}
