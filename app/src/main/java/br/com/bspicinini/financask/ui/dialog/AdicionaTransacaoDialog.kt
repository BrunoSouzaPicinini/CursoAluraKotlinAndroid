package br.com.bspicinini.financask.ui.dialog


import android.content.Context
import android.view.ViewGroup
import br.com.bspicinini.financask.model.Tipo
import br.com.bspicinini.financask.R

class AdicionaTransacaoDialog(
    viewGroup: ViewGroup,
    context: Context
) : FormularioTransacaoDialog(viewGroup, context) {

    override val tituloBotaoPositivo = "Adicionar"

    override fun tituloPor(tipo: Tipo) =
        if (tipo == Tipo.RECEITA) R.string.adiciona_receita else R.string.adiciona_despesa

}