package br.com.bspicinini.financask.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.AdapterView
import br.com.bspicinini.financask.R
import br.com.bspicinini.financask.model.Tipo
import br.com.bspicinini.financask.model.Transacao
import br.com.bspicinini.financask.ui.ResumoView
import br.com.bspicinini.financask.ui.adapter.ListaTransacoesAdapter
import br.com.bspicinini.financask.ui.dialog.AdicionaTransacaoDialog
import br.com.bspicinini.financask.ui.dialog.AlteraTransacaoDialog
import kotlinx.android.synthetic.main.activity_lista_transacoes.*

class ListaTransacoesActivity : AppCompatActivity() {

    private val transacoes: MutableList<Transacao> = mutableListOf()
    private val viewDaActivity by lazy {
        window.decorView
    }
    private val viewGroupDaActivity by lazy {
        viewDaActivity as ViewGroup
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_transacoes)

        configuraResumo()
        configuraLista()
        configuraFab()
    }

    private fun configuraFab() {
        lista_transacoes_adiciona_receita
                .setOnClickListener {
                    chamaDialogDeAdicao(Tipo.RECEITA)
                }

        lista_transacoes_adiciona_despesa
                .setOnClickListener {
                    chamaDialogDeAdicao(Tipo.DESPESA)
                }
    }

    private fun chamaDialogDeAdicao(tipo: Tipo) {
        AdicionaTransacaoDialog(viewGroupDaActivity, this)
                .chama(tipo) { transacaoAdicao ->
                    adiciona(transacaoAdicao)
                    lista_transacoes_adiciona_menu.close(true)
                }
    }

    private fun chamaDialogDeAlteracao(transacao: Transacao, position: Int) {
        AlteraTransacaoDialog(viewGroupDaActivity, this)
                .chama(transacao) { transacaoAlteracao ->
                    altera(transacaoAlteracao, position)
                }
    }

    private fun adiciona(transacao: Transacao) {
        transacoes.add(transacao)
        atualizaTransacoes()
    }

    private fun altera(transacao: Transacao, position: Int) {
        transacoes[position] = transacao
        atualizaTransacoes()
    }

    private fun atualizaTransacoes() {
        configuraResumo()
        configuraLista()
    }

    private fun configuraResumo() {
        val resumoView = ResumoView(this, viewDaActivity, transacoes)
        resumoView.atualiza()
    }

    private fun configuraLista() {
        with(lista_transacoes_listview)
        {
            adapter = ListaTransacoesAdapter(transacoes, this@ListaTransacoesActivity)
            setOnItemClickListener { _, _, position, _ ->
                val transacao = transacoes[position]
                chamaDialogDeAlteracao(transacao, position)
            }

            setOnCreateContextMenuListener { menu, view, menuInfo ->
                menu.add(Menu.NONE, 1, Menu.NONE, "Excluir")
            }
        }
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        var idMenu = item?.itemId
        if (idMenu == 1) {
            val adapterMenuInfo = item?.menuInfo as AdapterView.AdapterContextMenuInfo
            val posicaoDaTransacao = adapterMenuInfo.position
            transacoes.removeAt(posicaoDaTransacao)
            atualizaTransacoes()
        }
        return super.onContextItemSelected(item)
    }
}