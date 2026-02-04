package com.example.alfaversion;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//ליצור טיים מתחתיו כפתור לחיצה על הכפתור וברגע הלחיצה יתבצע וורקמנגר לשעה שכתובה בטיים עם המשפט היי הדביר הגבר

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment2 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment2() {
        // Required empty public constructor
    }


    public static Fragment2 newInstance(String param1, String param2) {
        Fragment2 fragment = new Fragment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    EditText etTime;
    Button btnNtification;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_2, container, false);

        etTime = view.findViewById(R.id.etTime);
        btnNtification = view.findViewById(R.id.btnNotification);
        requestNotificationPermission();


        // כאשר לוחצים על הכפתור
        btnNtification.setOnClickListener(new View.OnClickListener() {

            // פונקציה שרצה ברגע הלחיצה
            @Override
            public void onClick(View view) {

                // לוקח את הטקסט שהמשתמש כתב בתיבת השעה
                String time = etTime.getText().toString();

                // אם המשתמש לא כתב כלום – יוצאים מהפעולה
                if (time.isEmpty()) {
                    return;
                }

                // לוקח את השעה (לפני הנקודותיים)
                int hour = Integer.parseInt(time.split(":")[0]);

                // לוקח את הדקות (אחרי הנקודותיים)
                int minute = Integer.parseInt(time.split(":")[1]);

                // יוצר אובייקט שמכיל את הזמן הנוכחי
                Calendar now = Calendar.getInstance();

                // יוצר אובייקט חדש עבור הזמן שהמשתמש בחר
                Calendar selected = Calendar.getInstance();

                // קובע את השעה שהמשתמש הזין
                selected.set(Calendar.HOUR_OF_DAY, hour);

                // קובע את הדקות שהמשתמש הזין
                selected.set(Calendar.MINUTE, minute);

                // מאפס שניות
                selected.set(Calendar.SECOND, 0);

                // מאפס מילישניות
                selected.set(Calendar.MILLISECOND, 0);

                // אם השעה שהוזנה כבר עברה היום
                if (selected.before(now)) {

                    // מעביר את ההתראה ליום הבא
                    selected.add(Calendar.DAY_OF_MONTH, 1);
                }

                // מחשב כמה זמן צריך לחכות עד לשעה שנבחרה
                long delay = selected.getTimeInMillis() - now.getTimeInMillis();

                // יוצר משימה חד פעמית ל־WorkManager
                OneTimeWorkRequest work =
                        new OneTimeWorkRequest.Builder(NotificationWorker.class)

                                // קובע כמה זמן לחכות לפני ההרצה
                                .setInitialDelay(delay, TimeUnit.MILLISECONDS)

                                // בונה את המשימה
                                .build();

                // שולח את המשימה ל־WorkManager
                WorkManager.getInstance(getActivity()).enqueue(work);
                Toast.makeText(getContext(), "worked", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    private void requestNotificationPermission() {

        // רק באנדרואיד 13 ומעלה צריך לבקש הרשאה
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            // בודק אם אין עדיין הרשאה
            if (requireActivity().checkSelfPermission(
                    android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {

                // מבקש הרשאה מהמשתמש
                requestPermissions(
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        101
                );
            }
        }
    }

}