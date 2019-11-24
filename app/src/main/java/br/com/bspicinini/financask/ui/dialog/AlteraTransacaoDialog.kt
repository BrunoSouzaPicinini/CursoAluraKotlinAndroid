package br.com.bspicinini.financask.ui.dialog


import android.content.Context
import android.view.ViewGroup
import br.com.bspicinini.financask.R
import br.com.bspicinini.financask.delegate.TransacaoDelegate
import br.com.bspicinini.financask.extension.formataParaBrasileiro
import br.com.bspicinini.financask.model.Tipo
import br.com.bspicinini.financask.model.Transacao

class AlteraTransacaoDialog(
    viewGroup: ViewGroup,
    private val context: Context
) : FormularioTransacaoDialog(viewGroup, context) {

    override val tituloBotaoPositivo = "Alterar"

    fun chama(transacao: Transacao, transacaoDelegate: TransacaoDelegate) {
        super.chama(transacao.tipo, transacaoDelegate)

        inicializaCampos(transacao)
    }

    private fun inicializaCampos(transacao: Transacao) {
        inicializaCampoValor(transacao)
        inicializaCampoData(transacao)
        inicializaCampoCategoria(transacao)
    }

    private fun inicializaCampoCategoria(transacao: Transacao) {
        val categoriasRetornadas = context.resources.getStringArray(super.categoriaPor(transacao.tipo))
        campoCategoria.setSelection(categoriasRetornadas.indexOf(transacao.categoria), true)
    }

    private fun inicializaCampoData(transacao: Transacao) {
        campoData.setText(transacao.data.formataParaBrasileiro())
    }

    private fun inicializaCampoValor(transacao: Transacao) {
        campoValor.setText(transacao.valor.toString())
    }

    override fun tituloPor(tipo: Tipo) = if (tipo == Tipo.RECEITA) R.string.altera_receita else R.string.altera_despesa

}