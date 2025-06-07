package com.example.placareletronico;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.placareletronico.databinding.ActivityResultBinding;

public class ResultActivity extends AppCompatActivity {

    private ActivityResultBinding binding;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DBHelper(this);

        // Dados recebidos
        String teamLeft = getIntent().getStringExtra("teamLeft");
        String teamRight = getIntent().getStringExtra("teamRight");
        int scoreLeft = getIntent().getIntExtra("scoreLeft", 0);
        int scoreRight = getIntent().getIntExtra("scoreRight", 0);

        // Vencedor
        String winner;
        if (scoreLeft > scoreRight) {
            winner = teamLeft;
        } else if (scoreRight > scoreLeft) {
            winner = teamRight;
        } else {
            winner = "Empate";
        }

        binding.tvWinner.setText("Vencedor: " + winner);
        binding.tvFinalScore.setText("Placar Final: " + scoreLeft + " x " + scoreRight);

        // Carregar histórico
        carregarHistorico();
        binding.btnClearHistory.setOnClickListener(v -> {
            dbHelper.clearHistory(); // limpa o banco
            binding.historyContainer.removeAllViews(); // limpa a lista visual
        });

        binding.btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("newGame", true);
            startActivity(intent);
            finish(); // volta para a MainActivity
        });
    }

    private void carregarHistorico() {
        Cursor cursor = dbHelper.getAllMatches();

        LinearLayout container = findViewById(R.id.historyContainer);
        container.removeAllViews(); // limpa o container

        while (cursor.moveToNext()) {
            String teamL = cursor.getString(cursor.getColumnIndexOrThrow("team_left"));
            String teamR = cursor.getString(cursor.getColumnIndexOrThrow("team_right"));
            int scoreL = cursor.getInt(cursor.getColumnIndexOrThrow("score_left"));
            int scoreR = cursor.getInt(cursor.getColumnIndexOrThrow("score_right"));

            TextView tv = new TextView(this);
            tv.setText(teamL + " " + scoreL + " x " + scoreR + " " + teamR);
            tv.setTextSize(16);
            tv.setPadding(0, 8, 0, 8);
            tv.setTextColor(ContextCompat.getColor(this, R.color.white));
            container.addView(tv);
        }

        cursor.close();
    }

}
