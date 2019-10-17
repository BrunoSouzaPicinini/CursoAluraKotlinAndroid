package br.com.bspicinini.financask.ui.activity

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import br.com.bspicinini.financask.R
import br.com.bspicinini.financask.extension.formataParaBrasileiro
import br.com.bspicinini.financask.model.Tipo
import br.com.bspicinini.financask.model.Transacao
import br.com.bspicinini.financask.ui.ResumoView
import br.com.bspicinini.financask.ui.adapter.ListaTransacoesAdapter
import kotlinx.android.synthetic.main.activity_lista_transacoes.*
import kotlinx.android.synthetic.main.form_transacao.view.*
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

class ListaTransacoesActivity : AppCompatActivity() {

    private val transacoes: MutableList<Transacao> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_transacoes)

        configuraResumo()
        configuraLista()

        val viewCriada = LayoutInflater
            .from(this)
            .inflate(
                R.layout.form_transacao,
                window.decorView as ViewGroup,
                false
            )
        with(viewCriada.form_transacao_data) {
            var dataSelecionada = Calendar.getInstance()
            setText(dataSelecionada.formataParaBrasileiro())
            setOnClickListener {
                DatePickerDialog(
                    this@ListaTransacoesActivity,
                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                        dataSelecionada.set(year, month, dayOfMonth)
                        viewCriada.form_transacao_data.setText(dataSelecionada.formataParaBrasileiro())
                    },
                    dataSelecionada.get(Calendar.YEAR),
                    dataSelecionada.get(Calendar.MONTH),
                    dataSelecionada.get(Calendar.DAY_OF_MONTH)
                )
                    .show()
            }
        }

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.categorias_de_receita,
            android.R.layout.simple_spinner_dropdown_item
        )
        viewCriada.form_transacao_categoria.adapter = adapter

        lista_transacoes_adiciona_receita.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(R.string.adiciona_receita)
                .setView(viewCriada)
                .setPositiveButton(
                    "Adicionar"
                ) { dialogInterface, i ->

                    val valor = try {
                        BigDecimal(viewCriada.form_transacao_valor.text.toString())
                    } catch (e: NumberFormatException) {
                        Toast.makeText(this,
                            "Falha na conversão de valor",
                            Toast.LENGTH_LONG).show()
                        BigDecimal.ZERO
                    }
                    val dataConvertida =
                        SimpleDateFormat("dd/MM/yyyy").parse(viewCriada.form_transacao_data.text.toString())
                    val data = Calendar.getInstance()
                    data.time = dataConvertida
                    val categoriaEmTexto = viewCriada.form_transacao_categoria.selectedItem.toString()

                    val transacaoCriada =
                        Transacao(tipo = Tipo.RECEITA, valor = valor, data = data, categoria = categoriaEmTexto)

                    atualizaTransacoes(transacaoCriada)
                    lista_transacoes_adiciona_menu.close(true)
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }

    private fun atualizaTransacoes(transacao: Transacao) {
        transacoes.add(transacao)
        atualizaTransacoes()
    }

    private fun atualizaTransacoes() {
        configuraResumo()
        configuraLista()
    }

    private fun configuraResumo() {
        val resumoView = ResumoView(this, window.decorView, transacoes)
        resumoView.atualiza()
    }

    private fun configuraLista() {
        lista_transacoes_listview.adapter = ListaTransacoesAdapter(transacoes, this)
    }

}