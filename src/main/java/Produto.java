public class Produto {
    private String nome;
    private Double preco;
    private boolean ativoParaVenda;
    private String descricaoProduto;
    private int id;

    public Produto(String nome, Double preco, boolean ativoParaVenda, String descricaoProduto) {
        this.nome = nome;
        this.preco = preco;
        this.ativoParaVenda = ativoParaVenda;
        this.descricaoProduto = descricaoProduto;
    }

    public String getNome() {
        return nome;
    }

    public Double getPreco() {
        return preco;
    }

    public boolean isAtivoParaVenda() {
        return ativoParaVenda;
    }

    public String getDescricaoProduto() {
        return descricaoProduto;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public void setAtivoParaVenda(boolean ativoParaVenda) {
        this.ativoParaVenda = ativoParaVenda;
    }

    public void setDescricaoProduto(String descricaoProduto) {
        this.descricaoProduto = descricaoProduto;
    }

    public Produto(String nome, Double preco, boolean ativoParaVenda, String descricaoProduto, int id) {
        this.nome = nome;
        this.preco = preco;
        this.ativoParaVenda = ativoParaVenda;
        this.descricaoProduto = descricaoProduto;
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
