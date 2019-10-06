package br.com.bspicinini.financask.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.bspicinini.financask.R
import br.com.bspicinini.financask.model.Transacao
import br.com.bspicinini.financask.ui.adapter.ListaTransacoesAdapter
import kotlinx.android.synthetic.main.activity_lista_transacoes.*
import java.math.BigDecimal
import java.util.*

class ListaTransacoesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_transacoes)

        val transacoes = listOf(
            Transacao(
                BigDecimal(20.50),
                "Comida",
                Calendar.getInstance()
            ),
            Transacao(
                BigDecimal(100),
                "Economia",
                Calendar.getInstance()
            )
        )

        lista_transacoes_listview.setAdapter(
            ListaTransacoesAdapter(transacoes, this)
        )
    }

}