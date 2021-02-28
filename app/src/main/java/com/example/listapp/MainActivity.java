package com.example.listapp;

import android.graphics.drawable.Drawable;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // Генератор случайностей
    private Random random = new Random();
    // Наш адаптер
    private ItemsDataAdapter adapter;
    // Список картинок, которые мы будем брать для нашего списка
    private List<Drawable> images = new ArrayList<>();

    private static final String FILE_NAME = "sample.txt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        ListView listView = findViewById(R.id.listView);
        Button mAddBtn = findViewById(R.id.addBtn);

        List<Item> savedData = loadSamplesFromFile();
        if (!savedData.isEmpty()) {
            adapter.setData(savedData);
        }


            setSupportActionBar(toolbar);

        fillImages();

        // При тапе по кнопке добавим один новый элемент списка
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateRandomItemData();
            }
        });

        // Создаем и устанавливаем адаптер на наш список
        adapter = new ItemsDataAdapter(this, null);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // При тапе по элементу списка будем показывать его данные
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showItemData(position);
                return true;
            }
        });

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateRandomItemData();
                int position = 0;
                ItemData itemData = adapter.getItem(position);
                saveDataToFile(itemData.getTitle(), itemData.getSubtitle());
            }
        });
    }


    private List<Item> loadSamplesFromFile() {
        List<Item> result = new ArrayList<>();
        File file = new File(getExternalFilesDir(null), FILE_NAME);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String allLines = reader.readLine();
            if (allLines==null) {
                return result;
            } else {
                String[] lines = allLines.split(";");
                for (String line: lines) {
                    result.add(new Item(line));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader!=null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

        private void saveDataToFile (String title, String subtitle){
            File file = new File(getExternalFilesDir(null), FILE_NAME);
            BufferedWriter writer = null;
            try {
                    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
                    writer.write(title + subtitle + ";");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException e) {
                        e.printStackTrace();
                        }
                }
            }
        }


        // Заполним различными картинками, которые встроены в сам Android
        // ContextCompat обеспечит нам поддержку старых версий Android
        private void fillImages () {
            images.add(ContextCompat.getDrawable(MainActivity.this,
                    android.R.drawable.ic_menu_report_image));
            images.add(ContextCompat.getDrawable(MainActivity.this,
                    android.R.drawable.ic_menu_add));
            images.add(ContextCompat.getDrawable(MainActivity.this,
                    android.R.drawable.ic_menu_agenda));
            images.add(ContextCompat.getDrawable(MainActivity.this,
                    android.R.drawable.ic_menu_camera));
            images.add(ContextCompat.getDrawable(MainActivity.this,
                    android.R.drawable.ic_menu_call));
        }

        // Создадим ну почти случайные данные для нашего списка.
        // random.nextInt(граница_последнего_элемента)
        // Для каждого элемента мы возьмем 1 случайную картинку
        // из 5, которые мы сделали вначале.
        private void generateRandomItemData () {
            adapter.addItem(new ItemData(
                    images.get(random.nextInt(images.size())),
                    "Hello" + adapter.getCount(),
                    "It\'s me"));
        }

        // Покажем сообщение с данными
        private void showItemData ( int position){
            ItemData itemData = adapter.getItem(position);
            Toast.makeText(MainActivity.this,
                    "Title: " + itemData.getTitle() + "\n" +
                            "Subtitle: " + itemData.getSubtitle(),
                    Toast.LENGTH_SHORT).show();
        }
    }
