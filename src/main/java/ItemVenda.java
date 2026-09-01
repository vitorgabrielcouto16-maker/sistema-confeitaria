public class ItemVenda {
    private int produtoId;
    private int quantidade;
    private double precoUnitario;
    private int idItemVenda;

    public ItemVenda(int quantidade, double precoUnitario, int produtoId) {
        this.precoUnitario = precoUnitario;
        this.quantidade = quantidade;
        this.produtoId = produtoId;

    }

    public int getProdutoId() {
        return produtoId;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public int getIdItemVenda() {
        return idItemVenda;
    }

    public ItemVenda(int produtoId, int quantidade, double precoUnitario, int idItemVenda) {
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.idItemVenda = idItemVenda;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
