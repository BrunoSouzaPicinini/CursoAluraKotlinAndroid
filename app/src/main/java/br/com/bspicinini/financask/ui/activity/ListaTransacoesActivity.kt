package br.com.bspicinini.financask.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.bspicinini.financask.R
import br.com.bspicinini.financask.model.Tipo
import br.com.bspicinini.financask.model.Transacao
import br.com.bspicinini.financask.ui.ResumoView
import br.com.bspicinini.financask.ui.adapter.ListaTransacoesAdapter
import kotlinx.android.synthetic.main.activity_lista_transacoes.*
import java.math.BigDecimal
import java.util.*

class ListaTransacoesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_transacoes)

        val transacoes: List<Transacao> = transacoesExemplo()

        configuraResumo(transacoes)
        configuraLista(transacoes)
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