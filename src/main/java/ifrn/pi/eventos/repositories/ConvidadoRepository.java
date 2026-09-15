package ifrn.pi.eventos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ifrn.pi.eventos.models.Convidado;

@Repository
public interface ConvidadoRepository extends JpaRepository<Convidado, Long> {

}