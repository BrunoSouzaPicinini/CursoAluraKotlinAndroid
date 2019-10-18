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
import br.com.bspicinini.financask.extension.converteParaCalendar
import br.com.bspicinini.financask.extension.formataParaBrasileiro
import br.com.bspicinini.financask.model.Tipo
import br.com.bspicinini.financask.model.Transacao
import kotlinx.android.synthetic.main.form_transacao.view.*
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

class AdicionaTransacaoDialog(
    private val viewGroup: ViewGroup,
    private val context: Context
) {
    private val viewCriada = criaLayout()

    fun configuraDialog() {
        configuraCampoData()
        configuraCampoCategoria()
        configuraFormulario()
    }

    private fun configuraFormulario() {
        AlertDialog.Builder(context)
            .setTitle(R.string.adiciona_receita)
            .setView(viewCriada)
            .setPositiveButton(
                "Adicionar"
            ) { _, _ ->

                val valor = converteCampoValor(viewCriada.form_transacao_valor.text.toString() )

                val data = viewCriada.form_transacao_data.text.toString().converteParaCalendar()

                val categoriaEmTexto = viewCriada.form_transacao_categoria.selectedItem.toString()

                val transacaoCriada =
                    Transacao(tipo = Tipo.RECEITA, valor = valor, data = data, categoria = categoriaEmTexto)

                atualizaTransacoes(transacaoCriada)
                lista_transacoes_adiciona_menu.close(true)
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
        viewCriada.form_transacao_categoria.adapter = adapter
    }

    private fun configuraCampoData() {
        var dataSelecionada = Calendar.getInstance()
        viewCriada.form_transacao_data.setText(dataSelecionada.formataParaBrasileiro())
        viewCriada.form_transacao_data.setOnClickListener {
            DatePickerDialog(
                context,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->

                    dataSelecionada.set(year, month, dayOfMonth)
                    viewCriada.form_transacao_data.setText(dataSelecionada.formataParaBrasileiro())
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