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
class AdicionaTransacaoDialog(
        private val viewGroup: ViewGroup,
        private val context: Context
) {
    private val viewCriada = criaLayout()

    private val campoValor = viewCriada.form_transacao_valor
    private val campoData = viewCriada.form_transacao_data
    private val campoCategoria = viewCriada.form_transacao_categoria

    fun chama(tipo: Tipo, transacaoDelegate: TransacaoDelegate) {
        configuraCampoData()
        configuraCampoCategoria()
        configuraFormulario(tipo, transacaoDelegate)
    }

    private fun configuraFormulario(tipo: Tipo, transacaoDelegate: TransacaoDelegate) {

        val tituloTransacao = if (tipo == Tipo.RECEITA) R.string.adiciona_receita else R.string.adiciona_despesa

        AlertDialog.Builder(context)
                .setTitle(tituloTransacao)
                .setView(viewCriada)
                .setPositiveButton(
                        "Adicionar"
                ) { _, _ ->

                    val valor = converteCampoValor(campoValor.text.toString())

                    val data = campoData.text.toString().converteParaCalendar()

                    val categoriaEmTexto = campoCategoria.selectedItem.toString()

                    val transacaoCriada =
                            Transacao(tipo = tipo, valor = valor, data = data, categoria = categoriaEmTexto)

                    transacaoDelegate.delegate(transacaoCriada)
                }
                .setNegativeButton("Cancelar", null)
                .show()
    }

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

    private fun configuraCampoCategoria() {
        val adapter = ArrayAdapter.createFromResource(
                context,
                R.array.categorias_de_receita,
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