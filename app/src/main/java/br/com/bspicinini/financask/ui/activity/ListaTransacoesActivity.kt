package br.com.bspicinini.financask.ui.activity

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import br.com.bspicinini.financask.R
import br.com.bspicinini.financask.extension.formataParaBrasileiro
import br.com.bspicinini.financask.model.Tipo
import br.com.bspicinini.financask.model.Transacao
import br.com.bspicinini.financask.ui.ResumoView
import br.com.bspicinini.financask.ui.adapter.ListaTransacoesAdapter
import kotlinx.android.synthetic.main.activity_lista_transacoes.*
import kotlinx.android.synthetic.main.form_transacao.view.*
import java.math.BigDecimal
import java.util.*

class ListaTransacoesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_transacoes)

        val transacoes: List<Transacao> = transacoesExemplo()

        configuraResumo(transacoes)
        configuraLista(transacoes)

        val viewCriada = LayoutInflater
            .from(this)
            .inflate(
                R.layout.form_transacao,
                window.decorView as ViewGroup,
                false
            )
        with(viewCriada.form_transacao_data) {
            setText(Calendar.getInstance().formataParaBrasileiro())
            setOnClickListener {
                DatePickerDialog(this@ListaTransacoesActivity,
                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        val dataSelecionada = Calendar.getInstance()
                        dataSelecionada.set(year, month, dayOfMonth)
                        viewCriada.form_transacao_data.setText(dataSelecionada.formataParaBrasileiro())
                    }, 2017, 9, 1)
                    .show()
            }
        }

        val adapter = ArrayAdapter.createFromResource(this, R.array.categorias_de_receita,android.R.layout.simple_spinner_dropdown_item)
        viewCriada.form_transacao_categoria.adapter = adapter

        lista_transacoes_adiciona_receita.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(R.string.adiciona_receita)
                .setView(viewCriada)
                .setPositiveButton("Adicionar", null)
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }

    private fun configuraResumo(transacoes: List<Transacao>) {
        val resumoView = ResumoView(this, window.decorView, transacoes)
        resumoView.atualiza()
    }

    private fun configuraLista(transacoes: List<Transacao>) {
        lista_transacoes_listview.adapter = ListaTransacoesAdapter(transacoes, this)
    }

    private fun transacoesExemplo(): List<Transacao> {
        return listOf(
            Transacao(
                BigDecimal(2000.50),
                tipo = Tipo.DESPESA,
                data = Calendar.getInstance()
            ),
            Transacao(
                BigDecimal(100),
                "Economia",
                Tipo.RECEITA
            ),
            Transacao(
                BigDecimal(10),
                "FGTS resgate anual",
                Tipo.RECEITA
            )
        )
    }

}