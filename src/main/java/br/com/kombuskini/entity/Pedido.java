package br.com.kombuskini.entity;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    public enum StatusPedido{
        RASCUNHO,
        REGISTRADO,
        ENVIADO,
        FINALIZADO
    }

    private int id;
    private Cliente cliente;
    private final List<ItemPedido> itens = new ArrayList<>();
    private StatusPedido status = StatusPedido.RASCUNHO;

    public Pedido(int id, Cliente cliente){
        if(cliente == null) throw  new IllegalArgumentException("O cliente é obrigatório no novo pedido!");
        this.id = id;
        this.cliente = cliente;
    }

    public void adicionarProduto(Kombuskini produto, int quantidade){

    }
}
