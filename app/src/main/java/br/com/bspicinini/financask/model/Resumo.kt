package br.com.bspicinini.financask.model

import java.math.BigDecimal

class Resumo(val transacoes: List<Transacao>) {

    fun receita(): BigDecimal {
        return BigDecimal(
            transacoes
                .filter { transacao -> transacao.tipo == Tipo.RECEITA }
                .sumByDouble { transacao -> transacao.valor.toDouble() }
        )
    }

    fun despesa(): BigDecimal {
        return BigDecimal(
            transacoes
                .filter { transacao -> transacao.tipo == Tipo.DESPESA }
                .sumByDouble { transacao -> transacao.valor.toDouble() }
        )
    }

    fun total(): BigDecimal {
        return receita().subtract(despesa())
    }
}