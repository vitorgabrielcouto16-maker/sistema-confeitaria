import java.time.LocalDate;
import java.util.List;

public class Venda {
    private int idVenda;
    private LocalDate dataVenda;
    private int clienteId;
    private List<ItemVenda> itens;

    public Venda(int idVenda, LocalDate dataVenda, int clienteId, List<ItemVenda> ites) {
        this.idVenda = idVenda;
        this.dataVenda = dataVenda;
        this.clienteId = clienteId;
        this.itens = ites;
    }

    public Venda(LocalDate dataVenda, int clienteId, List<ItemVenda> itens) {
        this.dataVenda = dataVenda;
        this.clienteId = clienteId;
        this.itens = itens;
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
    public double getTotal(){
        double total = 0;
        for (ItemVenda item : itens){
            total += item.getQuantidade() * item.getPrecoUnitario();
        }
        return total;
    }
}
