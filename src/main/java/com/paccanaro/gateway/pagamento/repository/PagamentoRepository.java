package com.paccanaro.gateway.pagamento.repository;
import com.paccanaro.gateway.pagamento.model.Pagamento;
import com.paccanaro.gateway.pagamento.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {

    Optional<Pagamento> findByCodigoTransacao(String codigoTransacao);
    List<Pagamento> findByUsuarioOrderByDataCriacaoDesc(Usuario usuario);

}
