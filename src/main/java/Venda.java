import java.time.LocalDate;
import java.util.List;

public class Venda {
    private int idVenda;
    private LocalDate dataVenda;
    private int clienteId;
    private List<ItemVenda> itens;
    private String formaPagamento;

    public Venda(int idVenda, LocalDate dataVenda, int clienteId, List<ItemVenda> itens, String formaPagamento) {
        this.idVenda = idVenda;
        this.dataVenda = dataVenda;
        this.clienteId = clienteId;
        this.itens = itens;
        this.formaPagamento = formaPagamento;
    }

    public Venda(LocalDate dataVenda, int clienteId, List<ItemVenda> itens, String formaPagamento) {
        this.dataVenda = dataVenda;
        this.clienteId = clienteId;
        this.itens = itens;
        this.formaPagamento = formaPagamento;
    }

    // Construtores antigos mantidos por compatibilidade (ex: Main.java), formaPagamento fica null
    public Venda(int idVenda, LocalDate dataVenda, int clienteId, List<ItemVenda> itens) {
        this(idVenda, dataVenda, clienteId, itens, null);
    }

    public Venda(LocalDate dataVenda, int clienteId, List<ItemVenda> itens) {
        this(dataVenda, clienteId, itens, null);
    }

    public int getIdVenda() {
        return idVenda;
    }

    public LocalDate getDataVenda() {
        return dataVenda;
    }

    public int getClienteId() {
        return clienteId;
    }

    public List<ItemVenda> getItens() {
        return itens;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public double getTotal() {
        double total = 0;
        for (ItemVenda item : itens) {
            total += item.getQuantidade() * item.getPrecoUnitario();
        }
        return total;
    }
}