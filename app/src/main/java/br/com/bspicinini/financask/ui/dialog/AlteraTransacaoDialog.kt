package br.com.bspicinini.financask.ui.dialog


import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import br.com.bspicinini.financask.R
import br.com.bspicinini.financask.delegate.TransacaoDelegate
import br.com.bspicinini.financask.extension.converteParaCalendar
import br.com.bspicinini.financask.extension.formataParaBrasileiro
import br.com.bspicinini.financask.model.Tipo
import br.com.bspicinini.financask.model.Transacao
import kotlinx.android.synthetic.main.form_transacao.view.*
import java.math.BigDecimal
import java.util.*

class AlteraTransacaoDialog(
        private val viewGroup: ViewGroup,
        private val context: Context
) {
    private val viewCriada = criaLayout()

    private val campoValor = viewCriada.form_transacao_valor
    private val campoData = viewCriada.form_transacao_data
    private val campoCategoria = viewCriada.form_transacao_categoria

    fun chama(transacao: Transacao, transacaoDelegate: TransacaoDelegate) {
        configuraCampoData()
        configuraCampoCategoria(transacao.tipo)
        configuraFormulario(transacao, transacaoDelegate)
    }

    private fun configuraFormulario(transacao: Transacao, transacaoDelegate: TransacaoDelegate) {
        val tituloTransacao = tituloPor(transacao.tipo)
        campoValor.setText(transacao.valor.toString())
        campoData.setText(transacao.data.formataParaBrasileiro())
        val categoriasRetornadas = context.resources.getStringArray(categoriaPor(transacao.tipo))
        campoCategoria.setSelection(categoriasRetornadas.indexOf(transacao.categoria), true)

        AlertDialog.Builder(context)
                .setTitle(tituloTransacao)
                .setView(viewCriada)
                .setPositiveButton(
                        "Alterar"
                ) { _, _ ->

                    val valor = converteCampoValor(campoValor.text.toString())

                    val data = campoData.text.toString().converteParaCalendar()

                    val categoriaEmTexto = campoCategoria.selectedItem.toString()

                    val transacaoCriada =
                            Transacao(tipo = transacao.tipo, valor = valor, data = data, categoria = categoriaEmTexto)

                    transacaoDelegate.delegate(transacaoCriada)
                }
                .setNegativeButton("Cancelar", null)
                .show()
    }

    private fun tituloPor(tipo: Tipo) = if (tipo == Tipo.RECEITA) R.string.altera_receita else R.string.altera_despesa

    private fun categoriaPor(tipo: Tipo) = if (tipo == Tipo.RECEITA) R.array.categorias_de_receita else R.array.categorias_de_despesa;

    private fun converteCampoValor(valorTexto: String): BigDecimal {
        return try {
            BigDecimal(valorTexto)
        } catch (e: NumberFormatException) {
            Toast.makeText(
                    context,
                    "Falha na conversão de valor",
                    Toast.LENGTH_LONG
            ).show()
            BigDecimal.ZERO
        }
    }

    private fun configuraCampoCategoria(tipo: Tipo) {
        val adapter = ArrayAdapter.createFromResource(
                context,
                categoriaPor(tipo),
                android.R.layout.simple_spinner_dropdown_item
        )
        campoCategoria.adapter = adapter
    }

    private fun configuraCampoData() {
        var dataSelecionada = Calendar.getInstance()
        campoData.setText(dataSelecionada.formataParaBrasileiro())
        campoData.setOnClickListener {
            DatePickerDialog(
                    context,
                    DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                        dataSelecionada.set(year, month, dayOfMonth)
                        campoData.setText(dataSelecionada.formataParaBrasileiro())
                    },
                    dataSelecionada.get(Calendar.YEAR),
                    dataSelecionada.get(Calendar.MONTH),
                    dataSelecionada.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

    }

    private fun criaLayout(): View {
        return LayoutInflater
                .from(context)
                .inflate(
                        R.layout.form_transacao,
                        viewGroup,
                        false
                )
    }
}