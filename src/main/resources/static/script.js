const API = "/api/projetos";

const formulario = document.getElementById("formProjeto");
const lista = document.getElementById("listaProjetos");
const mensagem = document.getElementById("mensagem");
const botaoCadastrar = formulario.querySelector("button[type='submit']");

function mostrarMensagem(texto, tipo) {
    mensagem.textContent = texto;
    mensagem.className = tipo;
}

function formatarData(data) {
    if (!data) return "-";

    const partes = data.split("-");
    return `${partes[2]}/${partes[1]}/${partes[0]}`;
}

async function obterErro(resposta) {
    try {
        const dados = await resposta.json();
        return dados.erro || dados.message || "Não foi possível realizar a operação.";
    } catch {
        return "Não foi possível realizar a operação.";
    }
}

async function carregarProjetos() {
    lista.replaceChildren();

    const carregando = document.createElement("tr");
    const celula = document.createElement("td");
    celula.colSpan = 5;
    celula.textContent = "Carregando projetos...";
    carregando.appendChild(celula);
    lista.appendChild(carregando);

    try {
        const resposta = await fetch(API);

        if (!resposta.ok) {
            throw new Error(await obterErro(resposta));
        }

        const projetos = await resposta.json();
        lista.replaceChildren();

        if (projetos.length === 0) {
            const linha = document.createElement("tr");
            const coluna = document.createElement("td");
            coluna.colSpan = 5;
            coluna.textContent = "Nenhum projeto cadastrado.";
            linha.appendChild(coluna);
            lista.appendChild(linha);
            return;
        }

        projetos.forEach(projeto => {
            const linha = document.createElement("tr");

            const valores = [
                projeto.id,
                projeto.descricao,
                formatarData(projeto.dataInicio),
                formatarData(projeto.dataFim),
                (projeto.funcionarios || [])
                    .map(funcionario => funcionario.nome)
                    .join(", ") || "Nenhum"
            ];

            valores.forEach(valor => {
                const coluna = document.createElement("td");
                coluna.textContent = String(valor);
                linha.appendChild(coluna);
            });

            lista.appendChild(linha);
        });
    } catch (erro) {
        lista.replaceChildren();

        const linha = document.createElement("tr");
        const coluna = document.createElement("td");
        coluna.colSpan = 5;
        coluna.textContent = erro.message || "Erro ao carregar projetos.";

        linha.appendChild(coluna);
        lista.appendChild(linha);
    }
}

formulario.addEventListener("submit", async evento => {
    evento.preventDefault();

    const projeto = {
        descricao: document.getElementById("descricao").value.trim(),
        dataInicio: document.getElementById("dataInicio").value,
        dataFim: document.getElementById("dataFim").value
    };

    if (!projeto.descricao) {
        mostrarMensagem("Informe a descrição do projeto.", "erro");
        return;
    }

    if (projeto.dataFim < projeto.dataInicio) {
        mostrarMensagem(
            "A data final não pode ser anterior à data inicial.",
            "erro"
        );
        return;
    }

    botaoCadastrar.disabled = true;
    mostrarMensagem("Cadastrando projeto...", "");

    try {
        const resposta = await fetch(API, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(projeto)
        });

        if (!resposta.ok) {
            throw new Error(await obterErro(resposta));
        }

        mostrarMensagem("Projeto cadastrado com sucesso!", "sucesso");
        formulario.reset();
        await carregarProjetos();
    } catch (erro) {
        mostrarMensagem(
            erro.message || "Não foi possível cadastrar o projeto.",
            "erro"
        );
    } finally {
        botaoCadastrar.disabled = false;
    }
});

document.getElementById("btnAtualizar")
    .addEventListener("click", carregarProjetos);

carregarProjetos();
