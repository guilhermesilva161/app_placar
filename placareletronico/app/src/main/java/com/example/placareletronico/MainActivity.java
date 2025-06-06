package com.example.placareletronico;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.placareletronico.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private int scoreLeft = 0;
    private int scoreRight = 0;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DBHelper(this);

        // Botões esquerda
        binding.btnPlusLeft.setOnClickListener(v -> {
            scoreLeft++;
            updateScore();
        });

        binding.btnMinusLeft.setOnClickListener(v -> {
            if (scoreLeft > 0) scoreLeft--;
            updateScore();
        });

        // Botões direita
        binding.btnPlusRight.setOnClickListener(v -> {
            scoreRight++;
            updateScore();
        });

        binding.btnMinusRight.setOnClickListener(v -> {
            if (scoreRight > 0) scoreRight--;
            updateScore();
        });

        binding.btnSave.setOnClickListener(v -> {
            salvarPartida();
        });
    }
    private void verificarFimDePartida() {
        if (scoreLeft >= 12 || scoreRight >= 12) {
            salvarPartida();
        }
    }

    private void updateScore() {
        binding.tvScoreLeft.setText(String.valueOf(scoreLeft));
        binding.tvScoreRight.setText(String.valueOf(scoreRight));
        verificarFimDePartida();  //
    }

    private void salvarPartida() {
        String teamLeft = binding.etTeamLeft.getText().toString().trim();
        String teamRight = binding.etTeamRight.getText().toString().trim();

        if (TextUtils.isEmpty(teamLeft) || TextUtils.isEmpty(teamRight)) {
            Toast.makeText(this, "Por favor, insira o nome das duas equipes", Toast.LENGTH_SHORT).show();
            return;
        }

        // Salvar no banco
        long id = dbHelper.saveMatch(teamLeft, teamRight, scoreLeft, scoreRight);
        if (id > 0) {
            Toast.makeText(this, "Partida salva!", Toast.LENGTH_SHORT).show();
            abrirResultado(teamLeft, teamRight, scoreLeft, scoreRight);
        } else {
            Toast.makeText(this, "Erro ao salvar partida", Toast.LENGTH_SHORT).show();
        }
    }

    public void resetGame() {
        scoreLeft = 0;
        scoreRight = 0;
        updateScore();

        binding.etTeamLeft.setText("");
        binding.etTeamRight.setText("");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra("newGame", false)) {
            resetGame();
            // Remove a flag para não resetar novamente se o usuário ficar na activity
            intent.removeExtra("newGame");
        }
    }

    private void abrirResultado(String teamLeft, String teamRight, int scoreLeft, int scoreRight) {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("teamLeft", teamLeft);
        intent.putExtra("teamRight", teamRight);
        intent.putExtra("scoreLeft", scoreLeft);
        intent.putExtra("scoreRight", scoreRight);
        startActivity(intent);
    };
};
