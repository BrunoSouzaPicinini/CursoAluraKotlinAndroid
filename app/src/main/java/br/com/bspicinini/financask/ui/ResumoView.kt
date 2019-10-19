package br.com.bspicinini.financask.ui

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import br.com.bspicinini.financask.R
import br.com.bspicinini.financask.extension.formataParaBrasileiro
import br.com.bspicinini.financask.model.Resumo
import br.com.bspicinini.financask.model.Transacao
import kotlinx.android.synthetic.main.resumo_card.view.*
import java.math.BigDecimal

class ResumoView(
    private val context: Context,
    private val view: View,
    val transacoes: List<Transacao>
) {

    private val resumo = Resumo(transacoes)
    private val corReceita = ContextCompat.getColor(context, R.color.receita)
    private val corDespesa = ContextCompat.getColor(context, R.color.despesa)

    fun atualiza() {
        adicionaDespesa()
        adicionaReceita()
        adicionaTotal()
    }

    private fun adicionaReceita() {
        with(view.resumo_card_receita) {
            setTextColor(corReceita)
            text = resumo.receita.formataParaBrasileiro()
        }
    }

    private fun adicionaDespesa() {
        with(view.resumo_card_despesa) {
            setTextColor(corDespesa)
            text = resumo.despesa.formataParaBrasileiro()
        }
    }

    private fun adicionaTotal() {
        val total = resumo.total
        val cor = corPor(total)
        with(view.resumo_card_total) {
            setTextColor(cor)
            text = total.formataParaBrasileiro()
        }
    }

    private fun corPor(total: BigDecimal): Int {
        return if (total >= BigDecimal.ZERO) corReceita else corDespesa
    }
}