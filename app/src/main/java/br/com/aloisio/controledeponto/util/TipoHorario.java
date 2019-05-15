package br.com.aloisio.controledeponto.util;

public enum TipoHorario {
    ENTRADA(0,"ENTRADA"),
    ALMOCO(1,"ALMOÇO"),
    RETORNO(2,"RETORNO"),
    SAIDA(3, "SAÍDA");

    private int valor;
    private String descricao;

    private TipoHorario(int valor, String descricao){
        this.valor = valor;
        this.descricao = descricao;
    }

    public int getValor(){
        return valor;
    }

    public String getDescricao(){
        return descricao;
    }
}
