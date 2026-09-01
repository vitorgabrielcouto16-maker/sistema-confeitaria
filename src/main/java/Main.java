import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            CriadorTabelas.criarTabelas();
            System.out.println("Tabelas criadas com sucesso!");

            Produto p = new Produto("Brigadeiro", 3.50, true, "Brigadeiro gourmet");
            ProdutoRepository repo = new ProdutoRepository();
            repo.salvar(p);
            System.out.println("Produto salvo!");

            List<Produto> todos = repo.listarTodos();
            for (Produto p2 : todos) {
                System.out.println(p.getNome() + " - R$" + p.getPreco());
            }

            // precisa de um cliente e um produto já existentes no banco pra testar
            Cliente c = new Cliente("Maria", "11999998888");
            ClienteRepository clienteRepo = new ClienteRepository();
            clienteRepo.salvar(c);

            List<ItemVenda> itens = new ArrayList<>();
            itens.add(new ItemVenda(2, 3.50,1 )); // produtoId=1, quantidade=2, precoUnitario=3.50

            Venda venda = new Venda(LocalDate.now(), 1, itens); // clienteId=1 (ajuste conforme o id real salvo)
            VendaRepository vendaRepo = new VendaRepository();
            vendaRepo.salvar(venda);

            System.out.println("Venda salva!");

        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }

    }
}