# Simulador de Financiamentos
Este simulador permite realizar as simulações básicas de financiamento imobiliário nos modelos PRICE e SAC, e permite também simular amortizações extras, fornecendo uma estimativa do tempo de duração do financiamento caso sejam feitas essas amortizações extra.

## Amortizações Extra
Qualquer um dos 3 checkbox disponíveis se enquadram como formas de amortização extra. Ao utilizar pelo menos um deles, será possível observar que o número de parcelas até o fim do financiamento será menor que o prazo contratado.

### Recorrência
Simula o cenário simples em que o usuário deseja utilizar recursos próprios para amortizar a dívida. É possível definir o valor a ser pago, a frequência, a quantidade de repetições, entre outros parâmetros.

### Investimentos
Permite simular o cenário onde o usuário realiza uma aplicação financeira com determinado rendimento e valor, e permite definir a ação a ser tomada no resgate. Esta opção serve para o usuário verificar se é mais vantajoso investir para ter um montante maior ao final do financiamento, ou amortizar para quitá-lo mais rápido.
Ação Amortizar: usa o lucro líquido para amortizar a dívida e investe novamente o mesmo valor inicial. 
Ação Reinvestir: não amortiza a dívida, apenas reinveste o valor total resgatado, ou seja, o valor aportado é sempre igual ao anterior mais o lucro líquido. 

### FGTS
A partir do salário bruto e do saldo inicial em conta do FGTS, utiliza o saldo para abater o saldo devedor a cada 2 anos, conforme previsto em lei.

## Instalação
### Para usuários Windows
Baixar o arquivo SimuladorFinanciamento.zip, descompactar, e executar o simuladorFinanciamento.exe.

### Para usuários Linux/MacOS
#### Opção 1 
Baixar a versão da JDK 11 correspondente ao sistema operacional, na página da Oracle. Após instalar o ambiente Java, executar o arquivo SimuladorFinanciamento.jar.
Downloads JDK 11 Oracle: https://www.oracle.com/br/java/technologies/javase/jdk11-archive-downloads.html
#### Opção 2
Baixar o arquivo SimuladorFinanciamento.jar. Utilizar a linha de comando e digitar:
brew install openjdk@11
export PATH="/opt/homebrew/opt/openjdk@11/bin:$PATH"
java -jar SimuladorFinanciamento.jar

## Observação
Os campos de entrada aceitam apenas "," como separador decimal. 
O separador de milhar é o "." e ele pode ser utilizado opcionalmente.

### Exemplos
Valores permitidos:
12
12,00
12,34
1234
1.234
1.234,56
123.456,78
123456,78

Valores não permitidos:
12.34
1234.56
12.34,56
123.4,56

## Esta versão contempla as seguintes correções/melhorias:
* Correção e validação de inputs de usuário.
* Correção de cálculos de amortização na última parcela
* Correção de cálculo da taxa de júros
* Transformar o checkbox resetar campos num botão específico
* Deixar a tabela no formato numérico brasileiro
* Colocar dialog de feedback ao salvar dados em Excel com sucesso 
* Deixar as linhas dos graficos mais grossas
* Alterar o tema do gráfico combinado para ficar igual aos demais gráficos.
* Arrumar o comportamento dos checkbox de amortização extra, para que quando não estiver selecionado, não levar em consideração no cálculo. Ao trocar de abas, garantir que automaticamente seja selecionado caso aquela simulação tenha uso valor maior que zero, e garantir que automaticamente seja desmarcada se a simulação não tiver utilizado o checkbox.
* Ajuste no formato aceito nos campos de entrada do usuário, passando a exigir o padrão numérico brasileiro.
* Adiciona validações para os campos de entrada das Opções Avançadas.
* Ajuste para resetar os campos para o default quando fechar a última aba de simulação.
* Validação do formato dos campos de entrada utilizando RegEx.

