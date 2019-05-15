package br.com.aloisio.controledeponto;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import br.com.aloisio.controledeponto.util.TipoHorario;
import br.com.aloisio.controledeponto.util.Util;

public class ControleDePontoActivity extends AppCompatActivity implements View.OnClickListener {

    Time[] horario = new Time[4];

    TimePicker tp;
    TextView horaSaida;
    TextView horarioAtual;

    Button botaoH1;
    Button botaoH2;
    Button botaoH3;

    TipoHorario opcao = TipoHorario.ENTRADA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controle_de_ponto);

        setTitle("CONTROLE DE PONTO");


        horaSaida =  findViewById(R.id.saida);
        tp = findViewById(R.id.t1);
        tp.setIs24HourView( Boolean.valueOf(true));

        horarioAtual = findViewById(R.id.horario);

        botaoH1 = findViewById(R.id.botaoEntrada);
        botaoH2 = findViewById(R.id.botaoAlmoco);
        botaoH3 = findViewById(R.id.botaoRetorno);

        botaoH1.setOnClickListener(this);
        botaoH2.setOnClickListener(this);
        botaoH3.setOnClickListener(this);

        codificarBotoes();

    }

    @Override
    protected void onResume() {

        super.onResume();

        SharedPreferences ponto_prefs = getSharedPreferences("ponto_prefs", 0);
        String h1 = ((SharedPreferences) ponto_prefs).getString("h1", null);
        String h2 = ((SharedPreferences) ponto_prefs).getString("h2", null);
        String h3 = ((SharedPreferences) ponto_prefs).getString("h3", null);

        horario[0] = new Time();
        horario[0].set(0, 30, 7, Util.getDia(), Util.getMes(), Util.getAno());
        horario[1] = new Time();
        horario[1].set(0, 0, 12, Util.getDia(), Util.getMes(), Util.getAno());
        horario[2] = new Time();
        horario[2].set(0, 30, 13, Util.getDia(), Util.getMes(), Util.getAno());



        if (h1 != null)
        {
            this.horario[0].hour = Integer.parseInt(h1.substring(0, 2));
            this.horario[0].minute = Integer.parseInt(h1.substring(3, 5));
        }
        if (h2 != null)
        {
            this.horario[1].hour = Integer.parseInt(h2.substring(0, 2));
            this.horario[1].minute = Integer.parseInt(h2.substring(3, 5));
        }
        if (h3 != null)
        {
            this.horario[2].hour = Integer.parseInt(h3.substring(0, 2));
            this.horario[2].minute = Integer.parseInt(h3.substring(3, 5));
        }

        botaoH1.setText( String.format("%02d", horario[0].hour) + ":" +  String.format("%02d", horario[0].minute) );
        botaoH2.setText( String.format("%02d", horario[1].hour) + ":" +  String.format("%02d", horario[1].minute) );
        botaoH3.setText( String.format("%02d", horario[2].hour) + ":" +  String.format("%02d", horario[2].minute) );

        opcao = TipoHorario.ALMOCO;
        horarioAtual.setText( opcao.getDescricao() );
        tp.setCurrentHour( horario[0].hour );
        tp.setCurrentMinute( horario[0].minute );

        exbirSaida();

    }

    private void codificarBotoes() {

        findViewById(R.id.botaoHoraAtual).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                tp.setCurrentHour( Util.getHora() ) ;
                tp.setCurrentMinute( Util.getMinuto() );
            }
        });

        findViewById(R.id.botaoRefresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                exbirSaida();
                Toast.makeText(ControleDePontoActivity.this, "SAÍDA: " + horaSaida.getText().toString() , Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.botaoSalvar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);

                int i = opcao.getValor();

                horario[i].hour = tp.getCurrentHour().intValue();
                horario[i].minute = tp.getCurrentMinute().intValue();

                String desc = new String (String.format("%02d", horario[i].hour) + ":" +  String.format("%02d", horario[i].minute));
                atualizarDescBotao( opcao, desc );

                SharedPreferences.Editor ponto_prefs = getSharedPreferences("ponto_prefs", 0).edit();
                ponto_prefs.putString("h" + (i+1), desc);

                ponto_prefs.apply();

                Toast.makeText(ControleDePontoActivity.this, opcao.getDescricao() + " ALTERADO(A)", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void exbirSaida() {

        int cargaHoraria = 528; // 8 horas e 48 miniutos

        int manha = Util.getDateDiff( horario[0], horario[1] );
        int residuo =  cargaHoraria - manha;

        int retorno = (horario[2].minute + horario[2].hour * 60);
        int saidaSugerida = retorno + residuo;

        int horaDeSaida = saidaSugerida / 60;
        int minutoSaida = saidaSugerida - (horaDeSaida * 60);

        horaSaida.setText(  new String (String.format("%02d", horaDeSaida) + ":" + String.format("%02d", minutoSaida)));
    }

    private void atualizarDescBotao( TipoHorario horario, String desc ){

        switch (horario){

            case ENTRADA:
                botaoH1.setText(desc);
                break;

            case ALMOCO:
                botaoH2.setText(desc);
                break;

            case RETORNO:
                botaoH3.setText(desc);
                break;
        }

    }

    @Override
    public void onClick(View v) {

        switch ( v.getId() ){

            case R.id.botaoEntrada:
                tp.setCurrentHour(Integer.valueOf(horario[0].hour));
                tp.setCurrentMinute(Integer.valueOf( this.horario[0].minute));
                opcao = TipoHorario.ENTRADA;
                break;

            case R.id.botaoAlmoco:
                tp.setCurrentHour(Integer.valueOf(horario[1].hour));
                tp.setCurrentMinute(Integer.valueOf( this.horario[1].minute));
                opcao = TipoHorario.ALMOCO;
                break;

            case R.id.botaoRetorno:
                tp.setCurrentHour(Integer.valueOf(horario[2].hour));
                tp.setCurrentMinute(Integer.valueOf( this.horario[2].minute));
                opcao = TipoHorario.RETORNO;
                break;

        }

        horarioAtual.setText( opcao.getDescricao() );


    }
}
