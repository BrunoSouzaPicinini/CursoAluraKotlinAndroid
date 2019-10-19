package br.com.bspicinini.financask.delegate

import br.com.bspicinini.financask.model.Transacao

interface TransacaoDelegate {

    fun delegate(transacao: Transacao)
}