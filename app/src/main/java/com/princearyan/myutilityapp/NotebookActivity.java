package com.princearyan.myutilityapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NotebookActivity extends AppCompatActivity {
    private EditText noteTitle, noteContent;
    private Button saveNote, exportPDF;
    private LinearLayout noteHistory;
    private ArrayList<Note> notesList;
    private SharedPreferences sharedPreferences;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notebook);

        noteTitle = findViewById(R.id.noteTitle);
        noteContent = findViewById(R.id.noteContent);
        saveNote = findViewById(R.id.saveNote);
        exportPDF = findViewById(R.id.exportPDF);
        noteHistory = findViewById(R.id.noteHistory);

        sharedPreferences = getSharedPreferences("NotebookPrefs", Context.MODE_PRIVATE);
        notesList = loadNotes();

        displayNotes();

        saveNote.setOnClickListener(view -> {
            String title = noteTitle.getText().toString().trim();
            String content = noteContent.getText().toString().trim();
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

            if (!title.isEmpty() && !content.isEmpty()) {
                notesList.add(new Note(title, content, timestamp));
                saveNotes();
                displayNotes();
                noteTitle.setText("");
                noteContent.setText("");
            } else {
                Toast.makeText(this, "Both title and content are required", Toast.LENGTH_SHORT).show();
            }
        });

        exportPDF.setOnClickListener(view -> exportNotesToPDF());
    }

    private void displayNotes() {
        noteHistory.removeAllViews();
        for (int i = 0; i < notesList.size(); i++) {
            Note note = notesList.get(i);

            View noteView = LayoutInflater.from(this).inflate(R.layout.note_item, null);
            TextView titleView = noteView.findViewById(R.id.noteTitleView);
            TextView timeView = noteView.findViewById(R.id.noteTimeView);
            Button editButton = noteView.findViewById(R.id.editButton);
            Button deleteButton = noteView.findViewById(R.id.deleteButton);

            titleView.setText(note.getTitle());
            timeView.setText(note.getTimestamp());

            final int pos = i;

            noteView.setOnClickListener(v -> showFullNoteDialog(note));
            editButton.setOnClickListener(v -> editNoteDialog(pos));
            deleteButton.setOnClickListener(v -> {
                notesList.remove(pos);
                saveNotes();
                displayNotes();
            });

            noteHistory.addView(noteView);
        }
    }

    private void showFullNoteDialog(Note note) {
        new AlertDialog.Builder(this)
                .setTitle(note.getTitle())
                .setMessage(note.getContent())
                .setPositiveButton("OK", null)
                .show();
    }

    private void editNoteDialog(int index) {
        Note note = notesList.get(index);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_note, null);
        EditText editTitle = dialogView.findViewById(R.id.editNoteTitle);
        EditText editContent = dialogView.findViewById(R.id.editNoteContent);

        editTitle.setText(note.getTitle());
        editContent.setText(note.getContent());

        builder.setView(dialogView);
        builder.setPositiveButton("Save", (dialog, which) -> {
            note.setTitle(editTitle.getText().toString());
            note.setContent(editContent.getText().toString());
            saveNotes();
            displayNotes();
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void exportNotesToPDF() {
        try {
            File pdfFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Notebook.pdf");
            PdfWriter writer = new PdfWriter(new FileOutputStream(pdfFile));
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            for (Note note : notesList) {
                document.add(new Paragraph(note.getTitle()).setBold());
                document.add(new Paragraph("Created: " + note.getTimestamp()));
                document.add(new Paragraph(note.getContent()));
                document.add(new Paragraph("\n------------------\n"));
            }

            document.close();
            Toast.makeText(this, "PDF saved: " + pdfFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error exporting PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveNotes() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(notesList);
        editor.putString("notes", json);
        editor.apply();
    }

    private ArrayList<Note> loadNotes() {
        String json = sharedPreferences.getString("notes", null);
        Type type = new TypeToken<ArrayList<Note>>() {}.getType();
        return json != null ? gson.fromJson(json, type) : new ArrayList<>();
    }

    private static class Note {
        private String title, content, timestamp;
        public Note(String title, String content, String timestamp) { this.title = title; this.content = content; this.timestamp = timestamp; }
        public String getTitle() { return title; }
        public String getContent() { return content; }
        public String getTimestamp() { return timestamp; }
        public void setTitle(String title) { this.title = title; }
        public void setContent(String content) { this.content = content; }
    }
}
