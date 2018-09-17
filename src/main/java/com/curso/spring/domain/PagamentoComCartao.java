package com.curso.spring.domain;

import com.curso.spring.enums.EstadoPagamento;

import javax.persistence.Entity;

@Entity
public class PagamentoComCartao extends Pagamento {
    private static final long serialVersionUID = 1L;

    private Integer numDeParcelas;

    public PagamentoComCartao(){}

    public PagamentoComCartao(Integer id, EstadoPagamento estado, Pedido pedido, Integer numDeParcelas) {
        super(id, estado, pedido);
        this.numDeParcelas = numDeParcelas;
    }

    public Integer getNumDeParcelas() {
        return numDeParcelas;
    }

    public void setNumDeParcelas(Integer numDeParcelas) {
        this.numDeParcelas = numDeParcelas;
    }
}
